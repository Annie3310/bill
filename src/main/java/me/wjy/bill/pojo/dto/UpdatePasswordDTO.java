package me.wjy.bill.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王金义
 * @date 2021/8/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDTO extends BaseDTO{
    String oldPassword;
    String newPassword;
}
