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
@Profile({"randombot"})
public class RandomBot implements Bot {

    public GameAction decide(boolean attacker, Board board, Set<GameAction> possibleActions) {
        think();
        List<GameAction> list = new ArrayList<>(possibleActions);
        int randIdx = new Random().nextInt(list.size());
        return list.get(randIdx);
    }

    private static void think() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            // ignore
        }
    }
}