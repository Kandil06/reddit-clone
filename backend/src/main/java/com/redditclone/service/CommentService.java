package com.redditclone.service;

import com.redditclone.dto.request.CreateCommentRequest;
import com.redditclone.dto.response.CommentResponse;
import com.redditclone.entity.Comment;
import com.redditclone.entity.Post;
import com.redditclone.entity.User;
import com.redditclone.exception.BadRequestException;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.exception.UnauthorizedActionException;
import com.redditclone.repository.CommentRepository;
import com.redditclone.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final VoteService voteService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public CommentResponse create(CreateCommentRequest request) {
        Post post = postService.findById(request.getPostId());
        User user = currentUserProvider.requireCurrentUser();

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            if (!parent.getPost().getId().equals(post.getId())) {
                throw new BadRequestException("Parent comment does not belong to this post");
            }
        }

        Comment comment = Comment.builder()
                .post(post)
                .author(user)
                .content(request.getContent())
                .parent(parent)
                .build();
        commentRepository.save(comment);
        return toResponse(comment, false);
    }

    /** Returns top-level comments for a post, each with its replies nested inside. */
    public List<CommentResponse> getThreadForPost(Long postId) {
        Post post = postService.findById(postId);
        List<Comment> all = commentRepository.findByPostOrderByCreatedAtAsc(post);

        return all.stream()
                .filter(c -> c.getParent() == null)
                .map(c -> toResponseWithReplies(c, all))
                .toList();
    }

    private CommentResponse toResponseWithReplies(Comment comment, List<Comment> all) {
        List<CommentResponse> replies = all.stream()
                .filter(c -> c.getParent() != null && c.getParent().getId().equals(comment.getId()))
                .map(c -> toResponseWithReplies(c, all))
                .toList();
        CommentResponse response = toResponse(comment, false);
        response.setReplies(replies);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        User user = currentUserProvider.requireCurrentUser();
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public void vote(Long commentId, com.redditclone.entity.VoteType voteType) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        voteService.voteOnComment(comment, voteType);
    }

    private CommentResponse toResponse(Comment comment, boolean includeEmptyReplies) {
        long up = voteService.upvotes(comment);
        long down = voteService.downvotes(comment);
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorUsername(comment.getAuthor().getUsername())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .upvotes(up)
                .downvotes(down)
                .score((int) (up - down))
                .currentUserVote(voteService.currentUserVoteOnComment(comment))
                .replies(includeEmptyReplies ? List.of() : null)
                .build();
    }
}
