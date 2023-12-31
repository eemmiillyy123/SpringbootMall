package com.example.springbootmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	
//	@Override
//	public List<Product> getProducts(ProductCategory category,String search) {
//		return productDao.getProducts(category,search);
//	}
	
	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {
		// TODO Auto-generated method stub
		return productDao.countProduct(productQueryParams);
	}
	
	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		return productDao.getProducts(productQueryParams);
	}
	
	@Override
	public Product getProductById(Integer productId) {
		return productDao.getProductById(productId);
	}
	@Override
	public Integer createProduct(ProductRequest productRequest) {
		return productDao.createProduct(productRequest);
	}
	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {
		productDao.updateProduct(productId, productRequest);
		
	}
	@Override
	public void deleteProduct(Integer productId) {
		productDao.deleteProductById(productId);
		
	}

	
	
	

}
