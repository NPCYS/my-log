package com.mqz.aspect;

import com.alibaba.fastjson.JSON;
import com.mqz.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
//切面，通过切面里面的切入点和通知来完成相关的AOP工作
public class LogAspect {
    //切入点用注解的方式来确定在何处切入
    @Pointcut("@annotation(com.mqz.annotation.SystemLog)")
    public void pt(){
    }
    //环绕通知，在找到切入点pt的时候进行处理
    //形参中的joinPoint叫连接点，连接点也就是你要加aop切面的那个方法
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;

        try {
            //增强方法前的一个方法
            handleBefore(joinPoint);
            //获取环绕的原方法的返回值
            ret = joinPoint.proceed();
            //增强方法后的一个方法
            handleAfter(ret);
        } finally {
            // 结束后换行，注意换行符的使用，不清楚是windows系统还是Linux系统所以使用这个换行符
            log.info("=======End=======" + System.lineSeparator());
        }
        return ret;
    }



    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //想打印request url啥的就得获取一下request的内容
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //得到一下那个SystemLog里的信息
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}",systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}",request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature)joinPoint.getSignature()).getMethod());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }
    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}",JSON.toJSONString(ret));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }
}
