package ru.yandex.practicum.shopping.cart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID shoppingCartId;

    @NotBlank
    String username;

    boolean active = true;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_items",
            joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;
}
