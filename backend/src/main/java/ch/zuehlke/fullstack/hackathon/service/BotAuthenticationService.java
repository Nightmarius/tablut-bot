package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.Token;
import ch.zuehlke.fullstack.hackathon.controller.AuthenticationResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotAuthenticationService {
    private final BotService botService;

    public AuthenticationResult authenticate(PlayerName name, Token token) {
        BotDto savedBot = botService.getBot(name).orElse(null);
        if (savedBot == null || !savedBot.token().value().equals(token.value())) {
            return AuthenticationResult.DENIED;
        }
        return AuthenticationResult.SUCCESS;
    }
}
