package com.example.demo.product.controller;

import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/product")
public class ProductController {

	private final ProductRepository productRepository;

	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@RequestMapping(value = "/find", method = GET)
	public Product findProduct(@RequestParam long articleId) {
		return productRepository.findByArticleId(articleId);
	}

	@RequestMapping(value = "/findAll", method = GET)
	public List<Product> findAllProduct() {
		return productRepository.findAll();
	}

	@RequestMapping(value = "/add", method = POST)
	public ResponseEntity<String> addProduct(@RequestParam long articleId,
											 @RequestParam String name,
											 @RequestParam(required = false) String description,
											 @RequestParam float price,
											 @RequestParam(defaultValue = "1") long storageQuantity) {
		val p = new Product.ProductBuilder(articleId, name, price)
				.withDescription(description)
				.withQuantity(storageQuantity)
				.build();
		productRepository.save(p);
		return ResponseEntity.ok().body("Product has been added");
	}

	@RequestMapping(value = "/delete", method = DELETE)
	public ResponseEntity<String> deleteProduct(@RequestParam long articleId) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) {
			return ResponseEntity.notFound().build();
		}
		productRepository.delete(p);
		return ResponseEntity.ok("Product has been deleted");
	}

	@RequestMapping(value = "updateQuantity", method = PATCH)
	public ResponseEntity<String> updateQuantityOfProductInStorage(@RequestParam long articleId,
																   @RequestParam long storageQuantity) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) {
			return ResponseEntity.notFound().build();
		}

		p.setQuantity(storageQuantity);
		productRepository.save(p);
		return ResponseEntity.ok("Quantity of product has been modified");
	}

	@RequestMapping(value = "updatePrice", method = PATCH)
	public ResponseEntity<String> updatePriceOfProduct(@RequestParam long articleId,
													   @RequestParam float price) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null)
			return ResponseEntity.notFound().build();

		p.setPrice(price);
		productRepository.save(p);
		return ResponseEntity.ok("Price of product has been modified");
	}
}
