package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkCountProducts(ShoppingCartDto shoppingCartDto);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    AddressDto getAddress();

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void shippedDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Integer> returnProducts);
}
