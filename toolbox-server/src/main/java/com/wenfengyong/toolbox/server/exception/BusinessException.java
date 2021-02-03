package com.wenfengyong.toolbox.server.exception;

import com.wenfengyong.toolbox.server.stereotype.ResultStatus;

/**
 * 业务类异常
 * @author fywen
 * @date 2021/2/2 15:04
 */
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;
    
    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public BusinessException(ResultStatus resultStatus) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
