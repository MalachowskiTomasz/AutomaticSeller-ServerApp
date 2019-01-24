package com.company.automaticseller.server.product.repository;

import com.company.automaticseller.server.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByArticleId(long articleId);
}
