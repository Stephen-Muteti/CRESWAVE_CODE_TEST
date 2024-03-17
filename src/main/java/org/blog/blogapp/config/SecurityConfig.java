package org.blog.blogapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.blog.blogapp.filters.JwtTokenFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                    .requestMatchers("/api/users/edit", "/api/comments/create/**", "/api/comments/read/**",
                            "/api/posts/create/**", "/api/posts/read/**", "/api/posts/search/**").authenticated()
                    .requestMatchers("/api/comments/update/**", "/api/comments/delete/**",
                            "/api/posts/edit/**", "/api/posts/delete/**").authenticated()
                    .anyRequest().permitAll()
            )
            .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}