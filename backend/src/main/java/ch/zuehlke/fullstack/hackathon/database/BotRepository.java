package ch.zuehlke.fullstack.hackathon.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}
