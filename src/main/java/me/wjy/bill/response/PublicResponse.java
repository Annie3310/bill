package me.wjy.bill.response;

import lombok.*;

/**
 * @author ηιδΉ
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
