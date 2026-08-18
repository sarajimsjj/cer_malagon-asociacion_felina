package com.cermalagon.backend.repository;

import com.cermalagon.backend.entity.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, UUID> {

    List<SolicitudAdopcion> findByGatoIdOrderByFechaCreacionDesc(UUID gatoId);

    Optional<SolicitudAdopcion> findByIdAndGatoId(UUID id, UUID gatoId);

    long countByVistaFalse();

    // Un gato por fila, con cuántas solicitudes suyas nadie ha visto todavía.
    @Query("""
            SELECT s.gatoId AS gatoId, COUNT(s) AS cantidad
            FROM SolicitudAdopcion s
            WHERE s.vista = false
            GROUP BY s.gatoId
            """)
    List<ConteoNoVistasPorGato> contarNoVistasAgrupadoPorGato();

    interface ConteoNoVistasPorGato {
        UUID getGatoId();
        long getCantidad();
    }
}
