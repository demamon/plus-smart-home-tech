package ru.yandex.practicum.interaction.api.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import ru.yandex.practicum.interaction.api.enums.product.QuantityState;

import java.util.UUID;

@Data
public class QuantityStateUpdateDto {
    @NotNull
    private UUID productId;
    @NotNull
    private QuantityState quantityState;
}
