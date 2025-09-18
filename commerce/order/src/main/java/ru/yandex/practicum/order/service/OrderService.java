package ru.yandex.practicum.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    OrderDto addOrder(CreateNewOrderRequest newOrderRequest);

    Page<OrderDto> getOrders(String username, Pageable pageable);

    OrderDto returnOder(ProductReturnRequest returnRequest);

    OrderDto paymentOrder(UUID uuid);

    OrderDto failedPaymentOrder(UUID uuid);

    OrderDto deliveryOrder(UUID uuid);

    OrderDto failedDeliveryOrder(UUID uuid);

    OrderDto completedOrder(UUID uuid);

    OrderDto assemblyOrder(UUID uuid);

    OrderDto failedAssemblyOrder(UUID uuid);

    OrderDto calculateTotalCost(UUID uuid);

    OrderDto calculateDeliveryCost(UUID uuid);
}
