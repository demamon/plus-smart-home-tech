package ru.yandex.practicum.shopping.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.interaction.api.dto.product.ProductCreateDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductResponseDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductUpdateDto;
import ru.yandex.practicum.shopping.store.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toProduct(ProductCreateDto dto);

    ProductResponseDto toResponseDto(Product product);

    void updateEntityFromDto(ProductUpdateDto dto, @MappingTarget Product product);
}
