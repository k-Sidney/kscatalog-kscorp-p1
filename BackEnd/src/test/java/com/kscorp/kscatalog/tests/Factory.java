package com.kscorp.kscatalog.tests;

import java.time.Instant;

import com.kscorp.kscatalog.dto.ProductDTO;
import com.kscorp.kscatalog.entities.Category;
import com.kscorp.kscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone","Good Phone", 1200.0, "https://img.com/img.png",Instant.parse("2023-10-20T03:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(1L, "Eletronics");
	}
}
