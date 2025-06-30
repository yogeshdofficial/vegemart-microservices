package io.github.yogeshdofficial.product_service.product;

import io.github.yogeshdofficial.product_service.product.dtos.ProductFilterDto;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {

    public static Specification<ProductEntity> hasName(String name) {
        if (name != null && !name.trim().isEmpty())
            return (root, query, cb) -> cb.like(cb.lower(root.get("name")), name);
        else return null;
    }

    public static Specification<ProductEntity> isInStock(Boolean inStock) {
        if (inStock != null) return (root, query, cb) -> cb.equal(root.get("inStock"), inStock);
        else return null;
    }

    public static Specification<ProductEntity> hasPriceLesserThan(Double price) {
        if (price != null) return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price);
        else return null;
    }

    public static Specification<ProductEntity> hasPriceGreaterThan(Double price) {
        if (price != null) return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), price);
        else return null;
    }

    public static Specification<ProductEntity> buildFilterSpec(ProductFilterDto productFilterDto) {
        Specification<ProductEntity> spec = (root, query, cb) -> null;
        return spec.and(hasName(productFilterDto.getName()))
                .and(hasPriceGreaterThan(productFilterDto.getMinPrice()))
                .and(hasPriceLesserThan(productFilterDto.getMaxPrice()))
                .and(isInStock(productFilterDto.getInStock()));
    }
}
