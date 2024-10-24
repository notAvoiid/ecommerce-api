package com.abreu.ecommerce.security;

import com.abreu.ecommerce.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        var token = this.recoverToken(request);

        if (token != null && tokenService.validateToken(token)) {
            var username = tokenService.getUsernameFromToken(token);
            var role = tokenService.getRoleFromToken(token);
            var user = userRepository.findByUsername(username).orElse(null);

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var authorities = AuthorityUtils.createAuthorityList("ROLE_" + role);
                var authentication = new UsernamePasswordAuthenticationToken(user , null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) return authHeader.substring(7);
        return null;
    }
}