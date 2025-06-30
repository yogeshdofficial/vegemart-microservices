package io.github.yogeshdofficial.product_service.product;

import io.github.yogeshdofficial.product_service.product.dtos.ProductDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductDto toDto(ProductEntity productEntity);

    ProductEntity toEntity(ProductDto productDto);

    ProductEntity toEntity(ProductRequestDto productRequestDto);
}
