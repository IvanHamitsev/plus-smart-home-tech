package commerce.warehouse.mapper;

import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.warehouse.model.ProductInWarehouse;

import java.util.UUID;

public class WarehouseMapper {
    public static ProductInWarehouse mapRequest(NewProductInWarehouseRequest request) {
        return ProductInWarehouse.builder()
                .productId(UUID.fromString(request.getProductId()))
                .fragile(request.getFragile())
                .weight(request.getWeight())
                .height(request.getDimension().getHeight())
                .width(request.getDimension().getWidth())
                .depth(request.getDimension().getDepth())
                .quantity(0)
                .build();
    }

    public static ProductDto mapProductInWarehouseToProductDto(ProductInWarehouse inp) {
        return ProductDto.builder()
                .productId(inp.getProductId().toString())
                .build();
    }
}
