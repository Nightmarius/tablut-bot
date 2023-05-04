package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.fullstack.hackathon.database.Bot;
import ch.zuehlke.fullstack.hackathon.database.BotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BotServiceTest {

    private BotService botService;
    private BotRepository botRepoMock;

    private final Bot bot = new Bot("bestBot", "1111");
    private final BotDto botDto = bot.getDto();


    @BeforeEach
    void setUp() {
        botRepoMock = mock(BotRepository.class);
        botService = new BotService(botRepoMock);
        when(botRepoMock.findById(bot.getName())).thenReturn(Optional.of(bot));
    }

    @Test
    void generateToken_successfully() {
        String token = botService.generateToken();
        assertThat(token).hasSize(32);
    }

    @Test
    void removeBot_successfully() {
        botService.removeBot(botDto.name());
        verify(botRepoMock).delete(bot);
    }

    @Test
    void removeBot_doesNotRemoveOtherBots() {
        botService.removeBot(new PlayerName("fakeBot"));
        verify(botRepoMock, never()).delete(any());
    }
}
