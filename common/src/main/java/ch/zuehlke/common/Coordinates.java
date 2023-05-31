package ch.zuehlke.common;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Coordinates(int x, int y) {

    public static Set<Coordinates> getAllCoordinates() {
        Set<Coordinates> coordinates = new HashSet<>();
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                coordinates.add(new Coordinates(x, y));
            }
        }
        return coordinates;
    }

    public Optional<Coordinates> left() {
        if (x == 0) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(x - 1, y));
    }

    public Optional<Coordinates> right() {
        if (x >= Board.SIZE - 1) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(x + 1, y));
    }

    public Optional<Coordinates> up() {
        if (y == 0) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(x, y - 1));
    }

    public Optional<Coordinates> down() {
        if (y >= Board.SIZE - 1) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(x, y + 1));
    }

    @Transient
    public boolean isBorder() {
        return x == 0 || x == Board.SIZE -1 ||
                y == 0 || y == Board.SIZE -1;
    }

    @Transient
    public boolean isCastle() {
        return x == 4 && y == 4;
    }

    @Transient
    public boolean isAdjacentCastle() {
        Coordinates castle = new Coordinates(4, 4);
        Set<Coordinates> coordinateSet = Stream.of(castle.up(), castle.down(), castle.left(), castle.right())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        return coordinateSet.contains(this);
    }

    @Transient
    public List<Optional<Coordinates>> getNeighbours() {
        return List.of(up(), down(), left(), right());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
