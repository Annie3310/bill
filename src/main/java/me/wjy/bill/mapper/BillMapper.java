package me.wjy.bill.mapper;

import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.po.BillDO;
import me.wjy.bill.pojo.dto.FilterDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王金义
 */
@Repository
public interface BillMapper {
    /**
     * 插入一条记录, 为多个业务所调用
     * @param billDTO require: money, account
     * @return 受影响的行数
     */
    Integer insert(BillDTO billDTO);

    /**
     * 多条件筛选
     * @param filterDTO 条件
     * @return 找到的记录
     */
    List<BillDO> filter(FilterDTO filterDTO);
}
