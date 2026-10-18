package com.redditclone.repository;

import com.redditclone.entity.Community;
import com.redditclone.entity.CommunityMembership;
import com.redditclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityMembershipRepository extends JpaRepository<CommunityMembership, Long> {
    Optional<CommunityMembership> findByUserAndCommunity(User user, Community community);
    long countByCommunity(Community community);
    boolean existsByUserAndCommunity(User user, Community community);
}
