import { useState } from 'react'
import { Link } from 'react-router-dom'
import { crearAdministradora, ErrorValidacionAuth } from '../api/auth'
import '../pages/NuevoGato.css'

export default function NuevaAdministradora() {
  const [nombreUsuario, setNombreUsuario] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [erroresPorCampo, setErroresPorCampo] = useState({})
  const [errorGeneral, setErrorGeneral] = useState(null)
  const [mensajeExito, setMensajeExito] = useState(null)
  const [enviando, setEnviando] = useState(false)

  async function manejarEnvio(evento) {
    evento.preventDefault()
    setEnviando(true)
    setErrorGeneral(null)
    setErroresPorCampo({})
    setMensajeExito(null)

    try {
      const creada = await crearAdministradora(nombreUsuario, contrasena)
      setMensajeExito(`Cuenta creada para "${creada.nombreUsuario}".`)
      setNombreUsuario('')
      setContrasena('')
    } catch (error) {
      if (error instanceof ErrorValidacionAuth) {
        setErroresPorCampo(error.erroresPorCampo)
      } else {
        setErrorGeneral(error.message)
      }
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="pagina">
      <header className="cabecera">
        <h1 className="cabecera__titulo">Nueva administradora</h1>
        <p className="cabecera__subtitulo">Invita a otra persona del equipo a gestionar la asociación (solo tendrá acceso a las solicitudes de adopción).</p>
      </header>

      <main className="contenido contenido--formulario">
        <form className="formulario" onSubmit={manejarEnvio} noValidate>
          {errorGeneral && <p className="formulario__error-general">{errorGeneral}</p>}
          {mensajeExito && <p className="mensaje-estado">{mensajeExito}</p>}

          <div className="formulario__campo">
            <label htmlFor="nombreUsuario">Usuario</label>
            <input
              id="nombreUsuario"
              type="text"
              autoComplete="off"
              value={nombreUsuario}
              onChange={(e) => setNombreUsuario(e.target.value)}
            />
            {erroresPorCampo.nombreUsuario && (
              <p className="formulario__error">{erroresPorCampo.nombreUsuario}</p>
            )}
          </div>

          <div className="formulario__campo">
            <label htmlFor="contrasena">Contraseña</label>
            <input
              id="contrasena"
              type="password"
              autoComplete="new-password"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
            />
            {erroresPorCampo.contrasena && (
              <p className="formulario__error">{erroresPorCampo.contrasena}</p>
            )}
          </div>

          <div className="formulario__acciones">
            <Link to="/" className="boton boton--secundario">
              Volver
            </Link>
            <button type="submit" className="boton boton--primario" disabled={enviando}>
              {enviando ? 'Creando…' : 'Crear cuenta'}
            </button>
          </div>
        </form>
      </main>
    </div>
  )
}
