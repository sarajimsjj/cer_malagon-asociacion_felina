import { createContext, useContext, useState } from 'react'
import {
  obtenerSesionGuardada,
  guardarSesion,
  borrarSesion,
  iniciarSesion as iniciarSesionApi,
} from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [sesion, setSesion] = useState(() => obtenerSesionGuardada())

  async function iniciarSesion(nombreUsuario, contrasena) {
    const { token, nombreUsuario: usuarioConfirmado, rol } = await iniciarSesionApi(nombreUsuario, contrasena)
    guardarSesion(token, usuarioConfirmado, rol)
    setSesion({ token, nombreUsuario: usuarioConfirmado, rol })
  }

  function cerrarSesion() {
    borrarSesion()
    setSesion(null)
  }

  const valor = {
    nombreUsuario: sesion?.nombreUsuario ?? null,
    estaAutenticada: sesion !== null,
    esPrincipal: sesion?.rol === 'PRINCIPAL',
    iniciarSesion,
    cerrarSesion,
  }

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>
}

// eslint-disable-next-line react-refresh/only-export-components -- hook y provider comparten contexto
export function useAuth() {
  const contexto = useContext(AuthContext)
  if (!contexto) {
    throw new Error('useAuth debe usarse dentro de un AuthProvider')
  }
  return contexto
}
