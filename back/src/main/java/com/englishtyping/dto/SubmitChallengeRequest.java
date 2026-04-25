package com.englishtyping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitChallengeRequest {

    @NotBlank
    private String contentType;  // WORD / SENTENCE

    @NotBlank
    private String timeMode;     // TIMED / INFINITE

    private Integer timeLimit;   // 限时秒数，无限模式为 null

    @NotNull
    private Integer passedCount;

    @NotNull
    private Double accuracy;

    private Long timeMs;         // 无限模式用时

    @NotNull
    private Integer wpm;
}
