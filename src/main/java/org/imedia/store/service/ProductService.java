package org.imedia.store.service;

import org.imedia.store.domain.product.ProductDto;
import org.imedia.store.domain.product.ProductNotFoundException;

import java.util.List;

public interface ProductService {

    /**
     * Get product by SKU
     *
     * @param sku the product SKU
     * @return the product DTO
     * @throws ProductNotFoundException if product not found
     */
    ProductDto getProductBySku(String sku);

    /**
     * Get multiple products by SKUs
     *
     * @param skus list of product SKUs
     * @return list of product DTOs
     */
    List<ProductDto> getProductsBySkus(List<String> skus);
}