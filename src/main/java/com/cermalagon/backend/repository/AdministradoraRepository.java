package com.cermalagon.backend.repository;

import com.cermalagon.backend.entity.Administradora;
import com.cermalagon.backend.entity.RolAdministradora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdministradoraRepository extends JpaRepository<Administradora, UUID> {

    Optional<Administradora> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByRol(RolAdministradora rol);

    Optional<Administradora> findFirstByOrderByFechaCreacionAsc();
}
