package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto addDelivery(DeliveryDto deliveryDto);

    void deliveryStateSuccessful(UUID uuidDelivery);

    void deliveryStatePicked(UUID uuidDelivery);

    void deliveryStateFailed(UUID uuidDelivery);

    BigDecimal fullCostDelivery(OrderDto orderDto);
}
