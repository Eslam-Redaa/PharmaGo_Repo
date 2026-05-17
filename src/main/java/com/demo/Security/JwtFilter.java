package com.demo.Security;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtUtils jutils;
	
	@Autowired
	AppUserDetailsService userdetailsservice;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String token = "";
		String username = "";
		String path = request.getServletPath();
		
		if(path.equals("/users/login") || path.equals("/users/register")  ) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		if( header != null && header.startsWith("Bearer "))
		{
			token = header.substring(7);
			username = jutils.extractUsername(token);
		}		
		
		if( username != null && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			UserDetails user = userdetailsservice.loadUserByUsername(username);
			if(jutils.IsTokenValid(token , user))
			{
				UsernamePasswordAuthenticationToken authToken = new 
						UsernamePasswordAuthenticationToken(user , null , user.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
