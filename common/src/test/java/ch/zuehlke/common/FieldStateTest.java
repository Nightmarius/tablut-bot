package ch.zuehlke.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldStateTest {

    @Test
    void isEnemy_forAttacker_areKingAndDefender() {
        FieldState attacker = FieldState.ATTACKER;

        assertTrue(attacker.isEnemy(FieldState.KING));
        assertTrue(attacker.isEnemy(FieldState.DEFENDER));
        assertFalse(attacker.isEnemy(FieldState.ATTACKER));
    }

    @Test
    void isEnemy_forKing_areAttacker() {
        FieldState king = FieldState.KING;

        assertTrue(king.isEnemy(FieldState.ATTACKER));
        assertFalse(king.isEnemy(FieldState.KING));
        assertFalse(king.isEnemy(FieldState.DEFENDER));
    }

    @Test
    void isEnemy_forDefender_areAttacker() {
        FieldState defender = FieldState.DEFENDER;

        assertTrue(defender.isEnemy(FieldState.ATTACKER));
        assertFalse(defender.isEnemy(FieldState.KING));
        assertFalse(defender.isEnemy(FieldState.DEFENDER));
    }

    @Test
    void getSymbol_areTheCorrectSymbols() {
        assertEquals(" ", FieldState.EMPTY.getSymbol());
        assertEquals("A", FieldState.ATTACKER.getSymbol());
        assertEquals("D", FieldState.DEFENDER.getSymbol());
        assertEquals("K", FieldState.KING.getSymbol());
    }
}
