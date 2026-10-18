package com.redditclone.repository;

import com.redditclone.entity.Comment;
import com.redditclone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
    long countByPost(Post post);
}
