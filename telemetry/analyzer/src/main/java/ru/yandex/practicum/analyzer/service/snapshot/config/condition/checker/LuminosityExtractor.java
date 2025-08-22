package ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LuminosityExtractor implements ValueExtractor {

    @Override
    public boolean supports(ConditionType conditionType, Object indicator) {
        return conditionType == ConditionType.LUMINOSITY && indicator instanceof LightSensorAvro;
    }

    @Override
    public Integer extractValue(Object indicator) {
        return ((LightSensorAvro) indicator).getLuminosity();
    }
}
