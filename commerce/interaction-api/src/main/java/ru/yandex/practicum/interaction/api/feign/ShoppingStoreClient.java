package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.product.*;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.QuantityState;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {
    @PutMapping("/api/v1/shopping-store")
    ProductResponseDto createProduct(@Valid @RequestBody ProductCreateDto createDto);

    @PostMapping("/api/v1/shopping-store")
    ProductResponseDto updateProduct(@Valid @RequestBody ProductUpdateDto updateDto);

    @GetMapping("/api/v1/shopping-store")
    List<ProductResponseDto> getProductsByCategory(@RequestParam ProductCategory category, PageableDto pageable);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean removeProductById(@RequestBody UUID uuid);

    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean quantityStateUpdate(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductResponseDto getProductById(@PathVariable @NotNull UUID productId);
}
