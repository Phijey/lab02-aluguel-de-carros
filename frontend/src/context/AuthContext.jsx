import { createContext, useContext, useState } from 'react'
import * as authService from '../services/authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(() => {
    const salvo = localStorage.getItem('usuario')
    return salvo ? JSON.parse(salvo) : null
  })

  async function entrar(login, senha) {
    const dados = await authService.login(login, senha)
    localStorage.setItem('usuario', JSON.stringify(dados))
    setUsuario(dados)
    return dados
  }

  function sair() {
    localStorage.removeItem('usuario')
    setUsuario(null)
  }

  const isAgente = usuario?.perfil === 'EMPRESA' || usuario?.perfil === 'BANCO'

  return (
    <AuthContext.Provider value={{ usuario, entrar, sair, isAgente }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
