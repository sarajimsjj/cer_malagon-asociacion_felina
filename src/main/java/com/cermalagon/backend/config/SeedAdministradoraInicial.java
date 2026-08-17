package com.cermalagon.backend.config;

import com.cermalagon.backend.entity.Administradora;
import com.cermalagon.backend.entity.RolAdministradora;
import com.cermalagon.backend.repository.AdministradoraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Como el alta de administradoras es solo por invitación (no hay sign up público),
 * hace falta crear la primera cuenta de algún modo. Si al arrancar no existe
 * ninguna administradora, se crea una usando ADMIN_INICIAL_USUARIO y
 * ADMIN_INICIAL_CONTRASENA, con rol PRINCIPAL. A partir de ahí, esa cuenta puede
 * invitar al resto (con rol ESTANDAR) desde la aplicación y estas variables ya no se usan.
 *
 * También cubre la migración de instalaciones ya existentes: si hay administradoras
 * pero ninguna es PRINCIPAL (porque el rol se introdujo después), se asciende
 * automáticamente a la más antigua para que alguien conserve el acceso completo.
 */
@Component
public class SeedAdministradoraInicial implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedAdministradoraInicial.class);

    private final AdministradoraRepository administradoraRepository;
    private final PasswordEncoder passwordEncoder;
    private final String usuarioInicial;
    private final String contrasenaInicial;

    public SeedAdministradoraInicial(
            AdministradoraRepository administradoraRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin-inicial.usuario:}") String usuarioInicial,
            @Value("${app.admin-inicial.contrasena:}") String contrasenaInicial
    ) {
        this.administradoraRepository = administradoraRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioInicial = usuarioInicial;
        this.contrasenaInicial = contrasenaInicial;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (administradoraRepository.count() == 0) {
            crearAdministradoraInicial();
            return;
        }

        ascenderSiNoHayPrincipal();
    }

    private void crearAdministradoraInicial() {
        if (usuarioInicial.isBlank() || contrasenaInicial.isBlank()) {
            log.warn("No hay ninguna administradora registrada y no se han definido " +
                    "ADMIN_INICIAL_USUARIO / ADMIN_INICIAL_CONTRASENA: nadie podrá iniciar sesión " +
                    "ni añadir gatos hasta crear una cuenta manualmente.");
            return;
        }

        Administradora administradora = new Administradora();
        administradora.setNombreUsuario(usuarioInicial);
        administradora.setContrasenaHash(passwordEncoder.encode(contrasenaInicial));
        administradora.setRol(RolAdministradora.PRINCIPAL);
        administradoraRepository.save(administradora);

        log.info("Creada la administradora principal '{}'.", usuarioInicial);
    }

    private void ascenderSiNoHayPrincipal() {
        if (administradoraRepository.existsByRol(RolAdministradora.PRINCIPAL)) {
            return;
        }

        administradoraRepository.findFirstByOrderByFechaCreacionAsc().ifPresent(administradora -> {
            administradora.setRol(RolAdministradora.PRINCIPAL);
            administradoraRepository.save(administradora);
            log.info("'{}' ascendida a administradora principal (no había ninguna).",
                    administradora.getNombreUsuario());
        });
    }
}
