package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClinet {
    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartResponseDto addProducts(@RequestParam @NotBlank String username,
                                        @RequestBody Map<UUID, Integer> request);

    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartResponseDto getShoppingCart(@RequestParam @NotBlank String username);

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateShoppingCart(@RequestParam @NotBlank String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartResponseDto removeProductsFromCart(@RequestParam @NotBlank String username,
                                                   @RequestBody List<UUID> productId);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartResponseDto changeProductQuantity(@RequestParam @NotBlank String username,
                                                  @RequestBody @Valid ChangeProductQuantityRequest request);
}
