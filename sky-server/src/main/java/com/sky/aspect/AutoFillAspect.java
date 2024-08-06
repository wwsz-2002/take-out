package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充
 */
@Slf4j
@Component
@Aspect//定义为切面类
public class AutoFillAspect {
    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {//joinPoint获取被拦截的对象
        log.info("开始进行公共字段自动填充...");

        //获取当前被拦截方法的AutoFill注解上对应的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//因为我们知道这个是个方法，所以将其转为MethodSignature,获取方法的信息
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取注解对象
        OperationType operationType = autoFill.value();//获得注解中标识的数据库操作类型

        //获取到被拦截方法的参数（实体对象）
        Object[] args = joinPoint.getArgs();//获取方法的数据
        if (args == null || args.length == 0) {
            return;//防止出现空指针
        }
        Object arg = args[0];//约定，需要进行自动填充的类放在第一个参数位

        //准备需要更新的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据不同的类型，通过反射更改被拦截的实体对象的那几个值
        if (operationType == OperationType.INSERT) {//如果是插入操作
            //四个公共字段都赋值
            try {
                //反射，找到arg中的setCreatTime方法，传入的值是LocalDateTime类型的
                Method setCreatTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreatUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //为对象属性赋值
                setCreatTime.invoke(arg, now);
                setUpdateTime.invoke(arg, now);
                setCreatUser.invoke(arg, currentId);
                setUpdateUser.invoke(arg, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {//如果是更新操作
            //两个公共字段赋值
            try {
                Method setUpdateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(arg, now);
                setUpdateUser.invoke(arg, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

