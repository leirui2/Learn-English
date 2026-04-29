package com.englishtyping.service.impl;

import com.englishtyping.dto.CreateGiftRequest;
import com.englishtyping.dto.ExchangeGiftResponse;
import com.englishtyping.dto.GiftDto;
import com.englishtyping.dto.UpdateGiftRequest;
import com.englishtyping.dto.UserItemDto;
import com.englishtyping.entity.*;
import com.englishtyping.repository.GiftRepository;
import com.englishtyping.repository.PointsRecordRepository;
import com.englishtyping.repository.UserItemRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.service.GiftService;
import jakarta.persistence.EntityNotFoundException;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 道具服务实现
 * @author lei
 */
@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {

    private final GiftRepository giftRepository;
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final PointsRecordRepository pointsRecordRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.gift.default-image-url}")
    private String defaultImageUrl;

    @Override
    @Transactional(readOnly = true)
    public Page<GiftDto> getAllGifts(Pageable pageable) {
        return giftRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftDto> getOnShelfGifts(Pageable pageable) {
        return giftRepository.findByStatus(GiftStatus.ON_SHELF, pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftDto getGiftDetail(Long id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));
        return toDto(gift);
    }

    @Override
    public GiftDto createGift(CreateGiftRequest request) {
        Gift gift = Gift.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .pointsCost(request.getPointsCost())
                .stock(request.getStock())
                .status(GiftStatus.ON_SHELF)
                .build();

        gift = giftRepository.save(gift);
        return toDto(gift);
    }

    @Override
    public GiftDto updateGift(Long id, UpdateGiftRequest request) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));

        // 只更新非空字段
        if (request.getName() != null) {
            gift.setName(request.getName());
        }
        if (request.getDescription() != null) {
            gift.setDescription(request.getDescription());
        }
        if (request.getIcon() != null) {
            gift.setIcon(request.getIcon());
        }
        if (request.getPointsCost() != null) {
            gift.setPointsCost(request.getPointsCost());
        }
        if (request.getStock() != null) {
            gift.setStock(request.getStock());
        }

        gift = giftRepository.save(gift);
        return toDto(gift);
    }

    @Override
    public void deleteGift(Long id) {
        if (!giftRepository.existsById(id)) {
            throw new EntityNotFoundException("道具不存在");
        }
        giftRepository.deleteById(id);
    }

    @Override
    public void onShelf(Long id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));
        gift.setStatus(GiftStatus.ON_SHELF);
        giftRepository.save(gift);
    }

    @Override
    public void offShelf(Long id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));
        gift.setStatus(GiftStatus.OFF_SHELF);
        giftRepository.save(gift);
    }

    /**
     * Entity 转 DTO
     */
    private GiftDto toDto(Gift gift) {
        return GiftDto.builder()
                .id(gift.getId())
                .name(gift.getName())
                .description(gift.getDescription())
                .icon(gift.getIcon() != null ? gift.getIcon() : defaultImageUrl)
                .pointsCost(gift.getPointsCost())
                .stock(gift.getStock())
                .status(gift.getStatus())
                .createdAt(gift.getCreatedAt())
                .updatedAt(gift.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public ExchangeGiftResponse exchangeGift(Long giftId, String userId) {
        // 1. 查询道具
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));

        // 2. 检查道具是否上架
        if (gift.getStatus() != GiftStatus.ON_SHELF) {
            return ExchangeGiftResponse.builder()
                    .success(false)
                    .giftName(gift.getName())
                    .pointsSpent(0)
                    .remainingPoints(0)
                    .message("该道具已下架")
                    .build();
        }

        // 3. 检查库存
        if (gift.getStock() <= 0) {
            return ExchangeGiftResponse.builder()
                    .success(false)
                    .giftName(gift.getName())
                    .pointsSpent(0)
                    .remainingPoints(0)
                    .message("该道具暂时缺货")
                    .build();
        }

        // 4. 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        // 5. 检查积分是否充足
        if (user.getTotalScore() < gift.getPointsCost()) {
            int need = gift.getPointsCost() - user.getTotalScore();
            return ExchangeGiftResponse.builder()
                    .success(false)
                    .giftName(gift.getName())
                    .pointsSpent(0)
                    .remainingPoints(user.getTotalScore())
                    .message("积分不足，还差 " + need + " 积分")
                    .build();
        }

        // 6. 扣除积分
        int pointsCost = gift.getPointsCost();
        user.setTotalScore(user.getTotalScore() - pointsCost);
        userRepository.save(user);

        // 7. 扣减库存
        gift.setStock(gift.getStock() - 1);
        giftRepository.save(gift);

        // 8. 记录积分变动（负数表示消耗）
        PointsRecord record = PointsRecord.builder()
                .userId(userId)
                .points(-pointsCost)
                .type(PointsType.GIFT_EXCHANGE)
                .description("兑换道具：" + gift.getName())
                .relatedId(giftId)
                .balanceAfter(user.getTotalScore())
                .build();
        pointsRecordRepository.save(record);

        // 9. 添加至用户背包
        UserItem userItem = UserItem.builder()
                .userId(userId)
                .giftId(giftId)
                .status(UserItemStatus.UNUSED)
                .build();
        userItemRepository.save(userItem);

        // 10. 返回成功响应
        return ExchangeGiftResponse.builder()
                .success(true)
                .giftName(gift.getName())
                .pointsSpent(pointsCost)
                .remainingPoints(user.getTotalScore())
                .message("兑换成功")
                .build();
    }

    @Override
    public List<UserItemDto> getUserItems(String userId) {
        List<UserItem> userItems = userItemRepository.findAllByUserId(userId);
        
        return userItems.stream().map(item -> {
            Gift gift = giftRepository.findById(item.getGiftId()).orElse(null);
            return UserItemDto.builder()
                    .id(item.getId())
                    .giftId(item.getGiftId())
                    .giftName(gift != null ? gift.getName() : "未知道具")
                    .giftIcon(gift != null ? (gift.getIcon() != null ? gift.getIcon() : defaultImageUrl) : defaultImageUrl)
                    .status(item.getStatus())
                    .redeemedAt(item.getRedeemedAt())
                    .usedAt(item.getUsedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void useUserItem(Long userItemId, String userId) {
        UserItem userItem = userItemRepository.findById(userItemId)
                .orElseThrow(() -> new EntityNotFoundException("道具记录不存在"));

        // 验证是否属于当前用户
        if (!userItem.getUserId().equals(userId)) {
            throw new RuntimeException("无权使用该道具");
        }

        // 检查是否已使用
        if (userItem.getStatus() == UserItemStatus.USED) {
            throw new RuntimeException("该道具已使用");
        }

        // 标记为已使用
        userItem.setStatus(UserItemStatus.USED);
        userItem.setUsedAt(LocalDateTime.now());
        userItemRepository.save(userItem);

        // TODO: 根据道具类型执行具体逻辑（如改名卡则修改用户名）
    }

    @Override
    public Page<UserItemDto> getUserExchangeRecords(String userId, Pageable pageable) {
        // 查询用户的道具兑换记录（按兑换时间倒序）
        return userItemRepository.findByUserIdOrderByRedeemedAtDesc(userId, pageable).map(item -> {
            Gift gift = giftRepository.findById(item.getGiftId()).orElse(null);
            return UserItemDto.builder()
                    .id(item.getId())
                    .giftId(item.getGiftId())
                    .giftName(gift != null ? gift.getName() : "未知道具")
                    .giftIcon(gift != null ? (gift.getIcon() != null ? gift.getIcon() : defaultImageUrl) : defaultImageUrl)
                    .status(item.getStatus())
                    .redeemedAt(item.getRedeemedAt())
                    .usedAt(item.getUsedAt())
                    .build();
        });
    }

    @Override
    public String uploadGiftImage(Long giftId, MultipartFile file) {
        // 查询道具
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("道具不存在"));

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只允许上传图片文件");
        }

        // 上传到 MinIO，路径按 giftId 区分
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("gifts/" + giftId + "/")
                .upload();

        if (fileInfo == null) {
            throw new RuntimeException("道具图片上传失败");
        }

        // 更新道具图片 URL
        gift.setIcon(fileInfo.getUrl());
        giftRepository.save(gift);

        return fileInfo.getUrl();
    }
}