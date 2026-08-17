import { obtenerToken } from './auth'
import { ErrorValidacion } from './gatos'

export async function crearSolicitud(gatoId, datos) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/solicitudes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(datos),
  })
  if (!respuesta.ok) {
    if (respuesta.status === 400) {
      const errores = await respuesta.json()
      throw new ErrorValidacion(errores)
    }
    throw new Error(`Error al enviar la solicitud (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function obtenerSolicitudes(gatoId) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/solicitudes`, {
    headers: { Authorization: `Bearer ${obtenerToken()}` },
  })
  if (!respuesta.ok) {
    if (respuesta.status === 401 || respuesta.status === 403) {
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    throw new Error(`Error al cargar las solicitudes (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function cambiarEstadoSolicitud(gatoId, solicitudId, estado) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/solicitudes/${solicitudId}/estado`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${obtenerToken()}`,
    },
    body: JSON.stringify({ estado }),
  })
  if (!respuesta.ok) {
    if (respuesta.status === 401 || respuesta.status === 403) {
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    throw new Error(`Error al actualizar la solicitud (${respuesta.status})`)
  }
  return respuesta.json()
}
