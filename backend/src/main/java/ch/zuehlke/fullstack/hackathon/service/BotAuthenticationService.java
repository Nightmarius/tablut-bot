package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.fullstack.hackathon.controller.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BotAuthenticationService {
    private final BotService botService;

    public AuthenticationResult authenticate(BotDto botDto) {
        BotDto savedBot = botService.getBot(botDto.name()).orElse(null);
        if (savedBot == null || !savedBot.token().value().equals(botDto.token().value())) {
            return AuthenticationResult.DENIED;
        }
        return AuthenticationResult.SUCCESS;
    }
}
