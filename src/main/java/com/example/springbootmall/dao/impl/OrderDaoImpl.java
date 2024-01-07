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

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.rowMapper.OrderItemRowMapper;
import com.example.springbootmall.rowMapper.OrderRowMapper;

@Component
public class OrderDaoImpl implements OrderDao{

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	@Override
	public Integer createOrder(Integer userId, Integer totalAmount) {
		String sql="insert into `order`(user_id,total_amount,created_date,last_modified_date)"
				+ "values(:userId,:totalAmount,:createdDate,:lastModifiedDate)";
		Map<String,Object> map=new HashMap<>();
		map.put("userId", userId);
		map.put("totalAmount", totalAmount);
		Date now=new Date();
		map.put("createdDate", now);
		map.put("lastModifiedDate", now);
		KeyHolder keyHolder=new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),keyHolder);
		int orderId=keyHolder.getKey().intValue();
		return orderId;
	}
	
	@Override
	public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
		//使用for loop 一條一條sql加入數據，效率較低
		/*
		for(OrderItem orderItem:orderItemList) {
			String sql="insert into `order_item`(order_id ,product_id,quantity,amount)"
					+ "values(:orderId.:productId,:quantity,:amount)";
			Map<String,Object> map=new HashMap<>();
			map.put("orderId", orderId);
			map.put("productId", orderItem.getProductId());
			map.put("quantity", orderItem.getQuantity());
			map.put("amount", orderItem.getAmount());
			namedParameterJdbcTemplate.update(sql, map);
		}*/
		//使用batchUpdate一次性加入數據，效率更高
		String sql="insert into `order_item`(order_id ,product_id,quantity,amount)"
				+ " values(:orderId,:productId,:quantity,:amount)";
		MapSqlParameterSource[] parameterSources=new MapSqlParameterSource[orderItemList.size()];
		for(int i=0;i<orderItemList.size();i++) {
			OrderItem orderItem=orderItemList.get(i);
			parameterSources[i]=new MapSqlParameterSource();
			parameterSources[i].addValue("orderId", orderId);
			parameterSources[i].addValue("productId", orderItem.getProductId());
			parameterSources[i].addValue("quantity", orderItem.getQuantity());
			parameterSources[i].addValue("amount", orderItem.getAmount());
		}
		namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
	}

	@Override
	public Order getOrderById(Integer orderId) {
		String sql="select order_id ,user_id,total_amount,created_date,last_modified_date"
				+ " from `order` where order_id=:orderId";
		Map<String,Object> map=new HashMap<>();
		map.put("orderId", orderId);
		List<Order> orderList=namedParameterJdbcTemplate.query(sql, map,new OrderRowMapper());
		if(orderList.size()>0) {
			return orderList.get(0);
		}
		else {
			return null;			
		}
	}

	@Override
	public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
		//因為使用者看不懂productId(給工程師看的)，他們只看得懂image和name所以用join去取得這兩個資訊
		String sql="select oi.order_item_id ,oi.order_id,oi.product_id,oi.quantity,oi.amount,p.product_name,p.image_url"
				+ " from `order_item` as oi "+
				"left join product as p on oi.product_id=p.product_id "+
				"where oi.order_id=:orderId";
		Map<String,Object> map=new HashMap<>();
		map.put("orderId", orderId);
		List<OrderItem> orderItemList=namedParameterJdbcTemplate.query(sql, map,new OrderItemRowMapper());
		return orderItemList;
	}

	@Override
	public Integer countOrder(OrderQueryParams orderQueryParams) {
		String sql="select count(*) from `order` where 1=1";
		Map<String,Object> map=new HashMap<>();
		sql=addFilteringSql(sql, map, orderQueryParams);
		Integer total=namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
		return total;
	}

	@Override
	public List<Order> getOrders(OrderQueryParams orderQueryParams) {
		String sql="select order_id,user_id,total_amount,created_date,last_modified_date from `order` where 1=1";
		Map<String,Object> map=new HashMap<>();
		//查詢條件
		sql=addFilteringSql(sql, map, orderQueryParams);
		//排序
		//希望新的訂單排在最前面 不希望讓前端去改變訂單的排序紀錄
		sql=sql+" order by created_date desc";
		//分頁
		sql=sql+" limit :limit offset :offset";
		map.put("limit", orderQueryParams.getLimit());
		map.put("offset", orderQueryParams.getOffset());
		List<Order> orderList=namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
		return orderList;
	}
	private  String addFilteringSql(String sql,Map<String,Object> map,OrderQueryParams orderQueryParams) {
		if(orderQueryParams.getUserId()!=null) {
			sql=sql+" and user_id=:userId";
			map.put("userId", orderQueryParams.getUserId());
		}
		return sql;
	}

	
}
