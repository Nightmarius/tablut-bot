package ch.zuehlke.fullstack.hackathon.model;

import ch.zuehlke.common.TournamentDto;
import ch.zuehlke.common.TournamentState;

import java.util.List;

public class TournamentMapper {

    public static TournamentDto map(Tournament tournament, List<Game> games) {
        return TournamentDto.builder()
                .id(tournament.getTournamentId())
                .players(tournament.getPlayers())
                .status(tournament.getStatus())
                .state(new TournamentState(games.stream().map(GameMapper::map).toList()))
                .winner(tournament.getWinner().orElse(null))
                .scores(tournament.getScores())
                .build();
    }
}
