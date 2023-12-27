package com.example.springbootmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.ProductService;

@Component
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductDao productDao;
	@Override
	public Product getProductById(Integer productId) {
		// TODO Auto-generated method stub
		return productDao.getProductById(productId);
	}

}
