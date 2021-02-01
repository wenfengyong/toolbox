package com.wenfengyong.toolbox.server.common.advice;

import java.lang.annotation.Annotation;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.wenfengyong.toolbox.server.common.WrapperResponse;
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
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }
    
    /**
     * 当类或者方法使用了 {@link ResponseResultBody} 就会调用这个方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {
        // 防止出现重复包裹的问题
        if (body instanceof WrapperResponse) {
            return body;
        }
        return WrapperResponse.success(body);
    }
    
    /**
     * 提供对标准Spring MVC异常的处理
     *
     * @param ex      the target exception
     * @param request the current request
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<WrapperResponse<?>> exceptionHandler(Exception ex, WebRequest request) {
        log.error("ExceptionHandler: {}", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
//        if (ex instanceof ResultException) {
//            return this.handleResultException((ResultException) ex, headers, request);
//        }
        // TODO: 2019/10/05 galaxy 这里可以自定义其他的异常拦截
        return this.handleException(ex, headers, request);
    }
    
//    /** 对ResultException类返回返回结果的处理 */
//    protected ResponseEntity<WrapperResponse<?>> handleResultException(ResultException ex, HttpHeaders headers, WebRequest request) {
//        Result<?> body = Result.failure(ex.getResultStatus());
//        HttpStatus status = ex.getResultStatus().getHttpStatus();
//        return this.handleExceptionInternal(ex, body, headers, status, request);
//    }
    
    /** 异常类的统一处理 */
    protected ResponseEntity<WrapperResponse<?>> handleException(Exception ex, HttpHeaders headers, WebRequest request) {
        WrapperResponse<?> body = WrapperResponse.fail(ResultStatus.BAD_REQUEST);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return this.handleExceptionInternal(ex, body, headers, status, request);
    }
    
    /**
     * org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
     * <p>
     * A single place to customize the response body of all exception types.
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     */
    protected ResponseEntity<WrapperResponse<?>> handleExceptionInternal(
            Exception ex, WrapperResponse<?> body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("exception", ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
