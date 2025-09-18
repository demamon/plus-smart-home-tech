package ru.yandex.practicum.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto saveDelivery(DeliveryDto deliveryDto) {
        return deliveryService.addDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void deliveryStateSuccessful(UUID uuidDelivery) {
        deliveryService.deliveryStateSuccessful(uuidDelivery);
    }

    @PostMapping("/picked")
    public void deliveryStatePicked(UUID uuidDelivery) {
        deliveryService.deliveryStatePicked(uuidDelivery);
    }

    @PostMapping("/failed")
    public void deliveryStateFailed(UUID uuidDelivery) {
        deliveryService.deliveryStateFailed(uuidDelivery);
    }

    @PostMapping("/cost")
    public BigDecimal fullCostDelivery(OrderDto orderDto) {
        return deliveryService.fullCostDelivery(orderDto);
    }
}
