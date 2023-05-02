package ch.zuehlke.common;

public record BotDto(PlayerName name, Token token) {
    public BotDto map(String name, String token) {
        return new BotDto(new PlayerName(name), new Token(token));
    }
}
