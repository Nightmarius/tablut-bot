package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.fullstack.hackathon.database.Bot;
import ch.zuehlke.fullstack.hackathon.database.BotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BotService {
    private final BotRepository botRepo;

    public Optional<BotDto> getBot(PlayerName name) {
        return botRepo.findById(name.value()).map(Bot::getDto);
    }

    public void addBot(PlayerName name) {
        botRepo.save(new Bot(name.value(), generateToken()));
    }

    public List<BotDto> getBots() {
        return botRepo.findAll().stream().map(Bot::getDto).toList();
    }

    String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
