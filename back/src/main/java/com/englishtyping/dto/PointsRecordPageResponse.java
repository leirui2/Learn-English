package com.englishtyping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 积分记录分页响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsRecordPageResponse {

    /** 积分记录列表 */
    private List<PointsRecordDto> records;

    /** 总记录数 */
    private Long total;

    /** 当前页码 */
    private Integer page;

    /** 每页大小 */
    private Integer size;

    /** 总页数 */
    private Integer totalPages;
}
