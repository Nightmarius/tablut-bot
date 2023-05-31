package ch.zuehlke.common;

import org.junit.jupiter.api.Test;

import static ch.zuehlke.common.Board.SIZE;
import static org.junit.jupiter.api.Assertions.*;

class GameActionTest {

    private final Coordinates kingCoordinates = new Coordinates(4, 4);

    @Test
    void movesNextToTheKing() {
        GameAction gameAction = new GameAction(kingCoordinates, new Coordinates(4, 0));
        Board boardWithKingInTheCenter = createBoardWithSparePieces();

        assertTrue(gameAction.isAWinningDefendingMove(boardWithKingInTheCenter));
    }

    @Test
    void isAWinningDefendingMove() {
        GameAction gameAction = new GameAction(kingCoordinates, new Coordinates(4, 0));
        Board boardWithKingInTheCenter = createBoardWithSparePieces();

        assertTrue(gameAction.isAWinningDefendingMove(boardWithKingInTheCenter));
    }

    @Test
    void isKingMove_whereFromCoordinatesAreKingField_true() {
        GameAction gameAction = new GameAction(kingCoordinates, new Coordinates(4, 0));
        Board boardWithKingInTheCenter = createBoardWithSparePieces();

        assertTrue(gameAction.isKingMove(boardWithKingInTheCenter));
    }

    @Test
    void movesToSideOfBoard_goToTheMiddle_false() {
        GameAction gameAction = new GameAction(new Coordinates(4, 0), new Coordinates(4, 4));

        assertFalse(gameAction.movesToSideOfBoard());
    }

    @Test
    void movesToSideOfBoard_goToTheCorner_true() {
        GameAction gameAction = new GameAction(new Coordinates(0, 4), new Coordinates(0, 0));

        assertTrue(gameAction.movesToSideOfBoard());
    }

    private Board createBoardWithSparePieces() {
        Board board = Board.createEmptyBoard();
        board.updateField(new Field(kingCoordinates, FieldState.KING));
        return board;
    }
}
