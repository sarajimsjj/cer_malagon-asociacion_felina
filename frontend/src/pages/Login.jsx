import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import '../pages/NuevoGato.css'

export default function Login() {
  const { iniciarSesion } = useAuth()
  const navigate = useNavigate()
  const ubicacion = useLocation()

  const [nombreUsuario, setNombreUsuario] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [error, setError] = useState(null)
  const [enviando, setEnviando] = useState(false)

  async function manejarEnvio(evento) {
    evento.preventDefault()
    setEnviando(true)
    setError(null)

    try {
      await iniciarSesion(nombreUsuario, contrasena)
      const destino = ubicacion.state?.desde?.pathname ?? '/'
      navigate(destino, { replace: true })
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="pagina">
      <header className="cabecera">
        <h1 className="cabecera__titulo">Acceso administradoras</h1>
        <p className="cabecera__subtitulo">Inicia sesión para añadir gatos y gestionar la asociación.</p>
      </header>

      <main className="contenido contenido--formulario">
        <form className="formulario" onSubmit={manejarEnvio} noValidate>
          {error && <p className="formulario__error-general">{error}</p>}

          <div className="formulario__campo">
            <label htmlFor="nombreUsuario">Usuario</label>
            <input
              id="nombreUsuario"
              type="text"
              autoComplete="username"
              value={nombreUsuario}
              onChange={(e) => setNombreUsuario(e.target.value)}
            />
          </div>

          <div className="formulario__campo">
            <label htmlFor="contrasena">Contraseña</label>
            <input
              id="contrasena"
              type="password"
              autoComplete="current-password"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
            />
          </div>

          <div className="formulario__acciones">
            <Link to="/" className="boton boton--secundario">
              Cancelar
            </Link>
            <button type="submit" className="boton boton--primario" disabled={enviando}>
              {enviando ? 'Entrando…' : 'Entrar'}
            </button>
          </div>
        </form>
      </main>
    </div>
  )
}
