package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {
    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addProducts(@RequestParam @NotBlank String username,
                                @RequestBody Map<UUID, Integer> request);

    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto getShoppingCart(@RequestParam @NotBlank String username);

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateShoppingCart(@RequestParam @NotBlank String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeProductsFromCart(@RequestParam @NotBlank String username,
                                           @RequestBody List<UUID> productId);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam @NotBlank String username,
                                          @RequestBody @Valid ChangeProductQuantityRequest request);

    @GetMapping("/api/v1/shopping-cart/all")
    List<ShoppingCartDto> getAllShoppingCartByName(@RequestParam @NotBlank String username);
}
