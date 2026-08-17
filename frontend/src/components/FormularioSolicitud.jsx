import { useState } from 'react'
import { crearSolicitud } from '../api/solicitudes'
import { ErrorValidacion } from '../api/gatos'
import '../pages/NuevoGato.css'

const VALORES_INICIALES = {
  nombreSolicitante: '',
  email: '',
  telefono: '',
  mensaje: '',
}

export default function FormularioSolicitud({ gatoId }) {
  const [valores, setValores] = useState(VALORES_INICIALES)
  const [erroresPorCampo, setErroresPorCampo] = useState({})
  const [errorGeneral, setErrorGeneral] = useState(null)
  const [enviando, setEnviando] = useState(false)
  const [enviada, setEnviada] = useState(false)

  function actualizarCampo(campo, valor) {
    setValores((anteriores) => ({ ...anteriores, [campo]: valor }))
  }

  async function manejarEnvio(evento) {
    evento.preventDefault()
    setEnviando(true)
    setErrorGeneral(null)
    setErroresPorCampo({})

    try {
      await crearSolicitud(gatoId, {
        ...valores,
        telefono: valores.telefono.trim() || null,
        mensaje: valores.mensaje.trim() || null,
      })
      setEnviada(true)
    } catch (error) {
      if (error instanceof ErrorValidacion) {
        setErroresPorCampo(error.erroresPorCampo)
      } else {
        setErrorGeneral(error.message)
      }
    } finally {
      setEnviando(false)
    }
  }

  if (enviada) {
    return (
      <p className="mensaje-estado">
        ¡Gracias por tu solicitud! Nos pondremos en contacto contigo lo antes posible.
      </p>
    )
  }

  return (
    <form className="formulario" onSubmit={manejarEnvio} noValidate>
      {errorGeneral && <p className="formulario__error-general">{errorGeneral}</p>}

      <div className="formulario__campo">
        <label htmlFor="nombreSolicitante">Nombre</label>
        <input
          id="nombreSolicitante"
          type="text"
          value={valores.nombreSolicitante}
          onChange={(e) => actualizarCampo('nombreSolicitante', e.target.value)}
        />
        {erroresPorCampo.nombreSolicitante && (
          <p className="formulario__error">{erroresPorCampo.nombreSolicitante}</p>
        )}
      </div>

      <div className="formulario__fila">
        <div className="formulario__campo">
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            value={valores.email}
            onChange={(e) => actualizarCampo('email', e.target.value)}
          />
          {erroresPorCampo.email && <p className="formulario__error">{erroresPorCampo.email}</p>}
        </div>

        <div className="formulario__campo">
          <label htmlFor="telefono">Teléfono</label>
          <input
            id="telefono"
            type="tel"
            value={valores.telefono}
            onChange={(e) => actualizarCampo('telefono', e.target.value)}
          />
          {erroresPorCampo.telefono && <p className="formulario__error">{erroresPorCampo.telefono}</p>}
        </div>
      </div>

      <div className="formulario__campo">
        <label htmlFor="mensaje">Cuéntanos por qué quieres adoptarlo</label>
        <textarea
          id="mensaje"
          rows={4}
          value={valores.mensaje}
          onChange={(e) => actualizarCampo('mensaje', e.target.value)}
        />
      </div>

      <div className="formulario__acciones">
        <button type="submit" className="boton boton--primario" disabled={enviando}>
          {enviando ? 'Enviando…' : 'Enviar solicitud'}
        </button>
      </div>
    </form>
  )
}
