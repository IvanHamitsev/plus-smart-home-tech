package commerce.interaction.dto.product;

public enum QuantityState {
    ENDED,
    FEW,
    ENOUGH,
    MANY;

    public static QuantityState getFromInteger(Integer quantity) {
        if (quantity <= 0) return ENDED;
        if (quantity < 10) return FEW;
        if (quantity <= 100) return ENOUGH;
        return MANY;
    }
}
