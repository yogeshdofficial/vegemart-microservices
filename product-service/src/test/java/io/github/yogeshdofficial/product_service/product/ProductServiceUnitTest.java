package io.github.yogeshdofficial.product_service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.github.yogeshdofficial.product_service.product.dtos.ProductDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductFilterDto;
import io.github.yogeshdofficial.product_service.product.exceptions.ProductNotFoundException;
import io.github.yogeshdofficial.product_service.shared.dtos.PagedResultDto;
import io.github.yogeshdofficial.product_service.shared.mappers.PagedResultMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {
    @Mock
    ProductRepo productRepo;

    @Spy
    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Spy
    ProductSpec productSpec = new ProductSpec();

    @Spy
    PagedResultMapper pagedResultMapper = Mappers.getMapper(PagedResultMapper.class);

    @InjectMocks
    ProductService productService;

    @Test
    public void should_find_product_by_id() {
        // Arrange
        long id = 1L;
        when(productRepo.findById(id))
                .thenReturn(Optional.of(ProductEntity.builder()
                        .name("Apple")
                        .description("A red fruit")
                        .price(Double.valueOf(100))
                        .unit("1 KG")
                        .build()));

        // Act
        ProductDto product = productService.getProductById(1L);

        // Assert
        assertThat(product.getName()).isEqualTo("Apple");
        assertThat(product.getDescription()).isEqualTo("A red fruit");
        assertThat(product.getPrice()).isEqualTo(Double.valueOf(100));
        assertThat(product.getUnit()).isEqualTo("1 KG");
    }

    @Test
    public void should_throw_if_product_not_found() {
        long id = 1L;
        when(productRepo.findById(id)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.getProductById(id))
                .withMessage("Product with id:%s not found".formatted(id));
    }

    @Test
    public void should_return_all_products() {
        // Arrange
        ProductEntity product1 = ProductEntity.builder()
                .id(1L)
                .name("Apple")
                .description("A red fruit")
                .price(Double.valueOf(100))
                .unit("1 kg")
                .build();
        ProductEntity product2 = ProductEntity.builder()
                .id(2L)
                .name("Banana")
                .description("A yellow fruit")
                .price(Double.valueOf(50))
                .unit("5 pc")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> pagedProductEntities =
                new PageImpl<ProductEntity>(List.of(product1, product2), pageable, 2);
        when(productRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(pagedProductEntities);

        // Act
        PagedResultDto<ProductDto> pagedResultProductEntities =
                productService.getAllProducts(ProductFilterDto.builder().build());

        // Assert
        assertThat(pagedResultProductEntities.getMisc().getTotalPages()).isEqualTo(1);
    }
}
