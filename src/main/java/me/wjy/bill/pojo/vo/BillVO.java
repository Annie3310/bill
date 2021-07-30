package me.wjy.bill.pojo.vo;

import lombok.*;
import me.wjy.bill.response.PublicResponse;

/**
 * 账单 VO
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BillVO{
    String description;
    String uuid;
    Double money;
    String account;
    Integer type;
    String createTime;
}
