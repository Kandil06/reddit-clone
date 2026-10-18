package com.redditclone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchResponse {
    private List<PostResponse> posts;
    private List<CommunityResponse> communities;
    private List<UserProfileResponse> users;
}
