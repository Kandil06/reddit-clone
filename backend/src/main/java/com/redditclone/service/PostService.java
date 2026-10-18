package com.redditclone.service;

import com.redditclone.dto.request.CreatePostRequest;
import com.redditclone.dto.response.PostResponse;
import com.redditclone.entity.Community;
import com.redditclone.entity.Post;
import com.redditclone.entity.User;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.exception.UnauthorizedActionException;
import com.redditclone.repository.CommentRepository;
import com.redditclone.repository.PostRepository;
import com.redditclone.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommunityService communityService;
    private final VoteService voteService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public PostResponse create(CreatePostRequest request) {
        Community community = communityService.findById(request.getCommunityId());
        User user = currentUserProvider.requireCurrentUser();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .community(community)
                .author(user)
                .build();
        postRepository.save(post);
        return toResponse(post);
    }

    public Page<PostResponse> feed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::toResponse);
    }

    public Page<PostResponse> byCommunity(Long communityId, Pageable pageable) {
        Community community = communityService.findById(communityId);
        return postRepository.findByCommunity(community, pageable).map(this::toResponse);
    }

    public PostResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    @Transactional
    public void delete(Long id) {
        Post post = findById(id);
        User user = currentUserProvider.requireCurrentUser();
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    @Transactional
    public void vote(Long postId, com.redditclone.entity.VoteType voteType) {
        Post post = findById(postId);
        voteService.voteOnPost(post, voteType);
    }

    public Page<PostResponse> search(String keyword, Pageable pageable) {
        return postRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::toResponse);
    }

    PostResponse toResponse(Post post) {
        long up = voteService.upvotes(post);
        long down = voteService.downvotes(post);
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .communityName(post.getCommunity().getName())
                .communityId(post.getCommunity().getId())
                .authorUsername(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .upvotes(up)
                .downvotes(down)
                .score((int) (up - down))
                .commentCount(commentRepository.countByPost(post))
                .currentUserVote(voteService.currentUserVoteOnPost(post))
                .build();
    }
}
