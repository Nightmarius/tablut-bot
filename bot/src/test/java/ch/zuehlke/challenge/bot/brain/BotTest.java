package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.GameAction;
import ch.zuehlke.common.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is supposed to be used to locally test your bots against each other
 * This can help you to get a feeling for how long your bot takes to make a decision
 * Also you can use it to manually analyze the board states and the bots decisions
 */
class BotTest {

    @Test
    void play_oneGameBetterBotsAgainstItself_untilItIsFinished() {
        BetterBot betterBot = new BetterBot();

        GameState gameState = new GameState();
        gameState.printBoard();
        while (!gameState.isGameFinished()) {

            GameAction action = betterBot.decide(gameState.attackersTurn(), gameState.board(), gameState.getPossibleActions());
            gameState.playAction(action);
            gameState.printBoard();
        }

        assertTrue(gameState.isGameFinished());
    }

    @Test
    void play_oneGameBetweenTwoBots_untilItIsFinished() {
        BetterBot betterBot = new BetterBot();
        RandomBot randomBot = new RandomBot();

        GameState gameState = new GameState();
        gameState.printBoard();
        while (!gameState.isGameFinished()) {

            GameAction action;
            if (gameState.attackersTurn()) {
                action = betterBot.decide(true, gameState.board(), gameState.getPossibleActions());
            } else {
                action = randomBot.decide(false, gameState.board(), gameState.getPossibleActions());
            }

            gameState.playAction(action);
            gameState.printBoard();
        }

        assertTrue(gameState.isGameFinished());
    }
}
