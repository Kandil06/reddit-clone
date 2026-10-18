package com.redditclone.dto.request;

import com.redditclone.entity.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotNull
    private VoteType voteType; // UP or DOWN; send same value again to remove vote
}
