package com.redditclone.service;

import com.redditclone.entity.*;
import com.redditclone.repository.VoteRepository;
import com.redditclone.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final CurrentUserProvider currentUserProvider;

    /** Casts, changes, or removes (if same type sent again) a vote on a post. */
    @Transactional
    public void voteOnPost(Post post, VoteType voteType) {
        User user = currentUserProvider.requireCurrentUser();
        Optional<Vote> existing = voteRepository.findByUserAndPost(user, post);
        applyVote(existing, voteType, () -> Vote.builder().user(user).post(post).voteType(voteType).build());
    }

    @Transactional
    public void voteOnComment(Comment comment, VoteType voteType) {
        User user = currentUserProvider.requireCurrentUser();
        Optional<Vote> existing = voteRepository.findByUserAndComment(user, comment);
        applyVote(existing, voteType, () -> Vote.builder().user(user).comment(comment).voteType(voteType).build());
    }

    private void applyVote(Optional<Vote> existing, VoteType voteType, java.util.function.Supplier<Vote> newVote) {
        if (existing.isPresent()) {
            Vote vote = existing.get();
            if (vote.getVoteType() == voteType) {
                voteRepository.delete(vote); // toggling same vote off
            } else {
                vote.setVoteType(voteType);
                voteRepository.save(vote);
            }
        } else {
            voteRepository.save(newVote.get());
        }
    }

    public long upvotes(Post post) { return voteRepository.countByPostAndVoteType(post, VoteType.UP); }
    public long downvotes(Post post) { return voteRepository.countByPostAndVoteType(post, VoteType.DOWN); }
    public long upvotes(Comment comment) { return voteRepository.countByCommentAndVoteType(comment, VoteType.UP); }
    public long downvotes(Comment comment) { return voteRepository.countByCommentAndVoteType(comment, VoteType.DOWN); }

    public String currentUserVoteOnPost(Post post) {
        return currentUserProvider.getCurrentUser()
                .flatMap(u -> voteRepository.findByUserAndPost(u, post))
                .map(v -> v.getVoteType().name())
                .orElse(null);
    }

    public String currentUserVoteOnComment(Comment comment) {
        return currentUserProvider.getCurrentUser()
                .flatMap(u -> voteRepository.findByUserAndComment(u, comment))
                .map(v -> v.getVoteType().name())
                .orElse(null);
    }
}
