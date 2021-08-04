package me.wjy.bill.mapper;

import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author 王金义
 */
@Repository
public interface UserMapper {
    /**
     * 获取一个 user 实体
     * @param userId 用户 ID
     * @return 找到的 user
     */
    UserDO getUser(String userId);

    /**
     * 添加一个用户
     * @param userDTO  require: name, password
     * @return 受影响的行数
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer save(UserDTO userDTO);

    /**
     * 更新
     * @param userDTO require: oldName
     * @return 受影响的行数
     */
    Integer update(UserDTO userDTO);
}
