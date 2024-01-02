package com.example.springbootmall.service.impl;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.UserLoginRequest;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.UserService;
@Component
public class UserServiceImpl implements UserService{
	private final static Logger log=LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;

	@Override
	public Integer register(UserRegisterRequest userRegisterRequest) {
		//檢查email是否有被註冊過
		User user=userDao.getUserByEmail(userRegisterRequest.getEmail());
		//有數據的話就噴錯來停止執行
		if(user!=null) {
			log.warn("該email{}已被註冊",userRegisterRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		//使用MD5生成密碼的雜湊值
		String hashedPassword=DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
		//字串轉成byte類型
		userRegisterRequest.setPassword(hashedPassword);
		//創建帳號
		return userDao.createUser(userRegisterRequest);
	}

	@Override
	public User getUserById(Integer userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public User login(@Valid UserLoginRequest userLoginRequest) {
		User user=userDao.getUserByEmail(userLoginRequest.getEmail());
		//檢查user是否存在
		if(user==null) {
			log.warn("該email {}尚未註冊",userLoginRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		//使用MD5生成密碼的雜湊值
		String hashedPassword=DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
		
		//比較密碼
		if(user.getPassword().equals(hashedPassword)) {
			return user;
		}
		else {
			log.warn("該email {}的密碼不正確",userLoginRequest.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
}
