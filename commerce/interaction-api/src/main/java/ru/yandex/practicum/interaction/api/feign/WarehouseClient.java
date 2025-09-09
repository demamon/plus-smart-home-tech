package ru.yandex.practicum.interaction.api.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PutMapping("/api/v1/warehouse")
    void addProduct(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkCountProducts(@RequestBody @Valid ShoppingCartResponseDto shoppingCartResponseDto);

    @PostMapping("/api/v1/warehouse/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress();
}
