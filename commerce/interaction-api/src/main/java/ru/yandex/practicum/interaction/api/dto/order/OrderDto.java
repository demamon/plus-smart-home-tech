package ru.yandex.practicum.interaction.api.dto.order;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID orderId;

    UUID shoppingCartId;

    Map<UUID, Integer> products;

    UUID paymentId;

    UUID deliveryId;

    OrderState state;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal totalPrice;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal  deliveryPrice;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal  productPrice;
}
