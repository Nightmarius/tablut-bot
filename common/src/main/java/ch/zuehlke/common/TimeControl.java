package ch.zuehlke.common;

import java.time.Duration;
import java.time.Instant;

public record TimeControl(PlayTime remainingTimeForAttacker, PlayTime remainingTimeForDefender,
                          Instant lastInteractionTime) {

    public static final Duration TIME_PER_PLAYER = Duration.ofMinutes(2);

    public TimeControl() {
        this(new PlayTime(TIME_PER_PLAYER), new PlayTime(TIME_PER_PLAYER), Instant.now());
    }

    public TimeControl subtractTimeForAttacker() {
        return new TimeControl(new PlayTime(this.remainingTimeForAttacker.remainingTime().minus(Duration.between(lastInteractionTime, Instant.now()))), this.remainingTimeForDefender, Instant.now());
    }

    public TimeControl subtractTimeForDefender() {
        return new TimeControl(this.remainingTimeForAttacker, new PlayTime(this.remainingTimeForDefender.remainingTime().minus(Duration.between(lastInteractionTime, Instant.now()))), Instant.now());
    }

    public TimeControl resetLastInteractionTime() {
        return new TimeControl(this.remainingTimeForAttacker, this.remainingTimeForDefender, Instant.now());
    }


    public boolean hasAttackerNoTimeLeft() {
        return this.remainingTimeForAttacker.hasEnded();
    }

    public boolean hasDefenderNoTimeLeft() {
        return this.remainingTimeForDefender.hasEnded();
    }
}
