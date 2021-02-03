package com.wenfengyong.toolbox.server.stereotype;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

/**
 * @author fywen
 * @date 2021/2/1 14:14
 */
@ToString
@Getter
public enum ResultStatus {
    /**
     * 0 成功
     */
    SUCCESS(HttpStatus.OK, 0, "OK"),
    
    /**
     * 100~999 服务出错
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal Server Error"),
    
    /**
     * 1000 业务出错
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 1000, "业务异常"),
    ;
    
    /**
     * 返回的HTTP状态码,  符合http请求
     */
    private HttpStatus httpStatus;
    /**
     * 业务异常码
     */
    private Integer code;
    /**
     * 业务异常信息描述
     */
    private String message;
    
    ResultStatus(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
