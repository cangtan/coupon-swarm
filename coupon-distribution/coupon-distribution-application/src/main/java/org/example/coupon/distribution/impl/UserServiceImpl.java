package org.example.coupon.distribution.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.cola.exception.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.common.constant.Constants;
import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.distribution.api.IUserService;
import org.example.coupon.distribution.dto.AcquireTemplateAddCmd;
import org.example.coupon.distribution.dto.CouponClassify;
import org.example.coupon.distribution.dto.UserCouponDTO;
import org.example.coupon.distribution.entity.CouponMessage;
import org.example.coupon.distribution.entity.UserCoupon;
import org.example.coupon.distribution.gateway.CouponTemplateGateway;
import org.example.coupon.distribution.gateway.ICacheService;
import org.example.coupon.distribution.gateway.SettlementGateway;
import org.example.coupon.distribution.gateway.UserCouponDao;
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

    private final ICacheService cacheService;

    private final CouponTemplateGateway templateClient;

    private final SettlementGateway settlementClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<UserCouponDTO> findCouponsByStatus(Long userId, Integer status) {
        List<UserCoupon> curCached = cacheService.getCachedCoupons(userId, status);
        List<UserCouponDTO> preTarget;
        if (!curCached.isEmpty()) {
            preTarget = BeanUtil.copyToList(curCached, UserCouponDTO.class);
        } else {
            List<UserCoupon> dbCoupon = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            // 如果数据库中没有记录则直接返回
            if (dbCoupon.isEmpty()) {
                return Collections.emptyList();
            }
            Map<Integer, CouponTemplateDTO> id2Template =
                    templateClient.findIdsToTemplate(dbCoupon.stream()
                            .map(UserCoupon::getTemplateId).collect(Collectors.toList()));
            dbCoupon.forEach(item -> {
                item.setCouponTemplate(id2Template.get(item.getTemplateId()));
            });
            // 数据库中存在记录
            preTarget = BeanUtil.copyToList(dbCoupon, UserCouponDTO.class);
            // 将记录写回Cache
            cacheService.addCouponToCache(userId, BeanUtil.copyToList(preTarget, UserCoupon.class), status);
        }
        // 将无效优惠券剔除
        preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        // 如果获取到的是可用的优惠券，还需要做已过期的优惠券处理
        if (CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(BeanUtil.copyToList(preTarget, UserCouponDTO.class));
            if (!classify.getExpiredList().isEmpty()) {
                cacheService.addCouponToCache(userId, BeanUtil.copyToList(classify.getExpiredList(), UserCoupon.class), CouponStatus.EXPIRED.getCode());
                // 将过期优惠券发送到Kafka做异步处理
                CouponMessage couponKafkaMessage = new CouponMessage(CouponStatus.EXPIRED.getCode(),
                        classify.getExpiredList().stream().map(UserCouponDTO::getId).collect(Collectors.toList()));
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
    public List<CouponTemplateDTO> findAvailable(Long userId) {
        long curTime = new Date().getTime();
        List<CouponTemplateDTO> list = templateClient.findAllUsableTemplate();
        list = list.stream().filter(item -> item.getRule().getExpiration().getDeadLine() > curTime)
                .collect(Collectors.toList());
        // key -> templateId, value left -> template.limitation, right为模板
        Map<Integer, Pair<Integer, CouponTemplateDTO>> limit2Template =
                new HashMap<>(list.size());
        list.forEach(item -> {
            limit2Template.put(item.getId(), new Pair<>(item.getRule().getLimitation(), item));
        });
        List<CouponTemplateDTO> result = new ArrayList<>(limit2Template.size());
        List<UserCouponDTO> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());

        Map<Integer, List<UserCouponDTO>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(UserCouponDTO::getTemplateId));
        // 根据template group 判断是否可以领取优惠券
        limit2Template.forEach((k, v) -> {
            int limitation = v.getKey();
            CouponTemplateDTO couponTemplate = v.getValue();
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
    public UserCouponDTO acquireTemplate(AcquireTemplateAddCmd request) {
        CouponTemplateDTO template = request.getTemplate();
        Map<Integer, CouponTemplateDTO> id2Template = templateClient.findIdsToTemplate(
                Collections.singletonList(template.getId()));
        if (id2Template.size() <= 0) {
            // 优惠券是需要存在的
            throw new BizException("优惠券不存在");
        }
        List<UserCouponDTO> userUsableCoupons = findCouponsByStatus(request.getUserId(), CouponStatus.USABLE.getCode());
        Map<Integer, List<UserCouponDTO>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(UserCouponDTO::getTemplateId));
        boolean isLimitation = templateId2Coupons.containsKey(template.getId())
                && templateId2Coupons.get(template.getId()).size() >= template.getRule().getLimitation();
        if (isLimitation) {
            // 已领取并且已达到上线
            throw new BizException("当前领取已达上线");
        }
        String couponCode = cacheService.tryToAcquireCouponCodeFromCache(template.getId());
        if (null == couponCode) {
            throw new BizException("优惠券已领取完");
        }
        UserCouponDTO userCoupon = new UserCouponDTO(template.getId(), request.getUserId(), couponCode, CouponStatus.USABLE);
        UserCoupon save = couponDao.save(BeanUtil.copyProperties(userCoupon, UserCoupon.class));
        save.setCouponTemplate(template);
        cacheService.addCouponToCache(request.getUserId(),
                Collections.singletonList(save), CouponStatus.USABLE.getCode());
        userCoupon.setId(save.getId());
        return userCoupon;
    }

//    @Override
//    public SettlementInfoBO settlement(SettlementInfoBO settlementInfo) {
//        return null;
//    }
}
