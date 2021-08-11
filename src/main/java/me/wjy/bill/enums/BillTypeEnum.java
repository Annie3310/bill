package me.wjy.bill.enums;

import lombok.Getter;

/**
 * 收入或支出
 * @author 王金义
 */
@Getter
public enum BillTypeEnum {
    /**
     * 对应数据表中的收入和支出关系
     */
    INCOME(1, "收入"),
    EXPENSE(0, "支出");

    Integer type;
    String name;

    BillTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
