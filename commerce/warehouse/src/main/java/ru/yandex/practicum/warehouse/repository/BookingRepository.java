package ru.yandex.practicum.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.model.Booking;

import java.util.UUID;

public interface BookingRepository extends JpaRepository <Booking, UUID> {
}
