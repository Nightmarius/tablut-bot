package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.GameId;
import ch.zuehlke.common.TournamentId;
import ch.zuehlke.fullstack.hackathon.model.GameMapper;
import ch.zuehlke.fullstack.hackathon.model.TournamentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate template;

    private final LobbyService lobbyService;
    private final GameService gameService;
    private final TournamentService tournamentService;

    public void notifyLobbyUpdate() {
        template.convertAndSend("/topic/lobby/", lobbyService.getPlayers());
    }

    public void notifyGameUpdate(GameId gameId) {
        gameService.getGame(gameId.value())
                .map(GameMapper::map)
                .ifPresent(game -> template.convertAndSend("/topic/game/", game)
                );
    }

    public void notifyTournamentUpdate(TournamentId tournamentId) {
        tournamentService.getTournament(tournamentId.value())
                .map(tournament -> TournamentMapper.map(tournament, gameService.getGames(tournament.getGameIds())))
                .ifPresent(tournamentDto -> template.convertAndSend("/topic/tournament/" + tournamentId.value(), tournamentDto)
                );
    }
}
