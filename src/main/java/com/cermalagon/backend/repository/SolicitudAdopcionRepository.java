package com.cermalagon.backend.repository;

import com.cermalagon.backend.entity.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, UUID> {

    List<SolicitudAdopcion> findByGatoIdOrderByFechaCreacionDesc(UUID gatoId);

    Optional<SolicitudAdopcion> findByIdAndGatoId(UUID id, UUID gatoId);
}
