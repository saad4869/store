package org.imedia.store.controller;

import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



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
}