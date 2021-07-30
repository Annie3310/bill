package me.wjy.bill.controller;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.*;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.impl.BillServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王金义
 */
@Validated
@RestController
@RequestMapping("bill")
public class BillController {
    private final BillServiceImpl billService;

    public BillController(BillServiceImpl billService) {
        this.billService = billService;
    }
    // TODO 用切面设置 userId

    @PostMapping("income")
    public PublicResponse income(@RequestBody BillDTO billDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        setUserId(billDTO, httpServletRequest);
        return billService.income(billDTO);
    }

    @PostMapping("expense")
    public PublicResponse expense(@RequestBody BillDTO billDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        setUserId(billDTO, httpServletRequest);
        return billService.expense(billDTO);
    }

    @PostMapping("transfer")
    public PublicResponse transfer(@RequestBody @Validated TransferDTO transferDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        setUserId(transferDTO, httpServletRequest);
        return billService.transfer(transferDTO);
    }

    @PostMapping("paid")
    public PublicResponse getPaid(HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.getPaid(httpServletRequest.getHeader("id"));
    }

    @GetMapping("sum")
    public PublicResponse getSum(HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.getSum(httpServletRequest.getHeader("id"));
    }

    @GetMapping("filter")
    public PublicResponse getBillList(@RequestBody @Validated SelectBillDTO selectBillDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        setUserId(selectBillDTO, httpServletRequest);
        return billService.selectByFilter(selectBillDTO);
    }

    private void setUserId(BaseDTO baseDTO, HttpServletRequest httpServletRequest) {
        baseDTO.setUserId(httpServletRequest.getHeader("id"));
    }
}
