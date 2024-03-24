package org.example.coupon.common.exception;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;

public abstract class AbstractExceptionHandler {

    public Response handleServiceException(BizException e) {
        return Response.buildFailure(e.getErrCode().getErrCode(), e.getMessage());
    }
}
