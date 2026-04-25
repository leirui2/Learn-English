package com.englishtyping.service;

import com.englishtyping.dto.CategoryDto;
import com.englishtyping.dto.CompleteLevelRequest;
import com.englishtyping.dto.CompleteLevelResponse;
import com.englishtyping.dto.LevelDto;
import com.englishtyping.entity.Exercise;

import java.util.List;

/**
 * 关卡服务接口
 */
public interface LevelService {

    /**
     * 获取所有分类及其关卡列表，附带当前用户的解锁/完成状态
     */
    List<CategoryDto> getLevelList(String userId);

    /**
     * 获取关卡详情及所有练习内容
     */
    LevelDto getLevelDetail(Integer levelId, String userId);

    /**
     * 获取关卡的所有练习内容
     */
    List<Exercise> getLevelExercises(Integer levelId, String userId);

    /**
     * 完成关卡
     */
    CompleteLevelResponse completeLevel(Integer levelId, String userId, CompleteLevelRequest request);
}