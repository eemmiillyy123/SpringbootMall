package com.example.springbootmall.dao;

import java.util.List;

import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;

public interface OrderDao {

	Integer createOrder(Integer userId, Integer totalAmount);

	void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

	Order getOrderById(Integer orderId);

	List<OrderItem> getOrderItemsByOrderId(Integer orderId);

	Integer countOrder(OrderQueryParams orderQueryParams);

	List<Order> getOrders(OrderQueryParams orderQueryParams);

}
