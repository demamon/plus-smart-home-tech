package ru.yandex.practicum.interaction.api.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    UUID paymentId;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal totalPayment;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal  deliveryTotal;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal  feeTotal;
}
