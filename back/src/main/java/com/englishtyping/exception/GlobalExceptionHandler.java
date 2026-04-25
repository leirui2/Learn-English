package com.englishtyping.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一响应格式：{"code": xxx, "message": "...", "data": null}
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（如 409 邮箱已注册、401 认证失败等）
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResult> handleResponseStatusException(ResponseStatusException ex) {
        int code = ex.getStatusCode().value();
        return ResponseEntity.status(code)
                .body(new ErrorResult(code, ex.getReason(), null));
    }

    /**
     * 处理 @Valid 校验失败（422 Unprocessable Entity）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleValidationException(MethodArgumentNotValidException ex) {
        // 收集所有字段错误信息
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResult(422, message, null));
    }

    /**
     * 处理通用未知异常（500）
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleGenericException(Exception ex) {
        log.error("未捕获的异常", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(500, "服务器内部错误", null));
    }

    /**
     * 统一错误响应体
     */
    public record ErrorResult(int code, String message, Object data) {
    }
}
