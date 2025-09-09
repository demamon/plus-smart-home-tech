package ru.yandex.practicum.interaction.api.dto.cart;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class ShoppingCartResponseDto {
    UUID shoppingCartId;
    Map<UUID, Integer> products;
}
