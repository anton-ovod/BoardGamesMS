package org.example.modules.auth.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtRequestFilter(JwtTokenService jwtTokenService, JwtUserDetailsService userDetailsService) {
        this.jwtUserDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        DecodedJWT jwt = jwtTokenService.validateToken(token);
        if (jwt == null || jwt.getSubject() == null) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails;
        try {
            userDetails = jwtUserDetailsService.loadUserByUsername(jwt.getSubject());
        } catch (UsernameNotFoundException userNotFoundEx) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}