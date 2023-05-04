package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.fullstack.hackathon.controller.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BotAuthenticationService {
    private final BotService botService;

    public AuthenticationResult authenticate(BotDto botDto) {
        BotDto savedBot = botService.getBot(botDto.name()).orElse(null);
        if (savedBot == null) {
            log.info("Bot " + botDto.name() + " not found");
            return AuthenticationResult.DENIED;
        }
        if (!savedBot.token().value().equals(botDto.token().value())) {
            log.info("Bot token " + botDto.token() + " not valid");
            return AuthenticationResult.DENIED;
        }
        return AuthenticationResult.SUCCESS;
    }
}
