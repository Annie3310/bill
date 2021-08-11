package me.wjy.bill.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    @JsonProperty("userId")
    String userId;
    String name;

    @NotNull(message = "输入的密码不能为空")
    @Size(message = "密码长度应该在 8 到 20 之间", max = 20, min = 8)
    String password;

    Long salt;
}
