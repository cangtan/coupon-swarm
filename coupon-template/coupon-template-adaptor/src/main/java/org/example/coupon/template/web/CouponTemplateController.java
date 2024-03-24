package org.example.coupon.template.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.SingleResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.template.api.IBuildTemplateService;
import org.example.coupon.template.api.ITemplateBaseService;
import org.example.coupon.template.dto.TemplateAddCmd;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/template")
public class CouponTemplateController {

    private final IBuildTemplateService templateService;

    private final ITemplateBaseService baseService;

    @PostMapping("/build")
    public SingleResponse<CouponTemplateDTO> buildTemplate(@RequestBody TemplateAddCmd request) {
        log.info("Build Template: {}", request);
        return SingleResponse.of(templateService.buildTemplate(request));
    }

    @GetMapping("/info")
    public SingleResponse<CouponTemplateDTO> buildTemplateInfo(@RequestParam Integer id) {
        log.info("Build Template Info For:{}", id);
        return SingleResponse.of(baseService.buildTemplateInfo(id));
    }

    @GetMapping("/sdk/all")
    public MultiResponse<CouponTemplateDTO> findAllUsableTemplate() {
        List<CouponTemplateDTO> result = baseService.findAllUsableTemplate();
        return MultiResponse.of(result, result.size());
    }

    @GetMapping("/sdk/infos")
    public SingleResponse<Map<Integer, CouponTemplateDTO>> findIdsToTemplate(@RequestParam Collection<Integer> ids) {
        return SingleResponse.of(baseService.findIdsTemplateSDK(ids));
    }
}
