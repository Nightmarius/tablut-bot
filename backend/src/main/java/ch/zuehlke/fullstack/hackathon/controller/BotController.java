package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.common.*;
import ch.zuehlke.fullstack.hackathon.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BotController {

    private final BotAuthenticationService botAuthService;
    private final LobbyService lobbyService;
    private final GameService gameService;
    private final TournamentService tournamentService;
    private final NotificationService notificationService;

    @Operation(summary = "Bot joins",
            description = "Bot joins the lobby")
    @ApiResponse(responseCode = "200", description = "Successfully joined the lobby")
    @ApiResponse(responseCode = "403", description = "Bot credentials invalid")
    @PostMapping("/lobby/join")
    public ResponseEntity<JoinResponse> join(@RequestHeader("token") String token, @RequestBody JoinRequest joinRequest) {
        BotDto bot = new BotDto(joinRequest.name(), new Token(token));
        AuthenticationResult result = botAuthService.authenticate(bot);
        if (result == AuthenticationResult.DENIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        JoinResult joinResult = lobbyService.join(joinRequest.name());

        notificationService.notifyLobbyUpdate();
        return ResponseEntity.ok(new JoinResponse(joinResult.name()));
    }


    @Operation(summary = "Plays a move",
            description = "Plays a move")
    @ApiResponse(responseCode = "200", description = "Successfully played the move")
    @ApiResponse(responseCode = "400", description = "Player is not part of the game or the move is invalid")
    @ApiResponse(responseCode = "404", description = "Game was not found")
    @PostMapping("/game/{gameIdInt}/play")
    public ResponseEntity<Void> play(@PathVariable int gameIdInt, @RequestHeader String token, @RequestBody Move move) {
        GameId gameId = new GameId(gameIdInt);
        BotDto bot = new BotDto(move.name(), new Token(token));
        AuthenticationResult result = botAuthService.authenticate(bot);
        if (result == AuthenticationResult.DENIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PlayResult playResult = gameService.play(move, gameId);
        if (playResult == PlayResult.GAME_NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }
        if (playResult == PlayResult.PLAYER_NOT_PART_OF_GAME || playResult == PlayResult.INVALID_ACTION) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.notifyGameUpdate(gameId);
        TournamentId tournamentId = tournamentService.getTournamentId(gameId);
        tournamentService.update(tournamentId);
        notificationService.notifyTournamentUpdate(tournamentId);

        return ResponseEntity.ok().build();
    }
}