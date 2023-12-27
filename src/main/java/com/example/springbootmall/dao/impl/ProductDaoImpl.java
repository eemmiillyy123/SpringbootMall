package com.example.springbootmall.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.springbootmall.dao.ProductDao;
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

}
