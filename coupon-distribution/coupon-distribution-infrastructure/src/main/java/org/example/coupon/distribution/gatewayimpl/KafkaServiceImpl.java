package org.example.coupon.distribution.gatewayimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.coupon.common.constant.Constants;
import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.distribution.entity.CouponMessage;
import org.example.coupon.distribution.entity.UserCoupon;
import org.example.coupon.distribution.gateway.IMessageQueueService;
import org.example.coupon.distribution.gateway.UserCouponDao;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 将Cache中的Coupon状态变化同步到DB中
 */
@Slf4j
@Service
@AllArgsConstructor
public class KafkaServiceImpl implements IMessageQueueService {

    private UserCouponDao couponDao;

    /**
     * KafkaListener 当kafka有消息的时候将会调用该方法
     *
     * @param record
     */
    @KafkaListener(topics = Constants.TOPIC)
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object dto = message.get();
            try {
                CouponMessage couponKafkaMessage = new ObjectMapper().readValue(dto.toString(), CouponMessage.class);
                CouponStatus status = CouponStatus.of(couponKafkaMessage.getStatus());
                switch (status) {
                    case USABLE:
                        break;
                    case USRED:
                        processUsedCoupons(couponKafkaMessage, status);
                        break;
                    case EXPIRED:
                        processExpiredCoupons(couponKafkaMessage, status);
                        break;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void processUsedCoupons(CouponMessage message, CouponStatus status) {
        try {
            processCouponByStatus(message, status);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void processExpiredCoupons(CouponMessage message, CouponStatus status) {
        try {
            processCouponByStatus(message, status);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据状态处理优惠券信息
     *
     * @param message
     * @param status
     */
    private void processCouponByStatus(CouponMessage message, CouponStatus status) throws JsonProcessingException {
        List<UserCoupon> coupons = couponDao.findAllById(message.getIds());
        if (coupons.isEmpty() || coupons.size() != message.getIds().size()) {
            log.error("Can Not Find Right Coupon Info:{}", new ObjectMapper().writeValueAsString(message));
            return;
        }

        coupons.forEach(item -> {
            item.setStatus(status);
        });

        log.info("CouponKafkaMessage Op Coupon Count: {}", couponDao.saveAll(coupons));
    }
}
