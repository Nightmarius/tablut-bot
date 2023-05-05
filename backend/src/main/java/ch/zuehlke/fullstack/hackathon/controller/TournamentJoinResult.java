package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.PlayerName;
import org.springframework.lang.Nullable;

public record TournamentJoinResult(@Nullable PlayerName playerName, TournamentJoinResultType resultType) {

    public enum TournamentJoinResultType {
        SUCCESS,
        TOURNAMENT_FULL,
        TOURNAMENT_NOT_FOUND
    }
}
