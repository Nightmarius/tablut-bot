package ch.zuehlke.common;

import java.util.ArrayList;
import java.util.List;

// Improve: Split this into DTO and domain object
public record GameState(PlayRequest playRequest, List<Move> moves, Board board) {

    public GameState(PlayRequest playRequest, List<Move> moves) {
        this(playRequest, moves, Board.createInitialBoard());
    }

    public GameState() {
        this(null, new ArrayList<>(), Board.createInitialBoard());
    }


}
