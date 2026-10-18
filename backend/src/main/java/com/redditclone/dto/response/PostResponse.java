package com.redditclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String communityName;
    private Long communityId;
    private String authorUsername;
    private Instant createdAt;
    private long upvotes;
    private long downvotes;
    private int score;
    private long commentCount;
    private String currentUserVote; // "UP", "DOWN", or null
}
