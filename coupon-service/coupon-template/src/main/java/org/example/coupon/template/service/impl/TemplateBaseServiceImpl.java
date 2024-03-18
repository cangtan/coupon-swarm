package org.example.coupon.template.service.impl;

import com.alibaba.cola.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.template.dao.CouponTemplateDao;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.example.coupon.template.service.ITemplateBaseService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private CouponTemplateDao templateDao;

    @Override
    public CouponTemplate buildTemplateInfo(Integer id) {
        Optional<CouponTemplate> templateRecord = templateDao.findById(id);
        if (!templateRecord.isPresent()) {
            throw new BizException("template is not exist:" + id);
        }
        return templateRecord.get();
    }

    @Override
    public List<CouponTemplateBO> findAllUsableTemplate() {
        List<CouponTemplate> recordList = templateDao.findAllByAvailableAndExpired(true, false);
        return recordList.stream().map(this::templateToBiz).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateBO> findIdsTemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> recordList = templateDao.findAllById(ids);
        return recordList.stream().map(this::templateToBiz)
                .collect(Collectors.toMap(CouponTemplateBO::getId, Function.identity()));
    }

    public CouponTemplateBO templateToBiz(CouponTemplate template) {
        CouponTemplateBO result = new CouponTemplateBO();
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
