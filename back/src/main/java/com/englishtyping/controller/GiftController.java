package com.englishtyping.controller;

import com.englishtyping.dto.ExchangeGiftResponse;
import com.englishtyping.dto.GiftDto;
import com.englishtyping.dto.UserItemDto;
import com.englishtyping.service.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 道具商城控制器（用户端）
 */
@RestController
@RequestMapping("/gifts")
@RequiredArgsConstructor
public class GiftController {

    private final GiftService giftService;

    /**
     * 获取道具列表（仅显示上架的道具）
     * GET /gifts
     *
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页大小（默认 20）
     */
    @GetMapping
    public ResponseEntity<ApiResult<Page<GiftDto>>> getGiftList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GiftDto> gifts = giftService.getOnShelfGifts(pageRequest);
        return ResponseEntity.ok(ApiResult.success(gifts));
    }

    /**
     * 获取道具详情
     * GET /gifts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<GiftDto>> getGiftDetail(@PathVariable Long id) {
        GiftDto gift = giftService.getGiftDetail(id);
        return ResponseEntity.ok(ApiResult.success(gift));
    }

    /**
     * 兑换道具
     * POST /gifts/{id}/exchange
     */
    @PostMapping("/{id}/exchange")
    public ResponseEntity<ApiResult<ExchangeGiftResponse>> exchangeGift(
            @PathVariable("id") Long giftId,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        ExchangeGiftResponse response = giftService.exchangeGift(giftId, userId);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    /**
     * 获取我的道具背包
     * GET /user/items
     */
    @GetMapping("/user/items")
    public ResponseEntity<ApiResult<List<UserItemDto>>> getMyItems(Authentication authentication) {
        String userId = authentication.getName();
        List<UserItemDto> items = giftService.getUserItems(userId);
        return ResponseEntity.ok(ApiResult.success(items));
    }

    /**
     * 使用道具
     * POST /user/items/{id}/use
     */
    @PostMapping("/user/items/{id}/use")
    public ResponseEntity<ApiResult<Void>> useItem(
            @PathVariable("id") Long userItemId,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        giftService.useUserItem(userItemId, userId);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 获取我的兑换记录（分页）
     * GET /user/exchange-records
     *
     * @param page 页码（从 0 开始，默认 0）
     * @param size 每页大小（默认 20）
     */
    @GetMapping("/user/exchange-records")
    public ResponseEntity<ApiResult<Page<UserItemDto>>> getExchangeRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "redeemedAt"));
        Page<UserItemDto> records = giftService.getUserExchangeRecords(userId, pageRequest);
        return ResponseEntity.ok(ApiResult.success(records));
    }

    /**
     * 统一 API 响应格式
     */
    public record ApiResult<T>(int code, String message, T data) {
        public static <T> ApiResult<T> success(T data) {
            return new ApiResult<>(200, "success", data);
        }
    }
}
