package ru.yandex.practicum.interaction.api.dto.warehouse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    @NonNull
    Double deliveryWeight;
    @NonNull
    Double deliveryVolume;
    @NonNull
    Boolean fragile;
}
