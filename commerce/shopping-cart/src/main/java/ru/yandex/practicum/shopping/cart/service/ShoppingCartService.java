package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartResponseDto addProducts(String username, Map<UUID, Integer> request);

    ShoppingCartResponseDto getShoppingCart(String username);

    void deactivateShoppingCart(String username);

    ShoppingCartResponseDto removeProductsFromCart(String username, List<UUID> productId);

    ShoppingCartResponseDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
