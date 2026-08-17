package com.cermalagon.backend.repository;
import com.cermalagon.backend.entity.Gato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GatoRepository extends JpaRepository<Gato, UUID> {
    // Con extender JpaRepository ya tenemos findAll(), findById(), save(), etc.
    // Aquí añadiremos más adelante métodos como findByEstado(...) cuando haga falta.

    @Query(value = """
            SELECT * FROM gatos ORDER BY 
                CASE estado WHEN 'urgente' THEN 0
                            WHEN 'disponible' THEN 1
                            ELSE 2
                END,
                fecha_creacion ASC""", nativeQuery = true)
    List<Gato> findAllOrdenadosPorPrioridad();
}