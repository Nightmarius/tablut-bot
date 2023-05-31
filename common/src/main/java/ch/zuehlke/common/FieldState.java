package ch.zuehlke.common;

import java.beans.Transient;

public enum FieldState {
    EMPTY, ATTACKER, DEFENDER, KING;

    public boolean isEnemy(FieldState other) {
        if (this == ATTACKER && (other == KING || other == DEFENDER)) {
            return true;
        }
        return (this == DEFENDER || this == KING) && other == ATTACKER;
    }

    @Transient
    public String getSymbol() {
        return switch (this) {
            case EMPTY -> " ";
            case ATTACKER -> "A";
            case DEFENDER -> "D";
            case KING -> "K";
        };
    }
}
