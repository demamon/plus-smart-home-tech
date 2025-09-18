package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.payment.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    UUID paymentId;

    UUID orderId;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal totalPayment;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal deliveryTotal;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal feeTotal;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal productTotal;

    @Enumerated(EnumType.STRING)
    PaymentState paymentState;
}
