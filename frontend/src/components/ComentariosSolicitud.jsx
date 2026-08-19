import { useEffect, useState } from 'react'
import { obtenerComentarios, crearComentario } from '../api/comentarios'
import './ComentariosSolicitud.css'

export default function ComentariosSolicitud({ gatoId, solicitudId }) {
  const [comentarios, setComentarios] = useState([])
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'
  const [texto, setTexto] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    obtenerComentarios(gatoId, solicitudId)
      .then((datos) => {
        setComentarios(datos)
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [gatoId, solicitudId])

  async function manejarEnvio(evento) {
    evento.preventDefault()
    if (!texto.trim()) return

    setEnviando(true)
    setError(null)
    try {
      const creado = await crearComentario(gatoId, solicitudId, texto.trim())
      setComentarios((anteriores) => [...anteriores, creado])
      setTexto('')
    } catch (error) {
      setError(error.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="comentarios-solicitud">
      {estado === 'cargando' && <p className="mensaje-estado">Cargando comentarios…</p>}
      {estado === 'error' && (
        <p className="mensaje-estado mensaje-estado--error">No se han podido cargar los comentarios.</p>
      )}

      {estado === 'listo' && comentarios.length > 0 && (
        <ul className="comentarios-solicitud__lista">
          {comentarios.map((comentario) => (
            <li key={comentario.id} className="comentario">
              <div className="comentario__cabecera">
                <span className="comentario__autora">{comentario.autora}</span>
                {comentario.fechaCreacion && (
                  <span className="comentario__fecha">
                    {new Date(comentario.fechaCreacion).toLocaleString('es-ES', {
                      dateStyle: 'short',
                      timeStyle: 'short',
                    })}
                  </span>
                )}
              </div>
              <p className="comentario__texto">{comentario.texto}</p>
            </li>
          ))}
        </ul>
      )}

      <form className="comentarios-solicitud__formulario" onSubmit={manejarEnvio}>
        <textarea
          rows={2}
          placeholder="Añadir un comentario…"
          value={texto}
          onChange={(e) => setTexto(e.target.value)}
        />
        <button type="submit" className="boton boton--secundario" disabled={enviando || !texto.trim()}>
          {enviando ? 'Enviando…' : 'Comentar'}
        </button>
      </form>

      {error && <p className="formulario__error">{error}</p>}
    </div>
  )
}
