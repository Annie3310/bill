package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "账户名不能为空")
    String name;
    Double balance;
}
