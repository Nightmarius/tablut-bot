package ch.zuehlke.fullstack.hackathon.database;

import ch.zuehlke.common.BotDto;
import ch.zuehlke.common.PlayerName;
import ch.zuehlke.common.Token;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bot {
    @Id
    private String name;
    private String token;

    public BotDto getDto() {
        return new BotDto(new PlayerName(this.name), new Token(this.token));
    }
}