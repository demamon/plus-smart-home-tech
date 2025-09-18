package ru.yandex.practicum.interaction.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    public DeliveryDto saveDelivery(DeliveryDto deliveryDto);

    @PostMapping("/successful")
    public void deliveryStateSuccessful(UUID uuidDelivery);

    @PostMapping("/picked")
    public void deliveryStatePicked(UUID uuidDelivery);

    @PostMapping("/failed")
    public void deliveryStateFailed(UUID uuidDelivery);

    @PostMapping("/cost")
    public BigDecimal fullCostDelivery(OrderDto orderDto);
}
