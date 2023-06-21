package ch.zuehlke.common;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class PlayTimeTest {

    @Test
    void construct_withRemainingTime_successfully() {
        var playTime = new PlayTime(Duration.ofMinutes(2));

        assertThat(playTime.remainingTime()).isEqualTo(Duration.ofMinutes(2));
    }

    @Test
    void construct_withNegativeRemainingTime_willSetItToZeroDuration() {
        var playTime = new PlayTime(Duration.ofMinutes(-2));

        assertThat(playTime.remainingTime()).isEqualTo(Duration.ZERO);
    }

    @Test
    void construct_withNullRemainingTime_willSetItToZeroDuration() {
        var playTime = new PlayTime(null);

        assertThat(playTime.remainingTime()).isEqualTo(Duration.ZERO);
    }

    @Test
    void hasEnded_withRemainingTime_isFalse() {
        var playTime = new PlayTime(Duration.ofMinutes(2));

        assertThat(playTime.hasEnded()).isFalse();
    }

    @Test
    void hasEnded_withZeroRemainingTime_isTrue() {
        var playTime = new PlayTime(Duration.ZERO);

        assertThat(playTime.hasEnded()).isTrue();
    }
}
