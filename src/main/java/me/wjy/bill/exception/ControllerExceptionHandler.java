package me.wjy.bill.exception;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.response.PublicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
                .code(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public PublicResponse onHttpMessageNotReadableException() {
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode())
                .message("参数解析异常")
                .build();
    }
}

