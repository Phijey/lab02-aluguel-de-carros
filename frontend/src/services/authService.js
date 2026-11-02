import api from './api'

export async function login(login, senha) {
  const { data } = await api.post('/auth/login', { login, senha })
  return data
}

export const registrarCliente = (cliente) => api.post('/auth/registro/cliente', cliente).then(r => r.data)

export const registrarAgente = (agente) => api.post('/auth/registro/agente', agente).then(r => r.data)
