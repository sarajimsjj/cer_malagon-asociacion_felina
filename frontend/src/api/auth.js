import { API_BASE_URL } from './config'

const CLAVE_TOKEN = 'cerMalagon.token'
const CLAVE_USUARIO = 'cerMalagon.nombreUsuario'
const CLAVE_ROL = 'cerMalagon.rol'

export function obtenerSesionGuardada() {
  const token = localStorage.getItem(CLAVE_TOKEN)
  const nombreUsuario = localStorage.getItem(CLAVE_USUARIO)
  const rol = localStorage.getItem(CLAVE_ROL)
  if (!token || !nombreUsuario || !rol) return null
  return { token, nombreUsuario, rol }
}

export function guardarSesion(token, nombreUsuario, rol) {
  localStorage.setItem(CLAVE_TOKEN, token)
  localStorage.setItem(CLAVE_USUARIO, nombreUsuario)
  localStorage.setItem(CLAVE_ROL, rol)
}

export function borrarSesion() {
  localStorage.removeItem(CLAVE_TOKEN)
  localStorage.removeItem(CLAVE_USUARIO)
  localStorage.removeItem(CLAVE_ROL)
}

export function obtenerToken() {
  return localStorage.getItem(CLAVE_TOKEN)
}

export async function iniciarSesion(nombreUsuario, contrasena) {
  const respuesta = await fetch(`${API_BASE_URL}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nombreUsuario, contrasena }),
  })

  if (!respuesta.ok) {
    if (respuesta.status === 401) {
      throw new Error('Usuario o contraseña incorrectos')
    }
    throw new Error(`Error al iniciar sesión (${respuesta.status})`)
  }

  return respuesta.json()
}

export async function crearAdministradora(nombreUsuario, contrasena) {
  const respuesta = await fetch(`${API_BASE_URL}/api/auth/administradoras`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${obtenerToken()}`,
    },
    body: JSON.stringify({ nombreUsuario, contrasena }),
  })

  if (!respuesta.ok) {
    if (respuesta.status === 400) {
      const errores = await respuesta.json()
      throw new ErrorValidacionAuth(errores)
    }
    if (respuesta.status === 409) {
      const { error } = await respuesta.json()
      throw new Error(error)
    }
    throw new Error(`Error al crear la administradora (${respuesta.status})`)
  }

  return respuesta.json()
}

export class ErrorValidacionAuth extends Error {
  constructor(erroresPorCampo) {
    super('Datos no válidos')
    this.erroresPorCampo = erroresPorCampo
  }
}
