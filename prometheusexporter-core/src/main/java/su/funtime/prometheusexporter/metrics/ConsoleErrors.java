package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.collectors.ConsoleErrorHandler;
import su.funtime.prometheusexporter.utils.ErrorStream;
import org.bukkit.plugin.Plugin;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class ConsoleErrors extends MetricCollector {

    private final AtomicInteger errorCounter = new AtomicInteger(0);

    public ConsoleErrors(Plugin plugin) {
        super(plugin, "console_errors", "Total amount of console errors", true);

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
    public double collect() {
        //Для сбора ошибок с момента запуска сервера - .get(), для обновления при каждом сборе метрики - .getAndSet(0)
        return errorCounter.getAndSet(0);

    }
}