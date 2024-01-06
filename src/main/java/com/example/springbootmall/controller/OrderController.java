package com.example.springbootmall.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootmall.dao.impl.ProductDaoImpl;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.service.OrderService;
@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;
	//有帳號後才能買東西
	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<?> createOrder(@PathVariable Integer userId,
										@RequestBody @Valid CreateOrderRequest createOrderRequest){
		//插入數據
		Integer orderId= orderService.createOrder(userId,createOrderRequest);
		//回傳整筆資料給前端
		Order order=orderService.getOrderId(orderId);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}
	
}
