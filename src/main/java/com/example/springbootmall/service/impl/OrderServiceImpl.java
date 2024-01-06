package com.example.springbootmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootmall.dao.OrderDao;
import com.example.springbootmall.dao.ProductDao;
import com.example.springbootmall.dto.BuyItem;
import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.OrderService;

@Component
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ProductDao productDao;

	//讓多個table同時新增數據成功或失敗可確保數據事一致的
	@Transactional
	@Override
	public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
		int totalAmount=0;
		List<OrderItem> orderItemList=new ArrayList<>();
		for(BuyItem buyItem:createOrderRequest.getBuyItemList()) {
			//取得商品資訊
			Product product=productDao.getProductById(buyItem.getProductId());
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
}
