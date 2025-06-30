package io.github.yogeshdofficial.product_service.product;

import io.github.yogeshdofficial.product_service.product.dtos.ProductDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductFilterDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductNullableRequestDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductRequestDto;
import io.github.yogeshdofficial.product_service.product.exceptions.ProductNotFoundException;
import io.github.yogeshdofficial.product_service.shared.dtos.PagedResultDto;
import io.github.yogeshdofficial.product_service.shared.mappers.PagedResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepo productRepo;
  private final ProductMapper productMapper;
  private final PagedResultMapper pagedResultMapper;

  public ProductDto getProductById(Long id) {
    ProductEntity product =
        productRepo.findById(id).orElseThrow(() -> ProductNotFoundException.forId(id));
    return productMapper.toDto(product);
  }

  public PagedResultDto<ProductDto> getAllProducts(ProductFilterDto productFilterDto) {
    Pageable pageable = PageRequest.of(productFilterDto.getPage(), productFilterDto.getSize());
    Specification<ProductEntity> spec = ProductSpec.buildFilterSpec(productFilterDto);

    Page<ProductEntity> productsEntity = productRepo.findAll(spec, pageable);
    Page<ProductDto> productsDto = productsEntity.map(productMapper::toDto);
    return pagedResultMapper.toPagedResultDto(productsDto);
  }

  public ProductDto createProduct(ProductRequestDto productPostRequestDto) {
    ProductEntity mappedProductEntity = productMapper.toEntity(productPostRequestDto);
    ProductEntity createdProductEntity = productRepo.save(mappedProductEntity);
    log.info("Product created with id: {}", createdProductEntity.getId());
    return productMapper.toDto(createdProductEntity);
  }

  public ProductDto updateProduct(long id, ProductRequestDto productRequestDto) {
    ProductEntity productEnity =
        productRepo.findById(id).orElseThrow(() -> ProductNotFoundException.forId(id));

    productEnity.setName(productRequestDto.getName());
    productEnity.setUnit(productRequestDto.getUnit());
    productEnity.setDescription(productRequestDto.getDescription());
    productEnity.setPrice(productRequestDto.getPrice());

    productRepo.save(productEnity);
    log.info("Product updated with id: {}", productEnity.getId());
    return productMapper.toDto(productEnity);
  }

  public void deleteProductById(long id) {
    productRepo.findById(id).orElseThrow(() -> ProductNotFoundException.forId(id));
    productRepo.deleteById(id);
    log.info("Product deleted with id: {}", id);
  }

  public ProductDto partialUpdateProduct(long id, ProductNullableRequestDto productRequestDto) {
    ProductEntity productEntity =
        productRepo.findById(id).orElseThrow(() -> ProductNotFoundException.forId(id));
    if (productRequestDto.getName() != null) productEntity.setName(productRequestDto.getName());
    if (productRequestDto.getPrice() != null) productEntity.setPrice(productRequestDto.getPrice());
    if (productRequestDto.getDescription() != null)
      productEntity.setDescription(productRequestDto.getDescription());
    if (productRequestDto.getUnit() != null) productEntity.setUnit(productRequestDto.getUnit());

    ProductEntity updatedProduct = productRepo.save(productEntity);
    log.info("Product updated partially with id:{}", updatedProduct.getId());
    return productMapper.toDto(updatedProduct);
  }
}
