package com.redditclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String bio;
    private String avatarUrl;
    private Instant createdAt;
    private long postCount;
    private long commentCount;
    private long karma;
}
