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

    @PostMapping("income")
    public PublicResponse income(@RequestBody BillDTO billDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.income(billDTO);
    }

    @PostMapping("expense")
    public PublicResponse expense(@RequestBody BillDTO billDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.expense(billDTO);
    }

    @PostMapping("transfer")
    public PublicResponse transfer(@RequestBody @Validated TransferDTO transferDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.transfer(transferDTO);
    }

    @GetMapping("sum")
    public PublicResponse getSum(HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.getSum(httpServletRequest.getHeader("id"));
    }

    @GetMapping("filter")
    public PublicResponse getBillList(@RequestBody @Validated FilterDTO filterDTO, HttpServletRequest httpServletRequest) throws ServiceException {
        return billService.filter(filterDTO);
    }
}
