package com.cermalagon.backend.repository;

import com.cermalagon.backend.entity.GatoFoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GatoFotoRepository extends JpaRepository<GatoFoto, UUID> {

    List<GatoFoto> findByGatoIdOrderByEsPrincipalDescFechaCreacionAsc(UUID gatoId);

    Optional<GatoFoto> findByIdAndGatoId(UUID id, UUID gatoId);

    Optional<GatoFoto> findByGatoIdAndEsPrincipalTrue(UUID gatoId);

    // Usado para resolver la foto de portada de todos los gatos del listado en una sola consulta.
    List<GatoFoto> findByEsPrincipalTrue();
}
