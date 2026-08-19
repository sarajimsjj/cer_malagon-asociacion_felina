import { useEffect, useRef, useState } from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'
import './Navbar.css'

function claseEnlace({ isActive }) {
  return isActive ? 'navbar__enlace navbar__enlace--activo' : 'navbar__enlace'
}

function claseEnlaceAdmin({ isActive }) {
  return isActive ? 'navbar__admin-enlace navbar__admin-enlace--activo' : 'navbar__admin-enlace'
}

export default function Navbar() {
  const { estaAutenticada, esPrincipal, nombreUsuario, cerrarSesion } = useAuth()
  const navigate = useNavigate()
  const [menuAbierto, setMenuAbierto] = useState(false)
  const navRef = useRef(null)

  // El fondo de huellas (ver global.css) arranca justo debajo del navbar en vez
  // de por debajo de toda la pantalla, así que necesita saber su altura real:
  // cambia con el ancho de pantalla y con el menú hamburguesa abierto/cerrado.
  useEffect(() => {
    const nodo = navRef.current
    if (!nodo) return

    function actualizarAltura() {
      document.documentElement.style.setProperty('--navbar-h', `${nodo.getBoundingClientRect().height}px`)
    }

    actualizarAltura()
    const observador = new ResizeObserver(actualizarAltura)
    observador.observe(nodo)
    return () => observador.disconnect()
  }, [])

  function cerrarMenu() {
    setMenuAbierto(false)
  }

  function manejarCerrarSesion() {
    cerrarSesion()
    cerrarMenu()
    navigate('/')
  }

  return (
    <nav className="navbar" ref={navRef}>
      <div className="navbar__contenido">
        <Link to="/" className="navbar__marca" onClick={cerrarMenu}>
          <img src="/logo.png" alt="" className="navbar__logo" />
          CER
          <span className="navbar__marca-malagon">Malagón</span>
          <span className="navbar__marca-sub">asociación felina</span>
        </Link>

        <button
          type="button"
          className="navbar__hamburguesa"
          onClick={() => setMenuAbierto((anterior) => !anterior)}
          aria-expanded={menuAbierto}
          aria-label={menuAbierto ? 'Cerrar menú' : 'Abrir menú'}
        >
          <span />
          <span />
          <span />
        </button>

        <div className={`navbar__derecha${menuAbierto ? ' navbar__derecha--abierto' : ''}`}>
          <div className="navbar__enlaces">
            <NavLink to="/" end className={claseEnlace} onClick={cerrarMenu}>
              Conoce a nuestros gatos
            </NavLink>
            <NavLink to="/equipo" className={claseEnlace} onClick={cerrarMenu}>
              Nuestro equipo
            </NavLink>

            {esPrincipal && (
              <>
                <NavLink to="/gatos/nuevo" className={claseEnlace} onClick={cerrarMenu}>
                  + Añadir gato
                </NavLink>
                <NavLink to="/admin/nueva-administradora" className={claseEnlace} onClick={cerrarMenu}>
                  Nueva administradora
                </NavLink>
              </>
            )}
          </div>

          <div className="navbar__admin">
            {estaAutenticada ? (
              <>
                <span className="navbar__usuario">
                  <span className="navbar__usuario-punto" aria-hidden="true" />
                  {nombreUsuario}
                </span>
                <button type="button" className="navbar__admin-enlace navbar__boton" onClick={manejarCerrarSesion}>
                  Cerrar sesión
                </button>
              </>
            ) : (
              <NavLink to="/login" className={claseEnlaceAdmin} onClick={cerrarMenu}>
                Acceso administradoras
              </NavLink>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}
