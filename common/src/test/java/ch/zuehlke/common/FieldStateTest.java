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
    void isEnemy_forEmptyField_areNoEnemies() {
        FieldState empty = FieldState.EMPTY;

        assertFalse(empty.isEnemy(FieldState.ATTACKER));
        assertFalse(empty.isEnemy(FieldState.KING));
        assertFalse(empty.isEnemy(FieldState.DEFENDER));
        assertFalse(empty.isEnemy(FieldState.EMPTY));
    }

    @Test
    void getSymbol_areTheCorrectSymbols() {
        assertEquals(" ", FieldState.EMPTY.getSymbol());
        assertEquals("A", FieldState.ATTACKER.getSymbol());
        assertEquals("D", FieldState.DEFENDER.getSymbol());
        assertEquals("K", FieldState.KING.getSymbol());
    }

    @Test
    void isFriendly_forAttacker_areAttackers() {
        FieldState attacker = FieldState.ATTACKER;

        assertTrue(attacker.isFriendly(FieldState.ATTACKER));
        assertFalse(attacker.isFriendly(FieldState.KING));
        assertFalse(attacker.isFriendly(FieldState.DEFENDER));
    }

    @Test
    void isFriendly_forKing_areKingAndDefender() {
        FieldState king = FieldState.KING;

        assertTrue(king.isFriendly(FieldState.KING));
        assertTrue(king.isFriendly(FieldState.DEFENDER));
        assertFalse(king.isFriendly(FieldState.ATTACKER));
    }

    @Test
    void isFriendly_forDefender_areKingAndDefender() {
        FieldState defender = FieldState.DEFENDER;

        assertTrue(defender.isFriendly(FieldState.KING));
        assertTrue(defender.isFriendly(FieldState.DEFENDER));
        assertFalse(defender.isFriendly(FieldState.ATTACKER));
    }

    @Test
    void isFriendly_forEmptyField_areNoFriends() {
        FieldState empty = FieldState.EMPTY;

        assertFalse(empty.isFriendly(FieldState.ATTACKER));
        assertFalse(empty.isFriendly(FieldState.KING));
        assertFalse(empty.isFriendly(FieldState.DEFENDER));
        assertFalse(empty.isFriendly(FieldState.EMPTY));
    }
}
