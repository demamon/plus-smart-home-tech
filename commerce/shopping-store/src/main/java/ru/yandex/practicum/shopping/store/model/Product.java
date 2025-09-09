package ru.yandex.practicum.shopping.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.ProductState;
import ru.yandex.practicum.interaction.api.enums.product.QuantityState;


import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    UUID productId;

    @NotBlank
    @Size(max = 255)
    String productName;

    @NotBlank
    @Lob // Соответствует типу TEXT в БД
    String description;

    @Size(max = 500)
    String imageSrc;

    @NotNull
    @Enumerated(EnumType.STRING)
    QuantityState quantityState;

    @NotNull
    @Enumerated(EnumType.STRING)
    ProductState productState;

    @NotNull
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal price;

}
