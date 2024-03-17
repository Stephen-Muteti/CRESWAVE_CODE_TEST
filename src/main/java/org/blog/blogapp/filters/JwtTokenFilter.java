package org.blog.blogapp.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.blog.blogapp.exceptions.AuthenticationFailureException;
import org.blog.blogapp.utils.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            /*Extract JWT token from request headers*/
            String token = extractTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtTokenUtil.validateToken(token)) {
                 /*If valid, set up the authentication context*/
                String username = jwtTokenUtil.getUsernameFromToken(token);
                List<String> roles = jwtTokenUtil.getRolesFromToken(token);

                 /*Convert roles to a collection of GrantedAuthority*/
                Collection<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            throw new AuthenticationFailureException("Failed to set user authentication in security context");
        }

        /*Continue the filter chain*/
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        /*Extract JWT token from Authorization header*/
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
