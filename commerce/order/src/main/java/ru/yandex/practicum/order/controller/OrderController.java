package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto addOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest) {
        return orderService.addOrder(newOrderRequest);
    }

    @GetMapping
    public Page<OrderDto> getOrders(@RequestParam String username,
                                    Pageable pageable) {
        return orderService.getOrders(username, pageable);
    }

    @PostMapping("/return")
    public OrderDto returnOder(@RequestBody @Valid ProductReturnRequest returnRequest) {
        return orderService.returnOder(returnRequest);
    }

    @PostMapping("/payment")
    public OrderDto paymentOrder(@RequestBody UUID uuid) {
        return orderService.paymentOrder(uuid);
    }

    @PostMapping("/payment/failed")
    public OrderDto failedPaymentOrder(@RequestBody UUID uuid) {
        return orderService.failedPaymentOrder(uuid);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID uuid) {
        return orderService.deliveryOrder(uuid);
    }

    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryOrder(@RequestBody UUID uuid) {
        return orderService.failedDeliveryOrder(uuid);
    }

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID uuid) {
        return orderService.completedOrder(uuid);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID uuid) {
        return orderService.assemblyOrder(uuid);
    }

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyOrder(@RequestBody UUID uuid) {
        return orderService.failedAssemblyOrder(uuid);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID uuid) {
        return orderService.calculateTotalCost(uuid);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID uuid) {
        return orderService.calculateDeliveryCost(uuid);
    }

}
