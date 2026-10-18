package com.redditclone.controller;

import com.redditclone.dto.request.CreateCommunityRequest;
import com.redditclone.dto.response.CommunityResponse;
import com.redditclone.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<CommunityResponse> create(@Valid @RequestBody CreateCommunityRequest request) {
        return ResponseEntity.ok(communityService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CommunityResponse>> listAll() {
        return ResponseEntity.ok(communityService.listAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<CommunityResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(communityService.getByName(name));
    }

    @PostMapping("/{id}/toggle-membership")
    public ResponseEntity<CommunityResponse> joinOrLeave(@PathVariable Long id) {
        return ResponseEntity.ok(communityService.joinOrLeave(id));
    }
}
