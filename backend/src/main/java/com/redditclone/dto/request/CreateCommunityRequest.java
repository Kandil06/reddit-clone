package com.redditclone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommunityRequest {
    @NotBlank
    @Size(min = 3, max = 21)
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Only letters, numbers and underscores allowed")
    private String name;

    @Size(max = 500)
    private String description;
}
