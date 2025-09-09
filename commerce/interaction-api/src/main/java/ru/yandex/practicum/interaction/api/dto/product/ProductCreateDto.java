package ru.yandex.practicum.interaction.api.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.ProductState;
import ru.yandex.practicum.interaction.api.enums.product.QuantityState;


import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreateDto {
    private UUID productId;

    @NotBlank(message = "Product name is mandatory")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    @NotBlank(message = "Product description is mandatory")
    private String description;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageSrc;

    @NotNull(message = "Quantity state is mandatory")
    private QuantityState quantityState;

    @NotNull(message = "Product state is mandatory")
    private ProductState productState;

    @NotNull(message = "Product category is mandatory")
    private ProductCategory productCategory;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
