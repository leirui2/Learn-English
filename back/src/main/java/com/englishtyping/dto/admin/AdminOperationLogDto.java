package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员操作日志 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOperationLogDto {
    private Integer id;
    private String operatorId;
    private String operatorName;
    private String operationType;
    private String targetType;
    private String targetId;
    private String operationDetail;
    private String ipAddress;
    private LocalDateTime createdAt;
}
