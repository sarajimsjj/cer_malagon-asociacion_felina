import { obtenerToken } from './auth'

export async function obtenerGatos() {
  const respuesta = await fetch('/api/gatos')

  if (!respuesta.ok) {
    throw new Error(`Error al cargar los gatos (${respuesta.status})`)
  }

  return respuesta.json()
}

export async function obtenerGato(id) {
  const respuesta = await fetch(`/api/gatos/${id}`)

  if (!respuesta.ok) {
    if (respuesta.status === 404) {
      throw new Error('No se ha encontrado ese gato.')
    }
    throw new Error(`Error al cargar el gato (${respuesta.status})`)
  }

  return respuesta.json()
}

export async function actualizarGato(id, datos) {
  const respuesta = await fetch(`/api/gatos/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${obtenerToken()}`,
    },
    body: JSON.stringify(datos),
  })
  if (!respuesta.ok) {
    if (respuesta.status === 400) {
      const errores = await respuesta.json()
      throw new ErrorValidacion(errores)
    }
    if (respuesta.status === 401) {
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    if (respuesta.status === 404) {
      throw new Error('No se ha encontrado ese gato.')
    }
    throw new Error(`Error al actualizar el gato (${respuesta.status})`)
  }
  return respuesta.json()
}

export async function crearGato(datos){
  const respuesta = await fetch('/api/gatos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${obtenerToken()}`,
    },
    body: JSON.stringify(datos),
  })
  if(!respuesta.ok){
    if(respuesta.status === 400){
      const errores = await respuesta.json()
      throw new ErrorValidacion(errores)
    }
    if(respuesta.status === 401){
      throw new Error('Tu sesión ha caducado. Vuelve a iniciar sesión.')
    }
    throw new Error(`Error al crear el gato(${respuesta.status})`)
  }
  return respuesta.json()
}

//Error especial para poder distinguir 'el servidor rechazó los datos' (errores por campo) de un fallo de red o de servidor
export class ErrorValidacion extends Error{
  constructor (erroresPorCampo){
    super('Datos no válidos')
    this.erroresPorCampo = erroresPorCampo
  }
}