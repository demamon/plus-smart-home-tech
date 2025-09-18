package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    BigDecimal returnTotalCostOrder(OrderDto order);

    BigDecimal returnProductCostOrder(OrderDto order);

    void successPayment(UUID uuidPayment);

    void failedPayment(UUID uuidPayment);
}
