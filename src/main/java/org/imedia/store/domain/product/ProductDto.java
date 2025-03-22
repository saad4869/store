package org.imedia.store.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;

}