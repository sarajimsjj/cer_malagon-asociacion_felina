package com.cermalagon.backend.security;

import com.cermalagon.backend.entity.Administradora;
import com.cermalagon.backend.entity.RolAdministradora;
import com.cermalagon.backend.repository.AdministradoraRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el header "Authorization: Bearer <token>", y si el token es válido y
 * corresponde a una administradora existente, autentica la petición.
 * Si no hay token o no es válido, simplemente se sigue sin autenticar
 * (SecurityConfig decide después si la ruta requiere estarlo).
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final AdministradoraRepository administradoraRepository;

    public JwtAuthFilter(JwtService jwtService, AdministradoraRepository administradoraRepository) {
        this.jwtService = jwtService;
        this.administradoraRepository = administradoraRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String cabecera = request.getHeader("Authorization");
        String peticion = request.getMethod() + " " + request.getRequestURI();

        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            log.debug("[JWT] {} -> sin cabecera Authorization Bearer (normal en rutas públicas)", peticion);
        } else {
            String token = cabecera.substring("Bearer ".length());
            String nombreUsuario = jwtService.extraerNombreUsuarioSiValido(token);

            if (nombreUsuario == null) {
                log.debug("[JWT] {} -> token presente pero no válido o caducado", peticion);
            } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Administradora administradora = administradoraRepository.findByNombreUsuario(nombreUsuario).orElse(null);

                if (administradora == null) {
                    log.warn("[JWT] {} -> token válido para '{}' pero no existe esa administradora en BD", peticion, nombreUsuario);
                } else {
                    log.debug("[JWT] {} -> autenticada '{}' (rol {})", peticion, nombreUsuario, administradora.getRol());

                    List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
                    autoridades.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    if (administradora.getRol() == RolAdministradora.PRINCIPAL) {
                        autoridades.add(new SimpleGrantedAuthority("ROLE_PRINCIPAL"));
                    }

                    var autenticacion = new UsernamePasswordAuthenticationToken(
                            administradora.getNombreUsuario(), null, autoridades
                    );
                    autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
