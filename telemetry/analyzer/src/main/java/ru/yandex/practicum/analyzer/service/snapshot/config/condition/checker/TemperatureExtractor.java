package ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureExtractor implements ValueExtractor {

    @Override
    public boolean supports(ConditionType conditionType, Object indicator) {
        return conditionType == ConditionType.TEMPERATURE &&
                (indicator instanceof ClimateSensorAvro || indicator instanceof TemperatureSensorAvro);
    }

    @Override
    public Integer extractValue(Object indicator) {
        if (indicator instanceof ClimateSensorAvro) {
            return ((ClimateSensorAvro) indicator).getTemperatureC();
        } else if (indicator instanceof TemperatureSensorAvro) {
            return ((TemperatureSensorAvro) indicator).getTemperatureC();
        }
        return null;
    }
}
