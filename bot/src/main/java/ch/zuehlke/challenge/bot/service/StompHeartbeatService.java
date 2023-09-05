package ch.zuehlke.challenge.bot.service;

import ch.zuehlke.challenge.bot.client.StompClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class StompHeartbeatService {

    private final StompClient stompClient;

    // this heartbeat is used to send a message to the backend to keep the websocket connection alive
    // otherwise Heroku will close the connection after 55 seconds
    // this is not a problem for the bot, because it will reconnect automatically
    // however it causes a lot of misleading log messages org.springframework.messaging.simp.stomp.ConnectionLostException: Connection closed
    // see https://stackoverflow.com/questions/32728030/heroku-h15-error-on-web-socket-close
    // and https://devcenter.heroku.com/articles/error-codes#h15-idle-connection
    @Async
    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void sendHeartbeat() {
        stompClient.getStompSession().ifPresent(stompSession -> {
            stompSession.send("/update", "keepalive");
        });
    }
}
