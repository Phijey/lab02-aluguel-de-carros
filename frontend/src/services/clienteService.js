import api from './api'

export const listarClientes = () => api.get('/clientes').then(r => r.data)
export const buscarCliente = id => api.get(`/clientes/${id}`).then(r => r.data)

export const meusDados = () => api.get('/clientes/me').then(r => r.data)
export const atualizarMeusDados = dados => api.put('/clientes/me', dados).then(r => r.data)
export const excluirMinhaConta = () => api.delete('/clientes/me')
