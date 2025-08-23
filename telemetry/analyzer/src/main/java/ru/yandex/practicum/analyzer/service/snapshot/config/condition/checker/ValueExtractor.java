package ru.yandex.practicum.analyzer.service.snapshot.config.condition.checker;

import ru.yandex.practicum.analyzer.model.enums.ConditionType;

public interface ValueExtractor {
    boolean supports(ConditionType conditionType, Object indicator);
    Integer extractValue(Object indicator);
}
