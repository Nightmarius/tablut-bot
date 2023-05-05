package ch.zuehlke.fullstack.hackathon.model;

import ch.zuehlke.common.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Getter
public class Tournament {

    public static final int MAX_PLAYERS = 20;
    public static final int MIN_PLAYERS = 2;

    private final TournamentId tournamentId;
    private final List<PlayerName> players = new ArrayList<>();

    private TournamentStatus status = TournamentStatus.NOT_STARTED;

    private final List<GameId> gameIds = new ArrayList<>();
    private List<Score> scores = new ArrayList<>();

    public boolean addPlayer(PlayerName player) {
        if (players.size() >= MAX_PLAYERS) {
            return false;
        }
        players.add(player);
        return true;
    }

    public boolean canStartTournament() {
        return players.size() >= MIN_PLAYERS &&
                players.size() <= MAX_PLAYERS &&
                status == TournamentStatus.NOT_STARTED;
    }

    public void startTournament() {
        if (!canStartTournament()) {
            return;
        }

        status = TournamentStatus.IN_PROGRESS;
    }

    public void finishTournament() {
        status = TournamentStatus.FINISHED;
    }

    public void updateFromGames(List<Game> games) {
        var allGamesFinished = games.stream()
                .map(Game::getStatus)
                .allMatch(s -> s == GameStatus.FINISHED);

        if (allGamesFinished) {
            finishTournament();
        }

        var finishedGames = games.stream()
                .filter(g -> g.getStatus() == GameStatus.FINISHED)
                .toList();

        var multiMap = new HashMap<PlayerName, List<Game>>();
        for (var game : finishedGames) {
            for (var player : game.getPlayers()) {
                multiMap.computeIfAbsent(player, k -> new ArrayList<>())
                        .add(game);
            }
        }

        scores = multiMap.entrySet().stream()
                .map(e -> new Score(e.getKey(), calculateScore(e.getKey(), e.getValue())))
                .sorted(Comparator.comparing(Score::score).reversed())
                .toList();
    }

    private static int calculateScore(PlayerName playerId, List<Game> games) {
        int score = 0;

        for (Game game : games) {
            Optional<PlayerName> winner = game.getWinner();
            if (winner.isEmpty()) {
                score += 1;
            } else if (winner.get().equals(playerId)) {
                score += 3;
            }
        }

        return score;
    }

    public Optional<PlayerName> getWinner() {
        if (status != TournamentStatus.FINISHED || scores.isEmpty()) {
            return Optional.empty();
        }

        if (scores.size() >= 2 && scores.get(0).score() == scores.get(1).score()) {
            return Optional.empty();
        }

        return Optional.of(scores.get(0).playerName());
    }

    public List<Score> getScores() {
        return scores;
    }
}
