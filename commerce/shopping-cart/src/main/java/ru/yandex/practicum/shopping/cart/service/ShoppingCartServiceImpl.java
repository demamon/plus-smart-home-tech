package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;
import ru.yandex.practicum.interaction.api.feign.WarehouseClient;
import ru.yandex.practicum.shopping.cart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartResponseDto addProducts(String username, Map<UUID, Integer> request) {
        ShoppingCart shoppingCart = getActiveCartByUsername(username);
        shoppingCart.setProducts(request);
        warehouseClient.checkCountProducts(shoppingCartMapper.toResponseDto(shoppingCart));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(String username) {
        return shoppingCartMapper.toResponseDto(getActiveCartByUsername(username));
    }

    @Override
    public void deactivateShoppingCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username);
        shoppingCart.setActive(false);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto removeProductsFromCart(String username, List<UUID> productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Список товаров для удаления не может быть пустым");
        }
        ShoppingCart shoppingCart = validateAndGetCartWithProducts(username, productId);
        Map<UUID, Integer> products = shoppingCart.getProducts();

        for (UUID id : productId) {
            products.remove(id);
        }
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    public ShoppingCartResponseDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart shoppingCart = validateAndGetCartWithProducts(username, List.of(request.getProductId()));
        Map<UUID, Integer> products = shoppingCart.getProducts();
        if (request.getNewQuantity() == 0) {
            products.remove(request.getProductId());
        } else {
            products.put(request.getProductId(), request.getNewQuantity());
        }
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(shoppingCart);

    }

    private ShoppingCart getActiveCartByUsername(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .username(username)
                            .active(true)
                            .products(new HashMap<>())
                            .build();
                    return shoppingCartRepository.save(newCart);
                });
    }

    private ShoppingCart validateAndGetCartWithProducts(String username, List<UUID> productIds) {
        ShoppingCart shoppingCart = getActiveCartByUsername(username);
        Map<UUID, Integer> products = shoppingCart.getProducts();

        if (products.isEmpty()) {
            throw new NoProductsInShoppingCartException("Корзина пуста");
        }

        if (productIds != null && !productIds.isEmpty()) {
            boolean hasAnyProduct = productIds.stream()
                    .anyMatch(products::containsKey);

            if (!hasAnyProduct) {
                throw new NoProductsInShoppingCartException(
                        "В корзине отсутствуют указанные товары: " + productIds
                );
            }
        }

        return shoppingCart;
    }
}
