export const ESTADOS = {
  URGENTE: { texto: 'Adopción urgente', color: 'urgente' },
  DISPONIBLE: { texto: 'Disponible', color: 'disponible' },
  RESERVADO: { texto: 'Reservado', color: 'reservado' },
  ADOPTADO: { texto: 'Adoptado', color: 'adoptado' },
  EN_TRATAMIENTO: { texto: 'En tratamiento', color: 'enfermedad' },
}

export const SEXOS = {
  MACHO: 'Macho',
  HEMBRA: 'Hembra',
  DESCONOCIDO: 'Sexo sin determinar',
}

export const RESULTADOS_TEST = {
  NO_TESTADO: 'No testado',
  NEGATIVO: 'Negativo',
  POSITIVO: 'Positivo',
}

export const ESTADOS_SOLICITUD = {
  PENDIENTE: { texto: 'Pendiente', color: 'reservado' },
  ACEPTADA: { texto: 'Aceptada', color: 'disponible' },
  RECHAZADA: { texto: 'Rechazada', color: 'urgente' },
}

//Mismas claves que los objetos de arriba, pero como listas [clave, texto] para poder recorrerlas al construir un <select>
export const OPCIONES_ESTADO = 
  Object.entries(ESTADOS).map(([clave, { texto }]) => [clave, texto])

export const OPCIONES_SEXO = Object.entries(SEXOS)

export const OPCIONES_RESULTADO_TEST = Object.entries(RESULTADOS_TEST)