package su.funtime.prometheusexporter.metrics.tick_duration;

public interface TickDurationStrategy {
    long[] getTickDurations();
}
