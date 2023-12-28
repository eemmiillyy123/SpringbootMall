package com.example.springbootmall.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootmall.constant.ProductCategory;
import com.example.springbootmall.dto.ProductQueryParams;
import com.example.springbootmall.dto.ProductRequest;
import com.example.springbootmall.model.Product;
import com.example.springbootmall.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;	
	
//	@GetMapping("/products")//在restful表示查詢商品列表
//	//category是從url中取得的參數 前端透過這個值指定想要查看哪個分類的商品 springboot會自動把字串轉成ProductCategory這個enum
//	public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) ProductCategory category,@RequestParam(required = false)String search){
//		List<Product> list=productService.getProducts(category,search);
//		
//		return ResponseEntity.status(HttpStatus.OK).body(list);
//		
//	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts(
			//查詢條件filtering
			@RequestParam(required = false) ProductCategory category,
			@RequestParam(required = false)String search,
			//排序sorting
			@RequestParam(defaultValue = "created_date")String orderBy,
			@RequestParam(defaultValue = "desc")String sort
			){
		
		ProductQueryParams productQueryParams=new ProductQueryParams();
		productQueryParams.setCategory(category);
		productQueryParams.setSearch(search);
		productQueryParams.setOrderBy(orderBy);
		productQueryParams.setSort(sort);
		List<Product> list=productService.getProducts(productQueryParams);
		return ResponseEntity.status(HttpStatus.OK).body(list);
		
	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
		Product product=productService.getProductById(productId);
		if(product!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(product);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
		Integer productId= productService.createProduct(productRequest);
		Product product=productService.getProductById(productId);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,@RequestBody @Valid ProductRequest productRequest){
		Product  product=productService.getProductById(productId);
		//查詢商品是否存在，存在的話才去更新商品
		if(product==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		//修改商品數據
		productService.updateProduct(productId,productRequest);
		Product updateProduct=productService.getProductById(productId);//取得更新後的數據
		return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
		
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
		productService.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
	}
}
