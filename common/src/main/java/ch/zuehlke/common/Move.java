package ch.zuehlke.common;

public record Move(PlayerName name, RequestId requestId, GameId gameId, GameAction action) {

}
