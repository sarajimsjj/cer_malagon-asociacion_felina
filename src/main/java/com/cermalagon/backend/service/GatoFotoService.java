package com.cermalagon.backend.service;

import com.cermalagon.backend.dto.GatoFotoDto;
import com.cermalagon.backend.entity.GatoFoto;
import com.cermalagon.backend.entity.TipoMedia;
import com.cermalagon.backend.exception.RecursoNoEncontradoException;
import com.cermalagon.backend.exception.VideoNoPuedeSerPrincipalException;
import com.cermalagon.backend.repository.GatoFotoRepository;
import com.cermalagon.backend.repository.GatoRepository;
import com.cermalagon.backend.storage.AlmacenadorArchivos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class GatoFotoService {

    private final GatoFotoRepository gatoFotoRepository;
    private final GatoRepository gatoRepository;
    private final AlmacenadorArchivos almacenadorArchivos;

    public GatoFotoService(
            GatoFotoRepository gatoFotoRepository,
            GatoRepository gatoRepository,
            AlmacenadorArchivos almacenadorArchivos
    ) {
        this.gatoFotoRepository = gatoFotoRepository;
        this.gatoRepository = gatoRepository;
        this.almacenadorArchivos = almacenadorArchivos;
    }

    public List<GatoFotoDto> listarPorGato(UUID gatoId) {
        comprobarQueElGatoExiste(gatoId);

        return gatoFotoRepository.findByGatoIdOrderByEsPrincipalDescFechaCreacionAsc(gatoId)
                .stream()
                .map(GatoFotoDto::desde)
                .toList();
    }

    // Solo la llama la administradora principal (ver SecurityConfig).
    public GatoFotoDto subir(UUID gatoId, MultipartFile archivo) {
        comprobarQueElGatoExiste(gatoId);

        AlmacenadorArchivos.ArchivoGuardado guardado = almacenadorArchivos.guardar(archivo);

        GatoFoto foto = new GatoFoto();
        foto.setGatoId(gatoId);
        foto.setUrl(guardado.url());
        foto.setTipo(guardado.tipo());
        // La administradora principal elige explícitamente cuál es la foto principal
        // (con "Marcar principal"); ninguna se marca así de forma automática.

        GatoFoto guardada = gatoFotoRepository.save(foto);
        return GatoFotoDto.desde(guardada);
    }

    // Solo la llama la administradora principal (ver SecurityConfig).
    @Transactional
    public GatoFotoDto marcarPrincipal(UUID gatoId, UUID fotoId) {
        GatoFoto foto = buscarFotoOLanzar(gatoId, fotoId);

        if (foto.getTipo() == TipoMedia.VIDEO) {
            throw new VideoNoPuedeSerPrincipalException();
        }

        // saveAndFlush (no solo save) es necesario: hay un índice único en BD que exige
        // como mucho una foto principal por gato, y Hibernate no garantiza que este UPDATE
        // se ejecute antes que el de más abajo si ambos se dejan para el flush automático.
        // Sin forzarlo aquí, a veces intenta poner la nueva como principal mientras la
        // antigua todavía lo es, y el índice único lo rechaza.
        gatoFotoRepository.findByGatoIdAndEsPrincipalTrue(gatoId).ifPresent(actual -> {
            actual.setEsPrincipal(false);
            gatoFotoRepository.saveAndFlush(actual);
        });

        foto.setEsPrincipal(true);
        GatoFoto guardada = gatoFotoRepository.save(foto);
        return GatoFotoDto.desde(guardada);
    }

    // Solo la llama la administradora principal (ver SecurityConfig).
    public void eliminar(UUID gatoId, UUID fotoId) {
        GatoFoto foto = buscarFotoOLanzar(gatoId, fotoId);

        gatoFotoRepository.delete(foto);
        almacenadorArchivos.eliminar(foto.getUrl());
        // Si era la principal, ninguna foto queda marcada como tal: la administradora
        // elige la siguiente explícitamente con "Marcar principal", no se hace por ella.
    }

    private GatoFoto buscarFotoOLanzar(UUID gatoId, UUID fotoId) {
        return gatoFotoRepository.findByIdAndGatoId(fotoId, gatoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe ninguna foto con id " + fotoId + " para ese gato"));
    }

    private void comprobarQueElGatoExiste(UUID gatoId) {
        if (!gatoRepository.existsById(gatoId)) {
            throw new RecursoNoEncontradoException("No existe ningún gato con id " + gatoId);
        }
    }
}
