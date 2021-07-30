package me.wjy.bill.enums;

import lombok.Getter;

/**
 * @author 王金义
 * @date 2021/7/29
 */
@Getter
public enum ErrorCodeEnum {
    SYSTEM_EXECUTION_ERROR("B0001","系统执行错误"),
    USER_REQUEST_PARAM_ERROR("A0400","用户请求参数错误"),
    USER_AUTH_FAIL_ERROR("A0220", "用户身份验证失败");
    String errorCode;
    String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
