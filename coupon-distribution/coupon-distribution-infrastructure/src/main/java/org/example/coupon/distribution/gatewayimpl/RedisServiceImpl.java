package org.example.coupon.distribution.gatewayimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.common.constant.Constants;
import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.distribution.entity.UserCoupon;
import org.example.coupon.distribution.gateway.ICacheService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis相关的操作服务接口实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisServiceImpl implements ICacheService {

    private StringRedisTemplate redisTemplate;

    @Override
    public List<UserCoupon> getCachedCoupons(Long userId, Integer status) {
        log.info("Get Coupons From Cached: {}. {}", userId, status);
        String redisKey = statusToRedisKey(status, userId);
        List<String> couponStrs = redisTemplate.opsForHash().values(redisKey)
                .stream().map(item -> Objects.toString(item, null))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponStrs)) {
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStrs.stream().map(item -> {
            try {
                return new ObjectMapper().readValue(item, UserCoupon.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 保存空的优惠券列表到缓存中
     * 避免缓存穿透
     * @param userId
     * @param status
     */
    @Override
    @SuppressWarnings("all")
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List To Cache For User: {}, Status: {}", userId, status);
        Map<String, String> invalidCouponMap = new HashMap<>();
        try {
            invalidCouponMap.put("-1", new ObjectMapper().writeValueAsString(UserCoupon.invalidCoupon()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        使用SessionCallback把数据命令放入到Redis的pipeline
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                status.forEach(item -> {
                    String redisKey = statusToRedisKey(item, userId);
                    redisOperations.opsForHash().putAll(redisKey, invalidCouponMap);
                });
                return null;
            }
        };
        List<Object> backList = redisTemplate.executePipelined(sessionCallback);
        log.info("saveEmptyCouponListToCache{}", backList);
    }

    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        String redisKey = String.format("%s%s", Constants.RedisPrefix.COUPON_TEMPLATE, templateId.toString());
        return redisTemplate.opsForList().leftPop(redisKey);
    }

    @Override
    public Integer addCouponToCache(Long userId, List<UserCoupon> coupons, Integer status) {
        log.info("Add Coupon to Cache: {}, {}", userId, coupons, status);
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                result = addCouponToCacheForUsable(userId, coupons);
                break;
            case USRED:
                result = addCouponToCacheForUsed(userId, coupons);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId, coupons);
                break;
        }
        return result;
    }
    private Integer addCouponToCacheForUsable(Long userId, List<UserCoupon> coupons) {
        // 只会影响到一个cache
        Map<String, String> needCachedObject = new HashMap<>();
        coupons.forEach(item -> {
            try {
                needCachedObject.put(item.getId().toString(), new ObjectMapper().writeValueAsString(item));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        String redisKey = statusToRedisKey(CouponStatus.USABLE.getCode(), userId);
        redisTemplate.opsForHash().putAll(redisKey, needCachedObject);
        redisTemplate.expire(redisKey, getRandomExpirationTime(1, 10), TimeUnit.MINUTES);
        return needCachedObject.size();
    }

    private Integer addCouponToCacheForUsed(Long userId, List<UserCoupon> coupons) {
        Map<String, String> needCachedForUsed = new HashMap<>(coupons.size());
        String redisKeyForUsable =statusToRedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForUsed = statusToRedisKey(CouponStatus.USRED.getCode(), userId);
        List<UserCoupon> curUsableCoupons = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        // 当前可用的优惠券个数一定是大于1的（用户没有相关优惠券信息时将会有一个无效的优惠券缓存
        assert curUsableCoupons.size() > coupons.size();
        coupons.forEach(c ->
        {
            try {
                needCachedForUsed.put(c.getId().toString(), new ObjectMapper().writeValueAsString(c));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(UserCoupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(UserCoupon::getId).collect(Collectors.toList());
        // 判断paramIds 是不是 curUsableIds的子集
        List<String> needCleanKey = paramIds.stream()
                .map(item -> item.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public  Object execute(RedisOperations redisOperations) throws DataAccessException {
                // 已使用的Cache缓存添加
                redisOperations.opsForHash().putAll(redisKeyForUsed, needCachedForUsed);
                // 可用优惠券清理
                redisOperations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());
                // 重置过期时间
                redisOperations.expire(redisKeyForUsable, getRandomExpirationTime(10, 60), TimeUnit.MINUTES);
                redisOperations.expire(redisKeyForUsed, getRandomExpirationTime(10, 60), TimeUnit.MINUTES);
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        return coupons.size();
    }

    private Integer addCouponToCacheForExpired(Long userId, List<UserCoupon> coupons) {
        // 涉及到两个缓存，USABLE,EXPIRED
        Map<String, String> needCachedForExpired = new HashMap<>();
        String redisKeyForUsable = statusToRedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForExpired = statusToRedisKey(CouponStatus.EXPIRED.getCode(), userId);
        List<UserCoupon> curUsableCoupons = getCachedCoupons(userId, CouponStatus.USABLE.getCode());
        List<UserCoupon> curExpiredCoupons = getCachedCoupons(userId, CouponStatus.EXPIRED.getCode());
        // 当前可用优惠券个数>1
        assert curUsableCoupons.size() > curExpiredCoupons.size();
        coupons.forEach(c -> {
            try {
                needCachedForExpired.put(c.getId().toString(), new ObjectMapper().writeValueAsString(c));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        // TODO 校验当前优惠券是否与Cached中的匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(UserCoupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(UserCoupon::getId).collect(Collectors.toList());
        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForHash().putAll(redisKeyForExpired, needCachedForExpired);
                redisOperations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());
                // 重置过期时间
                redisOperations.expire(redisKeyForUsable, getRandomExpirationTime(10, 60), TimeUnit.MINUTES);
                redisOperations.expire(redisKeyForExpired, getRandomExpirationTime(10, 60), TimeUnit.MINUTES);
                return null;
            }
        };
        redisTemplate.executePipelined(sessionCallback);
        return coupons.size();
    }

    private String statusToRedisKey(Integer status, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                redisKey = String.format("%s%s",Constants.RedisPrefix.USER_COUPON_USABLE, userId);
                break;
            case USRED:
                redisKey = String.format("%s%s",Constants.RedisPrefix.USER_COUPON_USED, userId);;
                break;
            case EXPIRED:
                redisKey = String.format("%s%s",Constants.RedisPrefix.USER_COUPON_EXIPIRED, userId);
                break;
        }
        redisTemplate.expire(redisKey, getRandomExpirationTime(10, 60), TimeUnit.MINUTES);
        return redisKey;
    }

    /**
     * 防止缓存雪崩
     * @param min
     * @param max
     * @return
     */
    private Long getRandomExpirationTime(Integer min, Integer max) {
        return 5L;
    }
}
