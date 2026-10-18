package com.redditclone.controller;

import com.redditclone.dto.request.UpdateProfileRequest;
import com.redditclone.dto.response.UserProfileResponse;
import com.redditclone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
}
