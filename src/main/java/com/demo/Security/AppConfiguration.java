package com.demo.Security;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.cloudinary.Cloudinary;
import com.demo.Exceptions.MyAccessDeniedHandler;
import com.demo.Exceptions.MyAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfiguration {
	
	@Autowired
	AppUserDetailsService userdetailsservice;
	
	@Autowired
	MyAuthenticationEntryPoint myAuthenticationEntryPoint;
	
	@Autowired
	MyAccessDeniedHandler myAccessDeniedHandler;
	
	@Autowired
	JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain myFilterChain(HttpSecurity http) throws Exception {
		
		return http.authorizeHttpRequests( auth -> auth.requestMatchers("/users/login",
				"/users/register","/homePage").permitAll().anyRequest().authenticated() )			
				.csrf( cs -> cs.disable() )
				.sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling( ex -> ex.authenticationEntryPoint(myAuthenticationEntryPoint))
				.exceptionHandling(ex -> ex.accessDeniedHandler(myAccessDeniedHandler))
				.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class)
				.cors( Customizer.withDefaults() )
				.build();
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authManager()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userdetailsservice);
		provider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(provider);
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource()   //have to add the origines 
	{
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(List.of(""));               // heir
		corsConfig.setAllowedMethods(List.of("PUT","GET","POST","DELETE","OPTION"));
		corsConfig.setAllowedHeaders(List.of("*"));
		corsConfig.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return source;
	}
	
	
	@Value("${cloudinary.cloud-name}")
	private String cloudName;
	
	@Value("${cloudinary.api-key}")
	private String apiKey;
	
	@Value("${cloudinary.api-secret}")
	private String apiSecret;
	
	@Bean
	public Cloudinary cloudinaryCofing() {
		Map<String , String> config = new HashMap<>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		
		return new Cloudinary(config);
	}
}
