package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.api.enums.payment.PaymentState;
import ru.yandex.practicum.interaction.api.feign.OrderClient;
import ru.yandex.practicum.interaction.api.feign.ShoppingStoreClient;
import ru.yandex.practicum.payment.exception.NoPaymentFoundException;
import ru.yandex.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    private final String MESSAGE_NOT_INFORMATION = "Недостаточно информации в заказе для расчёта.";

    @Override
    public PaymentDto createPayment(OrderDto order) {
        checkOrder(order);
        Payment payment = Payment.builder()
                .orderId(order.getOrderId())
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .productTotal(order.getProductPrice())
                .feeTotal(getTax(order.getTotalPrice()))
                .paymentState(PaymentState.PENDING)
                .build();
        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal returnTotalCostOrder(OrderDto order) {
        if (order.getDeliveryPrice() == null || order.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(MESSAGE_NOT_INFORMATION);
        }
        return order.getProductPrice().add(getTax(order.getProductPrice())).add(order.getDeliveryPrice());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal returnProductCostOrder(OrderDto order) {
        if (order.getProducts() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(MESSAGE_NOT_INFORMATION);
        }
        return order.getProducts().entrySet().stream()
                .map(entry -> shoppingStoreClient.getProductById(entry.getKey())
                        .getPrice()
                        .multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void successPayment(UUID uuidPayment) {
        Payment payment = findByIdPayment(uuidPayment);
        orderClient.paymentOrder(payment.getOrderId());
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
    }

    @Override
    public void failedPayment(UUID uuidPayment) {
        Payment payment = findByIdPayment(uuidPayment);
        orderClient.failedPaymentOrder(payment.getOrderId());
        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);
    }

    private void checkOrder(OrderDto orderDto) {
        if (orderDto.getDeliveryPrice() == null ||
                orderDto.getProductPrice() == null ||
                orderDto.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(MESSAGE_NOT_INFORMATION);
        }
    }

    private BigDecimal getTax(BigDecimal totalPrice) {
        return totalPrice.multiply(new BigDecimal("0.1"));
    }

    private Payment findByIdPayment(UUID uuidPayment) {
        return paymentRepository.findById(uuidPayment)
                .orElseThrow(() -> new NoPaymentFoundException("информация о платеже с id = " + uuidPayment
                        + " не найдена"));
    }
}
