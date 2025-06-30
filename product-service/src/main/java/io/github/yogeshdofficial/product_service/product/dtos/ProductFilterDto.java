package io.github.yogeshdofficial.product_service.product.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductFilterDto {
    @Builder.Default
    @Min(value = 0, message = "Page number must be non-negative") private int page = 0;

    @Builder.Default
    @Min(value = 1, message = "Page size must be at least 1") private int size = 10;

    @Size(max = 255, message = "Name filter must not exceed 255 characters") private String name;

    private Double minPrice;
    private Double maxPrice;
    private Boolean inStock;
}
