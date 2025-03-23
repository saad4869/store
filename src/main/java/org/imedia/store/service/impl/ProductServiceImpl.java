package org.imedia.store.service.impl;

import org.imedia.store.db.entity.Product;
import org.imedia.store.db.repository.ProductRepository;
import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductNotFoundException;
import org.imedia.store.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
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

    @Override
    public List<ProductDto> getProductsBySkus(List<String> skus) {
        List<Product> products = productRepository.findBySkuIn(skus);

        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
