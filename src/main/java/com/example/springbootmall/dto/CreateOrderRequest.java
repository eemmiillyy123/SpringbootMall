 package com.example.springbootmall.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

//對應到前端所傳回來的Json object
public class CreateOrderRequest {
	//加在map或list上驗證集合不可以是空的
	@NotEmpty
	private List<BuyItem> buyItemList;

	public List<BuyItem> getBuyItemList() {
		return buyItemList;
	}

	public void setBuyItemList(List<BuyItem> buyItemList) {
		this.buyItemList = buyItemList;
	}
	

}
