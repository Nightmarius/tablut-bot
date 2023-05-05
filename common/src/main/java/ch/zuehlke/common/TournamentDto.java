package ch.zuehlke.common;

import lombok.Builder;

import java.util.List;

@Builder
public record TournamentDto(TournamentId id, List<PlayerName> players, TournamentStatus status, TournamentState state,
                            PlayerName winner, List<Score> scores) {
}
