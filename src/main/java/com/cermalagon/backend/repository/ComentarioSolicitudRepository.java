package com.cermalagon.backend.repository;

import com.cermalagon.backend.entity.ComentarioSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ComentarioSolicitudRepository extends JpaRepository<ComentarioSolicitud, UUID> {

    List<ComentarioSolicitud> findBySolicitudIdOrderByFechaCreacionAsc(UUID solicitudId);
}
