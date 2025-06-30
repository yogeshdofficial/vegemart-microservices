package io.github.yogeshdofficial.product_service.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductControllerIntegrationTest {
  @LocalServerPort
  private int port;

  @Autowired
  private ProductRepo productRepo;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost:" + port + "/api/v1";
    JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(NumberReturnType.DOUBLE);

    RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    productRepo.deleteAll();
  }

  @Test
  public void should_get_product_by_id() {
    ProductEntity product = productRepo.save(ProductUtils.getApple());

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/products/{id}", product.getId())
        .then()
        .statusCode(200)
        .body("name", is(product.getName()))
        .body("description", is(product.getDescription()))
        .body("price", is(product.getPrice()))
        .body("unit", is(product.getUnit()))
        .body("inStock", is(true));
  }

  @Test
  public void should_return_404_product_not_found_by_id() {
    int id = 1;
    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/products/{id}", id)
        .then()
        .statusCode(404)
        .body("errors", is(List.of("Product with id:%s not found".formatted(id))));
  }

  @Test
  public void should_get_all_products() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data", hasSize(2))
        .body("misc.pageNumber", is(0))
        .body("misc.totalElements", is(2))
        .body("misc.totalPages", is(1))
        .body("misc.first", is(true))
        .body("misc.last", is(true))
        .body("misc.hasNext", is(false))
        .body("misc.hasPrevious", is(false));
  }

  @Test
  public void should_get_all_products_with_paging() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .queryParam("page", 0)
        .queryParam("size", 1)
        .contentType(ContentType.JSON)
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data", hasSize(1))
        .body("misc.pageNumber", is(0))
        .body("misc.totalElements", is(2))
        .body("misc.totalPages", is(2))
        .body("misc.first", is(true))
        .body("misc.last", is(false))
        .body("misc.hasNext", is(true))
        .body("misc.hasPrevious", is(false));
  }

  @Test
  public void should_get_all_products_with_filtering_by_name() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("name", "apple")
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data[0].name", is(product1.getName()));
  }

  @Test
  public void should_get_all_products_with_filtering_by_minprice() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("minPrice", "60")
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data[0].name", is(product1.getName()))
        .body("data", hasSize(1));
  }

  @Test
  public void should_get_all_products_with_filtering_by_maxprice() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("maxPrice", "50")
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data[0].name", is(product2.getName()))
        .body("data", hasSize(1));
  }

  @Test
  public void should_get_all_products_with_filtering_by_inStock() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("inStock", false)
        .when()
        .get("/products")
        .then()
        .statusCode(200)
        .body("data", hasSize(0));
  }

  @Test
  public void should_return_400_invalid_filter_size() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("size", "0")
        .when()
        .get("/products")
        .then()
        .statusCode(400);
  }

  @Test
  public void should_return_400_invalid_filter_page() {
    ProductEntity product1 = productRepo.save(ProductUtils.getApple());
    ProductEntity product2 = productRepo.save(ProductUtils.getBanana());
    productRepo.saveAll(List.of(product1, product2));

    given()
        .contentType(ContentType.JSON)
        .queryParam("page", "-1")
        .when()
        .get("/products")
        .then()
        .statusCode(400);
  }

  @Test
  public void should_create_product() {
    String requestBody = """
        {
          "name":"Apple",
          "price":120,
          "unit":"1 kg",
          "description":"A red fruit",
          "inStock":true
        }
        """;
    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/products")
        .then()
        .statusCode(201)
        .body("name", is("Apple"));
  }

  @Test
  public void should_return_404_invalid_body() {
    String requestBody = """
        {
          "name":"",
          "unit":"1 kg",
          "description":"A red fruit"
        }
        """;
    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/products")
        .then()
        .statusCode(400)
        .body("errors", hasSize(2));
  }

  @Test
  public void should_update_products() {
    ProductEntity product = productRepo.save(ProductUtils.getApple());

    String requestBody = """
        {
        "name":"Banana"
        }
        """;

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .patch("/products/{id}", product.getId())
        .then()
        .statusCode(200)
        .body("name", is("Banana"));
  }

  @Test
  public void should_delete_product() {
    ProductEntity product = productRepo.save(ProductUtils.getApple());

    given()
        .contentType(ContentType.JSON)
        .when()
        .delete("/products/{id}", product.getId())
        .then()
        .statusCode(204);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/products/{id}", product.getId())
        .then()
        .statusCode(404);
  }

  @Test
  public void should_return_404_product_not_found_by_id_delete() {
    int id = 1;
    given()
        .contentType(ContentType.JSON)
        .when()
        .delete("/products/{id}", id)
        .then()
        .statusCode(404)
        .body("errors", is(List.of("Product with id:%s not found".formatted(id))));
  }
}
