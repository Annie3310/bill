package me.wjy.bill.response;

import lombok.*;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicResponse {
    String code;
    String message;
    Object result;
}
