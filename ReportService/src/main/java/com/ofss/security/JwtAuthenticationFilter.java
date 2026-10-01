package com.ofss.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        boolean hasHeader = false;
        if (authorization != null) {
            if (authorization.startsWith("Bearer ")) {
                hasHeader = true;
            }
        }

        if (hasHeader) {

            String token = authorization.substring(7);

            try {
                Claims claims = jwtUtil.validateToken(token);

                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                String authorityName = "ROLE_" + role;
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(authorityName);

                List<SimpleGrantedAuthority> authorities =
                        new ArrayList<>();
                authorities.add(authority);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch (Exception exception) {
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}