package ch.zuehlke.fullstack.hackathon.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BotData {
    @Id
    private Long id;
    private String name;
    private String token;
}