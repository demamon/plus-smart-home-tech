package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {

    @PutMapping
    public OrderDto addOrder(@RequestBody @Valid CreateNewOrderRequest newOrderRequest);

    @GetMapping
    public List<OrderDto> getOrders(@RequestParam String username,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size);

    @PostMapping("/return")
    public OrderDto returnOder(@RequestBody @Valid ProductReturnRequest returnRequest);

    @PostMapping("/payment")
    public OrderDto paymentOrder(@RequestBody UUID uuid);

    @PostMapping("/payment/failed")
    public OrderDto failedPaymentOrder(@RequestBody UUID uuid);

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID uuid);

    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryOrder(@RequestBody UUID uuid);

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID uuid);

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID uuid);

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyOrder(@RequestBody UUID uuid);

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID uuid);

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID uuid);
}
