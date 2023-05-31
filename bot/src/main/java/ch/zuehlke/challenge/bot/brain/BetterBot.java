package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.Board;
import ch.zuehlke.common.GameAction;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@Profile({"betterbot"})
public class BetterBot implements Bot {

    public GameAction decide(boolean attacker, Board board, Set<GameAction> possibleActions) {
         // Here is where you can start coding your bot.
        if (attacker) {
            return attack(board, possibleActions);
        } else {
            return defend(board, possibleActions);
        }
    }

    private GameAction attack(Board board, Set<GameAction> possibleActions) {
        return possibleActions.stream()
                .filter(action -> action.movesNextToTheKing(board))
                .findFirst().orElse(getRandomAction(possibleActions));
    }

    private GameAction defend(Board board, Set<GameAction> possibleActions) {
        return possibleActions.stream()
                .filter(action -> action.isAWinningDefendingMove(board))
                .findFirst().orElse(getRandomAction(possibleActions));
    }

    private GameAction getRandomAction(Set<GameAction> possibleActions) {
        List<GameAction> gameActions = new ArrayList<>(possibleActions);
        int randomIndex = new Random().nextInt(gameActions.size());
        return gameActions.get(randomIndex);
    }
}
