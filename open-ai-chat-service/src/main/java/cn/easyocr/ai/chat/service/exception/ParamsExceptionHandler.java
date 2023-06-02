package cn.easyocr.ai.chat.service.exception;

import cn.easyocr.common.enums.ResultCodeEnum;
import cn.easyocr.common.exception.ParamValidateException;
import cn.easyocr.common.exception.ServiceException;
import cn.easyocr.common.resp.ErrorResponse;
import cn.easyocr.common.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author : feiya
 * @description :
 * @since : 2023/4/11
 */
@RestControllerAdvice
public class ParamsExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return ResponseUtil.fail(ResultCodeEnum.PARAM_ERROR.getCode(), msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseUtil.fail(ResultCodeEnum.PARAM_ERROR.getCode(), ex.getMessage());
    }

    @ExceptionHandler({ParamValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerParamException(ParamValidateException ex) {
        return ResponseUtil.fail(ex.getResultCodeEnum().getCode(), ex.getResultCodeEnum().getDesc());
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerServiceException(ServiceException ex) {
        return ResponseUtil.fail(ex.getResultCodeEnum().getCode(), ex.getResultCodeEnum().getDesc());
    }
}
