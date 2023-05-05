package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.common.PlayerName;
import ch.zuehlke.fullstack.hackathon.controller.JoinResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LobbyService {
    private final GameService gameService;
    // Improve: Instead of storing this in-memory, store it in a database
    private final List<PlayerName> players = new ArrayList<>();

    public List<PlayerName> getPlayers() {
        return players;
    }

    public JoinResult join(PlayerName name) {
        players.add(name);
        return new JoinResult(name, JoinResult.JoinResultType.SUCCESS);
    }
}
