package com.englishtyping.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChallengeLeaderboardEntryDto {
    private Integer rank;
    private String userId;
    private String username;
    private Integer passedCount;
    private Double accuracy;
    private Long timeMs;
    private Integer wpm;
    private LocalDateTime createdAt;
    private Boolean isCurrentUser;
}
