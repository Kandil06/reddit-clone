package com.redditclone.service;

import com.redditclone.dto.request.CreateCommunityRequest;
import com.redditclone.dto.response.CommunityResponse;
import com.redditclone.entity.Community;
import com.redditclone.entity.CommunityMembership;
import com.redditclone.entity.User;
import com.redditclone.exception.BadRequestException;
import com.redditclone.exception.ResourceNotFoundException;
import com.redditclone.repository.CommunityMembershipRepository;
import com.redditclone.repository.CommunityRepository;
import com.redditclone.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMembershipRepository membershipRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public CommunityResponse create(CreateCommunityRequest request) {
        if (communityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("A community named r/" + request.getName() + " already exists");
        }
        User user = currentUserProvider.requireCurrentUser();

        Community community = Community.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .build();
        communityRepository.save(community);

        // creator automatically joins their own community
        membershipRepository.save(CommunityMembership.builder()
                .user(user).community(community).build());

        return toResponse(community);
    }

    public List<CommunityResponse> listAll() {
        return communityRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CommunityResponse getByName(String name) {
        Community community = findByName(name);
        return toResponse(community);
    }

    public Community findByName(String name) {
        return communityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Community r/" + name + " not found"));
    }

    public Community findById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found"));
    }

    @Transactional
    public CommunityResponse joinOrLeave(Long communityId) {
        Community community = findById(communityId);
        User user = currentUserProvider.requireCurrentUser();

        Optional<CommunityMembership> existing = membershipRepository.findByUserAndCommunity(user, community);
        if (existing.isPresent()) {
            membershipRepository.delete(existing.get());
        } else {
            membershipRepository.save(CommunityMembership.builder().user(user).community(community).build());
        }
        return toResponse(community);
    }

    public List<CommunityResponse> search(String keyword) {
        return communityRepository.findByNameContainingIgnoreCase(keyword).stream().map(this::toResponse).toList();
    }

    private CommunityResponse toResponse(Community community) {
        long memberCount = membershipRepository.countByCommunity(community);
        boolean joined = currentUserProvider.getCurrentUser()
                .map(u -> membershipRepository.existsByUserAndCommunity(u, community))
                .orElse(false);

        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .createdBy(community.getCreatedBy().getUsername())
                .createdAt(community.getCreatedAt())
                .memberCount(memberCount)
                .joinedByCurrentUser(joined)
                .build();
    }
}
