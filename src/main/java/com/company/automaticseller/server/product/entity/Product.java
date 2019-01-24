package com.company.automaticseller.server.product.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Products")
@Data
public class Product {

	@Id
	@Column(name = "Article_ID")
	private long articleId;

	@Column(name = "Name")
	private String name;

	@Column(name = "Description")
	private String description;

	@Column(name = "Price")
	private float price;

	@Column(name = "Storage_Quantity")
	private long quantity;

	private Product() {
	}

	private Product(long articleId, String name, String description, float price, long quantity) {
		this.articleId = articleId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

	public static class ProductBuilder {
		private long articleId;
		private String name;
		private String description;
		private float price;
		private long quantity = 1L;

		public ProductBuilder(long articleId, String name, float price) {
			this.articleId = articleId;
			this.name = name;
			this.price = price;
		}

		public ProductBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public ProductBuilder withQuantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public Product build() {
			return new Product(articleId, name, description, price, quantity);
		}
	}
}

