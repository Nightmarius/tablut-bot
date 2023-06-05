package ch.zuehlke.common;

import lombok.Builder;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Builder
public record Board(Field[][] fields) {

    public static int SIZE = 9;

    public static Board createEmptyBoard() {
        var fields = new Field[SIZE][SIZE];

        for (Coordinates coordinates: Coordinates.getAllCoordinates()) {
            fields[coordinates.y()][coordinates.x()] = new Field(coordinates, FieldState.EMPTY);
        }

        return new Board(fields);
    }

    public static Board createInitialBoard() {
        Board board = createEmptyBoard();

        board.updateField(new Field(new Coordinates(3, 0), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(4, 0), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(5, 0), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(4, 1), FieldState.ATTACKER));

        board.updateField(new Field(new Coordinates(0, 3), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(0, 4), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(0, 5), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(1, 4), FieldState.ATTACKER));

        board.updateField(new Field(new Coordinates(3, 8), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(4, 8), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(5, 8), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(4, 7), FieldState.ATTACKER));

        board.updateField(new Field(new Coordinates(8, 3), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(8, 4), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(8, 5), FieldState.ATTACKER));
        board.updateField(new Field(new Coordinates(7, 4), FieldState.ATTACKER));

        board.updateField(new Field(new Coordinates(4, 2), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(4, 3), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(2, 4), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(3, 4), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(4, 5), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(4, 6), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(5, 4), FieldState.DEFENDER));
        board.updateField(new Field(new Coordinates(6, 4), FieldState.DEFENDER));

        board.updateField(new Field(new Coordinates(4, 4), FieldState.KING));

        return board;
    }

    @Transient
    public Field getFieldForCoordinate(Coordinates coordinates) {
        return fields[coordinates.y()][coordinates.x()];
    }

    public void updateField(Field field) {
        fields[field.coordinates().y()][field.coordinates().x()] = field;
    }

    public void movePiece(Coordinates from, Coordinates to) {
        Field fromField = getFieldForCoordinate(from);

        updateField(new Field(to, fromField.state()));
        updateField(new Field(from, FieldState.EMPTY));
    }

    @Transient
    public List<Field> getAllFieldsAsList() {
        var fieldsList = new ArrayList<Field>();
        for (int y = 0; y < SIZE; y++) {
            fieldsList.addAll(Arrays.asList(fields[y]).subList(0, SIZE));
        }
        return fieldsList;
    }

    @Transient
    public Optional<Coordinates> getKingPosition() {
        return getAllFieldsAsList().stream()
            .filter(field -> field.state() == FieldState.KING)
            .findFirst()
            .map(Field::coordinates);
    }
}
