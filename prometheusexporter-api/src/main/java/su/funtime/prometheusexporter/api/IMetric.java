package su.funtime.prometheusexporter.api;

import java.util.concurrent.CompletableFuture;

public interface IMetric {

    CompletableFuture<Void> collect();
}
