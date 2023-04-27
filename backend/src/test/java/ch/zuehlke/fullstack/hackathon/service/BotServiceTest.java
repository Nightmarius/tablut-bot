package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.database.BotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class BotServiceTest {

    private BotService botService;
    private BotRepository mockRepository;


    @BeforeEach
    void setUp() {
        mockRepository = mock(BotRepository.class);
        botService = new BotService(mockRepository);
    }

    @Test
    void generateToken_successfully() {
        String token = botService.generateToken();
        assertThat(token).hasSize(32);
    }
}
