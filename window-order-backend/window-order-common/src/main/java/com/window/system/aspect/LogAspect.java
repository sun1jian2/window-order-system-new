package com.window.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.window.system.annotation.Log;
import com.window.system.mapper.SysOperationLogMapper;
import com.window.system.model.entity.SysOperationLog;
import com.window.system.security.AuthUser;
import com.window.system.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Global Log Aspect
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private SysOperationLogMapper logMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(logAnnotation)")
    public Object logAround(ProceedingJoinPoint point, Log logAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Integer status = 1; // 1: Success
        String errorMsg = null;

        try {
            result = point.proceed();
            return result;
        } catch (Throwable e) {
            status = 0; // 0: Failure
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long time = System.currentTimeMillis() - start;
            saveLog(point, logAnnotation, time, status, errorMsg);
        }
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Log logAnnotation, long time, Integer status, String errorMsg) {
        try {
            SysOperationLog opLog = new SysOperationLog();
            
            // Annotation info
            if (logAnnotation != null) {
                opLog.setModule(logAnnotation.module());
                opLog.setOperation(logAnnotation.operation());
            }

            // Method info
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            opLog.setMethod(className + "." + methodName + "()");

            // Request params (first arg)
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    String params = objectMapper.writeValueAsString(args[0]);
                    opLog.setParams(params.length() > 2000 ? params.substring(0, 2000) : params);
                } catch (Exception e) {
                    // ignore param serialization error
                }
            }

            // User from security context
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getPrincipal() instanceof AuthUser) {
                    AuthUser au = (AuthUser) auth.getPrincipal();
                    opLog.setUserId(au.getId());
                    opLog.setUsername(au.getUsername());
                }
            } catch (Exception e) {
                // ignore user fetch error
            }

            // IP
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    opLog.setIp(IpUtils.getClientIp(request));
                }
            } catch (Exception e) {
                // ignore ip fetch error
            }

            opLog.setStatus(status);
            opLog.setErrorMsg(errorMsg != null && errorMsg.length() > 2000 ? errorMsg.substring(0, 2000) : errorMsg);
            opLog.setCostTime(time);
            opLog.setCreateTime(LocalDateTime.now());

            logMapper.insert(opLog);
        } catch (Exception e) {
            log.error("Failed to save log", e);
        }
    }
}
