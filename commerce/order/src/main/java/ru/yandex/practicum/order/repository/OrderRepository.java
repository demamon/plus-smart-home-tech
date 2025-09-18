package ru.yandex.practicum.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository <Order, UUID> {
    Page<Order> findByShoppingCartIdIn(List<UUID> shoppingCartIds, Pageable pageable);
}
