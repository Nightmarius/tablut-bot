package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.Board;
import ch.zuehlke.common.GameAction;
import ch.zuehlke.common.Move;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@Profile({"randombot"})
public class RandomBot implements Bot {

    public GameAction decide(boolean isAttackersTurn, Board board, Set<GameAction> possibleActions, List<Move> moves) {
        List<GameAction> gameActions = new ArrayList<>(possibleActions);
        int randomIndex = new Random().nextInt(gameActions.size());
        return gameActions.get(randomIndex);
    }
}
