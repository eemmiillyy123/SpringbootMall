package com.example.springbootmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.BuyItem;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.OrderService;

@Component
public class OrderServiceImpl implements OrderService{
	private final static Logger log=LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;

	//讓多個table同時新增數據成功或失敗可確保數據事一致的
	@Transactional
	@Override
	public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
		//檢查user是否存在
		User user=userDao.getUserById(userId);
		if(user==null) {
			log.warn("該 userId {} 不存在",userId);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		int totalAmount=0;
		List<OrderItem> orderItemList=new ArrayList<>();
		
		for(BuyItem buyItem:createOrderRequest.getBuyItemList()) {
			//取得商品資訊
			Product product=productDao.getProductById(buyItem.getProductId());
			
			//檢查product是否存在、庫存是否足夠
			if(product==null) {
				log.warn("商品{}不存在",buyItem.getProductId());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			else if(product.getStock()<buyItem.getQuantity()) {
				log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存{}，欲購買數量{}",
						buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			//扣除商品扣除
			productDao.updateStock(product.getProductId(),product.getStock()-buyItem.getQuantity());
			//計算總價錢
			int amount=buyItem.getQuantity()*product.getPrice();
			totalAmount=totalAmount+amount;
			//轉換BuyItem to OrderItem
			OrderItem orderItem=new OrderItem();
			orderItem.setProductId(buyItem.getProductId());
			orderItem.setQuantity(buyItem.getQuantity());
			orderItem.setAmount(amount);
			orderItemList.add(orderItem);
		}
		//必須要傳遞userId、totalAmount
		Integer orderId= orderDao.createOrder(userId,totalAmount);
		orderDao.createOrderItems(orderId,orderItemList);
		return orderId;
	}

	@Override
	public Order getOrderId(Integer orderId) {
		Order order=orderDao.getOrderById(orderId);
		//一張order會包含多個orderItem所以去擴充order加入orderItemList變數最後去回傳order給前端
		List<OrderItem> orderItemList=orderDao.getOrderItemsByOrderId(orderId);
		order.setOrderItemList(orderItemList);
		return order;
	}
	
	@Override
	public Integer countOrder(OrderQueryParams orderQueryParams) {
		return orderDao.countOrder(orderQueryParams);
	}

	@Override
	public List<Order> getOrders(OrderQueryParams orderQueryParams) {
		List<Order> orderList=orderDao.getOrders(orderQueryParams);
		for(Order order:orderList) {
			List<OrderItem> orderItemList=orderDao.getOrderItemsByOrderId(order.getOrderId());
			order.setOrderItemList(orderItemList);
		}
		return orderList;
	}

	
}
