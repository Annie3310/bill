package me.wjy.bill.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author ηιδΉ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    @JsonProperty("userId")
    String userId;
}
