package com.redditclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private Long parentId;
    private Instant createdAt;
    private long upvotes;
    private long downvotes;
    private int score;
    private String currentUserVote;
    private List<CommentResponse> replies;
}
