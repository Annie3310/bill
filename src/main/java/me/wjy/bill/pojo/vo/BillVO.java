package me.wjy.bill.pojo.vo;

import lombok.*;

/**
 * 账单 VO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillVO{
    String description;
    String uuid;
    Double money;
    String account;
    String type;
    String createTime;
}
