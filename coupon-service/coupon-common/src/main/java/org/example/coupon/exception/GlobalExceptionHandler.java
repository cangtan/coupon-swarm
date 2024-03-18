package org.example.coupon.exception;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    public Response handleServiceException(BizException e) {
        return Response.buildFailure(e.getErrCode().getErrCode(), e.getMessage());
    }
}
