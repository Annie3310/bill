package me.wjy.bill.pojo.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;

/**
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDO{
    Long id;
    @JsonProperty("userId")
    String userId;
    String name;
    String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDO userDO = (UserDO) o;
        return Objects.equals(userId, userDO.userId) && Objects.equals(password, userDO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
