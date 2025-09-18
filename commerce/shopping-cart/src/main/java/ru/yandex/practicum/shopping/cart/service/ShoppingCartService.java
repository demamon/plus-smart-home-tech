package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto addProducts(String username, Map<UUID, Integer> request);

    ShoppingCartDto getShoppingCart(String username);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeProductsFromCart(String username, List<UUID> productId);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

    List<ShoppingCartDto> getAllShoppingCartByName(String username);
}
