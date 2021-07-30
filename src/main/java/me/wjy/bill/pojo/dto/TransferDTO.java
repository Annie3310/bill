package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 转账提交封装
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransferDTO extends BaseDTO {
    @NotNull(message = "源账户不能为空")
    String from;
    @NotNull(message = "目标账户不能为空")
    String to;
    @NotNull(message = "金额不能为空")
    Double money;
    String description;
}
