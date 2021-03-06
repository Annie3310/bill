package me.wjy.bill.response;

import lombok.*;
import java.util.Map;

/**
 * @author ηιδΉ
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
