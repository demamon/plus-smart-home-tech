package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.enums.order.OrderState;
import ru.yandex.practicum.interaction.api.feign.DeliveryClient;
import ru.yandex.practicum.interaction.api.feign.PaymentClient;
import ru.yandex.practicum.interaction.api.feign.ShoppingCartClient;
import ru.yandex.practicum.interaction.api.feign.WarehouseClient;
import ru.yandex.practicum.order.exception.NoOrderFoundException;
import ru.yandex.practicum.order.exception.NonAuthorizedUserException;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;


    @Override
    public OrderDto addOrder(CreateNewOrderRequest newOrderRequest) {
        Order order = Order.builder().
                shoppingCartId(newOrderRequest.getShoppingCart().getShoppingCartId()).
                products(newOrderRequest.getShoppingCart().getProducts()).
                state(OrderState.NEW).
                build();
        order = orderRepository.save(order);
        BookedProductsDto bookedProducts = warehouseClient.assemblyProducts(
                new AssemblyProductsForOrderRequest(
                        newOrderRequest.getShoppingCart().getProducts(),
                        order.getOrderId()
                ));

        order.setFragile(bookedProducts.getFragile());
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setProductPrice(paymentClient.returnProductCostOrder(orderMapper.toOrderDto(order)));

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .fromAddress(warehouseClient.getAddress())
                .toAddress(newOrderRequest.getDeliveryAddress())
                .build();
        order.setDeliveryId(deliveryClient.saveDelivery(deliveryDto).getDeliveryId());

        paymentClient.createPayment(orderMapper.toOrderDto(order));
        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(String username, Pageable pageable) {
        checkUsername(username);
        List<ShoppingCartDto> shoppingCartsDto = shoppingCartClient.getAllShoppingCartByName(username);
        List<UUID> cartsId = shoppingCartsDto.stream()
                .map(ShoppingCartDto::getShoppingCartId)
                .toList();
        if (cartsId.isEmpty()) {
            return Page.empty(pageable);
        }
        Page<Order> orderPage = orderRepository.findByShoppingCartIdIn(cartsId, pageable);
        return orderPage.map(orderMapper::toOrderDto);
    }

    @Override
    public OrderDto returnOder(ProductReturnRequest returnRequest) {
        Order order = findByIdOrder(returnRequest.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        warehouseClient.returnProducts(returnRequest.getProducts());
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto paymentOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.PAID);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedPaymentOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto deliveryOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedDeliveryOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto completedOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto assemblyOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto failedAssemblyOrder(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateTotalCost(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setTotalPrice(paymentClient.returnTotalCostOrder(orderMapper.toOrderDto(order)));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID uuid) {
        Order order = findByIdOrder(uuid);
        order.setDeliveryPrice(deliveryClient.fullCostDelivery(orderMapper.toOrderDto(order)));
        return orderMapper.toOrderDto(order);
    }

    private void checkUsername(String username) {
        if (username.isEmpty()) {
            throw new NonAuthorizedUserException("имя не может быть пустым");
        }
    }

    private Order findByIdOrder(UUID uuid) {
        return orderRepository.findById(uuid)
                .orElseThrow(() -> new NoOrderFoundException("заказ с uuid = " + uuid + "не найден"));
    }
}
