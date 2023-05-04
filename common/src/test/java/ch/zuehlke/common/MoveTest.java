//package ch.zuehlke.common;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class GameActionTest {
//
//    @Test
//    void getWinningPlayer_sameMoves_returnsNull() {
//        Move move1 = new Move(new PlayerName(), new RequestId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
//        Move move2 = new Move(new PlayerName(), new RequestId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
//
//        Optional<PlayerName> winningPlayer = Move.getWinningPlayer(move1, move2);
//
//        assertThat(winningPlayer).isEmpty();
//    }
//
//    @Test
//    void getWinningPlayer_rockBeatsScissors_returnsPlayer1() {
//        Move move1 = new Move(new PlayerName(), new RequestId(), new GameAction(new Coordinates(0, 3), new Coordinates(0, 0)));
//        Move move2 = new Move(new PlayerName(), new RequestId(), GameAction.SCISSORS);
//
//        Optional<PlayerName> winningPlayer = Move.getWinningPlayer(move1, move2);
//
//        assertThat(winningPlayer).isPresent();
//        assertThat(winningPlayer.get()).isEqualTo(move1.PlayerName());
//    }
//}