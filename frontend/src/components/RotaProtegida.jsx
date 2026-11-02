import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function RotaProtegida({ perfis, children }) {
  const { usuario } = useAuth()

  if (!usuario) return <Navigate to="/login" replace />
  if (perfis && !perfis.includes(usuario.perfil)) return <Navigate to="/" replace />
  return children
}
