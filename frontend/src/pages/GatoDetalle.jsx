import { useEffect, useState } from 'react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { obtenerGato, actualizarGato } from '../api/gatos'
import { useAuth } from '../contexts/AuthContext'
import { ESTADOS, SEXOS } from '../constants'
import GaleriaGato from '../components/GaleriaGato'
import InformacionGato from '../components/InformacionGato'
import FormularioSolicitud from '../components/FormularioSolicitud'
import ListaSolicitudes from '../components/ListaSolicitudes'
import './GatoDetalle.css'

const ESTADOS_NO_DISPONIBLES = ['ADOPTADO', 'EN_TRATAMIENTO']

// El PUT /api/gatos/{id} exige el gato completo: solo estos campos, sin id
// ni datos calculados como edadTexto o fotoPrincipalUrl que el backend no espera.
function datosParaGuardar(gato, cambios) {
  return {
    nombre: gato.nombre,
    fechaNacimientoEstim: gato.fechaNacimientoEstim,
    sexo: gato.sexo,
    esterilizado: gato.esterilizado,
    desparasitado: gato.desparasitado,
    vacunado: gato.vacunado,
    enfermedad: gato.enfermedad,
    estado: gato.estado,
    testFiv: gato.testFiv,
    testFelv: gato.testFelv,
    observaciones: gato.observaciones,
    ...cambios,
  }
}

export default function GatoDetalle() {
  const { id } = useParams()
  const location = useLocation()
  const { estaAutenticada, esPrincipal } = useAuth()
  const [gato, setGato] = useState(null)
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'
  const [cambiandoEstado, setCambiandoEstado] = useState(false)
  const [errorEstado, setErrorEstado] = useState(null)

  useEffect(() => {
    obtenerGato(id)
      .then((datos) => {
        setGato(datos)
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [id])

  async function manejarCambioEstado(nuevoEstado) {
    setCambiandoEstado(true)
    setErrorEstado(null)
    try {
      const actualizado = await actualizarGato(id, datosParaGuardar(gato, { estado: nuevoEstado }))
      setGato(actualizado)
    } catch (err) {
      setErrorEstado(err.message ?? 'No se ha podido cambiar el estado')
    } finally {
      setCambiandoEstado(false)
    }
  }

  if (estado === 'cargando') {
    return (
      <div className="pagina">
        <p className="mensaje-estado">Cargando gato…</p>
      </div>
    )
  }

  if (estado === 'error') {
    return (
      <div className="pagina">
        <p className="mensaje-estado mensaje-estado--error">
          No se ha podido encontrar este gato.
        </p>
      </div>
    )
  }

  const gatoDisponible = !ESTADOS_NO_DISPONIBLES.includes(gato.estado)

  return (
    <div className="pagina">
      <Link to="/" className="gato-detalle__volver">
        ← Volver a todos los gatos
      </Link>

      <header className="gato-detalle__cabecera">
        <div className="gato-detalle__cabecera-fila">
          <div>
            <h1 className="gato-detalle__nombre">{gato.nombre}</h1>
            <p className="gato-detalle__meta">
              {gato.edadTexto} · {SEXOS[gato.sexo] ?? gato.sexo}
            </p>
          </div>
          {esPrincipal && (
            <Link to={`/gatos/${id}/editar`} className="boton boton--secundario">
              Editar gato
            </Link>
          )}
        </div>

        {esPrincipal && (
          <div className="gato-detalle__estados">
            <span className="gato-detalle__estados-etiqueta">Marcar como:</span>
            {Object.entries(ESTADOS).map(([clave, { texto, color }]) => (
              <button
                key={clave}
                type="button"
                className={`gato-detalle__boton-estado gato-detalle__boton-estado--${color}${
                  gato.estado === clave ? ' gato-detalle__boton-estado--activo' : ''
                }`}
                disabled={cambiandoEstado || gato.estado === clave}
                onClick={() => manejarCambioEstado(clave)}
              >
                {texto}
              </button>
            ))}
          </div>
        )}

        {errorEstado && <p className="formulario__error">{errorEstado}</p>}
      </header>

      <main className="contenido gato-detalle">
        <div className="gato-detalle__galeria">
          {location.state?.avisoFotos && (
            <p className="mensaje-estado mensaje-estado--error">{location.state.avisoFotos}</p>
          )}
          <GaleriaGato gatoId={id} estadoGato={gato.estado} />
        </div>

        <div className="gato-detalle__info">
          <InformacionGato gato={gato} />
        </div>

        <div className="gato-detalle__ancho-completo">
          {!estaAutenticada && (
            <section className="gato-detalle__seccion">
              <h2 className="gato-detalle__subtitulo">Solicitar adopción</h2>
              {gatoDisponible ? (
                <FormularioSolicitud gatoId={id} />
              ) : (
                <p className="mensaje-estado">
                  Este gato ya no está disponible para recibir solicitudes de adopción.
                </p>
              )}
            </section>
          )}

          {estaAutenticada && (
            <section className="gato-detalle__seccion">
              <h2 className="gato-detalle__subtitulo">Solicitudes recibidas</h2>
              <ListaSolicitudes gatoId={id} />
            </section>
          )}
        </div>
      </main>
    </div>
  )
}
