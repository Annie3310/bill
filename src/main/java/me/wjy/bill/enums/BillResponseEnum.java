package me.wjy.bill.enums;

import lombok.Getter;

/**
 * @author 王金义
 */
@Getter
public enum BillResponseEnum {
    /**
     *
     */
    INCOME_SUCCESS("00000", "收入成功"),
    EXPENSE_SUCCESS("00000", "支出成功"),
    TRANSFER_SUCCESS("00000", "转账成功"),
    GET_PAID_SUCCESS("00000", "收到工资"),
    GET_SUM_SUCCESS("00000", "获取总合成功"),
    GET_LIST_SUCCESS("00000", "筛选成功"),
    GET_DETAILS_SUCCESS("00000", "获取详情成功"),
    INCOME_FAIL("410", "收入失败"),
    EXPENSE_FAIL("411", "支出失败"),
    TRANSFER_FAIL("412", "转账失败"),
    GET_PAID_FAIL("413", "收工资失败"),
    GET_SUM_FAIL("414", "获取总合失败"),
    GET_DETAILS_FAIL("416", "获取详情失败");

    BillResponseEnum(String responseCode, String message) {
        this.message = message;
        this.responseCode = responseCode;
    }

    String message;
    String responseCode;
}
