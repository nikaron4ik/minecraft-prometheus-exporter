package de.sldk.mc.metrics;

import de.sldk.mc.collectors.ConsoleErrorHandler;
import de.sldk.mc.utils.ErrorStream;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConsoleErrors extends Metric {

    private static final Gauge ERROR_COUNTER = Gauge.build()
            .name(prefix("console_errors"))
            .help("Total amount of console errors")
            .create();

    private final AtomicInteger errorCounter = new AtomicInteger(0);

    public ConsoleErrors(Plugin plugin) {
        super(plugin, ERROR_COUNTER);

        Logger globalLogger = Logger.getLogger("");

        // Проверка на то, добавлен ли уже обработчик ошибок или нет. В случае его отсутствия - добавление обработчика.
        if (!isHandlerAlreadyRegistered(globalLogger)) {
            globalLogger.addHandler(new ConsoleErrorHandler(errorCounter));
        }

        // Перенаправление System.err в логгер
        System.setErr(new PrintStream(new ErrorStream(globalLogger), true, StandardCharsets.UTF_8));
    }

        private boolean isHandlerAlreadyRegistered(Logger logger) {
            for(Handler handler : logger.getHandlers()) {
                if (handler instanceof ConsoleErrorHandler) {
                    return true;
                }
            }
            return false;
        }

    @Override
    protected void doCollect() {
        ERROR_COUNTER.set(errorCounter.getAndSet(0));  //Для сбора ошибок с момента запуска сервера - .get(), для обновления при каждом сборе метрики -   .getAndSet(0)
    }

    @Override
    public boolean isFoliaCapable() {
        return true;
    }

    @Override
    public boolean isAsyncCapable() {
        return true;
    }
}