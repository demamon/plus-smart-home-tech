package ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionExtractor implements ValueExtractor {

    @Override
    public boolean supports(ConditionType conditionType, Object indicator) {
        return conditionType == ConditionType.MOTION && indicator instanceof MotionSensorAvro;
    }

    @Override
    public Integer extractValue(Object indicator) {
        return ((MotionSensorAvro) indicator).getMotion() ? 1 : 0;
    }
}
