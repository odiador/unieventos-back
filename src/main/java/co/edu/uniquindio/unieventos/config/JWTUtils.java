package co.edu.uniquindio.unieventos.config;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTUtils {

   @Autowired
   private AuthProps authProps;

   public String generateToken(String email, Map<String, Object> claims){
      
       Instant now = Instant.now();


       return Jwts.builder()
               .claims(claims)
               .subject(email)
               .issuedAt(Date.from(now))
               .expiration(Date.from(now.plus(1L, ChronoUnit.HOURS)))
               .signWith( getKey() )
               .compact();
   }
  
   public Jws<Claims> parseJwt(String jwtString) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
       JwtParser jwtParser = Jwts.parser().verifyWith( getKey() ).build();
       return jwtParser.parseSignedClaims(jwtString);
   }
  
   private SecretKey getKey(){
       String claveSecreta = authProps.getClientsecret();
       byte[] secretKeyBytes = claveSecreta.getBytes();
       return Keys.hmacShaKeyFor(secretKeyBytes);
   }

	public String getToken(HttpServletRequest req) {
		String header = req.getHeader("Authorization");
		return header != null && header.startsWith("Bearer ") ? header.replace("Bearer ", "") : null;
	}
	
	public String getEmail(HttpServletRequest req) {
		String token = getToken(req);
		return parseJwt(token).getPayload().getSubject();
	}
}
