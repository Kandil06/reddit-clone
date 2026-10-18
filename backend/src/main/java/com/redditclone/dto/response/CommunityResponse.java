package com.redditclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class CommunityResponse {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Instant createdAt;
    private long memberCount;
    private boolean joinedByCurrentUser;
}
