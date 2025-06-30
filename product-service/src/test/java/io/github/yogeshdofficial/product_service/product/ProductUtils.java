package io.github.yogeshdofficial.product_service.product;

public class ProductUtils {
  public static ProductEntity getApple() {
    return ProductEntity.builder()
        .name("Apple")
        .description("A red fruit")
        .price(100.0)
        .unit("1 kg")
        .inStock(true)
        .build();
  }

  public static ProductEntity getBanana() {
    return ProductEntity.builder()
        .name("Banana")
        .description("A yellow fruit")
        .price(50.0)
        .unit("10 pc")
        .inStock(true)
        .build();
  }
}
