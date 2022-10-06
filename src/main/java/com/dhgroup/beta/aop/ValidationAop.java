package com.dhgroup.beta.aop;

import com.dhgroup.beta.exception.NotValidBindingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ValidationAop {

    @Pointcut("@annotation(com.dhgroup.beta.aop.annotation.ValidAspect)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        BeanPropertyBindingResult bindingResult = searchBindingResult(joinPoint);

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = errorBinding(bindingResult);
            throw new NotValidBindingException("VALIDATION_ERR", errorMap);
        }

        return joinPoint.proceed();
    }


    private static BeanPropertyBindingResult searchBindingResult(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        BeanPropertyBindingResult bindingResult = null;

        for(Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class){
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }
        return bindingResult;
    }
    private static Map<String, String> errorBinding(BeanPropertyBindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap<String, String>();

        bindingResult.getFieldErrors().forEach(error -> {
            errorMap.put("errMsg", error.getDefaultMessage());
        });
        return errorMap;
    }
}
