package ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class HumidityExtractor implements ValueExtractor {

    @Override
    public boolean supports(ConditionType conditionType, Object indicator) {
        return conditionType == ConditionType.HUMIDITY && indicator instanceof ClimateSensorAvro;
    }

    @Override
    public Integer extractValue(Object indicator) {
        return ((ClimateSensorAvro) indicator).getHumidity();
    }
}
