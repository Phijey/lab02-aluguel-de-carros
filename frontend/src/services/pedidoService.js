import api from './api'

export const meusPedidos = () => api.get('/pedidos/meus').then(r => r.data)
export const buscarPedido = id => api.get(`/pedidos/${id}`).then(r => r.data)

export const criarPedido = pedido => api.post('/pedidos', pedido).then(r => r.data)
export const alterarPedido = (id, pedido) => api.put(`/pedidos/${id}`, pedido).then(r => r.data)
export const cancelarPedido = id => api.patch(`/pedidos/${id}/cancelar`).then(r => r.data)
