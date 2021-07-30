package me.wjy.bill.mapper;

import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import org.springframework.stereotype.Repository;

/**
 * @author 王金义
 */
@Repository
public interface UserMapper {
    UserDO getUser(UserDTO userDTO);
}
