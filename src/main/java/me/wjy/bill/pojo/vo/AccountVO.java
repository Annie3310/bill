package me.wjy.bill.pojo.vo;

import lombok.*;

/**
 * 账户 DO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AccountVO {
    String name;
    Double balance;
}
