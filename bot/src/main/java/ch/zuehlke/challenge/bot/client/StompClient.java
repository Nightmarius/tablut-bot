package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.service.GameService;
import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import ch.zuehlke.common.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PreDestroy;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@EnableAsync
// Adapted from tutorial at https://blog.dkwr.de/development/spring/spring-stomp-client/
public class StompClient implements StompSessionHandler {

    private final ApplicationProperties applicationProperties;

    private StompSession stompSession;

    private StompSession.Subscription subscription;

    private final GameService gameService;

    @EventListener(value = ApplicationReadyEvent.class)
    public void connect() {
        WebSocketStompClient stompClient = getWebSocketStompClient();
        try {
            String socketUrl = applicationProperties.getWebSocketUri();
            stompSession = stompClient.connectAsync(socketUrl, this).get();
        } catch (Exception e) {
            log.error("Connection failed.", e);
        }
    }

    private static WebSocketStompClient getWebSocketStompClient() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        int bufferSize = 1024 * 1024 * 100; // set it to a very large value

        container.setDefaultMaxTextMessageBufferSize(bufferSize);
        container.setDefaultMaxBinaryMessageBufferSize(bufferSize);

        WebSocketClient client = new StandardWebSocketClient(container);
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setInboundMessageSizeLimit(bufferSize);
        stompClient.setMessageConverter(getMappingJackson2MessageConverter());

        return stompClient;
    }

    // create a converter which is able to deserialize java.time classes
    private static MappingJackson2MessageConverter getMappingJackson2MessageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(new ObjectMapper().registerModule(new JavaTimeModule()));
        return messageConverter;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connection to STOMP server established. Subscribing to game updates...");
        this.subscription = stompSession.subscribe("/topic/game", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("""
                Got an exception while handling a frame.
                Command: {}
                Headers: {}
                Payload: {}
                {}""", command, headers, payload, exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Retrieved a transport error: {}", session);
        exception.printStackTrace();
        if (!session.isConnected()) {
            this.subscription = null;
            connect();
        }
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Got a new message {}", payload);
        GameDto game = (GameDto) payload;
        gameService.onGameUpdate(game);
    }

    public Optional<StompSession> getStompSession() {
        if (stompSession == null || !stompSession.isConnected()) {
            return Optional.empty();
        }
        return Optional.of(stompSession);
    }


    @PreDestroy
    void onShutDown() {
        if (this.subscription != null) {
            this.subscription.unsubscribe();
        }

        if (stompSession != null) {
            stompSession.disconnect();
        }
    }
}
