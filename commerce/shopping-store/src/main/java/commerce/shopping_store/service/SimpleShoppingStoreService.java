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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SimpleShoppingStoreService implements ShoppingStoreService {
    private final ShoppingStoreRepository repository;

    @Override
    public List<ProductDto> findProductByCategory(ProductCategory productCategory) {
        log.info("Get products for category {}", productCategory);
        return repository.findByProductCategory(productCategory).parallelStream()
                .map(ProductMapper::mapProduct)
                .toList();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Save product {}", productDto.getProductName());
        var product = repository.save(ProductMapper.mapProductDto(productDto));
        return ProductMapper.mapProduct(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Update product {}", productDto.getProductName());
        Product currentProduct = repository.findById(productDto.getProductId()).orElseThrow(
                () -> new NotFoundException("No product with id = " + productDto.getProductId())
        );
        return ProductMapper.mapProduct(repository.save(ProductMapper.updateProduct(currentProduct, productDto)));
    }

    @Override
    public void removeProduct(String productId) {
        log.info("Removing product with id {}", productId);
        Product currentProduct = repository.findById(productId).orElseThrow(
                () -> new NotFoundException("No product with id = " + productId)
        );
        // удаление не настоящее, выставляется лишь статус
        currentProduct.setProductState(ProductState.DEACTIVATE);
        repository.save(currentProduct);
    }

    @Override
    public void setQuantityState(ProductQuantityStateRequest request) {
        log.info("Change quantity {} for product with id = {}", request.getQuantityState(), request.getProductId());
        Product currentProduct = repository.findById(request.getProductId()).orElseThrow(
                () -> new NotFoundException("No product with id = " + request.getProductId())
        );
        currentProduct.setQuantityState(request.getQuantityState());
        repository.save(currentProduct);
    }

    @Override
    public ProductDto findProductById(String productId) {
        log.info("Get product with id {}", productId);
        return ProductMapper.mapProduct(repository.findById(productId).orElseThrow(
                () -> new NotFoundException("No product with id = " + productId)
        ));
    }
}
