package io.github.yogeshdofficial.product_service.product.dtos;

import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

  @NonNull private Long id;

  @NonNull @NotBlank @Size(max = 255) private String name;

  private Double price;

  @NonNull @NotBlank @Size(max = 255) private String unit;

  private String description;

  @Nonnull private Boolean inStock;
}
