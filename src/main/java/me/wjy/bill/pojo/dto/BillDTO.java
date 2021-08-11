package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 账单 DTO
 *
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class BillDTO extends BaseDTO {
    String uuid;
    String description;
    @NotNull(message = "金额不能为空")
    @Min(value = 0, message = "金额不能为负数")
    @Digits(integer = 12, fraction = 2)
    Double money;
    @NotNull(message = "账户不能为空")
    String account;
    Integer type;
}
