package ru.yandex.practicum.interaction.api.dto.warehouse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NonNull
    UUID productId;
    @NonNull
    Boolean fragile;
    DimensionDto dimension;
    double weight;
}
