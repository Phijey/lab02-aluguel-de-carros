import api from './api'

export const listarAutomoveis = () => api.get('/automoveis').then(r => r.data)
export const buscarAutomovel = id => api.get(`/automoveis/${id}`).then(r => r.data)
