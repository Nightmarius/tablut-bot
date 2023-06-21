package ch.zuehlke.common;

import lombok.extern.slf4j.Slf4j;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public record GameState(PlayRequest playRequest, List<Move> moves, Board board, TimeControl timeControl) {

    public GameState() {
        this(null, new ArrayList<>(), Board.createInitialBoard(), new TimeControl());
    }

    public GameState(PlayRequest playRequest, List<Move> moves) {
        this(playRequest, moves, Board.createInitialBoard(), new TimeControl());
        moves.forEach(m -> playAction(m.action()));
    }

    public boolean attackersTurn() {
        return moves.size() % 2 == 0;
    }

    public boolean defendersTurn() {
        return !attackersTurn();
    }

    // this method is used by the backend, probably no use during the implementation of the bot
    public TimeControl subtractTime() {
        if (attackersTurn()) {
            return timeControl.subtractTimeForAttacker();
        } else {
            return timeControl.subtractTimeForDefender();
        }
    }


    public Set<GameAction> getPossibleActions() {
        if (attackersTurn()) {
            return getPossibleActionsForAttacker();
        }
        return getPossibleActionsForDefender();
    }

    private Set<GameAction> getPossibleActionsForAttacker() {
        return board.getAllFieldsAsList().stream()
                .filter(field -> field.state().equals(FieldState.ATTACKER))
                .flatMap(a -> findAvailableActions(a).stream()).collect(Collectors.toSet());
    }

    private Set<GameAction> getPossibleActionsForDefender() {
        return board.getAllFieldsAsList().stream()
                .filter(field -> field.state() == FieldState.DEFENDER || field.state() == FieldState.KING)
                .flatMap(a -> findAvailableActions(a).stream()).collect(Collectors.toSet());
    }

    private Set<GameAction> findAvailableActions(Field field) {

        var result = new HashSet<GameAction>();
        result.addAll(findAvailableActions(field, Coordinates::left));
        result.addAll(findAvailableActions(field, Coordinates::right));
        result.addAll(findAvailableActions(field, Coordinates::up));
        result.addAll(findAvailableActions(field, Coordinates::down));

        return result;
    }

    private Set<GameAction> findAvailableActions(Field field, Function<Coordinates, Optional<Coordinates>> neighbourGetter) {
        var result = new HashSet<GameAction>();

        var neighbour = neighbourGetter.apply(field.coordinates());
        while (neighbour.isPresent()) {
            var neighbourCoordinates = neighbour.get();
            var neighbourField = board.getFieldForCoordinate(neighbourCoordinates);
            if ((neighbourField.state() == FieldState.EMPTY && !neighbourField.coordinates().isCastle()) ||
                    (field.state() == FieldState.KING && neighbourField.coordinates().isCastle())) {
                result.add(new GameAction(field.coordinates(), neighbourField.coordinates()));
            } else {
                break;
            }
            neighbour = neighbourGetter.apply(neighbourCoordinates);
        }
        return result;
    }

    public void playAction(GameAction gameAction) {
        board.movePiece(gameAction.from(), gameAction.to());

        var toField = board.getFieldForCoordinate(gameAction.to());

        checkPinchCapture(toField, Coordinates::left);
        checkPinchCapture(toField, Coordinates::right);
        checkPinchCapture(toField, Coordinates::up);
        checkPinchCapture(toField, Coordinates::down);
    }

    private void checkPinchCapture(Field field, Function<Coordinates, Optional<Coordinates>> neighbourGetter) {
        neighbourGetter.apply(field.coordinates())
                .map(board::getFieldForCoordinate)
                .ifPresent(neighbour -> {
                    if (field.state().isEnemy(neighbour.state())) {
                        neighbourGetter.apply(neighbour.coordinates())
                                .map(board::getFieldForCoordinate)
                                .ifPresent(nextNeighbour -> {
                                    boolean isEmptyCastle = nextNeighbour.coordinates().isCastle() && nextNeighbour.state() == FieldState.EMPTY;
                                    boolean isEnemy = neighbour.state().isEnemy(nextNeighbour.state());
                                    boolean isKingCapturable = neighbour.state() == FieldState.KING && (neighbour.coordinates().isCastle() || neighbour.coordinates().isAdjacentCastle());

                                    if (isKingCapturable) {
                                        long necessaryAttackersPresent = neighbour.coordinates().getNeighbours().stream()
                                                .filter(Optional::isPresent)
                                                .map(c -> board.getFieldForCoordinate(c.get()))
                                                .filter(f -> f.state() == FieldState.ATTACKER).count();
                                        boolean necessaryAttackers = necessaryAttackersPresent == 3 && neighbour.coordinates().isAdjacentCastle()
                                                || necessaryAttackersPresent == 4 && neighbour.coordinates().isCastle();
                                        if (necessaryAttackers)
                                            board.updateField(new Field(neighbour.coordinates(), FieldState.EMPTY));
                                    } else {
                                        if (isEnemy || isEmptyCastle) {
                                            board.updateField(new Field(neighbour.coordinates(), FieldState.EMPTY));
                                        }
                                    }
                                });
                    }
                });
    }

    @Transient
    public boolean isGameFinished() {
        return hasDefenderWon() || isDraw() || hasAttackerWon();
    }

    @Transient
    public boolean isDraw() {
        return moves.size() >= 200;
    }

    @Transient
    public boolean hasDefenderWon() {
        if (attackersTurn() && getPossibleActions().isEmpty()) {
            return true;
        }
        if (timeControl.hasAttackerNoTimeLeft()) {
            return true;
        }
        return board.getAllFieldsAsList().stream()
                .filter(a -> a.state() == FieldState.KING)
                .map(Field::coordinates)
                .anyMatch(Coordinates::isBorder);
    }

    @Transient
    public boolean hasAttackerWon() {
        if (defendersTurn() && getPossibleActions().isEmpty()) {
            return true;
        }
        if (timeControl.hasDefenderNoTimeLeft()) {
            return true;
        }
        return board.getAllFieldsAsList().stream()
                .filter(a -> a.state() == FieldState.KING)
                .findAny()
                .isEmpty();

    }

    // this is not used anywhere, but it is useful for debugging
    public void printBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nMove ");
        stringBuilder.append(moves.size());
        if (moves.size() > 0) {
            stringBuilder.append(attackersTurn() ? " WHITE played" : " BLACK played");
        }
        stringBuilder.append("\n  012345678");
        for (int y = 0; y < 9; y++) {
            stringBuilder.append("\n").append(y).append(" ");
            for (int x = 0; x < 9; x++) {
                var field = board.getFieldForCoordinate(new Coordinates(x, y));
                stringBuilder.append(field.state().getSymbol());
            }
        }

        if (isGameFinished()) {
            if (hasDefenderWon()) {
                stringBuilder.append("\nDefender won");
            } else if (hasAttackerWon()) {
                stringBuilder.append("\nAttacker won");
            } else {
                stringBuilder.append("\nDraw");
            }
        }

        log.info(stringBuilder.toString());
    }
}
