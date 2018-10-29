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
	public ResponseEntity addProduct(@RequestParam long articleId,
									 @RequestParam String name,
									 @RequestParam String description,
									 @RequestParam float price,
									 @RequestParam(defaultValue = "1") long quantity) {
		val p = new Product(articleId, name, description, price, quantity);
		productRepository.save(p);
		return ResponseEntity.ok().body("Product has been added");
	}

	@RequestMapping(value = "/delete", method = DELETE)
	public ResponseEntity deleteProduct(@RequestParam long articleId) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) {
			return ResponseEntity.notFound().build();
		}
		productRepository.delete(p);
		return ResponseEntity.ok("Product has been deleted");
	}

	@RequestMapping(value = "updateQuantity", method = PATCH)
	public ResponseEntity updateQuantityOfProduct(@RequestParam long articleId,
												  @RequestParam long quantity) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) {
			return ResponseEntity.notFound().build();
		}
		p.setQuantity(quantity);
		productRepository.save(p);
		return ResponseEntity.ok("Quantity of product has been modified");
	}

	@RequestMapping(value = "updatePrice", method = PATCH)
	public ResponseEntity updatePriceOfProduct(@RequestParam long articleId,
											   @RequestParam float price) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) {
			return ResponseEntity.notFound().build();
		}
		p.setPrice(price);
		productRepository.save(p);
		return ResponseEntity.ok("Price of product has been modified");
	}
}