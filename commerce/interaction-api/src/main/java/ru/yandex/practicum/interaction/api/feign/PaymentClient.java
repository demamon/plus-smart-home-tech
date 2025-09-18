package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/totalCost")
    BigDecimal returnTotalCostOrder(@RequestBody @Valid OrderDto order);

    @PostMapping("/productCost")
    BigDecimal returnProductCostOrder(@RequestBody @Valid OrderDto order);

    @PostMapping("/refund")
    void successPayment(@NotNull UUID uuidPayment);

    @PostMapping("/failed")
    void failedPayment(@NotNull UUID uuidPayment);
}
