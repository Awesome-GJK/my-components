package com.gjk.log.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.gjk.log.config.LogProperties;
import com.gjk.log.util.IPUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ControllerLogAspect
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:22
 * @description:
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class ControllerLogAspect {

    private final ControllerLogHandler controllerLogHandler;
    private final LogProperties.ControllerConfig controllerConfig;

    @Pointcut("execution(public * com.gjk..*.*Controller.*(..))&&!@annotation(com.gjk.log.annoation.LogIgnore)")
    public void controllerLog() {
    }

    @Around("controllerLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            result =  joinPoint.proceed();
            watch.stop();
            return result;
        } finally {
            try {
                doLog(joinPoint, result, watch.getTotalTimeMillis());
            } catch (Exception e){
                log.error("Controller 请求日志埋点失败，errMsg：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 日志处理
     *
     * @param joinPoint
     * @param res
     * @param time
     */
    private void doLog(ProceedingJoinPoint joinPoint, Object res, long time) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String apiClassMethodName = signature.getDeclaringTypeName() + "." + signature.getName();
        String reqParam = getReqParam(signature.getParameterNames(), joinPoint.getArgs());

        ControllerLog operationLog = ControllerLog.builder()
                .consumingTime(time + "ms")
                .apiClassMethodName(apiClassMethodName)
                .reqParam(reqParam)
                .build();
        if (controllerConfig.isRecordResponse()) {
            String resp = null == res ? "" : JSON.toJSONString(res);
            operationLog.setResp(resp);
        }

        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(attributes -> ((ServletRequestAttributes) attributes).getRequest())
                .ifPresent(request -> {
                    operationLog.setIp(IPUtils.getIpAddr(request));
                    operationLog.setHttpMethod(request.getMethod());
                    operationLog.setApiUrl(request.getRequestURL().toString());
                    operationLog.setOperatorId(controllerLogHandler.getOperatorId(request));
                });

        controllerLogHandler.handle(operationLog, controllerConfig);
    }

    /**
     * 获取入参
     *
     * @param argNames 方法参数名称数组
     * @param args 方法参数数组
     * @return 返回处理后的描述
     */
    private String getReqParam(String[] argNames, Object[] args) {
        if(null == argNames || null == args) {
            return "";
        }
        Map<Object, Object> map = new HashMap<>();
        for(int i = 0; i < argNames.length; i++){
            //剔除不用打印的参数
            if(args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse || args[i] instanceof MultipartFile) {
                continue;
            }
            map.put(argNames[i],args[i]);
        }
        return JSON.toJSONString(map);
    }
}
