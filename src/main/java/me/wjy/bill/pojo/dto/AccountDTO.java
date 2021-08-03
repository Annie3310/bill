package me.wjy.bill.pojo.dto;

import lombok.*;

/**
 * 账户 DO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends BaseDTO {
    String name;
    Double balance;
}
