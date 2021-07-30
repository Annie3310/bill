package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 账单 DTO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BillDTO extends BaseDTO {
    @Null
    String uuid;
    @Null
    String description;
    @NotNull(message = "金额不能为空")
    Double money;
    @NotNull(message = "账户不能为空")
    String account;
    Integer type;
}
