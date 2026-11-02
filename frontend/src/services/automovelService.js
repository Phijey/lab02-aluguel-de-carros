import api from './api'

export const listarAutomoveis = () => api.get('/automoveis').then(r => r.data)
export const buscarAutomovel = id => api.get(`/automoveis/${id}`).then(r => r.data)

export const criarAutomovel = a => api.post('/automoveis', a).then(r => r.data)
export const atualizarAutomovel = (id, a) => api.put(`/automoveis/${id}`, a).then(r => r.data)
export const excluirAutomovel = id => api.delete(`/automoveis/${id}`)
