import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function RutaProtegida({ children, soloPrincipal = false }) {
  const { estaAutenticada, esPrincipal } = useAuth()
  const ubicacion = useLocation()

  if (!estaAutenticada) {
    return <Navigate to="/login" state={{ desde: ubicacion }} replace />
  }

  if (soloPrincipal && !esPrincipal) {
    return <Navigate to="/" replace />
  }

  return children
}
