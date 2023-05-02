package ch.zuehlke.fullstack.hackathon.model;

import ch.zuehlke.common.GameDto;
import ch.zuehlke.common.GameId;
import ch.zuehlke.common.GameStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameMapperTest {

    @Test
    void map_newGame_successfully() {
        GameId gameId = new GameId(1);
        Game game = new Game(gameId);

        GameDto gameDto = GameMapper.map(game);

        assertThat(gameDto.id()).isEqualTo(gameId);
        assertThat(gameDto.players()).isEqualTo(game.getPlayers());
        assertThat(gameDto.status()).isEqualTo(GameStatus.NOT_STARTED);
        assertThat(gameDto.state().moves()).isEmpty();
        assertThat(gameDto.state().currentRequests()).isEmpty();
        assertThat(gameDto.state().board().fields()).hasDimensions(9, 9);
    }

}
