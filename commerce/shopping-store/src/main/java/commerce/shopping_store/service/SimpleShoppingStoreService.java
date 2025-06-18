package commerce.shopping_store.service;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.interaction.dto.product.ProductState;
import commerce.interaction.exception.NotFoundException;
import commerce.shopping_store.mapper.ProductMapper;
import commerce.shopping_store.model.Product;
import commerce.shopping_store.repository.ShoppingStoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SimpleShoppingStoreService implements ShoppingStoreService {
    private final ShoppingStoreRepository repository;

    @Override
    public Page<ProductDto> findProductByCategory(ProductCategory productCategory, Integer page, Integer size, String sort) {
        log.info("Get products for category {}", productCategory);
        Pageable pageParams;
        if (null == sort) {
            pageParams = PageRequest.of(page, size);
        } else {
            pageParams = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        }
        List<Product> firstList = repository.findByProductCategory(productCategory, pageParams);
        var returnVar = firstList.parallelStream()
                .map(ProductMapper::mapProduct)
                .toList();
        return new PageImpl<>(returnVar, pageParams, returnVar.size());
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Save product {}", productDto.getProductName());

        var varForSave = ProductMapper.mapProductDto(productDto);
        var product = repository.save(varForSave);

        return ProductMapper.mapProduct(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Update product {}", productDto.getProductName());
        Product currentProduct = repository.findById(UUID.fromString(productDto.getProductId())).orElseThrow(
                () -> new NotFoundException("No product with id = " + productDto.getProductId())
        );
        var savedProduct = repository.save(ProductMapper.updateProduct(currentProduct, productDto));
        var returnValue = ProductMapper.mapProduct(savedProduct);
        return returnValue;
    }

    @Override
    @Transactional
    public ProductDto removeProduct(String productId) {
        log.info("Removing product with id {}", productId);
        String finalProductId = productId.replaceAll("\"", "");
        Product currentProduct = repository.findById(UUID.fromString(finalProductId)).orElseThrow(
                () -> new NotFoundException("No product with id = " + finalProductId)
        );
        // удаление не настоящее, выставляется лишь статус DEACTIVATE
        currentProduct.setProductState(ProductState.DEACTIVATE);
        var returnDto = ProductMapper.mapProduct(repository.save(currentProduct));
        return returnDto;
    }

    @Override
    @Transactional
    public ProductDto setQuantityState(ProductQuantityStateRequest request) {
        log.info("Change quantity {} for product with id = {}", request.getQuantityState(), request.getProductId());
        Product currentProduct = repository.findById(UUID.fromString(request.getProductId())).orElseThrow(
                () -> new NotFoundException("No product with id = " + request.getProductId())
        );
        currentProduct.setQuantityState(request.getQuantityState());
        var result = ProductMapper.mapProduct(repository.save(currentProduct));
        return result;
    }

    @Override
    public ProductDto findProductById(String productId) {
        log.info("Trying to get product with id {}", productId);
        var product = repository.findById(UUID.fromString(productId));
        if (product.isEmpty()) {
            throw new NotFoundException("No product with id = " + productId);
        }
        return ProductMapper.mapProduct(product.get());
    }
}
