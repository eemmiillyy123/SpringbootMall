package com.example.springbootmall.dao;

import java.util.List;

import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;

public interface ProductDao {
	
	List<Product> getProducts(ProductCategory category,String search);
	
	Product getProductById(Integer productId);

	Integer createProduct(ProductRequest productRequest);

	void updateProduct(Integer productId, ProductRequest productRequest);

	void deleteProductById(Integer productId);

	

}
