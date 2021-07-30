package me.wjy.bill.response;

import lombok.*;

import java.util.List;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublicResponse {
    String code;
    String message;
    Object result;
}
