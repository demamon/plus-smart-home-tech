package ru.yandex.practicum.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.cart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PutMapping
    public ShoppingCartDto addProducts(@RequestParam @NotBlank String username,
                                       @RequestBody Map<UUID, Integer> request) {
        return shoppingCartService.addProducts(username, request);
    }

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotBlank String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam @NotBlank String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeProductsFromCart(@RequestParam @NotBlank String username,
                                                  @RequestBody List<UUID> productId) {
        return shoppingCartService.removeProductsFromCart(username, productId);
    }

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeProductQuantity(@RequestParam @NotBlank String username,
                                                 @RequestBody @Valid ChangeProductQuantityRequest request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }

    @GetMapping("/all")
    public List<ShoppingCartDto> getAllShoppingCartByName(@RequestParam @NotBlank String username) {
        return shoppingCartService.getAllShoppingCartByName(username);
    }

}
