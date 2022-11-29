package com.bhgeek.mishopapi.utils;

import com.bhgeek.mishopapi.entity.User;
import io.jsonwebtoken.*;
 
import java.util.Date;
import java.util.UUID;
 
/**
 * @author lw
 * @since 2021/12/29
 */
public class JwtUtil {
    
    private static final String secretKey="hello jwt key";
    
    /**
     * 生成 JWT TOKEN
     * @param user 用户信息
     * @return String token
     */
    public static String createToken(User user){
        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("userId", user.getId())
                .claim("userName", user.getUsername())
                .claim("role", user.getRole())
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }
 
    /**
     * 解析 JWT TOKEN
     * @param token JWT TOKEN
     * @return User user
     */
    public static User parse(String token){
        User user=new User();
        try {
            JwtParser parser = Jwts.parser();
            Jws<Claims> claimsJws = parser.setSigningKey(secretKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            user.setId(Integer.valueOf(claims.get("userId").toString()));
            user.setUsername((String)claims.get("userName"));
            user.setRole((String)claims.get("role"));
        }catch (Exception e){}
        return user;
    }
 
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        user.setRole("admin");
        String token = createToken(user);
        System.out.println(token);
 
        User u = parse(token);
        System.out.println(u);
    }
}