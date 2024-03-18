package org.example.coupon.distribution.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * kafka相关的接口定义
 */
public interface IKafkaService {

    /**
     * 消费优惠券kafka消息
     * @param record
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}
