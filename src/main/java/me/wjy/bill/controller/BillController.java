package me.wjy.bill.controller;

import me.wjy.bill.annotation.GetUserId;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.*;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.impl.BillServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetUserId
    @PostMapping("income")
    public PublicResponse income(@RequestBody @Validated BillDTO billDTO) throws ServiceException {
        return billService.income(billDTO);
    }

    @GetUserId
    @PostMapping("expense")
    public PublicResponse expense(@RequestBody @Validated BillDTO billDTO) throws ServiceException {
        return billService.expense(billDTO);
    }

    @GetUserId
    @PostMapping("transfer")
    public PublicResponse transfer(@RequestBody @Validated TransferDTO transferDTO) throws ServiceException {
        return billService.transfer(transferDTO);
    }

    @GetUserId
    @GetMapping("sum")
    public PublicResponse getSum(UserDTO userDTO) throws ServiceException {
        String id = userDTO.getUserId();
        return billService.getSum(id);
    }

    @GetUserId
    @PostMapping("filter")
    public PublicResponse getBillList(@RequestBody @Validated FilterDTO filterDTO) throws ServiceException {
        return billService.filter(filterDTO);
    }
}
