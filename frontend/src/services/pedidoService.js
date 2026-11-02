import api from './api'

export const meusPedidos = () => api.get('/pedidos/meus').then(r => r.data)
export const buscarPedido = id => api.get(`/pedidos/${id}`).then(r => r.data)

export const criarPedido = pedido => api.post('/pedidos', pedido).then(r => r.data)
export const alterarPedido = (id, pedido) => api.put(`/pedidos/${id}`, pedido).then(r => r.data)
export const cancelarPedido = id => api.patch(`/pedidos/${id}/cancelar`).then(r => r.data)

export const listarPedidos = status => api.get('/pedidos', { params: status ? { status } : {} }).then(r => r.data)
export const iniciarAnalise = id => api.patch(`/pedidos/${id}/analise`).then(r => r.data)
export const alterarPeloAgente = (id, dados) => api.put(`/pedidos/${id}/agente`, dados).then(r => r.data)
export const avaliarPedido = (id, aprovado, parecer) =>
  api.patch(`/pedidos/${id}/avaliacao`, { aprovado, parecer }).then(r => r.data)
