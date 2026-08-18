import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import { obtenerSolicitudesNoVistas } from '../api/solicitudes'
import './AvisoSolicitudes.css'

// Se refresca cada 2 minutos mientras haya sesión abierta, para que el aviso
// de solicitudes nuevas no dependa de recargar la página.
const INTERVALO_NOTIFICACIONES_MS = 2 * 60 * 1000

export default function AvisoSolicitudes() {
  const { estaAutenticada } = useAuth()
  const [datos, setDatos] = useState(null)

  useEffect(() => {
    if (!estaAutenticada) {
      return
    }

    let cancelado = false

    function actualizar() {
      obtenerSolicitudesNoVistas()
        .then((respuesta) => {
          if (!cancelado) setDatos(respuesta)
        })
        .catch(() => {})
    }

    actualizar()
    const intervalo = setInterval(actualizar, INTERVALO_NOTIFICACIONES_MS)
    return () => {
      cancelado = true
      clearInterval(intervalo)
    }
  }, [estaAutenticada])

  function manejarClick(gatoId, cantidad) {
    // Quita el gato de la lista al momento: no hace falta esperar al siguiente
    // refresco para que deje de aparecer, ya que al abrir su ficha se marca como visto.
    setDatos((anteriores) =>
      anteriores && {
        total: anteriores.total - cantidad,
        gatos: anteriores.gatos.filter((g) => g.gatoId !== gatoId),
      },
    )
  }

  if (!estaAutenticada || !datos || !Array.isArray(datos.gatos) || datos.total === 0) {
    return null
  }

  return (
    <div className="aviso-solicitudes" role="status">
      <div className="aviso-solicitudes__cabecera">
        {datos.total} solicitud{datos.total === 1 ? '' : 'es'} de adopción nueva
        {datos.total === 1 ? '' : 's'}
      </div>

      <ul className="aviso-solicitudes__lista">
        {datos.gatos.map((gato) => (
          <li key={gato.gatoId}>
            <Link to={`/gatos/${gato.gatoId}`} onClick={() => manejarClick(gato.gatoId, gato.cantidad)}>
              {gato.nombreGato}
              <span className="aviso-solicitudes__cantidad">{gato.cantidad}</span>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
