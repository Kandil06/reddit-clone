package com.redditclone.repository;

import com.redditclone.entity.Comment;
import com.redditclone.entity.Post;
import com.redditclone.entity.User;
import com.redditclone.entity.Vote;
import com.redditclone.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndPost(User user, Post post);
    Optional<Vote> findByUserAndComment(User user, Comment comment);
    long countByPostAndVoteType(Post post, VoteType voteType);
    long countByCommentAndVoteType(Comment comment, VoteType voteType);
}
