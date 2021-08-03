package me.wjy.bill.mapper;

import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.pojo.po.AccountDO;
import me.wjy.bill.pojo.vo.AccountVO;
import me.wjy.bill.pojo.dto.BillDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王金义
 */
@Repository
public interface AccountMapper {
    /**
     * 添加到账户
     * @param billDTO require: account, money
     * @return 受影响的行数
     */
    Integer plusTo(BillDTO billDTO);

    /**
     * 从账户中减去
     * @param billDTO require: account, money
     * @return 受影响的行数
     */
    Integer minusFrom(BillDTO billDTO);

    /**
     * 获取余额总和
     * @param userId 用户 ID
     * @return 总和
     */
    Double getSum(String userId);

    /**
     * 获取各账户的余额
     * @param userId 要获取的用户的 ID
     * @return 所有账户的余额
     */
    List<AccountVO> getSumDetails(String userId);

    /**
     * 获取当前用户的所有账户
     * @param userId 用户 ID
     * @return 所有账户的 name 和 balance
     */
    List<AccountDO> getAllAccount(String userId);

    /**
     * 为用户添加一个账户
     * @param accountDTO require: name
     * @return 受影响的行数
     */
    Integer addAccount(AccountDTO accountDTO);

    /**
     * 为用户删除一个账户
     * @param accountDTO require: name
     * @return 受影响的行数
     */
    Integer rmAccount(AccountDTO accountDTO);

    /**
     * 更新一个账户
     * @param accountUpdateDTO require: oldName
     * @return 受影响的行数
     */
    Integer updateAccount(AccountUpdateDTO accountUpdateDTO);

    /**
     * 通过名字获取账户
     * @param accountDTO require: name
     * @return 该账户的详细信息
     */
    AccountDO getByName(AccountDTO accountDTO);
}
