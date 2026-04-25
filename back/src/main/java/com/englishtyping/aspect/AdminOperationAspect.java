package com.englishtyping.aspect;

import com.englishtyping.annotation.AdminOperation;
import com.englishtyping.entity.AdminOperationLog;
import com.englishtyping.entity.User;
import com.englishtyping.repository.AdminOperationLogRepository;
import com.englishtyping.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 管理员操作日志切面
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminOperationAspect {

    private final AdminOperationLogRepository logRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.englishtyping.annotation.AdminOperation)")
    public Object logAdminOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        // 先执行目标方法，确保业务逻辑不受日志影响
        Object result = joinPoint.proceed();

        // 异步记录日志（不影响主事务）
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            AdminOperation annotation = method.getAnnotation(AdminOperation.class);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                return result;
            }

            String operatorId = authentication.getName();
            String operatorName = userRepository.findById(operatorId)
                    .map(User::getUsername)
                    .orElse("Unknown");

            String ipAddress = getClientIpAddress();

            // 简单记录操作类型和描述，不序列化复杂参数
            String detail = String.format("{\"method\":\"%s\",\"description\":\"%s\"}",
                    method.getName(), annotation.description());

            Object[] args = joinPoint.getArgs();
            // 只取第一个参数的 ID，如果是对象则取 "N/A"
            String targetId = "N/A";
            if (args.length > 0 && args[0] != null) {
                Object firstArg = args[0];
                if (firstArg instanceof Integer || firstArg instanceof Long || firstArg instanceof String) {
                    String raw = firstArg.toString();
                    // 截断到50字符以内
                    targetId = raw.length() > 50 ? raw.substring(0, 50) : raw;
                }
            }

            try {
                saveLog(operatorId, operatorName, annotation.type(), annotation.targetType(),
                        targetId, detail, ipAddress);
            } catch (Exception saveEx) {
                log.warn("保存操作日志失败: {}", saveEx.getMessage());
            }
        } catch (Exception e) {
            log.warn("记录操作日志失败（不影响主业务）: {}", e.getMessage());
        }

        return result;
    }

    /**
     * 在新事务中保存日志，与主业务事务隔离
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String operatorId, String operatorName, String operationType,
                        String targetType, String targetId, String detail, String ipAddress) {
        AdminOperationLog logEntry = AdminOperationLog.builder()
                .operatorId(operatorId)
                .operatorName(operatorName)
                .operationType(operationType)
                .targetType(targetType)
                .targetId(targetId)
                .operationDetail(detail)
                .ipAddress(ipAddress)
                .build();
        logRepository.save(logEntry);
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception e) {
            log.warn("获取IP失败: {}", e.getMessage());
        }
        return "Unknown";
    }
}
