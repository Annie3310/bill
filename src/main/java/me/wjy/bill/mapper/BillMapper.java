package me.wjy.bill.mapper;

import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.vo.BillVO;
import me.wjy.bill.pojo.dto.SelectBillDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 王金义
 */
@Repository
public interface BillMapper {
    int insert(BillDTO billDTO);
    int getPaid(BillDTO billDTO);
    BillVO getDetails(BillDTO billDTO);
    List<BillVO> selectBillList(SelectBillDTO selectBillDTO);
}
