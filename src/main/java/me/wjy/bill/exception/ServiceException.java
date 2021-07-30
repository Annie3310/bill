package me.wjy.bill.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 王金义
 * @date 2021/7/29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceException extends Exception{
    private String errorCode;
    private String errorMessage;
    private String stackTraceLog;
}
