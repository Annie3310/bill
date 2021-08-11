package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
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
    @Min(value = 0, message = "金额不能为负数")
    @Digits(integer = 12, fraction = 2)
    Double balance;
}
