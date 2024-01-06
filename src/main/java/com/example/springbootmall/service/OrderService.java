package com.example.springbootmall.service;

import javax.validation.Valid;

import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.model.Order;

public interface OrderService {

	Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

	Order getOrderId(Integer orderId);

}
