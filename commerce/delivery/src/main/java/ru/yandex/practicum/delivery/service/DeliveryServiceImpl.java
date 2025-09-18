package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.exception.NoDeliveryFoundException;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.api.enums.delivery.DeliveryState;
import ru.yandex.practicum.interaction.api.feign.OrderClient;
import ru.yandex.practicum.interaction.api.feign.WarehouseClient;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto addDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    public void deliveryStateSuccessful(UUID uuidDelivery) {
        Delivery delivery = findByIdDelivery(uuidDelivery);
        orderClient.deliveryOrder(uuidDelivery);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
    }

    @Override
    public void deliveryStatePicked(UUID uuidDelivery) {
        Delivery delivery = findByIdDelivery(uuidDelivery);
        orderClient.assemblyOrder(delivery.getOrderId());
        warehouseClient.shippedDelivery(
                ShippedToDeliveryRequest.builder()
                        .orderId(delivery.getOrderId())
                        .deliveryId(uuidDelivery)
                        .build()
        );
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
    }

    @Override
    public void deliveryStateFailed(UUID uuidDelivery) {
        Delivery delivery = findByIdDelivery(uuidDelivery);
        orderClient.failedDeliveryOrder(uuidDelivery);
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal fullCostDelivery(OrderDto orderDto) {
        Delivery delivery = findByIdDelivery(orderDto.getDeliveryId());

        final BigDecimal BASE_COST = BigDecimal.valueOf(5.0);
        final BigDecimal ADDRESS_1_MULTIPLIER = BigDecimal.valueOf(1);
        final BigDecimal ADDRESS_2_MULTIPLIER = BigDecimal.valueOf(2);
        final BigDecimal FRAGILE_RATE = BigDecimal.valueOf(0.2);
        final BigDecimal WEIGHT_RATE = BigDecimal.valueOf(0.3);
        final BigDecimal VOLUME_RATE = BigDecimal.valueOf(0.2);
        final BigDecimal DIFFERENT_STREET_RATE = BigDecimal.valueOf(0.2);

        BigDecimal currentCost = BASE_COST;

        String warehouseStreet = delivery.getFromAddress().getStreet();
        if ("ADDRESS_1".equals(warehouseStreet)) {
            currentCost = currentCost.add(BASE_COST.multiply(ADDRESS_1_MULTIPLIER));
        } else if ("ADDRESS_2".equals(warehouseStreet)) {
            currentCost = currentCost.add(BASE_COST.multiply(ADDRESS_2_MULTIPLIER));
        }

        if (delivery.getFragile()) {
            BigDecimal fragileSurcharge = currentCost.multiply(FRAGILE_RATE);
            currentCost = currentCost.add(fragileSurcharge);
        }

        BigDecimal weightSurcharge = BigDecimal.valueOf(delivery.getDeliveryWeight())
                .multiply(WEIGHT_RATE);
        BigDecimal volumeSurcharge = BigDecimal.valueOf(delivery.getDeliveryVolume())
                .multiply(VOLUME_RATE);
        currentCost = currentCost.add(weightSurcharge).add(volumeSurcharge);


        String deliveryStreet = delivery.getToAddress().getStreet();
        if (!warehouseStreet.equals(deliveryStreet)) {
            BigDecimal addressSurcharge = currentCost.multiply(DIFFERENT_STREET_RATE);
            currentCost = currentCost.add(addressSurcharge);
        }

        return currentCost;
    }

    private Delivery findByIdDelivery(UUID uuid) {
        return deliveryRepository.findById(uuid)
                .orElseThrow(() -> new NoDeliveryFoundException("доставка с id = " + uuid + "не найдена"));
    }
}
