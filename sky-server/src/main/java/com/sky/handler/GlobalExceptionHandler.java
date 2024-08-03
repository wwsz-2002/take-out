package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理sql异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'lisi' for key 'employee.idx_username' 用户名重复
        String message = ex.getMessage();//获取异常信息
        if(message.contains("Duplicate entry")){//判断错误信息中是否含有固定字段以判断是什么错误
            String[] split = message.split(" ");//以空格分隔异常信息
            String username = split[2];//去第三个部分，也就是名字
            String msg =username+ MessageConstant.ALREADY_EXISTS;//回复已存在
            return Result.error(msg);
        }
        else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);//回复未知错误
        }
    }

}
