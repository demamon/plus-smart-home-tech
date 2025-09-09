package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void addProduct (NewProductInWarehouseRequest request);

    BookedProductsDto checkCountProducts(ShoppingCartResponseDto shoppingCartResponseDto);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    AddressDto getAddress();
}
