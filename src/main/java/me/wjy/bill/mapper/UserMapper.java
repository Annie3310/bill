package me.wjy.bill.mapper;

import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.po.AccountDO;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王金义
 */
@Repository
public interface UserMapper {
    /**
     * 获取一个 user 实体
     * @param userId
     * @return
     */
    UserDO getUser(String userId);

    /**
     * 添加一个用户
     * @param userDTO
     * @return
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer save(UserDTO userDTO);

}
