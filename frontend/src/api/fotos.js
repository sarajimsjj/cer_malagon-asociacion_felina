import { obtenerToken } from './auth'

export async function obtenerFotos(gatoId) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/fotos`)
  if (!respuesta.ok) {
    throw new Error(`Error al cargar las fotos (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function subirFoto(gatoId, archivo) {
  const formData = new FormData()
  formData.append('archivo', archivo)

  const respuesta = await fetch(`/api/gatos/${gatoId}/fotos`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${obtenerToken()}` },
    body: formData,
  })
  if (!respuesta.ok) {
    if (respuesta.status === 400 || respuesta.status === 413) {
      const { error } = await respuesta.json()
      throw new Error(error)
    }
    throw new Error(`Error al subir la foto (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function marcarFotoPrincipal(gatoId, fotoId) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/fotos/${fotoId}/principal`, {
    method: 'PATCH',
    headers: { Authorization: `Bearer ${obtenerToken()}` },
  })
  if (!respuesta.ok) {
    throw new Error(`Error al marcar la foto como principal (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function eliminarFoto(gatoId, fotoId) {
  const respuesta = await fetch(`/api/gatos/${gatoId}/fotos/${fotoId}`, {
    method: 'DELETE',
    headers: { Authorization: `Bearer ${obtenerToken()}` },
  })
  if (!respuesta.ok) {
    throw new Error(`Error al eliminar la foto (${respuesta.status})`)
  }
}
