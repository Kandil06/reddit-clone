package com.redditclone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequest {
    @NotNull
    private Long communityId;

    @NotBlank
    @Size(max = 300)
    private String title;

    private String content;
    private String imageUrl;
}
