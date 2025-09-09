package ru.yandex.practicum.interaction.api.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.ProductState;
import ru.yandex.practicum.interaction.api.enums.product.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductUpdateDto {

    @NotNull(message = "Product id state is mandatory")
    private UUID productId;

    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    private String description;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;

    private ProductCategory productCategory;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
