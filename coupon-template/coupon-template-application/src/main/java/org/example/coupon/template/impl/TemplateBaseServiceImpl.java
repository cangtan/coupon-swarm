package org.example.coupon.template.impl;

import com.alibaba.cola.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.template.api.ITemplateBaseService;
import org.example.coupon.template.entity.CouponTemplate;
import org.example.coupon.template.gateway.CouponTemplateDao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@DubboService
@AllArgsConstructor
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao templateDao;

    @Override
    public CouponTemplateDTO buildTemplateInfo(Integer id) {
        Optional<CouponTemplate> templateRecord = templateDao.findById(id);
        if (!templateRecord.isPresent()) {
            throw new BizException("template is not exist:" + id);
        }
        return templateToBiz(templateRecord.get());
    }

    @Override
    public List<CouponTemplateDTO> findAllUsableTemplate() {
        List<CouponTemplate> recordList = templateDao.findAllByAvailableAndExpired(true, false);
        return recordList.stream().map(this::templateToBiz).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateDTO> findIdsTemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> recordList = templateDao.findAllById(ids);
        return recordList.stream().map(this::templateToBiz)
                .collect(Collectors.toMap(CouponTemplateDTO::getId, Function.identity()));
    }

    public CouponTemplateDTO templateToBiz(CouponTemplate template) {
        CouponTemplateDTO result = new CouponTemplateDTO();
        result.setId(template.getId());
        result.setName(template.getName());
        result.setLogo(template.getLogo());
        result.setIntro(template.getIntro());
        result.setCategory(String.valueOf(template.getCategory()));
        result.setProductLine(template.getProductLine().getCode());
        result.setTemplateKey(template.getTemplateKey());
        result.setTarget(template.getTarget().getCode());
        result.setRule(template.getRule());
        return result;
    };
}
