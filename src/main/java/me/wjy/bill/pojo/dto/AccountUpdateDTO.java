package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class AccountUpdateDTO extends BaseDTO{
    @NotNull
    String oldName;
    String newName;
    @Digits(integer = 12, fraction = 2)
    Double balance;
}
