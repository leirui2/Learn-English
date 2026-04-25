package com.englishtyping.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogDto {

    /** 日志 ID */
    private Integer id;

    /** 操作人 ID */
    private String operatorId;

    /** 操作人用户名 */
    private String operatorName;

    /** 操作类型 */
    private String operationType;

    /** 操作对象类型 */
    private String targetType;

    /** 操作对象 ID */
    private String targetId;

    /** 操作详情 */
    private String details;

    /** IP 地址 */
    private String ipAddress;

    /** 操作时间 */
    private LocalDateTime createdAt;
}
