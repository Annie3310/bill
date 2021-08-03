package me.wjy.bill.enums;

/**
 * 收入或支出
 * @author 王金义
 */
public enum BillTypeEnum {
    /**
     * 对应数据表中的收入和支出关系
     */
    INCOME(1),
    EXPENSE(0);

    Integer type;

    BillTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
