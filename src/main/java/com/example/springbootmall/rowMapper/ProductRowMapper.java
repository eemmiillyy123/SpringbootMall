package com.example.springbootmall.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.model.Product;

public class ProductRowMapper implements RowMapper<Product>{

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product=new Product();
		product.setProductId(rs.getInt("product_id"));
		product.setProductName(rs.getString("product_name"));
		
		System.out.println("dfbdfs");
		String categoryStr=rs.getString("category"); 
		System.out.println("categoryStr:"+categoryStr);
		ProductCategory category=ProductCategory.valueOf(categoryStr);
		System.out.println("category:"+category);
		product.setCategory(category);
		
		product.setImageUrl(rs.getString("image_url"));
		product.setPrice(rs.getInt("price"));
		product.setStock(rs.getInt("stock"));
		product.setDescription(rs.getString("description"));
		product.setCreatedDate(rs.getTimestamp("created_date"));
		product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
		return product;
	}

}
