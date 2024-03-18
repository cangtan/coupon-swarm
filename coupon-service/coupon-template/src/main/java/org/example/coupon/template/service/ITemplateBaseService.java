package org.example.coupon.template.service;

import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.template.dataobject.CouponTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板基础
 */
public interface ITemplateBaseService {
    /**
     * 根据优惠券模板id获取优惠券信息
     * @param id
     * @return
     */
    CouponTemplate buildTemplateInfo(Integer id);

    /**
     * 查询所有可用的优惠券模板
     * @return
     */
    List<CouponTemplateBO> findAllUsableTemplate();

    /**
     * 获取模板ids到CouponTemplateSDK的映射
     * @param ids 模板ids
     * @return Map<key:模板id,value:优惠券模板>
     */
    Map<Integer, CouponTemplateBO> findIdsTemplateSDK(Collection<Integer> ids);
}
