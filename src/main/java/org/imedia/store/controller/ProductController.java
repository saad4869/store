package org.imedia.store.controller;

import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductPatchDto;
import org.imedia.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Tag(name = "Product Management", description = "APIs for product management")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get product by SKU
     */
    @GetMapping("/product/{sku}")
    @Operation(summary = "Get product details by SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDto> getProductBySku(
            @Parameter(description = "Product SKU", required = true)
            @PathVariable String sku) {

        ProductDto productDto = productService.getProductBySku(sku);
        return ResponseEntity.ok(productDto);
    }

    /**
     * Get multiple products by SKUs
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProductsBySkus(@RequestParam("skus") String skusList) {
        // Split the comma-separated string into a list of SKUs
        List<String> skus = Arrays.stream(skusList.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<ProductDto> products = productService.getProductsBySkus(skus);
        return ResponseEntity.ok(products);
    }

    /**
     * Create Product Endpoint
     */
    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * partially update Product Endpoint
     */
    @PatchMapping("/product/{sku}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable String sku,
            @RequestBody ProductPatchDto productPatchDto) {

        ProductDto patchedProduct = productService.patchProduct(sku,productPatchDto);
        return ResponseEntity.ok(patchedProduct);
    }
}