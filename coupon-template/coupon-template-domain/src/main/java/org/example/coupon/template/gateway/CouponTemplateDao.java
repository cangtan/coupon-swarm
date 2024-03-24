package org.example.coupon.template.gateway;


import org.example.coupon.template.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@SuppressWarnings("all")
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {

    /**
     * 根据名字查询
     * @param name
     * @return
     */
    CouponTemplate findByName(String name);

    /**
     * 查询通过是否可用和是否过期
     * @param available
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    /**
     * 根据expired查询模板记录
     *
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByExpired(Boolean expired);

//    @Modifying
//    @Transactional
//    @Query("UPDATE coupon_template SET available = :available WHERE id = :id")
//    int updateAvailableById(@Param("id") Integer id,@Param("available") Integer available);
}
