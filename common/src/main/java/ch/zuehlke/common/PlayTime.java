package ch.zuehlke.common;

import java.beans.Transient;
import java.time.Duration;

public record PlayTime(Duration remainingTime) {

    public PlayTime(Duration remainingTime) {
        this.remainingTime = remainingTime == null || remainingTime.isNegative() ? Duration.ZERO : remainingTime;
    }

    @Transient
    public boolean hasEnded() {
        return remainingTime.isZero();
    }
}
