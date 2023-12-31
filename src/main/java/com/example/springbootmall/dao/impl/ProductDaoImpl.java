package com.example.springbootmall.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.rowMapper.ProductRowMapper;

@Component
public class ProductDaoImpl implements ProductDao{
	private final static Logger log=LoggerFactory.getLogger(ProductDaoImpl.class);
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
//	@Override
//	public List<Product> getProducts(ProductCategory category,String search) {
//		String sql="select product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date from product where 1=1";
//		Map<String,Object> map=new HashMap<>();
//		if(category!=null) {
//			sql=sql+" and category=:category";
//			map.put("category", category.name());
//		}
//		if(search!=null) {
//			sql=sql+" and product_name LIKE :search";
//			map.put("search", "%"+search+"%");
//		}
//		List<Product> productList=namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
//		return productList;
//	}
	
	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		String sql="select product_id,product_name, category, image_url, price, stock, description, "
				+ "created_date, last_modified_date from product where 1=1";
		Map<String,Object> map=new HashMap<>();
		//查詢條件
		if(productQueryParams.getCategory()!=null) {
			sql=sql+" and category=:category";
			map.put("category", productQueryParams.getCategory().name());
		}
		if(productQueryParams.getSearch()!=null) {
			sql=sql+" and product_name LIKE :search";
			map.put("search", "%"+productQueryParams.getSearch()+"%");
		}
		//排序
		sql=sql+" order by "+productQueryParams.getOrderBy()+" "+productQueryParams.getSort();
		//分頁
		sql=sql+" limit :limit offset :offset";
		map.put("limit", productQueryParams.getLimit());
		map.put("offset", productQueryParams.getOffset());
		List<Product> productList=namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
		return productList;
	}
	
	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {
		String sql="select count(*) from product where 1=1";
		Map<String,Object> map=new HashMap<>();
		//查詢條件
		if(productQueryParams.getCategory()!=null) {
			sql=sql+" and category=:category";
			map.put("category", productQueryParams.getCategory().name());
		}
		if(productQueryParams.getSearch()!=null) {
			sql=sql+" and product_name LIKE :search";
			map.put("search", "%"+productQueryParams.getSearch()+"%");
		}
		Integer total=  namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
		
		
		return total;
	}
	
	@Override
	public Product getProductById(Integer productId) {
		String sql="select product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date from product where product_id=:productId";
		Map<String,Object> map=new HashMap<>();
		map.put("productId", productId);
		try {
			Product list=namedParameterJdbcTemplate.queryForObject(sql, map, new ProductRowMapper());
			return list;
		}catch(EmptyResultDataAccessException e){
			log.info("Product with ID " + productId + " not found.");
			return null;
		}
//		if(list.size()>0) {
//			return list.get(0);
//		}
//		else {
//			return null;
//		}
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
	@Override
	public void deleteProductById(Integer productId) {
		String sql="delete from product where product_id=:productId";
		Map<String,Object> map=new HashMap<>();
		map.put("productId", productId);
		namedParameterJdbcTemplate.update(sql, map);
	}

	
	

}
