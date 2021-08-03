package me.wjy.bill.pojo.po;

import lombok.*;

/**
 * 账单 DO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDO{
    Long id;
    String uuid;
    Double money;
    String description;
    String account;
    Integer type;
    String createTime;
    String updateTime;
    Integer deleted;
}
