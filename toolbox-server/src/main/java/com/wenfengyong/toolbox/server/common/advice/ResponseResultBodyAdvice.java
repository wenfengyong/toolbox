package com.wenfengyong.toolbox.server.common.advice;

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.wenfengyong.toolbox.server.common.WrapperResponse;
import com.wenfengyong.toolbox.server.exception.BusinessException;
import com.wenfengyong.toolbox.server.stereotype.ResultStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fywen
 * @date 2021/2/1 14:08
 */
@RestControllerAdvice
@Slf4j
public class ResponseResultBodyAdvice implements ResponseBodyAdvice<Object> {
    
    private static final Class<? extends Annotation> ANNOTATION_TYPE = ResponseResultBody.class;
    
    /**
     * 判断类或者方法是否使用了 {@link ResponseResultBody}
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) ||
                returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }
    
    /**
     * 当类或者方法使用了 {@link ResponseResultBody} 就会调用这个方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        
        if (Objects.isNull(body)) {
            return WrapperResponse.success();
        }
        
        // 防止出现重复包裹的问题
        if (body instanceof WrapperResponse) {
            return body;
        }
        return WrapperResponse.success(body);
    }
    
    /**
     * 提供对标准Spring MVC异常的处理
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<WrapperResponse<?>> exceptionHandler(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof BusinessException) {
            return this.handleBusinessException((BusinessException) ex, headers);
        }
        return this.handleException(headers);
    }
    
    /**
     * 对 {@link BusinessException} 类返回返回结果的处理
     */
    protected ResponseEntity<WrapperResponse<?>> handleBusinessException(BusinessException ex, HttpHeaders headers) {
        WrapperResponse<?> body = WrapperResponse.fail(ex.getCode(), ex.getMessage(), null);
        return this.handleExceptionInternal(body, headers, HttpStatus.OK);
    }
    
    /**
     * 异常类的统一处理
     */
    protected ResponseEntity<WrapperResponse<?>> handleException(HttpHeaders headers) {
        WrapperResponse<?> body = WrapperResponse.fail(ResultStatus.BAD_REQUEST);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return this.handleExceptionInternal(body, headers, status);
    }
    
    protected ResponseEntity<WrapperResponse<?>> handleExceptionInternal(
            WrapperResponse<?> body, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(body, headers, status);
    }
}
