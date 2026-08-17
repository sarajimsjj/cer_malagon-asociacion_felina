package com.cermalagon.backend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Las fotos servidas como archivo estático: un <img src> nunca manda el token.
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/gatos", "/api/gatos/*").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/gatos/*/fotos").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/gatos/*/solicitudes").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/login").permitAll()
                        // Solo la administradora principal añade/edita gatos, gestiona sus fotos
                        // e invita a otras administradoras; el resto de administradoras (ROLE_ADMIN)
                        // solo gestionan solicitudes.
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/gatos").hasRole("PRINCIPAL")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/gatos/*").hasRole("PRINCIPAL")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/gatos/*/fotos").hasRole("PRINCIPAL")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/gatos/*/fotos/*/principal").hasRole("PRINCIPAL")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/gatos/*/fotos/*").hasRole("PRINCIPAL")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/administradoras").hasRole("PRINCIPAL")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN))
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
