package ch.zuehlke.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void initialGame_FirstMove_PossibleMoveForBlack() {
        assertThat(gameState.getPossibleActions()).contains(new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
    }

    @Test
    void initialGame_FirstMove_NoMoveWithDefenders() {
        Set<Coordinates> coordinatesDefender = Set.of(
                new Coordinates(4, 2),
                new Coordinates(4, 3),
                new Coordinates(2, 4),
                new Coordinates(3, 4),
                new Coordinates(4, 4),
                new Coordinates(5, 4),
                new Coordinates(6, 4),
                new Coordinates(4, 5),
                new Coordinates(4, 6)
        );

        assertThat(gameState.getPossibleActions().stream().map(GameAction::from).collect(Collectors.toSet()))
                .doesNotContainAnyElementsOf(coordinatesDefender);
    }

    @Test
    void getPossibleMoves_ForLeftAttackerAtStart() {
        Set<Coordinates> expectedGameActions = Set.of(
                new Coordinates(0, 0),
                new Coordinates(0, 1),
                new Coordinates(0, 2),
                new Coordinates(1, 3),
                new Coordinates(2, 3),
                new Coordinates(3, 3)
        );


        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(0, 3)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).isEqualTo(expectedGameActions);
    }

    @Test
    void getPossibleMoves_ForTopAttackerAtStart() {
        Set<Coordinates> expectedGameActions = Set.of();

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(4, 0)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).isEqualTo(expectedGameActions);
    }

    @Test
    void getPossibleMoves_ForRightAttackerAtStart() {
        Set<Coordinates> expectedGameActions = Set.of(
                new Coordinates(5, 5),
                new Coordinates(6, 5),
                new Coordinates(7, 5),
                new Coordinates(8, 6),
                new Coordinates(8, 7),
                new Coordinates(8, 8)
        );

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(8, 5)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).isEqualTo(expectedGameActions);
    }

    @Test
    void getPossibleMoves_ForBottomAttackerAtStart() {
        Set<Coordinates> expectedGameActions = Set.of(
                new Coordinates(0, 7),
                new Coordinates(1, 7),
                new Coordinates(2, 7),
                new Coordinates(3, 7),
                new Coordinates(5, 7),
                new Coordinates(6, 7),
                new Coordinates(7, 7),
                new Coordinates(8, 7)
        );

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(4, 7)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).isEqualTo(expectedGameActions);
    }

    @Test
    void getPossibleActions_kingCanMoveOutOfTheCastle() {

        // remove two pieces to the left of the king
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(2, 4), FieldState.EMPTY));

        // make a move with attacker so that defender is playing next
        gameState.moves().add(new Move(null, null, null, null));

        var expectedDestinations = Set.of(
                new Coordinates(3, 4),
                new Coordinates(2, 4)
        );

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(4, 4)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).isEqualTo(expectedDestinations);
    }

    @Test
    void getPossibleActions_kingCanMoveIntoTheCastle() {
        // Move king one field to the left (replacing the defender there)
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.KING));

        // make a move with attacker so that defender is playing next
        gameState.moves().add(new Move(null, null, null, null));

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(3, 4)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).contains(new Coordinates(4, 4));
    }

    @Test
    void getPossibleActions_kingCanMoveThroughTheCastle() {
        // Move king one field to the left (replacing the defender there)
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.KING));

        // also clear the field to the right of the castle
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));

        // make a move with attacker so that defender is playing next
        gameState.moves().add(new Move(null, null, null, null));

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(3, 4)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).contains(new Coordinates(5, 4));
    }

    @Test
    void getPossibleActions_attackersCanNotMoveIntoTheCastle() {
        // Remove the middle row of the defenders
        gameState.board().updateField(new Field(new Coordinates(2, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(1, 4)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).doesNotContain(new Coordinates(4, 4));
    }

    @Test
    void getPossibleActions_attackersCanNotMoveThroughTheCastle() {
        // Remove the middle row of the defenders
        gameState.board().updateField(new Field(new Coordinates(2, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(1, 4)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).doesNotContain(new Coordinates(5, 4));
    }

    @Test
    void getPossibleActions_defendersCanNotMoveIntoTheCastle() {
        // Remove the middle row of the defenders
        gameState.board().updateField(new Field(new Coordinates(2, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));

        // make a move with attacker so that defender is playing next
        gameState.playAction(new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));

        assertThat(gameState.getPossibleActions().stream()
                .filter(a -> a.from().equals(new Coordinates(4, 2)))
                .map(GameAction::to)
                .collect(Collectors.toSet())).doesNotContain(new Coordinates(4, 4));
    }

    @Test
    void isDraw() {
        assertThat(gameState.isDraw()).isFalse();

        for (int i = 0; i < 210; i++) {
            gameState.moves().add(new Move(null, null, null, null));
        }

        assertThat(gameState.isDraw()).isTrue();
    }

    @Test
    void hasAttackerWon() {
        assertThat(gameState.hasAttackerWon()).isFalse();

        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));

        assertThat(gameState.hasAttackerWon()).isTrue();
    }

    @Test
    void hasDefenderWon() {
        assertThat(gameState.hasDefenderWon()).isFalse();

        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.KING));

        assertThat(gameState.hasDefenderWon()).isTrue();
    }

    @Test
    void attackerCaptures_toTheLeft() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.DEFENDER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.DEFENDER);

        gameState.playAction(new GameAction(new Coordinates(3, 0), new Coordinates(2, 0)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCaptures_toTheRight() {
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(2, 0), FieldState.ATTACKER));
        gameState.playAction(new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCaptures_toTheTop() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(0, 1), FieldState.DEFENDER));
        gameState.playAction(new GameAction(new Coordinates(0, 3), new Coordinates(0, 2)));
        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(0, 1)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCaptures_toTheBottom() {
        gameState.board().updateField(new Field(new Coordinates(0, 1), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(0, 2), FieldState.ATTACKER));
        gameState.playAction(new GameAction(new Coordinates(3, 0), new Coordinates(0, 0)));
        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(0, 1)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCaptures_toTheLeftAndRight() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(2, 3), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(3, 0), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(4, 0), FieldState.ATTACKER));

        gameState.playAction(new GameAction(new Coordinates(2, 3), new Coordinates(2, 0)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(3, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void defenderCaptures_toTheLeft() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.ATTACKER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.ATTACKER);

        gameState.playAction(new GameAction(new Coordinates(2, 4), new Coordinates(2, 0)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void defenderCapturesFeaturingKing_toTheLeft() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.KING));
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.ATTACKER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.ATTACKER);

        gameState.playAction(new GameAction(new Coordinates(2, 4), new Coordinates(2, 0)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void defenderCapturesWithKing_toTheLeft() {
        gameState.board().updateField(new Field(new Coordinates(0, 0), FieldState.DEFENDER));
        gameState.board().updateField(new Field(new Coordinates(1, 0), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(2, 3), FieldState.KING));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.ATTACKER);

        gameState.playAction(new GameAction(new Coordinates(2, 3), new Coordinates(2, 0)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(1, 0)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCapturesWithCastleField() {
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 3), FieldState.KING));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(6, 3), FieldState.ATTACKER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.DEFENDER);

        gameState.playAction(new GameAction(new Coordinates(6, 3), new Coordinates(6, 4)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void defenderCapturesWithCastleField() {
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(3, 3), FieldState.KING));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(6, 3), FieldState.DEFENDER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.ATTACKER);

        gameState.playAction(new GameAction(new Coordinates(6, 3), new Coordinates(6, 4)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void defenderCapturesWithKingInCastleField() {
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(6, 3), FieldState.DEFENDER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.ATTACKER);

        gameState.playAction(new GameAction(new Coordinates(6, 3), new Coordinates(6, 4)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCapturesKingInCastle() {
        gameState.board().updateField(new Field(new Coordinates(4, 3), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(4, 5), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(4, 4)).state())
                .isEqualTo(FieldState.KING);


        gameState.playAction(new GameAction(new Coordinates(5, 8), new Coordinates(5, 4)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(4, 4)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void attackerCapturesKingAdjacentToCastle() {
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.KING));
        gameState.board().updateField(new Field(new Coordinates(5, 3), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(6, 4), FieldState.ATTACKER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.KING);


        gameState.playAction(new GameAction(new Coordinates(5, 8), new Coordinates(5, 5)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state())
                .isEqualTo(FieldState.EMPTY);
    }

    @Test
    void kingInCastleSurvivesTwoAttackers() {
        gameState.board().updateField(new Field(new Coordinates(3, 4), FieldState.ATTACKER));
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.EMPTY));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(4, 4)).state())
                .isEqualTo(FieldState.KING);


        gameState.playAction(new GameAction(new Coordinates(5, 8), new Coordinates(5, 4)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(4, 4)).state())
                .isEqualTo(FieldState.KING);
    }

    @Test
    void kingAdjacentToCastleSurvivesTwoAttackers() {
        gameState.board().updateField(new Field(new Coordinates(5, 4), FieldState.KING));
        gameState.board().updateField(new Field(new Coordinates(4, 4), FieldState.EMPTY));
        gameState.board().updateField(new Field(new Coordinates(5, 3), FieldState.ATTACKER));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state()).isEqualTo(FieldState.KING);

        gameState.playAction(new GameAction(new Coordinates(5, 8), new Coordinates(5, 5)));

        assertThat(gameState.board().getFieldForCoordinate(new Coordinates(5, 4)).state()).isEqualTo(FieldState.KING);
    }

}