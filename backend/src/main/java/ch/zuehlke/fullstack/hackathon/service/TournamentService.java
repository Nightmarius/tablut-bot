package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.GameId;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.TournamentId;
import ch.zuehlke.fullstack.hackathon.controller.EditResult;
import ch.zuehlke.fullstack.hackathon.controller.StartResult;
import ch.zuehlke.fullstack.hackathon.model.Game;
import ch.zuehlke.fullstack.hackathon.model.Tournament;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TournamentService {
    private final LobbyService lobbyService;
    private final GameService gameService;
    // Improve: Instead of storing this in-memory, store it in a database
    private final List<Tournament> tournaments = new ArrayList<>();
    private static int counter = 0;

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public Tournament createTournament() {
        // Improve: Find a better way to create game ids
        counter += 1;
        var tournament = new Tournament(new TournamentId(counter));
        tournaments.add(tournament);
        lobbyService.getPlayers().forEach(tournament::addPlayer);
        return tournament;
    }


    public boolean deleteTournament(int tournamentId) {
        return tournaments.removeIf(t -> t.getTournamentId().value() == tournamentId);
    }

    public Optional<Tournament> getTournament(int tournamentId) {
        return tournaments.stream()
                .filter(t -> t.getTournamentId().value() == tournamentId)
                .findFirst();
    }

    public StartResult startTournament(int tournamentId) {
        Optional<Tournament> optionalTournament = getTournament(tournamentId);
        if (optionalTournament.isEmpty()) {
            return StartResult.NOT_FOUND;
        }

        Tournament tournament = optionalTournament.get();
        if (!tournament.canStartTournament()) {
            return StartResult.NOT_ENOUGH_PLAYERS;
        }

        tournament.startTournament();
        generateRoundRobin(tournamentId, tournament.getPlayers());

        return StartResult.SUCCESS;
    }

    public EditResult editPlayers(TournamentId tournamentId, List<PlayerName> playerNames) {
        Optional<Tournament> tournament = getTournament(tournamentId.value());
        if (tournament.isEmpty()) {
            return EditResult.NOT_FOUND;
        }
        tournament.get().editPlayers(playerNames);
        return EditResult.SUCCESS;
    }

    private void generateRoundRobin(int tournamentId, List<PlayerName> players) {
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.size(); j++) {
                if (i == j) continue;

                log.info("Created game between " + players.get(i) + " and " + players.get(j));
                Game game = gameService.createGame();
                game.addPlayer(players.get(i));
                game.addPlayer(players.get(j));
                getTournament(tournamentId).ifPresent(t -> t.getGameIds().add(game.getGameId()));
            }
        }
        Collections.shuffle(gameService.getGames());
    }

    public void update(int gameId) {
        int tournamentId = getTournamentId(gameId);
        getTournament(tournamentId).ifPresent(t -> t.updateFromGames(gameService.getGames(t.getGameIds())));
    }

    public int getTournamentId(int gameId) {
        Optional<Tournament> tournament = tournaments.stream().filter(t -> t.getGameIds().contains(new GameId(gameId))).findFirst();
        return tournament.map(t -> t.getTournamentId().value()).orElse(0);
    }
}
