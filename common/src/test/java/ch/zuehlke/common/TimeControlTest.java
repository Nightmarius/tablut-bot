package ch.zuehlke.common;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeControlTest {

    @Test
    void construct_withRemainingTime_successfully() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ofMinutes(2)), Instant.now());

        assertThat(timeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(timeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(timeControl.lastInteractionTime()).isNotNull();
    }

    @Test
    void construct_withNoArguments_successfully() {
        TimeControl timeControl = new TimeControl();

        assertThat(timeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(timeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(timeControl.lastInteractionTime()).isNotNull();
    }

    @Test
    void subtractTimeForAttacker_successfully() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ofMinutes(2)), Instant.now().minus(Duration.ofSeconds(10)));

        TimeControl subtractedTimeControl = timeControl.subtractTimeForAttacker();

        assertThat(subtractedTimeControl.remainingTimeForAttacker().remainingTime()).isLessThanOrEqualTo(Duration.ofSeconds(110));
        assertThat(subtractedTimeControl.remainingTimeForDefender()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(subtractedTimeControl.lastInteractionTime()).isNotNull();
    }

    @Test
    void subtractTimeForDefender_successfully() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ofMinutes(2)), Instant.now().minus(Duration.ofSeconds(10)));

        TimeControl subtractedTimeControl = timeControl.subtractTimeForDefender();

        assertThat(subtractedTimeControl.remainingTimeForDefender().remainingTime()).isLessThanOrEqualTo((Duration.ofSeconds(110)));
        assertThat(subtractedTimeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(subtractedTimeControl.lastInteractionTime()).isNotNull();
    }

    @Test
    void resetLastInteractionTime_successfully() {
        Instant previousInteractionTime = Instant.now().minus(Duration.ofSeconds(10));
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ofMinutes(2)), previousInteractionTime);

        TimeControl subtractedTimeControl = timeControl.resetLastInteractionTime();

        assertThat(subtractedTimeControl.remainingTimeForDefender()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(subtractedTimeControl.remainingTimeForAttacker()).isEqualTo(new PlayTime(Duration.ofMinutes(2)));
        assertThat(subtractedTimeControl.lastInteractionTime()).isAfter(previousInteractionTime);
    }

    @Test
    void hasAttackerNoTimeLeft_withZeroTime_isTrue() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ZERO), new PlayTime(Duration.ofMinutes(2)), Instant.now());

        assertThat(timeControl.hasAttackerNoTimeLeft()).isTrue();
    }

    @Test
    void hasAttackerNoTimeLeft_withPositiveTime_isFalse() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(1)), new PlayTime(Duration.ofMinutes(2)), Instant.now());

        assertThat(timeControl.hasAttackerNoTimeLeft()).isFalse();
    }

    @Test
    void hasDefenderNoTimeLeft_withZeroTime_isTrue() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ZERO), Instant.now());

        assertThat(timeControl.hasDefenderNoTimeLeft()).isTrue();
    }

    @Test
    void hasDefenderNoTimeLeft_withPositiveTime_isFalse() {
        TimeControl timeControl = new TimeControl(new PlayTime(Duration.ofMinutes(2)), new PlayTime(Duration.ofMinutes(1)), Instant.now());

        assertThat(timeControl.hasDefenderNoTimeLeft()).isFalse();
    }
}
