package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartResponseDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkCountProducts(@RequestBody @Valid ShoppingCartResponseDto shoppingCartResponseDto) {
        return warehouseService.checkCountProducts(shoppingCartResponseDto);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto) {
        warehouseService.addProductToWarehouse(requestDto);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
