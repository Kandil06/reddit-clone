package com.redditclone.service;

import com.redditclone.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostService postService;
    private final CommunityService communityService;
    private final UserService userService;

    public SearchResponse search(String keyword) {
        return SearchResponse.builder()
                .posts(postService.search(keyword, PageRequest.of(0, 20)).getContent())
                .communities(communityService.search(keyword))
                .users(userService.search(keyword))
                .build();
    }
}
