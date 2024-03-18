package org.example.coupon.template.controller;

import javafx.print.Collation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.CouponTemplateBO;
import org.example.coupon.template.cmd.TemplateRequest;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.example.coupon.template.service.IBuildTemplateService;
import org.example.coupon.template.service.ITemplateBaseService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class CouponTemplateController {
    private final IBuildTemplateService templateService;
    private final ITemplateBaseService baseService;

    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request) {
        CouponTemplate result = new CouponTemplate();
        log.info("Build Template: {}", request);
        return templateService.buildTemplate(request);
    }

    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam Integer id) {
        log.info("Build Template Info For:{}", id);
        return baseService.buildTemplateInfo(id);
    }

    @GetMapping("/template/sdk/all")
    public List<CouponTemplateBO> findAllUsableTemplate() {
        return baseService.findAllUsableTemplate();
    }

    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateBO> findIdsToTemplate(@RequestParam Collection<Integer> ids) {
        return baseService.findIdsTemplateSDK(ids);
    }
}
