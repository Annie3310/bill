package me.wjy.bill.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 王金义
 * @date 2021/7/29
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException{
    private String errorCode;
    private String errorMessage;
    private String stackTraceLog;
}
