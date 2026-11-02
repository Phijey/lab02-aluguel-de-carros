import api from './api'

export const listarContratos = () => api.get('/contratos').then(r => r.data)
export const buscarContrato = id => api.get(`/contratos/${id}`).then(r => r.data)
export const executarContrato = dados => api.post('/contratos', dados).then(r => r.data)

export const listarBancos = () => api.get('/agentes', { params: { perfil: 'BANCO' } }).then(r => r.data)
