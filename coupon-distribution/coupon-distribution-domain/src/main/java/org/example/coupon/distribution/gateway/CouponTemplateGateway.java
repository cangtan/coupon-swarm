package org.example.coupon.distribution.gateway;

import org.example.coupon.common.dto.CouponTemplateDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 模板微服务调用
 */
public interface CouponTemplateGateway {

    List<CouponTemplateDTO> findAllUsableTemplate();

    Map<Integer, CouponTemplateDTO> findIdsToTemplate(Collection<Integer> ids);
}
