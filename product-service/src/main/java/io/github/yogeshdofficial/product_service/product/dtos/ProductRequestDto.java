package io.github.yogeshdofficial.product_service.product.dtos;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {
  @NotBlank @Size(max = 255) private String name;

  @Pattern(regexp = "\\S.*", message = "Field must not be blank if present") private String description;

  @Size(max = 255) @NotBlank private String unit;

  @NotNull private Double price;

  @NonNull private Boolean inStock;
}
