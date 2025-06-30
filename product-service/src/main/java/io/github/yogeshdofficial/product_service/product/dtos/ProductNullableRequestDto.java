package io.github.yogeshdofficial.product_service.product.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductNullableRequestDto {
  @Size(max = 255) @Pattern(regexp = "\\S.*", message = "Field must not be blank if present") private String name;

  @Pattern(regexp = "\\S.*", message = "Field must not be blank if present") private String description;

  @Size(max = 255) @Pattern(regexp = "\\S.*", message = "Field must not be blank if present") private String unit;

  private Double price;
  private Boolean inStock;
}
