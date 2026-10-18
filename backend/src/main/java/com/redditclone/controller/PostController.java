package com.redditclone.controller;

import com.redditclone.dto.request.CreatePostRequest;
import com.redditclone.dto.request.VoteRequest;
import com.redditclone.dto.response.PostResponse;
import com.redditclone.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> feed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.feed(PageRequest.of(page, size)));
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<Page<PostResponse>> byCommunity(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.byCommunity(communityId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<PostResponse> vote(@PathVariable Long id, @Valid @RequestBody VoteRequest request) {
        postService.vote(id, request.getVoteType());
        return ResponseEntity.ok(postService.getById(id));
    }
}
