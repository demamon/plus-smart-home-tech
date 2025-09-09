package ru.yandex.practicum.shopping.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.api.dto.product.ProductCreateDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductResponseDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductUpdateDto;
import ru.yandex.practicum.interaction.api.dto.product.QuantityStateUpdateDto;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.shopping.store.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@Valid @RequestBody ProductCreateDto createDto) {
        return productService.createProduct(createDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(@Valid @RequestBody ProductUpdateDto updateDto) {
        return productService.updateProduct(updateDto);
    }

    @GetMapping
    public Page<ProductResponseDto> getProductsByCategory(
            @RequestParam ProductCategory category,
            Pageable pageable) {
        return productService.getProductsByCategory(category, pageable);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProductById(@RequestBody UUID uuid) {
        return productService.removeProductById(uuid);
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public Boolean quantityStateUpdate(@Valid QuantityStateUpdateDto stateUpdateDto) {
        log.info("Проходим контроллер на изменение количества товара");
        return productService.quantityStateUpdate(stateUpdateDto);
    }

    @GetMapping("/{productId}")
    public ProductResponseDto getProductById(@PathVariable @NotNull UUID productId) {
        return productService.getProductById(productId);
    }

}
