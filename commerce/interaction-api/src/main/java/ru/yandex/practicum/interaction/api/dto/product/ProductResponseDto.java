package ru.yandex.practicum.interaction.api.dto.product;

import lombok.Data;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.ProductState;
import ru.yandex.practicum.interaction.api.enums.product.QuantityState;


import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductResponseDto {
    private UUID productId;

    private String productName;

    private String description;

    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;

    private ProductCategory productCategory;

    private BigDecimal price;

}
