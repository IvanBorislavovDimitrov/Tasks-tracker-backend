package com.tracker.taskstracker.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tracker.taskstracker.jwt.JwtUtil;
import com.tracker.taskstracker.service.api.UserService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Autowired
    public JwtRequestFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;
        if (StringUtils.startsWith(authorizationHeader, "Bearer")) {
            jwt = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(jwt);
        }
        if (username != null && SecurityContextHolder.getContext()
                                                     .getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userDetails == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (JwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                                                                  null,
                                                                                                                                  userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                                     .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
