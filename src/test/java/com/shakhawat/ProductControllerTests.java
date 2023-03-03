package com.shakhawat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shakhawat.controller.ProductController;
import com.shakhawat.model.Product;
import com.shakhawat.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateProduct() throws Exception {
		Product prod = new Product(1l, "Laptop", "18");

		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(prod))).andExpect(status().isCreated()).andDo(print());
	}

	@Test
	void shouldReturnProduct() throws Exception {
		long id = 1l;
		Product prod = new Product(id, "Mobile", "55");

		when(productService.findById(id)).thenReturn(Optional.of(prod));
		mockMvc.perform(get("/api/products/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.productName").value(prod.getProductName()))
				.andExpect(jsonPath("$.productQty").value(prod.getProductQty())).andDo(print());
	}

	@Test
	void shouldReturnNotFoundProduct() throws Exception {
		long id = 2l;

		when(productService.findById(id)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/products/{id}", id)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	void shouldReturnListOfProducts() throws Exception {
		List<Product> tutorials = new ArrayList<>(Arrays.asList(new Product(1l, "Spring Boot @WebMvcTest 1", "111"),
				new Product(2l, "Spring Boot @WebMvcTest 2", "222"),
				new Product(3l, "Spring Boot @WebMvcTest 3", "333")));

		when(productService.findAll()).thenReturn(tutorials);
		mockMvc.perform(get("/api/products")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(tutorials.size())).andDo(print());
	}

	@Test
	void shouldReturnListOfProductsWithFilter() throws Exception {
		List<Product> prodList = new ArrayList<>(Arrays.asList(
				new Product(1l, "Spring Boot Web Mvc Test", "Description 1"),
				new Product(3l, "Unit Test", "Description 3"), new Product(5l, "Boot Web MVC", "Description 5")));

		String productName = "Boot";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("productName", productName);

		when(productService.findByProductNameContaining(productName)).thenReturn(prodList);
		mockMvc.perform(get("/api/products/filter").params(paramsMap)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(prodList.size())).andDo(print());

		prodList = Collections.emptyList();

		when(productService.findByProductNameContaining(productName)).thenReturn(prodList);
		mockMvc.perform(get("/api/products/filter").params(paramsMap)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	void shouldReturnNoContentWhenFilter() throws Exception {
		String productName = "TVL";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("productName", productName);

		List<Product> prodList = Collections.emptyList();

		when(productService.findByProductNameContaining(productName)).thenReturn(prodList);
		mockMvc.perform(get("/api/products/filter").params(paramsMap)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	void shouldUpdateProduct() throws Exception {
		long id = 2l;

		Product prod = new Product(id, "Dell-Laptop", "98");
		Product updatedProduct = new Product(id, "DellLaptopInsprion", "99");

		when(productService.findById(id)).thenReturn(Optional.of(prod));
		when(productService.save(any(Product.class))).thenReturn(updatedProduct);
		
		System.out.println((objectMapper.writeValueAsString(updatedProduct))+" =============================================================");

		mockMvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedProduct))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedProduct.getId()))
				.andExpect(jsonPath("$.productName").value(updatedProduct.getProductName()))
				.andExpect(jsonPath("$.productQty").value(updatedProduct.getProductQty()))
				.andDo(print()).andReturn().getResponse().getContentType();
	}

	@Test
	void shouldReturnNotFoundUpdateProduct() throws Exception {
		
		long id = 1L;

		Product updatedProduct = new Product(id, "DellLaptopInsprion", "99");

		when(productService.findById(id)).thenReturn(Optional.empty());
		when(productService.save(any(Product.class))).thenReturn(updatedProduct);

		mockMvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedProduct))).andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	  void shouldDeleteProduct() throws Exception {
	    long id = 1L;

	    doNothing().when(productService).deleteById(id);
	    mockMvc.perform(delete("/api/products/{id}", id))
	         .andExpect(status().isNoContent())
	         .andDo(print());
	  }
	  
	  @Test
	  void shouldDeleteAllProducts() throws Exception {
	    doNothing().when(productService).deleteAll();
	    mockMvc.perform(delete("/api/products"))
	         .andExpect(status().isNoContent())
	         .andDo(print());
	  }

}
