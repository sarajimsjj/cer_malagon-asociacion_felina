package com.cermalagon.backend.service;

import com.cermalagon.backend.dto.AdministradoraResumenDto;
import com.cermalagon.backend.dto.CrearAdministradoraDto;
import com.cermalagon.backend.dto.LoginDto;
import com.cermalagon.backend.dto.TokenRespuestaDto;
import com.cermalagon.backend.entity.Administradora;
import com.cermalagon.backend.entity.RolAdministradora;
import com.cermalagon.backend.exception.CredencialesInvalidasException;
import com.cermalagon.backend.exception.NombreUsuarioDuplicadoException;
import com.cermalagon.backend.repository.AdministradoraRepository;
import com.cermalagon.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Hash bcrypt válido pero de una contraseña que nadie usa: sirve para que, cuando el
    // usuario no existe, se ejecute igualmente un passwordEncoder.matches() de coste similar
    // en vez de devolver el error al instante. Sin esto, un usuario inexistente responde
    // notablemente más rápido que uno real con contraseña incorrecta, lo que permite
    // enumerar nombres de usuario válidos midiendo el tiempo de respuesta.
    private static final String HASH_DUMMY = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

    private final AdministradoraRepository administradoraRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AdministradoraRepository administradoraRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.administradoraRepository = administradoraRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenRespuestaDto login(LoginDto datos) {
        Administradora administradora = administradoraRepository.findByNombreUsuario(datos.nombreUsuario()).orElse(null);
        String hashParaComparar = administradora != null ? administradora.getContrasenaHash() : HASH_DUMMY;
        boolean contrasenaCorrecta = passwordEncoder.matches(datos.contrasena(), hashParaComparar);

        if (administradora == null || !contrasenaCorrecta) {
            throw new CredencialesInvalidasException();
        }

        String token = jwtService.generarToken(administradora.getNombreUsuario());
        return new TokenRespuestaDto(token, administradora.getNombreUsuario(), administradora.getRol());
    }

    /**
     * Solo puede invocarla la administradora principal (lo exige SecurityConfig): las
     * cuentas que ella crea siempre tienen rol ESTANDAR, solo con acceso a solicitudes.
     */
    public AdministradoraResumenDto crearAdministradora(CrearAdministradoraDto datos) {
        if (administradoraRepository.existsByNombreUsuario(datos.nombreUsuario())) {
            throw new NombreUsuarioDuplicadoException(datos.nombreUsuario());
        }

        Administradora administradora = new Administradora();
        administradora.setNombreUsuario(datos.nombreUsuario());
        administradora.setContrasenaHash(passwordEncoder.encode(datos.contrasena()));
        administradora.setRol(RolAdministradora.ESTANDAR);

        Administradora guardada = administradoraRepository.save(administradora);
        return AdministradoraResumenDto.desde(guardada);
    }
}
