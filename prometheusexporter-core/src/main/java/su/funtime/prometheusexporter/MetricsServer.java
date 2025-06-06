package su.funtime.prometheusexporter;

import org.eclipse.jetty.http.pathmap.PathSpec;
import su.funtime.prometheusexporter.health.HealthChecks;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.PathMappingsHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

import java.net.InetSocketAddress;

public class MetricsServer {

    private final String host;
    private final int port;
    private final PrometheusExporterImpl prometheusExporter;
	private final HealthChecks healthChecks;

    private Server server;

    public MetricsServer(String host, int port, PrometheusExporterImpl prometheusExporter, HealthChecks healthChecks) {
        this.host = host;
        this.port = port;
        this.prometheusExporter = prometheusExporter;
    this.healthChecks = healthChecks;
	}

    public void start() throws Exception {
        GzipHandler gzipHandler = new GzipHandler();

        var pathMappings = new PathMappingsHandler();
        pathMappings.addMapping(PathSpec.from("/metrics"), MetricsController.create(prometheusExporter));
        pathMappings.addMapping(PathSpec.from("/health"), HealthController.create(healthChecks));

        gzipHandler.setHandler(pathMappings);

        InetSocketAddress address = new InetSocketAddress(host, port);
        server = new Server(address);
        server.setHandler(gzipHandler);

        server.start();
    }

    public void stop() throws Exception {
        if (server == null) {
            return;
        }

        server.stop();
    }
}
