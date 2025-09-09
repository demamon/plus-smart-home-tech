package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.address.Address;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.warehouse.model.Warehouse;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        warehouseRepository.findById(request.getProductId()).ifPresent(warehouse -> {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с таким описанием уже зарегистрирован на складе.");
        });
        Warehouse warehouse = warehouseMapper.toWarehouse(request);
        warehouseRepository.save(warehouse);
    }

    @Override
    public BookedProductsDto checkCountProducts(ShoppingCartResponseDto shoppingCartResponseDto) {
        Map<UUID, Integer> cartProducts = shoppingCartResponseDto.getProducts();

        Map<UUID, Warehouse> warehouseProducts = validateAndCheckQuantities(cartProducts);
        return calculateBookingInfo(warehouseProducts, cartProducts);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {

        Warehouse warehouse = warehouseRepository.findById(addProductToWarehouseRequest.getProductId()).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException("Нет информации о товаре на складе.")
        );

        warehouse.setQuantity(warehouse.getQuantity() + addProductToWarehouseRequest.getQuantity());
        warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getAddress() {
        String address = Address.CURRENT_ADDRESS;
        return AddressDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();
    }

    private Map<UUID, Warehouse> validateAndCheckQuantities(Map<UUID, Integer> cartProducts) {
        Set<UUID> cartProductIds = cartProducts.keySet();

        Map<UUID, Warehouse> warehouseProducts = warehouseRepository.findAllById(cartProductIds)
                .stream()
                .collect(Collectors.toMap(Warehouse::getProductId, Function.identity()));

        boolean errors = false;

        for (UUID productId : cartProductIds) {
            Warehouse warehouseProduct = warehouseProducts.get(productId);

            if (warehouseProduct == null) {
                errors = true;
                continue;
            }

            int requestedQuantity = cartProducts.get(productId);
            int availableQuantity = warehouseProduct.getQuantity();

            if (availableQuantity < requestedQuantity) {
                errors = true;
            }
        }

        if (errors) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    "товар из корзины не находится в требуемом количестве на складе");
        }

        return warehouseProducts;
    }

    private BookedProductsDto calculateBookingInfo(Map<UUID, Warehouse> warehouseProducts,
                                                   Map<UUID, Integer> cartProducts) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<UUID, Warehouse> entry : warehouseProducts.entrySet()) {
            UUID productId = entry.getKey();
            Warehouse product = entry.getValue();
            int quantity = cartProducts.get(productId);

            totalWeight += safeMultiply(product.getWeight(), quantity);

            double volume = safeGetValue(product.getWidth()) *
                    safeGetValue(product.getHeight()) *
                    safeGetValue(product.getDepth()) * quantity;
            totalVolume += volume;

            if (Boolean.TRUE.equals(product.getFragile())) {
                hasFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .fragile(hasFragile)
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .build();
    }

    private double safeGetValue(Double value) {
        return value != null && value > 0 ? value : 0.0;
    }

    private double safeMultiply(Double value, int multiplier) {
        return value != null && value > 0 ? value * multiplier : 0.0;
    }
}
