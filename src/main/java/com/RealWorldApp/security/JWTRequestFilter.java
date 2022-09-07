package com.RealWorldApp.security;

import com.RealWorldApp.entity.User;
import com.RealWorldApp.model.TokenPayload;
import com.RealWorldApp.repository.UserRepository;
import com.RealWorldApp.util.JWTTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String token = null;
        TokenPayload tokenPayload = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Token ")) {
            token = requestTokenHeader.substring(6).trim();
            try {
                tokenPayload = JWTTokenUtil.getTokenPayload(token);
//                System.out.println("Can have token payload : " + tokenPayload);
            } catch (ExpiredJwtException exception) {
                System.out.println("expire");
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("unable to get jwt");
            }
        } else {
            System.out.println("JWT token are not start with token");

        }
        System.out.println("Token payload : " + tokenPayload);
        if (tokenPayload != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> optionalUser = userRepository.findById(tokenPayload.getId());
//            System.out.println("optionalUser in JWTRequestFilter is : " + optionalUser);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                // is validate or not
                if (JWTTokenUtil.validate(token, user)) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
//                    System.out.println("UserDetail in JWTRequestFilter is : " + userDetails);
                    UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//                    System.out.println("UsernamePasswordAuthenticationToken in JWTRequestFilter is : " + token1);
                    SecurityContextHolder.getContext().setAuthentication(token1);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
