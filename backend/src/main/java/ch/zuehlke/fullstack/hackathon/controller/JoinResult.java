package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.PlayerName;
import org.springframework.lang.Nullable;

public record JoinResult(@Nullable PlayerName name, JoinResultType resultType) {

    public enum JoinResultType {
        SUCCESS,
        GAME_FULL,
        GAME_NOT_FOUND
    }
}
