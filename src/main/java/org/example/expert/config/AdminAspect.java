package org.example.expert.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AdminAspect {

    @Pointcut("execution(* org.example.*.*.*.*.CommentAdminController.*(..))")
    public void CommentAdminMethods() {
    }

    @Pointcut("execution(* org.example.*.*.*.*.UserAdminController.*(..))")
    public void UserAdminMethods() {
    }

    @Around("CommentAdminMethods() || UserAdminMethods()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(AdminAspect.class);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestTime = LocalDateTime.now().toString();

        logger.info("[Request 정보] RequestUrl: {}, RequestBody : {}, CurrentTime : {}", request.getRequestURL(), params(joinPoint), requestTime);

        Object result = joinPoint.proceed(); // 메서드 실행

        logger.info("[Response 정보] ResponseBody : {}", toJson(result));

        return result;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String params(JoinPoint joinPoint) throws JsonProcessingException {
        // 실행 중인 메서드의 정보를 CodeSignature 타입으로 변환
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        // 메서드 파라미터 이름들을 배열로 가져온다.
        String[] parameterNames = codeSignature.getParameterNames();

        // 메서드에 전달된 파라미터 값을 배열로 가져온다.
        Object[] args = joinPoint.getArgs();

        // 파라미터 이름과 값을 저장할 Map 생성
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }

        // JSON 문자열로 변환
        return objectMapper.writeValueAsString(params);
    }

    private String toJson(Object obj) throws JsonProcessingException {
        // JSON 문자열로 변환
        return objectMapper.writeValueAsString(obj);

    }
}
