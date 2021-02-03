package com.wenfengyong.toolbox.server.common;

import com.wenfengyong.toolbox.server.stereotype.ResultStatus;

import lombok.Getter;
import lombok.ToString;

/**
 * 包装后返回的请求结果
 *
 * @author fywen
 * @date 2021/2/1 14:01
 */
@Getter
@ToString
public class WrapperResponse<T> {
    
    /**
     * 业务错误码
     */
    private Integer code;
    
    /**
     * 信息描述
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    private WrapperResponse(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
    }
    
    private WrapperResponse(Integer code,  String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 业务成功返回业务代码和描述信息
     */
    public static WrapperResponse<Void> success() {
        return new WrapperResponse<>(ResultStatus.SUCCESS, null);
    }
    
    /**
     * 业务成功返回业务代码,描述和返回的参数
     */
    public static <T> WrapperResponse<T> success(T data) {
        return new WrapperResponse<>(ResultStatus.SUCCESS, data);
    }
    
    /**
     * 业务成功返回业务代码,描述和返回的参数
     */
    public static <T> WrapperResponse<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new WrapperResponse<>(resultStatus, data);
    }
    
    /**
     * 业务异常返回业务代码和描述信息
     */
    public static <T> WrapperResponse<T> fail() {
        return new WrapperResponse<>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }
    
    /**
     * 业务异常返回业务代码,描述和返回的参数
     */
    public static <T> WrapperResponse<T> fail(ResultStatus resultStatus) {
        return fail(resultStatus, null);
    }
    
    /**
     * 业务异常返回业务代码,描述和返回的参数
     */
    public static <T> WrapperResponse<T> fail(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new WrapperResponse<>(ResultStatus.INTERNAL_SERVER_ERROR, data);
        }
        return new WrapperResponse<>(resultStatus, data);
    }
    
    public static <T> WrapperResponse<T> fail(Integer code, String message, T data) {
        return new WrapperResponse<>(code, message,data);
    }
}
