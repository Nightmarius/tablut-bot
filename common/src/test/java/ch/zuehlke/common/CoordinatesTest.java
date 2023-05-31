package ch.zuehlke.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoordinatesTest {

    @Test
    void getAllCoordinates_haveSize81() {
        assertThat(Coordinates.getAllCoordinates()).hasSize(81);
    }

    @Test
    void left_inTheMiddleOfTheBoard_isOneToTheLeft() {
        Coordinates middleOfTheBoard = new Coordinates(4, 4);
        Optional<Coordinates> oneLeft = middleOfTheBoard.left();

        assertThat(oneLeft).isPresent();
        assertThat(oneLeft.get()).isEqualTo(new Coordinates(3, 4));
    }

    @Test
    void left_onTheLeftSideOfTheBoard_isEmpty() {
        Coordinates leftSideOfTheBoard = new Coordinates(0, 4);
        Optional<Coordinates> oneLeft = leftSideOfTheBoard.left();

        assertThat(oneLeft).isEmpty();
    }

    @Test
    void right_inTheMiddleOfTheBoard_isOneToTheRight() {
        Coordinates middleOfTheBoard = new Coordinates(4, 4);
        Optional<Coordinates> oneRight = middleOfTheBoard.right();

        assertThat(oneRight).isPresent();
        assertThat(oneRight.get()).isEqualTo(new Coordinates(5, 4));
    }

    @Test
    void right_onTheRightSideOfTheBoard_isEmpty() {
        Coordinates rightSideOfTheBoard = new Coordinates(8, 4);
        Optional<Coordinates> oneRight = rightSideOfTheBoard.right();

        assertThat(oneRight).isEmpty();
    }

    @Test
    void up_inTheMiddleOfTheBoard_isOneUp() {
        Coordinates middleOfTheBoard = new Coordinates(4, 4);
        Optional<Coordinates> oneUp = middleOfTheBoard.up();

        assertThat(oneUp).isPresent();
        assertThat(oneUp.get()).isEqualTo(new Coordinates(4, 3));
    }

    @Test
    void up_onTheTopOfTheBoard_isEmpty() {
        Coordinates topOfTheBoard = new Coordinates(4, 0);
        Optional<Coordinates> oneUp = topOfTheBoard.up();

        assertThat(oneUp).isEmpty();
    }

    @Test
    void down_inTheMiddleOfTheBoard_isOneDown() {
        Coordinates middleOfTheBoard = new Coordinates(4, 4);
        Optional<Coordinates> oneDown = middleOfTheBoard.down();

        assertThat(oneDown).isPresent();
        assertThat(oneDown.get()).isEqualTo(new Coordinates(4, 5));
    }

    @Test
    void down_onTheBottomOfTheBoard_isEmpty() {
        Coordinates bottomOfTheBoard = new Coordinates(4, 8);
        Optional<Coordinates> oneDown = bottomOfTheBoard.down();

        assertThat(oneDown).isEmpty();
    }

    @Test
    void isCastle_centerField_true() {
        Coordinates castle = new Coordinates(4,4);
        assertTrue(castle.isCastle());
    }

    @ParameterizedTest
    @MethodSource("coordinatesWhichAreNotCastle")
    void isCastle_forCoordinatesWhichAreNotCastle(Coordinates coordinatesWhichAreNotCastle) {
        assertFalse(coordinatesWhichAreNotCastle.isCastle());
    }

    @Test
    void isAdjacentCastle_centerField_false() {
        Coordinates castle = new Coordinates(4,4);
        assertFalse(castle.isAdjacentCastle());
    }

    @ParameterizedTest
    @MethodSource("coordinatesAdjacentToCastle")
    void isAdjacentCastle_forAFieldAdjacentToCastle_true(Coordinates adjacentCoordinates) {
        assertTrue(adjacentCoordinates.isAdjacentCastle());
    }

    @ParameterizedTest
    @MethodSource("coordinatesNotAdjacentToCastle")
    void isAdjacentCastle_forAFieldNotAdjacentToCastle_false(Coordinates anyOtherCoordinates) {
        assertFalse(anyOtherCoordinates.isAdjacentCastle());
    }

    @Test
    void getNeighbours_centerField_areAdjacentToCastle() {
        Coordinates castle = new Coordinates(4,4);

        List<Optional<Coordinates>> neighbours = castle.getNeighbours();

        assertThat(neighbours).hasSize(4);
        assertThat(neighbours.stream()
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet()))
                .containsExactlyInAnyOrderElementsOf(coordinatesAdjacentToCastleSet());
    }

    @ParameterizedTest
    @MethodSource("coordinatesOnTheEdgeOfTheBoard")
    void getNeighbours_forFieldsOnTheEdge_haveAtLeastOneEmptyCoordinates(Coordinates coordinatesOnTheEdgeOfTheBoard) {
        List<Optional<Coordinates>> neighbours = coordinatesOnTheEdgeOfTheBoard.getNeighbours();

        assertThat(neighbours).hasSize(4);
        assertTrue(neighbours.stream().anyMatch(Optional::isEmpty));
    }

    @ParameterizedTest
    @MethodSource("coordinatesNotOnTheEdgeOfTheBoard")
    void getNeighbours_forFieldsNotOnTheEdge_haveNoEmptyCoordinates(Coordinates coordinatesNotOnTheEdgeOfTheBoard) {
        List<Optional<Coordinates>> neighbours = coordinatesNotOnTheEdgeOfTheBoard.getNeighbours();

        assertThat(neighbours).hasSize(4);
        assertTrue(neighbours.stream().noneMatch(Optional::isEmpty));
    }

    @ParameterizedTest
    @MethodSource("coordinatesOnTheEdgeOfTheBoard")
    void isBorder_OnTheEdge_true(Coordinates coordinatesOnTheEdgeOfTheBoard) {
        assertTrue(coordinatesOnTheEdgeOfTheBoard.isBorder());
    }

    @ParameterizedTest
    @MethodSource("coordinatesNotOnTheEdgeOfTheBoard")
    void isBorder_forFieldsNotOnTheEdge_false(Coordinates coordinatesNotOnTheEdgeOfTheBoard) {
        assertFalse(coordinatesNotOnTheEdgeOfTheBoard.isBorder());
    }

    private static Set<Coordinates> coordinatesOnTheEdgeOfTheBoardSet() {
        return Set.of(
                new Coordinates(0, 0),
                new Coordinates(0, 1),
                new Coordinates(0, 2),
                new Coordinates(0, 3),
                new Coordinates(0, 4),
                new Coordinates(0, 5),
                new Coordinates(0, 6),
                new Coordinates(0, 7),
                new Coordinates(0, 8),
                new Coordinates(1, 0),
                new Coordinates(2, 0),
                new Coordinates(3, 0),
                new Coordinates(4, 0),
                new Coordinates(5, 0),
                new Coordinates(6, 0),
                new Coordinates(7, 0),
                new Coordinates(8, 0),
                new Coordinates(1, 8),
                new Coordinates(2, 8),
                new Coordinates(3, 8),
                new Coordinates(4, 8),
                new Coordinates(5, 8),
                new Coordinates(6, 8),
                new Coordinates(7, 8),
                new Coordinates(8, 8),
                new Coordinates(8, 1),
                new Coordinates(8, 2),
                new Coordinates(8, 3),
                new Coordinates(8, 4),
                new Coordinates(8, 5),
                new Coordinates(8, 6),
                new Coordinates(8, 7)
        );
    }

    private static Set<Coordinates> coordinatesAdjacentToCastleSet() {
        return Stream.of(
                new Coordinates(3, 4),
                new Coordinates(5, 4),
                new Coordinates(4, 3),
                new Coordinates(4, 5)
        ).collect(Collectors.toSet());
    }

    private static Stream<Arguments> coordinatesAdjacentToCastle() {
        return coordinatesAdjacentToCastleSet().stream().map(Arguments::of);
    }

    private static Stream<Arguments> coordinatesNotAdjacentToCastle() {
        return Coordinates.getAllCoordinates().stream()
                .filter(coordinates -> !coordinatesAdjacentToCastleSet().contains(coordinates))
                .map(Arguments::of);
    }

    private static Stream<Arguments> coordinatesWhichAreNotCastle() {
        return Coordinates.getAllCoordinates().stream()
                .filter(coordinates -> !coordinates.equals(new Coordinates(4, 4)))
                .map(Arguments::of);
    }

    private static Stream<Arguments> coordinatesOnTheEdgeOfTheBoard() {
        return coordinatesOnTheEdgeOfTheBoardSet().stream().map(Arguments::of);
    }

    private static Stream<Arguments> coordinatesNotOnTheEdgeOfTheBoard() {
        return Coordinates.getAllCoordinates().stream()
                .filter(coordinates -> !coordinatesOnTheEdgeOfTheBoardSet().contains(coordinates))
                .map(Arguments::of);
    }

}
