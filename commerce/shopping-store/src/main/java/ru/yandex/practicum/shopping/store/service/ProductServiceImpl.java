package ru.yandex.practicum.shopping.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.product.ProductCreateDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductResponseDto;
import ru.yandex.practicum.interaction.api.dto.product.ProductUpdateDto;
import ru.yandex.practicum.interaction.api.dto.product.QuantityStateUpdateDto;
import ru.yandex.practicum.interaction.api.enums.product.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.product.ProductState;
import ru.yandex.practicum.shopping.store.exception.ProductNotFoundException;
import ru.yandex.practicum.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.shopping.store.model.Product;
import ru.yandex.practicum.shopping.store.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductCreateDto createDto) {
        Product product = productMapper.toProduct(createDto);
        Product saveProduct = productRepository.save(product);
        return productMapper.toResponseDto(saveProduct);
    }

    @Override
    public ProductResponseDto updateProduct(ProductUpdateDto updateDto) {
        Product product = findByIdProduct(updateDto.getProductId());
        productMapper.updateEntityFromDto(updateDto, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponseDto(updatedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getProductsByCategory(ProductCategory productCategory, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByProductCategory(productCategory, pageable);
        return productPage.map(productMapper::toResponseDto);
    }

    @Override
    public Boolean removeProductById(UUID uuid) {
        Product product = findByIdProduct(uuid);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean quantityStateUpdate(QuantityStateUpdateDto stateUpdateDto) {
        log.info("заходим в сервис по обновлению количества");
        log.info("ид продукта, {} , количество {} ", stateUpdateDto.getProductId(), stateUpdateDto.getQuantityState());
        Product product = findByIdProduct(stateUpdateDto.getProductId());
        if (!product.getQuantityState().equals(stateUpdateDto.getQuantityState())) {
            product.setQuantityState(stateUpdateDto.getQuantityState());
            productRepository.save(product);
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponseDto getProductById(UUID uuid) {
        Product product = findByIdProduct(uuid);
        return productMapper.toResponseDto(product);
    }

    private Product findByIdProduct(UUID uuid) {
        log.info("проверяем существование товара");
        return productRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}
