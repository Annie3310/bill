package me.wjy.bill.enums;

/**
 * @author 王金义
 */

public enum ErrorEnum {
    AUTH_FAIL("601","验证失败"),
    LOGIC_ERROR("602","逻辑错误");

    String code;
    String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
