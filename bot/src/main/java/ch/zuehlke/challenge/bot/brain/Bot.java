package ch.zuehlke.challenge.bot.brain;

import ch.zuehlke.common.GameAction;
import ch.zuehlke.common.Board;

import java.util.Set;

public interface Bot {

    GameAction decide(boolean attacker, Board board, Set<GameAction> possibleActions);
}