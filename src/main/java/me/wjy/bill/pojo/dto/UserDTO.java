package me.wjy.bill.pojo.dto;

import lombok.*;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO extends BaseDTO {
    String name;
    String password;
}
