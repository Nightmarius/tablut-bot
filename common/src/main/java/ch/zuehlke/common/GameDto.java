package ch.zuehlke.common;

import lombok.Builder;

import java.util.List;

@Builder
public record GameDto(GameId id, List<PlayerName> players, GameStatus status, GameState state, PlayerName winner) {
}
