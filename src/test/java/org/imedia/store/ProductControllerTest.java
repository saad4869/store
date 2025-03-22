package org.imedia.store;

import org.imedia.store.controller.ProductController;
import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductNotFoundException;
import org.imedia.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.TestConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

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
                new BigDecimal("19.99")
        );

        when(productService.getProductBySku(testSku)).thenReturn(productDto);

        // Act & Assert
        mockMvc.perform(get("/product/{sku}", testSku)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is(testSku)))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.price", is(19.99)));
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
}