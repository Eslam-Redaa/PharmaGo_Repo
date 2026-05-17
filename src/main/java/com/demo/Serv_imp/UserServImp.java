package com.demo.Serv_imp;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.demo.Entities.User;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.IO.AppResponse;
import com.demo.IO.LoginRequest;
import com.demo.IO.UserRequest;
import com.demo.IO.UserResponse;
import com.demo.Repositories.UserRepository;
import com.demo.Security.JwtUtils;
import com.demo.Services.UserServices;
import com.demo.Utils.AppUtils;

@Service
public class UserServImp implements UserServices {
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	AppUtils utils;
	
	@Autowired
	JwtUtils jutils;
	
	@Autowired
	AuthenticationManager authmanager;

	@Override
	public AppResponse LogIn(LoginRequest request) {
		
		Authentication authentication = authmanager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
		if(!authentication.isAuthenticated())
		{
			throw new RuntimeException("Wrong Email or Password ......");
		}
		
		String token = jutils.generateToken(request.getEmail());
		String role = getUserRole(request.getEmail());
		
		AppResponse response = new AppResponse();
		response.setToken(token);
		response.setRole(role);
		response.setExpirationDate(jutils.extractExpirationDate(token).toString() );
		response.setStatusCode(200);
		response.setSuccessMessage("Authenticated Successfully..");
		
		return response;
	}
	
	//---------------------------------------------------------

	@Override
	public AppResponse AddUser(UserRequest request) {
		
		if(urepo.existsByEmail(request.getEmail())) {
			throw new RuntimeException("This Email Already Exist . User Another One!!");
		}
		
		if(request.getRole() == null ) {
			request.setRole("user");
		}

		User user = utils.ConvertUserToEntity(request);
		urepo.save(user);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setSuccessMessage("User Created Successfully...");
		
		return response;
	}
	
	//---------------------------------------------------------

	@Override
	public AppResponse UpdateUser(Long userId, UserRequest request) {

		User user = urepo.findById(userId)
				.orElseThrow( () -> new ElementNotFoundException("User with Id : "+userId+" -> Not Found"));
		
		user.setName(request.getName());
		user.setPassword(request.getPassword());
		user.setPhone(request.getPhone());
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("User Updated Successfully...");
		return response;
	}
	
	//---------------------------------------------------------
	
	@Override
	public AppResponse ChangeEmail(Long id , String newEmail) {
		
		if(urepo.existsByEmail(newEmail)) {
			throw new RuntimeException("This Email Already Exist . User Another One!!");
		}
		
		User user = urepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("User with Id : "+id+" -> Not Found"));
		
		user.setEmail(newEmail);
		urepo.save(user);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("User Updated Successfully...");
		return response;
	}
	
	//---------------------------------------------------------
	
	@Override
	public AppResponse DeleteUser(Long id) {

		User user = urepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("User with Id : "+id+" -> Not Found"));
		
		urepo.delete(user);
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setSuccessMessage("User Deleted Successfully...");
		return response;
	}
	
	//---------------------------------------------------------

	@Override
	public AppResponse GetUserById(Long id) {

		User user = urepo.findById(id)
				.orElseThrow( () -> new ElementNotFoundException("User with Id : "+id+" -> Not Found"));
		
		UserResponse userResponse = new UserResponse();
		userResponse = utils.ConvertUserToResponse(user);
		userResponse.setRole(user.getRole());
		userResponse.setPassword(user.getPassword());
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setUser(userResponse);
		return response;
	}
	
	//---------------------------------------------------------

	@Override
	public AppResponse GetUserByEmail(String email) {

		User user = urepo.findByEmail(email)
				.orElseThrow( () -> new ElementNotFoundException("User with Id : "+email+" -> Not Found"));
		
		UserResponse userResponse = new UserResponse();
		userResponse = utils.ConvertUserToResponse(user);
		userResponse.setRole(user.getRole());
		userResponse.setPassword(user.getPassword());
		
		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setUser(userResponse);
		return response;
	}
	
	//---------------------------------------------------------

	@Override
	public AppResponse GetAllUsers() {

		AppResponse response = new AppResponse();
		response.setStatusCode(200);
		response.setUserList( urepo.findAll().stream()
				.map( ent -> utils.ConvertUserToResponse(ent))
				.collect(Collectors.toList()));
		return response;
	}
	
	//---------------------------------------------------------
	
	public String getUserRole(String email) {

		User user = urepo.findByEmail(email)
				.orElseThrow(() -> new ElementNotFoundException("user with email not found : " + email));
		return user.getRole();
	}

}
