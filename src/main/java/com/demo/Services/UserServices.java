package com.demo.Services;

import com.demo.IO.AppResponse;
import com.demo.IO.LoginRequest;
import com.demo.IO.UserRequest;

public interface UserServices {
	
	public AppResponse LogIn(LoginRequest request); 
	
	public AppResponse AddUser(UserRequest request); 
	
	public AppResponse UpdateUser(Long id , UserRequest request);
	
	public AppResponse ChangeEmail(Long id , String newEmail);
	
	public AppResponse DeleteUser(Long id);
	
	public AppResponse GetUserById(Long id);
	
	public AppResponse GetUserByEmail(String email);
	
	public AppResponse GetAllUsers();
	
	
}
