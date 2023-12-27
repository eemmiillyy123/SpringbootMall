package com.example.springbootmall.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.rowMapper.ProductRowMapper;

@Component
public class ProductDaoImpl implements ProductDao{

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Override
	public Product getProductById(Integer productId) {
		String sql="select product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date from product where product_id=:productId";
		Map<String,Object> map=new HashMap<>();
		map.put("productId", productId);
		List <Product> list=namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
		if(list.size()>0) {
			return list.get(0);
		}
		else {
			return null;
		}
	}
	@Override
	public Integer createProduct(ProductRequest productRequest) {
		System.out.println(productRequest.getCategory());
		String sql="insert into product(product_name, category, image_url, price, stock, description, created_date, last_modified_date)"+
				"values(:productName,:category,:imageUrl,:price,:stock,:description,:createdDate,:lastModifiedDate)";
		Map<String, Object> map=new HashMap<>();
		map.put("productName", productRequest.getProductName());
		map.put("category", productRequest.getCategory().name());
		map.put("imageUrl", productRequest.getImageUrl());
		map.put("price", productRequest.getPrice());
		map.put("stock", productRequest.getStock());
		map.put("description", productRequest.getDescription());
		
		Date now=new Date();
		map.put("createdDate", now);
		map.put("lastModifiedDate", now);
		
		KeyHolder keyHolder=new GeneratedKeyHolder();//儲存資料庫自動生成的productId
		
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
		
		int productId=keyHolder.getKey().intValue();

		return productId;
	}
	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {
		String sql="update product set product_name=:productName,category=:category,image_url=:imageUrl,price=:price,"+
				"stock=:stock,description=:description,last_modified_date=:lastModifiedDate" +" where product_id=:productId";
		Map<String,Object> map=new HashMap<>();
		map.put("productId", productId);
		
		map.put("productName", productRequest.getProductName());
		map.put("category", productRequest.getCategory().toString());
		map.put("imageUrl", productRequest.getImageUrl());
		map.put("price", productRequest.getPrice());
		map.put("stock", productRequest.getStock());
		map.put("description", productRequest.getDescription());
		
		map.put("lastModifiedDate", new Date());
		namedParameterJdbcTemplate.update(sql, map);
		
	}

}
