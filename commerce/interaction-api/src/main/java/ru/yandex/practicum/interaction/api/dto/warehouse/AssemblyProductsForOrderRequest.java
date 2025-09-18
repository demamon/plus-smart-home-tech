package ru.yandex.practicum.interaction.api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductsForOrderRequest {
    @NotNull
    Map<UUID, Integer> products;

    @NotNull
    UUID orderId;
}
