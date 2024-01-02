package com.example.springbootmall.dao;

import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;

public interface UserDao {

	Integer createUser(UserRegisterRequest userRegisterRequest);
	
	User getUserByEmail(String email);

	User getUserById(Integer userId);

	

}
