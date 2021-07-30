package me.wjy.bill.mapper;

import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.po.BillDO;
import me.wjy.bill.pojo.dto.SelectBillDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王金义
 */
@Repository
public interface BillMapper {
    /**
     * 插入一条记录, 为多个业务所调用
     * @param billDTO
     * @return
     */
    int insert(BillDTO billDTO);

    /**
     * 多条件筛选
     * @param selectBillDTO
     * @return
     */
    List<BillDO> filter(SelectBillDTO selectBillDTO);
}
