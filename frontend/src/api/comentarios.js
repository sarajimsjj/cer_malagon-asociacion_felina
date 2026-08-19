import { obtenerToken } from './auth'
import { API_BASE_URL } from './config'

export async function obtenerComentarios(gatoId, solicitudId) {
  const respuesta = await fetch(
    `${API_BASE_URL}/api/gatos/${gatoId}/solicitudes/${solicitudId}/comentarios`,
    { headers: { Authorization: `Bearer ${obtenerToken()}` } },
  )
  if (!respuesta.ok) {
    if (respuesta.status === 401 || respuesta.status === 403) {
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    throw new Error(`Error al cargar los comentarios (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function crearComentario(gatoId, solicitudId, texto) {
  const respuesta = await fetch(
    `${API_BASE_URL}/api/gatos/${gatoId}/solicitudes/${solicitudId}/comentarios`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${obtenerToken()}`,
      },
      body: JSON.stringify({ texto }),
    },
  )
  if (!respuesta.ok) {
    if (respuesta.status === 401 || respuesta.status === 403) {
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    throw new Error(`Error al enviar el comentario (${respuesta.status})`)
  }
  return respuesta.json()
}
