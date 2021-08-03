package me.wjy.bill.pojo.po;

import lombok.*;
/**
 * 账户 DO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDO {
    Long id;
    String name;
    Double balance;
}
