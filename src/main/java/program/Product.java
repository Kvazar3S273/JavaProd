package program;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private String description;

    public Product() {
    }

    public Product(int id, String name, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
