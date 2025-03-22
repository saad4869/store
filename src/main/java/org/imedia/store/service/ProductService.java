package org.imedia.store.service;

import org.imedia.store.db.entity.Product;
import org.imedia.store.db.repository.ProductRepository;
import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Get product by SKU
     *
     * @param sku the product SKU
     * @return the product DTO
     * @throws ProductNotFoundException if product not found
     */

    public ProductDto getProductBySku(String sku) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        return convertToDto(product);
    }

    /**
     * Convert Product entity to ProductDto
     */
    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
