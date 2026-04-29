package com.englishtyping.service;

import com.englishtyping.dto.CreateGiftRequest;
import com.englishtyping.dto.ExchangeGiftResponse;
import com.englishtyping.dto.GiftDto;
import com.englishtyping.dto.UpdateGiftRequest;
import com.englishtyping.dto.UserItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 道具服务接口
 */
public interface GiftService {

    /**
     * 获取所有道具（管理员，分页）
     *
     * @param pageable 分页参数
     * @return 道具分页数据
     */
    Page<GiftDto> getAllGifts(Pageable pageable);

    /**
     * 获取上架的道具列表（用户端，分页）
     *
     * @param pageable 分页参数
     * @return 道具分页数据
     */
    Page<GiftDto> getOnShelfGifts(Pageable pageable);

    /**
     * 获取道具详情
     *
     * @param id 道具 ID
     * @return 道具详情
     */
    GiftDto getGiftDetail(Long id);

    /**
     * 创建道具
     *
     * @param request 创建请求
     * @return 创建的道具
     */
    GiftDto createGift(CreateGiftRequest request);

    /**
     * 更新道具
     *
     * @param id 道具 ID
     * @param request 更新请求
     * @return 更新后的道具
     */
    GiftDto updateGift(Long id, UpdateGiftRequest request);

    /**
     * 删除道具
     *
     * @param id 道具 ID
     */
    void deleteGift(Long id);

    /**
     * 上架道具
     *
     * @param id 道具 ID
     */
    void onShelf(Long id);

    /**
     * 下架道具
     *
     * @param id 道具 ID
     */
    void offShelf(Long id);

    /**
     * 兑换道具
     *
     * @param giftId 道具 ID
     * @param userId 用户 ID
     * @return 兑换结果
     */
    ExchangeGiftResponse exchangeGift(Long giftId, String userId);

    /**
     * 获取用户道具列表
     *
     * @param userId 用户 ID
     * @return 用户道具列表
     */
    List<UserItemDto> getUserItems(String userId);

    /**
     * 使用道具
     *
     * @param userItemId 用户道具 ID
     * @param userId 用户 ID
     */
    void useUserItem(Long userItemId, String userId);

    /**
     * 获取用户兑换记录（道具兑换历史）
     *
     * @param userId 用户 ID
     * @param pageable 分页参数
     * @return 兑换记录分页数据
     */
    Page<UserItemDto> getUserExchangeRecords(String userId, Pageable pageable);

    /**
     * 上传道具图片
     *
     * @param giftId 道具 ID
     * @param file 图片文件
     * @return 图片 URL
     */
    String uploadGiftImage(Long giftId, org.springframework.web.multipart.MultipartFile file);
}