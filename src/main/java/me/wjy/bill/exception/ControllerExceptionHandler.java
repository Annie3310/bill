package me.wjy.bill.exception;

import me.wjy.bill.enums.ErrorCodeEnum;
import me.wjy.bill.response.PublicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 王金义
 * @date 2021/7/29
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public PublicResponse onServiceException(ServiceException serviceException) {
        return PublicResponse
                .builder()
                .code(serviceException.getErrorCode())
                .message(serviceException.getErrorMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public PublicResponse onMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        String message = "";
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if (!allErrors.isEmpty()) {
                ObjectError objectError = allErrors.get(0);
                message = objectError.getDefaultMessage();
            }
        }
        return PublicResponse
                .builder()
                .code(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode())
                .message(message)
                .build();
    }
}

