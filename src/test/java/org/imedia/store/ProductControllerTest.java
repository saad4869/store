package org.imedia.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.imedia.store.controller.ProductController;
import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductNotFoundException;
import org.imedia.store.domain.product.ProductPatchDto;
import org.imedia.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.TestConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;


    static class TestConfig {
        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }

    @Test
    public void testGetProductBySku_Success() throws Exception {
        // Arrange
        String testSku = "Test SKU";
        ProductDto productDto = new ProductDto(
                testSku,
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        when(productService.getProductBySku(testSku)).thenReturn(productDto);

        // Act & Assert
        mockMvc.perform(get("/product/{sku}", testSku)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is(testSku)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.price", is(19.99)))
                .andExpect(jsonPath("$.stockQuantity", is(100)));
    }

    @Test
    public void testGetProductBySku_NotFound() throws Exception {
        // Arrange
        String nonExistentSku = "NONEXISTENT";

        when(productService.getProductBySku(nonExistentSku))
                .thenThrow(new ProductNotFoundException(nonExistentSku));

        // Act & Assert
        mockMvc.perform(get("/product/{sku}", nonExistentSku)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductsBySkus_Success() throws Exception {
        // Arrange
        List<String> testSkus = Arrays.asList("123", "4567", "8901");

        ProductDto product1 = new ProductDto("123", "Product 1", "Description 1", new BigDecimal("19.99"),100);
        ProductDto product2 = new ProductDto("4567", "Product 2", "Description 2", new BigDecimal("29.99"),200);
        ProductDto product3 = new ProductDto("8901", "Product 3", "Description 3", new BigDecimal("39.99"),300);

        List<ProductDto> products = Arrays.asList(product1, product2, product3);

        when(productService.getProductsBySkus(anyList())).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/products")
                        .param("skus", "123,4567,8901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].sku", is("123")))
                .andExpect(jsonPath("$[1].sku", is("4567")))
                .andExpect(jsonPath("$[2].sku", is("8901")));
    }

    @Test
    public void testCreateProduct_Success() throws Exception {
        // Arrange
        ProductDto inputDto = new ProductDto(
                "NEW123",
                "New Product",
                "New Description",
                new BigDecimal("24.99"),
                50
        );

        ProductDto savedDto = new ProductDto(
                "NEW123",
                "New Product",
                "New Description",
                new BigDecimal("24.99"),
                50
        );

        when(productService.createProduct(any(ProductDto.class))).thenReturn(savedDto);

        // Act & Assert
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku", is("NEW123")))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.price", is(24.99)))
                .andExpect(jsonPath("$.stockQuantity", is(50)));
    }

    @Test
    public void testPatchedProduct_Success() throws Exception {
        // Arrange
        String testSku = "Test SKU";
        ProductPatchDto patchDto = new ProductPatchDto(
                "Updated Product",
                "updated Description",
                new BigDecimal("99.99")
        );

        ProductDto patchedDto = new ProductDto(
                testSku,
                "Updated Product",
                "updated Description",
                new BigDecimal("99.99"),
                50
        );


        when(productService.patchProduct(eq(testSku),any(ProductPatchDto.class))).thenReturn(patchedDto);

        // Act & Assert
        mockMvc.perform(patch("/product/{sku}", testSku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is(testSku)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.description", is("updated Description")))
                .andExpect(jsonPath("$.price", is(99.99)))
                .andExpect(jsonPath("$.stockQuantity", is(50)));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        // Arrange
        String nonExistentSku = "NONEXISTENT";
        ProductPatchDto updateDto = new ProductPatchDto(
                "Updated Name",
                "Updated Description",
                new BigDecimal("49.99")
        );

        when(productService.patchProduct(eq(nonExistentSku), any(ProductPatchDto.class)))
                .thenThrow(new ProductNotFoundException(nonExistentSku));

        // Act & Assert
        mockMvc.perform(patch("/product/{sku}", nonExistentSku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }
}