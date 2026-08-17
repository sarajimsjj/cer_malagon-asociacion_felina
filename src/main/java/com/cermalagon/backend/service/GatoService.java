package com.cermalagon.backend.service;

import com.cermalagon.backend.dto.GatoCreacionDto;
import com.cermalagon.backend.dto.GatoResumenDto;
import com.cermalagon.backend.entity.Gato;
import com.cermalagon.backend.entity.GatoFoto;
import com.cermalagon.backend.exception.RecursoNoEncontradoException;
import com.cermalagon.backend.repository.GatoFotoRepository;
import com.cermalagon.backend.repository.GatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GatoService {

    private final GatoRepository gatoRepository;
    private final GatoFotoRepository gatoFotoRepository;

    public GatoService(GatoRepository gatoRepository, GatoFotoRepository gatoFotoRepository) {
        this.gatoRepository = gatoRepository;
        this.gatoFotoRepository = gatoFotoRepository;
    }

    public List<GatoResumenDto> listarTodos() {
        List<Gato> gatos = gatoRepository.findAllOrdenadosPorPrioridad();

        // Una sola consulta para las fotos de portada de todos los gatos del listado, en vez de una por gato.
        Map<UUID, String> fotosPrincipalesPorGato = gatoFotoRepository.findByEsPrincipalTrue().stream()
                .collect(Collectors.toMap(GatoFoto::getGatoId, GatoFoto::getUrl, (a, b) -> a));

        return gatos.stream()
                .map(gato -> GatoResumenDto.desde(gato, fotosPrincipalesPorGato.get(gato.getId())))
                .toList();
    }

    public GatoResumenDto obtenerPorId(UUID id) {
        Gato gato = buscarOLanzar(id);
        return GatoResumenDto.desde(gato, obtenerFotoPrincipalUrl(id));
    }

    public GatoResumenDto crear(GatoCreacionDto datos){
        Gato gato = new Gato();
        aplicarDatos(gato, datos);

        Gato guardado = gatoRepository.save(gato);
        return GatoResumenDto.desde(guardado, null); // un gato recién creado todavía no tiene fotos
    }

    public GatoResumenDto actualizar(UUID id, GatoCreacionDto datos) {
        Gato gato = buscarOLanzar(id);
        aplicarDatos(gato, datos);

        Gato guardado = gatoRepository.save(gato);
        return GatoResumenDto.desde(guardado, obtenerFotoPrincipalUrl(id));
    }

    private String obtenerFotoPrincipalUrl(UUID gatoId) {
        return gatoFotoRepository.findByGatoIdAndEsPrincipalTrue(gatoId)
                .map(GatoFoto::getUrl)
                .orElse(null);
    }

    private Gato buscarOLanzar(UUID id) {
        return gatoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe ningún gato con id " + id));
    }

    private void aplicarDatos(Gato gato, GatoCreacionDto datos) {
        gato.setNombre(datos.nombre());
        gato.setFechaNacimientoEstim(datos.fechaNacimientoEstim());
        gato.setSexo(datos.sexo());
        gato.setEsterilizado(datos.esterilizado());
        gato.setDesparasitado(datos.desparasitado());
        gato.setVacunado(datos.vacunado());
        gato.setEnfermedad(datos.enfermedad());
        gato.setEstado(datos.estado());
        gato.setTestFiv(datos.testFiv());
        gato.setTestFelv(datos.testFelv());
        gato.setObservaciones(datos.observaciones());
    }
}
