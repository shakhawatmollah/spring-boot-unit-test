package com.shakhawat.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shakhawat.model.Product;
import com.shakhawat.repository.ProductRepository;
import com.shakhawat.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> findByProductNameContaining(String productName) {
		return productRepository.findByProductNameContainingIgnoreCase(productName);
	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}

}
