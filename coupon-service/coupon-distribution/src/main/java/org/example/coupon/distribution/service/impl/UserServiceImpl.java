package org.example.coupon.distribution.service.impl;

import com.alibaba.cola.exception.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.bo.SettlementInfoBO;
import org.example.coupon.constant.Constants;
import org.example.coupon.distribution.client.SettlementClient;
import org.example.coupon.distribution.client.TemplateClient;
import org.example.coupon.distribution.cmd.AcquireTemplateRequest;
import org.example.coupon.distribution.constant.CouponStatus;
import org.example.coupon.distribution.dao.UserCouponDao;
import org.example.coupon.distribution.dataobject.UserCoupon;
import org.example.coupon.distribution.dto.CouponClassify;
import org.example.coupon.distribution.dto.CouponKafkaMessage;
import org.example.coupon.distribution.service.IRedisService;
import org.example.coupon.distribution.service.IUserService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 所有的操作过程，状态都保存在Redis中，并通过Kafka把消息传递到MySQL中
 * 为什么使用Kafka，而不是直接使用SpringBoot中的异步处理？
 *     主要是考虑安全性（异步任务可能会失败的，而为了保证一致性，利用kafka重试
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserCouponDao couponDao;

    private final IRedisService redisService;

    private final TemplateClient templateClient;

    private final SettlementClient settlementClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<UserCoupon> findCouponsByStatus(Long userId, Integer status) {
        List<UserCoupon> curCached = redisService.getCachedCoupons(userId, status);
        List<UserCoupon> preTarget;
        if (!curCached.isEmpty()) {
            preTarget = curCached;
        } else {
            List<UserCoupon> dbCoupon = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            // 如果数据库中没有记录则直接返回
            if (dbCoupon.isEmpty()) {
                return dbCoupon;
            }
            Map<Integer, CouponTemplateBO> id2Template =
                    templateClient.findIdsToTemplate(dbCoupon.stream()
                    .map(UserCoupon::getTemplateId).collect(Collectors.toList()))
                    .getData();
            dbCoupon.forEach(item -> {
                item.setCouponTemplate(id2Template.get(item.getTemplateId()));
            });
            // 数据库中存在记录
            preTarget = dbCoupon;
            // 将记录写回Cache
            redisService.addCouponToCache(userId, preTarget, status);
        }
        // 将无效优惠券剔除
        preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        // 如果获取到的是可用的优惠券，还需要做已过期的优惠券处理
        if (CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(preTarget);
            if (!classify.getExpiredList().isEmpty()) {
                redisService.addCouponToCache(userId, classify.getExpiredList(), CouponStatus.EXPIRED.getCode());
                // 将过期优惠券发送到Kafka做异步处理
                CouponKafkaMessage couponKafkaMessage = new CouponKafkaMessage(CouponStatus.EXPIRED.getCode(),
                        classify.getExpiredList().stream().map(UserCoupon::getId).collect(Collectors.toList()));
                try {
                    kafkaTemplate.send(Constants.TOPIC, new ObjectMapper().writeValueAsString(couponKafkaMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return classify.getUsableList();
        }
        return preTarget;
    }

    @Override
    public List<CouponTemplateBO> findAvailable(Long userId) {
        long curTime = new Date().getTime();
        List<CouponTemplateBO> list = templateClient.findAllUsableTemplate().getData();
        list = list.stream().filter(item -> item.getRule().getExpiration().getDeadLine() > curTime)
                .collect(Collectors.toList());
        // key -> templateId, value left -> template.limitation, right为模板
        Map<Integer, Pair<Integer, CouponTemplateBO>> limit2Template =
                new HashMap<>(list.size());
        list.forEach(item -> {
            limit2Template.put(item.getId(), new Pair<>(item.getRule().getLimitation(), item));
        });
        List<CouponTemplateBO> result = new ArrayList<>(limit2Template.size());
        List<UserCoupon> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());

        Map<Integer, List<UserCoupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(UserCoupon::getTemplateId));
        // 根据template group 判断是否可以领取优惠券
        limit2Template.forEach((k, v) -> {
            int limitation = v.getKey();
            CouponTemplateBO couponTemplate = v.getValue();
            if (templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }
            result.add(couponTemplate);
        });
        return result;
    }

    /**
     * 用户领取优惠券
     * 从TemplateClient 拿到对应的优惠券，并检查是否过期
     * 根据limitation判断用户是否可以领取
     * save to db
     * 填充CouponTemplateBO
     * save to cache
     * @param request
     * @return
     */
    @Override
    public UserCoupon acquireTemplate(AcquireTemplateRequest request) {
        CouponTemplateBO template = request.getTemplate();
        Map<Integer, CouponTemplateBO> id2Template = templateClient.findIdsToTemplate(
                Collections.singletonList(template.getId())).getData();
        if (id2Template.size() <= 0) {
            // 优惠券是需要存在的
            throw new BizException("优惠券不存在");
        }
        List<UserCoupon> userUsableCoupons = findCouponsByStatus(request.getUserId(), CouponStatus.USABLE.getCode());
        Map<Integer, List<UserCoupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(UserCoupon::getTemplateId));
        boolean isLimitation = templateId2Coupons.containsKey(template.getId())
                && templateId2Coupons.get(template.getId()).size() >= template.getRule().getLimitation();
        if (isLimitation) {
            // 已领取并且已达到上线
            throw new BizException("当前领取已达上线");
        }
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(template.getId());
        if (null == couponCode) {
            throw new BizException("优惠券已领取完");
        }
        UserCoupon userCoupon = new UserCoupon(template.getId(), request.getUserId(), couponCode, CouponStatus.USABLE);
        UserCoupon save = couponDao.save(userCoupon);
        save.setCouponTemplate(template);
        redisService.addCouponToCache(request.getUserId(), 
                Collections.singletonList(save), CouponStatus.USABLE.getCode());

        return save;
    }

    @Override
    public SettlementInfoBO settlement(SettlementInfoBO settlementInfo) {
        return null;
    }
}
