package ru.yandex.practicum.analyzer.service.snapshot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker.ValueExtractor;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConditionChecker {

    private final List<ValueExtractor> valueExtractors;

    public boolean checkCondition(Condition condition, Object indicator) {
        if (condition == null || indicator == null) {
            log.debug("Condition or indicator is null");
            return false;
        }

        Integer sensorValue = extractSensorValue(condition.getType(), indicator);
        if (sensorValue == null) {
            log.info("Failed to extract value for condition type {} from {}",
                    condition.getType(), indicator.getClass().getSimpleName());
            return false;
        }

        return switch (condition.getOperation()) {
            case EQUALS -> sensorValue.equals(condition.getValue());
            case GREATER_THAN -> sensorValue > condition.getValue();
            case LOWER_THAN -> sensorValue < condition.getValue();
        };
    }

    private Integer extractSensorValue(ConditionType conditionType, Object indicator) {
        return valueExtractors.stream()
                .filter(extractor -> extractor.supports(conditionType, indicator))
                .findFirst()
                .map(extractor -> extractor.extractValue(indicator))
                .orElse(null);
    }
}
