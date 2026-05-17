package com.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.demo.Entities.User;
import com.demo.Exceptions.ElementNotFoundException;
import com.demo.Repositories.UserRepository;

@Component
public class AppUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository urepo;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = urepo.findByEmail(username)
				.orElseThrow( () -> new ElementNotFoundException("User not Found : " + username));
		
		return new AppUserDetails(user);
	}
	
}
