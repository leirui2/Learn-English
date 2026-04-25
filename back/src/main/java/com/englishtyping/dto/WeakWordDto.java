package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 易错词汇 DTO
 */
@Data
@Builder
public class WeakWordDto {

    /** 单词 */
    private String word;

    /** 错误次数 */
    private Integer errorCount;
}
