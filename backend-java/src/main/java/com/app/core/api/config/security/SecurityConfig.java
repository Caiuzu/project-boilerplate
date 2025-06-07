package com.app.core.api.config.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/**
 * Configuração de segurança para a aplicação
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("jwtAuthenticationFilter") Filter jwtFilter,
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                 // Rotas públicas
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/payments/webhook").permitAll()
                // Permitir acesso ao Swagger e OpenAPI
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher("/"),
                    AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                    AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                    AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                    AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                    AntPathRequestMatcher.antMatcher("/v3/api-docs.yaml"),
                    AntPathRequestMatcher.antMatcher("/webjars/**"),
                    AntPathRequestMatcher.antMatcher("/actuator/**")
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("USER", "GUEST")
                .requestMatchers(HttpMethod.PATCH, "/orders/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/orders/active").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/catalogs/**").hasAnyRole("ADMIN", "USER", "GUEST")
                .requestMatchers(HttpMethod.POST, "/catalogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/catalogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/catalogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/catalogs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/payments/*/qrCode").hasAnyRole("ADMIN", "USER", "GUEST")
                .requestMatchers(HttpMethod.GET, "/payments/*/status").hasAnyRole("ADMIN", "USER", "GUEST")

                // Permitir acesso a todos os endpoints (para desenvolvimento)
                .anyRequest().authenticated()
//                .anyRequest().permitAll()
            ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint));
                
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 