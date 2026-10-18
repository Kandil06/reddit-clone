package com.redditclone.controller;

import com.redditclone.dto.request.CreateCommentRequest;
import com.redditclone.dto.request.VoteRequest;
import com.redditclone.dto.response.CommentResponse;
import com.redditclone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.create(request));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getThread(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getThreadForPost(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Void> vote(@PathVariable Long id, @Valid @RequestBody VoteRequest request) {
        commentService.vote(id, request.getVoteType());
        return ResponseEntity.noContent().build();
    }
}
