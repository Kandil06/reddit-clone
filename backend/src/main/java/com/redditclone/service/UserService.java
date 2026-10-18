package com.redditclone.service;

import com.redditclone.dto.request.UpdateProfileRequest;
import com.redditclone.dto.response.UserProfileResponse;
import com.redditclone.entity.Post;
import com.redditclone.entity.User;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.PostRepository;
import com.redditclone.repository.UserRepository;
import com.redditclone.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final VoteService voteService;
    private final CurrentUserProvider currentUserProvider;

    public UserProfileResponse getProfile(String username) {
        User user = findByUsername(username);
        return toProfileResponse(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = currentUserProvider.requireCurrentUser();
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        userRepository.save(user);
        return toProfileResponse(user);
    }

    public List<UserProfileResponse> search(String keyword) {
        return userRepository.findByUsernameContainingIgnoreCase(keyword).stream()
                .map(this::toProfileResponse).toList();
    }

    private UserProfileResponse toProfileResponse(User user) {
        // karma = sum of (upvotes - downvotes) across all of the user's posts
        List<Post> posts = postRepository.findAll().stream()
                .filter(p -> p.getAuthor().getId().equals(user.getId())).toList();
        long karma = posts.stream()
                .mapToLong(p -> voteService.upvotes(p) - voteService.downvotes(p))
                .sum();

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .postCount(posts.size())
                .commentCount(countUserComments(user))
                .karma(karma)
                .build();
    }

    private long countUserComments(User user) {
        return commentRepository.findAll().stream()
                .filter(c -> c.getAuthor().getId().equals(user.getId())).count();
    }
}
