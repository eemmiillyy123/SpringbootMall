package com.example.springbootmall.rowMapper;

import java.sql.ResultSet;
import com.example.springbootmall.model.OrderItem;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



public class OrderItemRowMapper implements RowMapper<OrderItem>{
	//rowMapper不會在意這些欄位是從哪個table出來的，只是提供功能去取得select出來的數據
	@Override
	public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderItem orderItem=new OrderItem();
		orderItem.setOrderItemId(rs.getInt("order_item_id"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setProductId(rs.getInt("product_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		orderItem.setAmount(rs.getInt("amount"));
		
		//在orderItem多新增兩個變數，或是額外建立一個新class也可以
		orderItem.setProductName(rs.getString("product_name"));
		orderItem.setImageUrl(rs.getString("image_url"));	
		return orderItem;
	}
}
