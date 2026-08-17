// En desarrollo se deja vacío y se usa el proxy de Vite (ver vite.config.js) hacia
// localhost:8080. En producción (Vercel) no hay ese proxy, así que hace falta apuntar
// directamente a la URL pública del backend a través de la variable de entorno VITE_API_URL.
export const API_BASE_URL = import.meta.env.VITE_API_URL ?? ''
