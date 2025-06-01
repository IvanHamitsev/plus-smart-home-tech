package commerce.warehouse.model;

import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;

public class WarehouseMapper {
    public static ProductInWarehouse mapRequest(NewProductInWarehouseRequest request) {
        return ProductInWarehouse.builder()
                .id(request.getProductId())
                .fragile(request.getFragile())
                .weight(request.getWeight())
                .height(request.getDimension().getHeight())
                .width(request.getDimension().getWidth())
                .depth(request.getDimension().getDepth())
                .quantity(0)
                .build();
    }
}
