package commerce.interaction.dto.product;

public enum ProductCategory {
    LIGHTING("LIGHTING"),
    CONTROL("CONTROL"),
    SENSORS("SENSORS");

    private final String title;

    ProductCategory(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static ProductCategory fromString(String value) {
        if (value.equals("LIGHTING")) return LIGHTING;
        if (value.equals("CONTROL")) return CONTROL;
        if (value.equals("SENSORS")) return SENSORS;
        return LIGHTING;
    }
}
