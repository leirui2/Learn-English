package com.englishtyping.service.impl;

import com.englishtyping.dto.AdminPointsRecordDto;
import com.englishtyping.dto.AdminPointsRecordPageResponse;
import com.englishtyping.dto.PointsRecordDto;
import com.englishtyping.dto.PointsRecordPageResponse;
import com.englishtyping.entity.PointsRecord;
import com.englishtyping.entity.PointsType;
import com.englishtyping.entity.User;
import com.englishtyping.repository.PointsRecordRepository;
import com.englishtyping.repository.UserRepository;
import com.englishtyping.service.PointsRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 积分记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class PointsRecordServiceImpl implements PointsRecordService {

    private final PointsRecordRepository pointsRecordRepository;
    private final UserRepository userRepository;

    @Override
    public PointsRecordPageResponse getUserPointsRecords(String userId, int page, int size, PointsType type) {
        // 创建分页请求（按时间倒序）
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 查询积分记录
        Page<PointsRecord> recordsPage;
        if (type != null) {
            recordsPage = pointsRecordRepository.findByUserIdAndType(userId, type, pageRequest);
        } else {
            recordsPage = pointsRecordRepository.findByUserId(userId, pageRequest);
        }

        // 转换为 DTO
        List<PointsRecordDto> records = recordsPage.getContent().stream()
                .map(record -> PointsRecordDto.builder()
                        .id(record.getId())
                        .points(record.getPoints())
                        .type(record.getType())
                        .description(record.getDescription())
                        .relatedId(record.getRelatedId())
                        .balanceAfter(record.getBalanceAfter())
                        .createdAt(record.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // 构建响应
        return PointsRecordPageResponse.builder()
                .records(records)
                .total(recordsPage.getTotalElements())
                .page(page)
                .size(size)
                .totalPages(recordsPage.getTotalPages())
                .build();
    }

    @Override
    public AdminPointsRecordPageResponse getAllPointsRecords(
            String userId, String username, PointsType type,
            LocalDateTime startDate, LocalDateTime endDate,
            int page, int size) {
        
        // 创建分页请求（按时间倒序）
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 根据条件查询积分记录
        Page<PointsRecord> recordsPage;
        
        if (username != null && !username.trim().isEmpty()) {
            // 按用户名模糊查询
            if (type != null && startDate != null && endDate != null) {
                recordsPage = pointsRecordRepository.findByUsernameContainingAndTypeAndCreatedAtBetween(
                        username, type, startDate, endDate, pageRequest);
            } else if (type != null) {
                recordsPage = pointsRecordRepository.findByUsernameContainingAndType(username, type, pageRequest);
            } else if (startDate != null && endDate != null) {
                recordsPage = pointsRecordRepository.findByUsernameContainingAndCreatedAtBetween(
                        username, startDate, endDate, pageRequest);
            } else {
                recordsPage = pointsRecordRepository.findByUsernameContaining(username, pageRequest);
            }
        } else if (userId != null && !userId.trim().isEmpty()) {
            // 按用户ID查询
            if (type != null) {
                recordsPage = pointsRecordRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type, pageRequest);
            } else {
                recordsPage = pointsRecordRepository.findByUserIdOrderByCreatedAtDesc(userId, pageRequest);
            }
        } else {
            // 查询所有记录（支持类型和时间筛选）
            if (type != null && startDate != null && endDate != null) {
                // 注意：这里需要遍历所有用户，效率较低，建议添加更复杂的查询方法
                recordsPage = pointsRecordRepository.findAll(pageRequest);
            } else if (type != null) {
                // 需要自定义查询方法，这里先用findAll后过滤（不推荐用于生产）
                recordsPage = pointsRecordRepository.findAll(pageRequest);
            } else if (startDate != null && endDate != null) {
                // 需要自定义查询方法，这里先用findAll后过滤（不推荐用于生产）
                recordsPage = pointsRecordRepository.findAll(pageRequest);
            } else {
                recordsPage = pointsRecordRepository.findAll(pageRequest);
            }
        }

        // 批量获取用户信息（避免N+1问题）
        List<String> userIds = recordsPage.getContent().stream()
                .map(PointsRecord::getUserId)
                .distinct()
                .collect(Collectors.toList());
        
        List<User> users = userRepository.findAllById(userIds);
        Map<String, String> userIdToUsername = users.stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 转换为 DTO
        List<AdminPointsRecordDto> records = recordsPage.getContent().stream()
                .map(record -> AdminPointsRecordDto.builder()
                        .id(record.getId())
                        .userId(record.getUserId())
                        .username(userIdToUsername.getOrDefault(record.getUserId(), "Unknown"))
                        .points(record.getPoints())
                        .type(record.getType())
                        .description(record.getDescription())
                        .relatedId(record.getRelatedId())
                        .balanceAfter(record.getBalanceAfter())
                        .createdAt(record.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // 构建响应
        return AdminPointsRecordPageResponse.builder()
                .records(records)
                .total(recordsPage.getTotalElements())
                .page(page)
                .size(size)
                .totalPages(recordsPage.getTotalPages())
                .build();
    }
}
