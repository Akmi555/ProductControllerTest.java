package de.ait_tr.shop.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;


@Schema(description = "DTO for Product")
public class ProductDTO {

    @Schema(description = "Product unique identifier", example = "777", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id; // null // long / 0

    @Schema(description = "Product title", example = "Banana")
    @NotNull(message = "Product title cannot be null")
    @NotBlank(message = "Product title cannot be empty")
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9 ]{2,}$", message = "Product title should be at least 3 characters long and contains only letters and spaces")
    private String title;

    @Schema(description = "Product price", example = "8.50")
    @DecimalMin(value = "1.0", message = "Product price should be greater or equal than 1")
    @DecimalMax(value = "100000.0", message = "Product price should be less or l than 100_000")
    private BigDecimal price; // null

    private String image;


    @Override
    public String toString() {
        return String.format("ProductDTO: id - %d, title - %s, price - %s",
                id, title, price);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO dto)) return false;
        return Objects.equals(id, dto.id) && Objects.equals(title, dto.title) && Objects.equals(price, dto.price) && Objects.equals(image, dto.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, image);
    }
}
