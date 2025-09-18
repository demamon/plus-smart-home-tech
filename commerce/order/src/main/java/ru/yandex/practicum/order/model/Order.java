package ru.yandex.practicum.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    UUID orderId;

    UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;

    UUID paymentId;

    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    OrderState state;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal totalPrice;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal deliveryPrice;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal productPrice;
}
