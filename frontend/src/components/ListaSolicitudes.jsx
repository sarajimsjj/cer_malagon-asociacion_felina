import { useEffect, useState } from 'react'
import { obtenerSolicitudes, cambiarEstadoSolicitud } from '../api/solicitudes'
import { ESTADOS_SOLICITUD } from '../constants'
import './ListaSolicitudes.css'

export default function ListaSolicitudes({ gatoId }) {
  const [solicitudes, setSolicitudes] = useState([])
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'
  const [errorPorSolicitud, setErrorPorSolicitud] = useState({})

  useEffect(() => {
    obtenerSolicitudes(gatoId)
      .then((datos) => {
        setSolicitudes(datos)
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [gatoId])

  async function manejarCambioEstado(solicitudId, nuevoEstado) {
    setErrorPorSolicitud((anteriores) => ({ ...anteriores, [solicitudId]: null }))
    try {
      const actualizada = await cambiarEstadoSolicitud(gatoId, solicitudId, nuevoEstado)
      setSolicitudes((anteriores) =>
        anteriores.map((s) => (s.id === solicitudId ? actualizada : s)),
      )
    } catch (error) {
      setErrorPorSolicitud((anteriores) => ({ ...anteriores, [solicitudId]: error.message }))
    }
  }

  if (estado === 'cargando') {
    return <p className="mensaje-estado">Cargando solicitudes…</p>
  }

  if (estado === 'error') {
    return <p className="mensaje-estado mensaje-estado--error">No se han podido cargar las solicitudes.</p>
  }

  if (solicitudes.length === 0) {
    return <p className="mensaje-estado">Todavía no hay solicitudes para este gato.</p>
  }

  return (
    <ul className="lista-solicitudes">
      {solicitudes.map((solicitud) => {
        const infoEstado = ESTADOS_SOLICITUD[solicitud.estado] ?? { texto: solicitud.estado, color: 'adoptado' }

        return (
          <li key={solicitud.id} className="solicitud">
            <div className="solicitud__cabecera">
              <span className="solicitud__nombre">{solicitud.nombreSolicitante}</span>
              <span className={`solicitud__estado solicitud__estado--${infoEstado.color}`}>
                {infoEstado.texto}
              </span>
            </div>

            <p className="solicitud__contacto">
              {solicitud.email}
              {solicitud.telefono && ` · ${solicitud.telefono}`}
            </p>

            {solicitud.mensaje && <p className="solicitud__mensaje">{solicitud.mensaje}</p>}

            <div className="solicitud__acciones">
              <button
                type="button"
                className="boton boton--secundario"
                disabled={solicitud.estado === 'ACEPTADA'}
                onClick={() => manejarCambioEstado(solicitud.id, 'ACEPTADA')}
              >
                Aceptar
              </button>
              <button
                type="button"
                className="boton boton--secundario"
                disabled={solicitud.estado === 'RECHAZADA'}
                onClick={() => manejarCambioEstado(solicitud.id, 'RECHAZADA')}
              >
                Rechazar
              </button>
              <button
                type="button"
                className="boton boton--secundario"
                disabled={solicitud.estado === 'PENDIENTE'}
                onClick={() => manejarCambioEstado(solicitud.id, 'PENDIENTE')}
              >
                Marcar pendiente
              </button>
            </div>

            {errorPorSolicitud[solicitud.id] && (
              <p className="formulario__error">{errorPorSolicitud[solicitud.id]}</p>
            )}
          </li>
        )
      })}
    </ul>
  )
}
