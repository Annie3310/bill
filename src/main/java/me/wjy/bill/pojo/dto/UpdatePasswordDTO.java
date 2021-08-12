package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 王金义
 * @date 2021/8/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePasswordDTO extends BaseDTO{
    @NotNull(message = "旧密码不能为空")
    @Size(message = "密码长度应该在 8 到 20 之间", max = 20, min = 8)
    String oldPassword;

    @NotNull(message = "新密码不能为空")
    @Size(message = "密码长度应该在 8 到 20 之间", max = 20, min = 8)
    String newPassword;
}
