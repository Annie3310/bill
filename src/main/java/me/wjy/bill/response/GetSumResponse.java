package me.wjy.bill.response;

import lombok.*;
import java.util.Map;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GetSumResponse {
    Map<String,Double> details;
    Double sum;
}
