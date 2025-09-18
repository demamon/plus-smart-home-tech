package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Positive
    double deliveryWeight;
    @Positive
    double deliveryVolume;
    boolean fragile;

    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns =  @JoinColumn(name = "shopping_cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;

    @NotNull
    UUID orderId;
    UUID deliveryId;
}
