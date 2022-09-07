package com.RealWorldApp.util;

import com.RealWorldApp.entity.User;
import com.RealWorldApp.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTTokenUtil {
    private static String secret = "Tuan28";

    public static boolean validate(String token, User user) {
        TokenPayload tokenPayload = getTokenPayload(token);

        return tokenPayload.getId().equals(user.getId())
                && tokenPayload.getEmail().equals(user.getEmail())
                && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        // co the viet the nay
//        Date expireDate = getClaimsFromToken(token,(Claims claim) ->{
//            return claim.getExpiration();
//        });
//         hoac co the viet the nay
        Date expireDate = getClaimsFromToken(token, Claims::getExpiration);

        return expireDate.before(new Date());

    }


    public String generateToken(User user, Long expireDate) {
        TokenPayload tokenPayload = TokenPayload.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
        Map<String, Object> claims = new HashMap<>();
        claims.put("payload", tokenPayload);
        String token = Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireDate * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    public static TokenPayload getTokenPayload(String token) {
        return getClaimsFromToken(token, (Claims claim)->{
        Map<String, Object> mapResult = (Map<String, Object>) claim.get("payload");
        return TokenPayload.builder()
                .id((String) mapResult.get("id"))
                .email((String) mapResult.get("email"))
                .build();
        });
    }

   static private<T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);

    }
}
