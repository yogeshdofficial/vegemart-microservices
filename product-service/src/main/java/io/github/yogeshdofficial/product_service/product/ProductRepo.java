package io.github.yogeshdofficial.product_service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo
    extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {}
