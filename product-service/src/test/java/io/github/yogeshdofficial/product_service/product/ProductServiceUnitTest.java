package io.github.yogeshdofficial.product_service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import io.github.yogeshdofficial.product_service.product.dtos.ProductDto;
import io.github.yogeshdofficial.product_service.product.exceptions.ProductNotFoundException;
import io.github.yogeshdofficial.product_service.shared.mappers.PagedResultMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {
  @Mock ProductRepo productRepo;

  @Spy ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

  @Spy ProductSpec productSpec = new ProductSpec();

  @Spy PagedResultMapper pagedResultMapper = Mappers.getMapper(PagedResultMapper.class);

  @InjectMocks ProductService productService;

  @Test
  public void should_find_product_by_id() {
    // Arrange
    ProductEntity productEnity = ProductUtils.getApple();
    long id = 1L;
    when(productRepo.findById(id)).thenReturn(Optional.of(productEnity));

    // Act
    ProductDto productDto = productService.getProductById(1L);

    // Assert
    assertThat(productDto.getName()).isEqualTo(productEnity.getName());
    assertThat(productDto.getDescription()).isEqualTo(productEnity.getDescription());
    assertThat(productDto.getPrice()).isEqualTo(productDto.getPrice());
    assertThat(productDto.getUnit()).isEqualTo(productDto.getUnit());
    assertThat(productDto.getInStock()).isEqualTo(productDto.getInStock());
  }

  @Test
  public void should_throw_if_product_not_found() {
    long id = 1L;
    when(productRepo.findById(id)).thenReturn(Optional.empty());

    assertThatExceptionOfType(ProductNotFoundException.class)
        .isThrownBy(() -> productService.getProductById(id))
        .withMessage("Product with id:%s not found".formatted(id));
  }
}
