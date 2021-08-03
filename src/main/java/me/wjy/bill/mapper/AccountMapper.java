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
     * @param billDTO
     * @return
     */
    int plusTo(BillDTO billDTO);

    /**
     * 从账户中减去
     * @param billDTO
     * @return
     */
    int minusFrom(BillDTO billDTO);

    /**
     * 获取余额总和
     * @param userId
     * @return
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
     * @param userId
     * @return
     */
    List<AccountDO> getAllAccount(String userId);


    Integer addAccount(AccountDTO accountDTO);
    Integer rmAccount(AccountDTO accountDTO);
    Integer updateAccount(AccountUpdateDTO accountUpdateDTO);

    AccountDO getByName(AccountDTO accountDTO);
}
