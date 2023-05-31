package ch.zuehlke.common;

import java.util.ArrayList;
import java.util.List;

public record GameState(PlayRequest playRequest, List<Move> moves, Board board) {

    public GameState(PlayRequest playRequest, List<Move> moves) {
        this(playRequest, moves, Board.createInitialBoard());
    }

}
