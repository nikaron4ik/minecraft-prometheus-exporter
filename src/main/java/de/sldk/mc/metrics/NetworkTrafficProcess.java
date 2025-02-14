package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class NetworkTrafficProcess extends Metric {

    private static final Gauge NETWORK_BYTES = Gauge.build()
            .name(prefix("network_bytes"))
            .help("Network traffic in bytes")
            .labelNames("type")
            .create();


    private final SystemInfo systemInfo = new SystemInfo();
    private final int processId;

    private long previousBytesSent = 0;
    private long previousBytesReceived = 0;
    private long lastUpdateTime = System.currentTimeMillis();

    public NetworkTrafficProcess(Plugin plugin) {
        super(plugin, NETWORK_BYTES);
        this.processId = getCurrentProcessId();
    }

    private int getCurrentProcessId() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String processName = runtimeBean.getName();
        return Integer.parseInt(processName.split("@")[0]);
    }

    private long[] getNetworkTraffic() {
        OperatingSystem os = systemInfo.getOperatingSystem();
        OSProcess process = os.getProcess(processId);

        if (process != null) {
            long bytesSent = process.getBytesWritten();
            long bytesReceived = process.getBytesRead();
            return new long[]{bytesSent, bytesReceived};
        } else {
            return new long[]{0, 0};
        }
    }

    @Override
    protected void doCollect() {
        long[] traffic = getNetworkTraffic();
        long currentBytesSent = traffic[0];
        long currentBytesReceived = traffic[1];

        long currentTime = System.currentTimeMillis();
        long timeDifference = (currentTime - lastUpdateTime) / 1000;
        if (timeDifference == 0) {
            return;
        }

        double bytesSentPerSecond = ((double)(currentBytesSent - previousBytesSent) / timeDifference) * 8 / 1024 / 1024;
        double bytesReceivedPerSecond = ((double)(currentBytesReceived - previousBytesReceived) / timeDifference) * 8 / 1024 / 1024;

        NETWORK_BYTES.labels("sent").set(bytesSentPerSecond);
        NETWORK_BYTES.labels("received").set(bytesReceivedPerSecond);

        previousBytesSent = currentBytesSent;
        previousBytesReceived = currentBytesReceived;
        lastUpdateTime = currentTime;
    }

}