package com.redditclone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    // null for a top-level comment
    private Long parentId;
}
