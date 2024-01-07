package com.example.springbootmall.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootmall.dao.impl.ProductDaoImpl;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.service.OrderService;
import com.example.springbootmall.util.Page;
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
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<Order>> getOrders(
			@PathVariable Integer userId,
			@RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
			@RequestParam(defaultValue = "0") @Min(0) Integer offset){
		OrderQueryParams orderQueryParams=new OrderQueryParams();
		orderQueryParams.setUserId(userId);
		orderQueryParams.setLimit(limit);
		orderQueryParams.setOffset(limit);
		
		//取得order list
		List<Order> orderList=orderService.getOrders(orderQueryParams);
		//取得order 總數
		Integer count=orderService.countOrder(orderQueryParams);
		//分頁
		Page<Order> page=new Page<>();
		page.setLimit(limit);
		page.setOffset(offset);
		page.setOffset(count);
		page.setResults(orderList);
		return ResponseEntity.status(HttpStatus.OK).body(page);
	}
	
}
