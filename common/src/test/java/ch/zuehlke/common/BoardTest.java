package ch.zuehlke.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    void emptyBoard_sizeIs9x9() {
        var board = Board.createEmptyBoard();

        assertThat(board.fields().length).isEqualTo(9);
        assertThat(board.fields()[0].length).isEqualTo(9);
        assertThat(board.getAllFieldsAsList()).hasSize(81);
        assertThat(board.getAllFieldsAsList().stream().allMatch(field -> field.state() == FieldState.EMPTY)).isTrue();
    }

    @Test
    void initialBoard_sizeIs9x9() {
        var board = Board.createInitialBoard();

        assertThat(board.fields().length).isEqualTo(9);
        assertThat(board.fields()[0].length).isEqualTo(9);
        assertThat(board.getAllFieldsAsList()).hasSize(81);
    }

    @Test
    void initialBoard_hasCorrectNumberOfFieldStates() {
        var board = Board.createInitialBoard();

        assertThat(board.getAllFieldsAsList().stream().filter(field -> field.state() == FieldState.ATTACKER)).hasSize(16);
        assertThat(board.getAllFieldsAsList().stream().filter(field -> field.state() == FieldState.DEFENDER)).hasSize(8);
        assertThat(board.getAllFieldsAsList().stream().filter(field -> field.state() == FieldState.KING)).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("boardSetup")
    void initialBoard_isCorrectlySetUp(int x, int y, FieldState state) {
        Field expected = new Field(new Coordinates(x, y), state);
        var board = Board.createInitialBoard();

        assertThat(board.fields()[y][x]).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getKingPosition() {
        Board initialBoard = Board.createInitialBoard();

        assertThat(initialBoard.getKingPosition()).isPresent();
        assertThat(initialBoard.getKingPosition().get()).isEqualTo(new Coordinates(4, 4));
    }

    private static Stream<Arguments> boardSetup() {
        return Stream.of(
                Arguments.of(3, 0, FieldState.ATTACKER),
                Arguments.of(4, 0, FieldState.ATTACKER),
                Arguments.of(5, 0, FieldState.ATTACKER),
                Arguments.of(4, 1, FieldState.ATTACKER),
                Arguments.of(0, 3, FieldState.ATTACKER),
                Arguments.of(0, 4, FieldState.ATTACKER),
                Arguments.of(0, 5, FieldState.ATTACKER),
                Arguments.of(1, 4, FieldState.ATTACKER),
                Arguments.of(3, 8, FieldState.ATTACKER),
                Arguments.of(4, 8, FieldState.ATTACKER),
                Arguments.of(5, 8, FieldState.ATTACKER),
                Arguments.of(4, 7, FieldState.ATTACKER),
                Arguments.of(8, 3, FieldState.ATTACKER),
                Arguments.of(8, 4, FieldState.ATTACKER),
                Arguments.of(8, 5, FieldState.ATTACKER),
                Arguments.of(7, 4, FieldState.ATTACKER),
                Arguments.of(4, 2, FieldState.DEFENDER),
                Arguments.of(4, 3, FieldState.DEFENDER),
                Arguments.of(2, 4, FieldState.DEFENDER),
                Arguments.of(3, 4, FieldState.DEFENDER),
                Arguments.of(4, 5, FieldState.DEFENDER),
                Arguments.of(4, 6, FieldState.DEFENDER),
                Arguments.of(5, 4, FieldState.DEFENDER),
                Arguments.of(6, 4, FieldState.DEFENDER),
                Arguments.of(4, 4, FieldState.KING)
        );
    }
}
