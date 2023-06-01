package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.Board;
import ch.zuehlke.common.GameAction;

import java.util.Set;

public interface Bot {

    GameAction decide(boolean isAttackersTurn, Board board, Set<GameAction> possibleActions);
}
