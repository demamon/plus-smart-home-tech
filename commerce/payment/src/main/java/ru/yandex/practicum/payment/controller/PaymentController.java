package ru.yandex.practicum.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid OrderDto order) {
        return paymentService.createPayment(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal returnTotalCostOrder(@RequestBody @Valid OrderDto order) {
        return paymentService.returnTotalCostOrder(order);
    }

    @PostMapping("/productCost")
    public BigDecimal returnProductCostOrder(@RequestBody @Valid OrderDto order) {
        return paymentService.returnProductCostOrder(order);
    }

    @PostMapping("/refund")
    public void successPayment(@NotNull UUID uuidPayment) {
        paymentService.successPayment(uuidPayment);
    }

    @PostMapping("/failed")
    public void failedPayment(@NotNull UUID uuidPayment) {
        paymentService.failedPayment(uuidPayment);
    }
}
