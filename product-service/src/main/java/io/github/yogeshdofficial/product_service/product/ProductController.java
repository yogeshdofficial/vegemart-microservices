package io.github.yogeshdofficial.product_service.product;

import io.github.yogeshdofficial.product_service.product.dtos.ProductDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductFilterDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductNullableRequestDto;
import io.github.yogeshdofficial.product_service.product.dtos.ProductRequestDto;
import io.github.yogeshdofficial.product_service.shared.dtos.PagedResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieve a paginated list of products with optional filtering")
    public PagedResultDto<ProductDto> getAllProducts(
            @Parameter(description = "Filter criteria for products") @ModelAttribute @Valid final ProductFilterDto productFilterDto) {
        return productService.getAllProducts(productFilterDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    public ProductDto getProductById(@Parameter(description = "Product ID") @PathVariable final Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    public ProductDto createProduct(
            @Parameter(description = "Product details") @RequestBody @Valid final ProductRequestDto productRequestDto) {
        ProductDto newProduct = productService.createProduct(productRequestDto);
        return newProduct;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update product", description = "Update an existing product with new details")
    public ProductDto updateProduct(
            @Parameter(description = "Product ID") @PathVariable long id,
            @Parameter(description = "Updated product details") @RequestBody @Valid final ProductRequestDto productRequestDto) {
        var updatedProduct = productService.updateProduct(id, productRequestDto);
        return updatedProduct;
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Partially update product", description = "Update specific fields of an existing product")
    public ProductDto partialUpdateProduct(
            @Parameter(description = "Product ID") @PathVariable long id,
            @Parameter(description = "Partial product details") @RequestBody @Valid final ProductNullableRequestDto productRequestDto) {
        return productService.partialUpdateProduct(id, productRequestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Product deleted"),
                @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public void deleteProductById(@Parameter(description = "Product ID") @PathVariable long id) {
        productService.deleteProductById(id);
    }
}
