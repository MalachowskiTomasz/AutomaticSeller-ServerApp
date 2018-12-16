package com.example.demo.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Products")
@NoArgsConstructor
@AllArgsConstructor
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
}

