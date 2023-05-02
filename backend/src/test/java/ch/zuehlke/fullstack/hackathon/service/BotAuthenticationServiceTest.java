package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.Token;
import ch.zuehlke.fullstack.hackathon.controller.AuthenticationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BotAuthenticationServiceTest {
    private BotAuthenticationService botAuthService;
    private BotService botServiceMock;
    private final PlayerName bestBot = new PlayerName("bestBot");
    private final Token bestToken = new Token("1111");
    private final BotDto bestBotDto = new BotDto(bestBot, bestToken);
    private final BotDto fakeBot = new BotDto(bestBot, new Token("fakeToken"));

    @BeforeEach
    void setUp() {
        botServiceMock = mock(BotService.class);
        botAuthService = new BotAuthenticationService(botServiceMock);
        when(botServiceMock.getBot(bestBot)).thenReturn(Optional.of(bestBotDto));
    }

    @Test
    void authenticate_success() {
        AuthenticationResult result = botAuthService.authenticate(bestBotDto);
        assertThat(result).isEqualTo(AuthenticationResult.SUCCESS);
    }

    @Test
    void authenticate_denied() {
        AuthenticationResult result = botAuthService.authenticate(fakeBot);
        assertThat(result).isEqualTo(AuthenticationResult.DENIED);
    }
}
