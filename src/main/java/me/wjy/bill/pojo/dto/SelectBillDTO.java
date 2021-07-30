package me.wjy.bill.pojo.dto;

import lombok.*;

import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * 筛选条件提交封装
 *
 * @author 王金义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SelectBillDTO extends BaseDTO {
    /*
     * 添加开始时间, 结束时间, 都可为空
     * 应格式化为 yyyy-MM-dd 的格式
     * 如果都不为空, 则取区间值
     * 如果只精确到月, 则应该从当月的 1 日开始
     * 如果精确到年, 则从当年的 1 月 1 日开始
     */
    /// 以下为筛选条件
    /**
     * 起始时间
     */
    LocalDate startDate;
    /**
     * 结束时间
     */
    LocalDate endDate;
    /**
     * 账户
     */
    String account;
    /**
     * uuid
     */
    String uuid;
    /**
     * 金额大于
     */
    @Min(0)
    Double greaterThan;
    /**
     * 金额小于
     */
    @Min(0)
    Double lessThan;
}
