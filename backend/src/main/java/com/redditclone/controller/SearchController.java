package com.redditclone.controller;

import com.redditclone.dto.response.SearchResponse;
import com.redditclone.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String q) {
        return ResponseEntity.ok(searchService.search(q));
    }
}
