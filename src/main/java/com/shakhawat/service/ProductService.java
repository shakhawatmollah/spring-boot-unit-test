package com.shakhawat.service;

import java.util.List;
import java.util.Optional;

import com.shakhawat.model.Product;

public interface ProductService {
	
	Product save(Product product);
	
	List<Product> findAll();
	
	void deleteById(Long id);
	
	Optional<Product> findById(Long id);
	
	List<Product> findByProductNameContaining(String productName);

	void deleteAll();

}
