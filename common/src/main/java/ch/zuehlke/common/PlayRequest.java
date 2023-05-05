package ch.zuehlke.common;

import java.util.Set;

public record PlayRequest(PlayerName playerName, RequestId requestId, GameId gameId, boolean attacker, Board board,
                          Set<GameAction> possibleActions) {

    public PlayRequest(PlayerName playerName, GameId gameId, boolean attacker, Board board, Set<GameAction> possibleActions) {
        this(playerName, new RequestId(), gameId, attacker, board, possibleActions);
    }
}
