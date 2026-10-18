package com.redditclone.repository;

import com.redditclone.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Community> findByNameContainingIgnoreCase(String keyword);
}
