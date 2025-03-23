package org.imedia.store.db.repository;

import org.imedia.store.db.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    /**
     * Find products by SKUs
     *
     * @param skus list of SKUs to find
     * @return list of products matching the provided SKUs
     */
    List<Product> findBySkuIn(List<String> skus);
}