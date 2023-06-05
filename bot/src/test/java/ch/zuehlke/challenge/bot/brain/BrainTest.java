package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.GameAction;
import ch.zuehlke.common.InternalGameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class is supposed to be used to locally test your bots against each other
 * This can help you to get a feeling for how long your bot takes to make a decision
 * Also you can use it to manually analyze the board states and the bots decisions
 */
class BrainTest {

    @Test
    void play_oneGameBestBrainAgainstItself_untilItIsFinished() {
        BetterBot bestBrain = new BetterBot();

        InternalGameState internalGameState = new InternalGameState();
        internalGameState.printBoard();
        while (!internalGameState.isGameFinished()) {

            GameAction action = bestBrain.decide(internalGameState.attackersTurn(), internalGameState.board(), internalGameState.getPossibleActions());
            internalGameState.playAction(action);
            internalGameState.printBoard();
        }

        assertTrue(internalGameState.isGameFinished());
    }

    @Test
    void play_oneGameBetweenTwoBrains_untilItIsFinished() {
        BetterBot bestBrain = new BetterBot();
        RandomBot randomBrain = new RandomBot();

        InternalGameState internalGameState = new InternalGameState();
        internalGameState.printBoard();
        while (!internalGameState.isGameFinished()) {

            GameAction action;
            if (internalGameState.attackersTurn()) {
                action = bestBrain.decide(internalGameState.attackersTurn(), internalGameState.board(), internalGameState.getPossibleActions());
            } else {
                action = randomBrain.decide(internalGameState.attackersTurn(), internalGameState.board(), internalGameState.getPossibleActions());
            }

            internalGameState.playAction(action);
            internalGameState.printBoard();
        }

        assertTrue(internalGameState.isGameFinished());
    }

}
