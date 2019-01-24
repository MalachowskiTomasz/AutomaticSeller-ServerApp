package com.company.automaticseller.server.product.controller;

import com.company.automaticseller.server.product.repository.ProductRepository;
import com.company.automaticseller.server.product.entity.Product;
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

	/**
	 * Retrieves product information from database
	 *
	 * @param articleId Article ID (barcode on product)
	 * @return Product information in JSON format if has been found in database. Otherwise empty string
	 */
	@RequestMapping(value = "/find", method = GET)
	public Product findProduct(@RequestParam long articleId) {
		return productRepository.findByArticleId(articleId);
	}

	/**
	 * Retrieves all products' information from database
	 *
	 * @return All products' information in database in JSON format
	 */
	@RequestMapping(value = "/getAll", method = GET)
	public List<Product> findAllProduct() {
		return productRepository.findAll();
	}

	/**
	 * Adds product information to database
	 *
	 * @param articleId       Article ID (barcode on product)
	 * @param name            Name of product
	 * @param description     Description of product (not required)
	 * @param price           Price of product
	 * @param storageQuantity Quantity in storage (default = 1)
	 * @return Information "Product has been added"
	 */
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

	/**
	 * Deletes product information from database
	 *
	 * @param articleId Article ID (barcode on product)
	 * @return If product with given article ID exists in database information "Product has been deleted". Otherwise
	 * "Product has not been found in database"
	 */
	@RequestMapping(value = "/delete", method = DELETE)
	public ResponseEntity<String> deleteProduct(@RequestParam long articleId) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) return ResponseEntity.ok("Product has not been found in database");

		productRepository.delete(p);
		return ResponseEntity.ok("Product has been deleted");
	}

	/**
	 * Updates storage quantity of product
	 *
	 * @param articleId       Article ID of product (barcode on product)
	 * @param storageQuantity Quantity
	 * @return If product with given article ID exists in database information "Quantity of product has been modified".
	 * Otherwise "Product has not been found in database"
	 */
	@RequestMapping(value = "updateQuantity", method = PATCH)
	public ResponseEntity<String> updateQuantityOfProductInStorage(@RequestParam long articleId,
																   @RequestParam long storageQuantity) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) return ResponseEntity.ok("Product has not been found in database");

		p.setQuantity(storageQuantity);
		productRepository.save(p);
		return ResponseEntity.ok("Quantity of product has been modified");
	}

	/**
	 * Updates price of product
	 *
	 * @param articleId Article ID of product (barcode on product)
	 * @param price     New price of product
	 * @return If product with given article ID exists information "Price of product has been modified". Otherwise
	 * "Product has not been found in database"
	 */
	@RequestMapping(value = "updatePrice", method = PATCH)
	public ResponseEntity<String> updatePriceOfProduct(@RequestParam long articleId,
													   @RequestParam float price) {
		val p = productRepository.findByArticleId(articleId);
		if (p == null) return ResponseEntity.ok("Product has not been found in database");

		p.setPrice(price);
		productRepository.save(p);
		return ResponseEntity.ok("Price of product has been modified");
	}
}
