package me.wjy.bill.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    String password;
    Long salt;
}
