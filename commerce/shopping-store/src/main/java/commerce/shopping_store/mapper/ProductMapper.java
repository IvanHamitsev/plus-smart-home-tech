package commerce.shopping_store.mapper;

import commerce.interaction.dto.product.ProductDto;
import commerce.shopping_store.model.Product;

public class ProductMapper {
    public static Product mapProductDto(ProductDto dto) {
        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .imageSrc(dto.getImageSrc())
                .quantityState(dto.getQuantityState())
                .productState(dto.getProductState())
                .price(dto.getPrice())
                .build();
    }

    public static ProductDto mapProduct(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    public static Product updateProduct(Product old, ProductDto newProduct) {
        return Product.builder()
                .productId(old.getProductId())
                .productName(newProduct.getProductName() == null ? old.getProductName() : newProduct.getProductName())
                .description(newProduct.getDescription() == null ? old.getDescription() : newProduct.getDescription())
                .imageSrc(newProduct.getImageSrc()) // может быть пустым, если нужно удалить ссылку
                .quantityState(newProduct.getQuantityState() == null ? old.getQuantityState() : newProduct.getQuantityState())
                .productCategory(newProduct.getProductCategory() == null ? old.getProductCategory() : newProduct.getProductCategory())
                .price(newProduct.getPrice() == null ? old.getPrice() : newProduct.getPrice())
                .build();
    }
}
