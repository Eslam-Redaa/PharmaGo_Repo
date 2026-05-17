package com.demo.Security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

String secretkey = "";
	
	public JwtUtils() throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
		SecretKey skey = generator.generateKey();
		secretkey = Base64.getEncoder().encodeToString(skey.getEncoded());
	}
	
	
	private SecretKey getKey() {
		byte[] skey = Decoders.BASE64.decode(secretkey);
		return Keys.hmacShaKeyFor(skey);
	}
	
	//-------------------------------------------------------------
	
	public String generateToken(String username) {
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()  +  1000 * 60 * 60 * 10 ))
				.signWith( getKey() )
				.compact();
	}

	//-------------------------------------------------------------
	
	public Claims extractAllClaims(String token) {
		
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	//-------------------------------------------------------------
	
	public <T> T extractClaim(String token , Function<Claims , T> func) {
		Claims claims = extractAllClaims(token);
		return func.apply(claims);
	}
	
	//-------------------------------------------------------------

	public String extractUsername(String token) {
		
		return extractClaim(token , Claims::getSubject);
	}
	
	//-------------------------------------------------------------
	
	public Date extractExpirationDate(String token) {
		return extractClaim(token , Claims::getExpiration);
	}
	
	//-------------------------------------------------------------
	
	public boolean IsTokenExpired(String token) {
		return extractExpirationDate(token).before(new Date());
	}
	
	//-------------------------------------------------------------

	public boolean IsTokenValid(String token, UserDetails user) {
		return ( extractUsername(token).equals(user.getUsername()) && !IsTokenExpired(token));
	}
}
