import { useEffect, useState } from 'react'
import { obtenerFotos, subirFoto, marcarFotoPrincipal, eliminarFoto } from '../api/fotos'
import { useAuth } from '../contexts/AuthContext'
import EtiquetaCollar from './EtiquetaCollar'
import SiluetaGato from './SiluetaGato'
import SelectorArchivos from './SelectorArchivos'
import './GaleriaGato.css'

export default function GaleriaGato({ gatoId, estadoGato }) {
  const { esPrincipal } = useAuth()
  const [fotos, setFotos] = useState([])
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'
  const [indice, setIndice] = useState(0)
  const [subiendo, setSubiendo] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    obtenerFotos(gatoId)
      .then((datos) => {
        setFotos(datos)
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [gatoId])

  const actual = fotos[indice] ?? null

  function anterior() {
    setIndice((i) => (i - 1 + fotos.length) % fotos.length)
  }

  function siguiente() {
    setIndice((i) => (i + 1) % fotos.length)
  }

  async function manejarSubida(archivo) {
    setSubiendo(true)
    setError(null)
    try {
      const nueva = await subirFoto(gatoId, archivo)
      setFotos((anteriores) => {
        const nuevas = [...anteriores, nueva]
        setIndice(nuevas.length - 1)
        return nuevas
      })
    } catch (err) {
      setError(err.message)
    } finally {
      setSubiendo(false)
    }
  }

  async function manejarMarcarPrincipal() {
    setError(null)
    try {
      await marcarFotoPrincipal(gatoId, actual.id)
      setFotos((anteriores) => anteriores.map((f) => ({ ...f, esPrincipal: f.id === actual.id })))
    } catch (err) {
      setError(err.message)
    }
  }

  async function manejarEliminar() {
    setError(null)
    try {
      await eliminarFoto(gatoId, actual.id)
      setFotos((anteriores) => {
        const nuevas = anteriores.filter((f) => f.id !== actual.id)
        setIndice((i) => Math.min(i, Math.max(nuevas.length - 1, 0)))
        return nuevas
      })
    } catch (err) {
      setError(err.message)
    }
  }

  if (estado === 'cargando') {
    return <p className="mensaje-estado">Cargando fotos…</p>
  }

  if (estado === 'error') {
    return <p className="mensaje-estado mensaje-estado--error">No se han podido cargar las fotos.</p>
  }

  return (
    <div className="galeria-gato">
      <div className="galeria-gato__visor">
        <EtiquetaCollar estado={estadoGato} />

        <div className="galeria-gato__visor-interior">
          {fotos.length > 1 && (
            <button
              type="button"
              className="galeria-gato__flecha galeria-gato__flecha--izq"
              onClick={anterior}
              aria-label="Foto o vídeo anterior"
            >
              ‹
            </button>
          )}

          {actual ? (
            actual.tipo === 'VIDEO' ? (
              <video key={actual.id} src={actual.url} controls className="galeria-gato__media" />
            ) : (
              <img key={actual.id} src={actual.url} alt="" className="galeria-gato__media" />
            )
          ) : (
            <div className="galeria-gato__vacio">
              <SiluetaGato />
            </div>
          )}

          {fotos.length > 1 && (
            <button
              type="button"
              className="galeria-gato__flecha galeria-gato__flecha--der"
              onClick={siguiente}
              aria-label="Foto o vídeo siguiente"
            >
              ›
            </button>
          )}

          {esPrincipal && actual && (
            <div className="galeria-gato__overlay-acciones">
              {actual.esPrincipal ? (
                <span className="galeria-gato__insignia">Principal</span>
              ) : (
                <span />
              )}
              <div className="galeria-gato__overlay-botones">
                {actual.tipo !== 'VIDEO' && !actual.esPrincipal && (
                  <button type="button" className="boton boton--secundario" onClick={manejarMarcarPrincipal}>
                    Marcar principal
                  </button>
                )}
                <button type="button" className="boton boton--secundario" onClick={manejarEliminar}>
                  Eliminar
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {fotos.length > 0 && (
        <div className="galeria-gato__miniaturas">
          {fotos.map((foto, i) => (
            <button
              key={foto.id}
              type="button"
              className={`galeria-gato__miniatura${i === indice ? ' galeria-gato__miniatura--activa' : ''}`}
              onClick={() => setIndice(i)}
              aria-label={`Ver foto o vídeo ${i + 1}`}
            >
              {foto.tipo === 'VIDEO' ? (
                <video src={foto.url} className="galeria-gato__miniatura-media" muted preload="metadata" />
              ) : (
                <img src={foto.url} alt="" className="galeria-gato__miniatura-media" />
              )}
            </button>
          ))}
        </div>
      )}

      {esPrincipal && (
        <div className="galeria-gato__admin">
          <SelectorArchivos
            compacto
            disabled={subiendo}
            onSeleccionar={manejarSubida}
            texto={subiendo ? 'Subiendo…' : 'Añadir foto o vídeo'}
          />
        </div>
      )}

      {error && <p className="formulario__error">{error}</p>}
    </div>
  )
}
