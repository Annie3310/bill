package me.wjy.bill.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseDTO {
    @JsonProperty("userId")
    String userId;
}
