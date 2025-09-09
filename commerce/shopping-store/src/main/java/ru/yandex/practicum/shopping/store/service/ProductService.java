package ru.yandex.practicum.shopping.store.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.api.dto.product.ProductCreateDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductResponseDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductUpdateDto;
import ru.yandex.practicum.interaction.api.dto.product.QuantityStateUpdateDto;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;

import java.util.UUID;


public interface ProductService {
    ProductResponseDto createProduct(ProductCreateDto createDto);

    ProductResponseDto updateProduct(ProductUpdateDto updateDto);

    Page<ProductResponseDto> getProductsByCategory(ProductCategory productCategory, Pageable pageable);

    Boolean removeProductById(UUID uuid);

    Boolean quantityStateUpdate(QuantityStateUpdateDto stateUpdateDto);

    ProductResponseDto getProductById(UUID uuid);
}
