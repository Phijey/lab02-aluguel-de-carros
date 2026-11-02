import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import StatusBadge from '../components/StatusBadge'
import { buscarPedido, cancelarPedido } from '../services/pedidoService'
import { data, mensagemErro, moeda } from '../utils/erro'

export default function DetalhePedido() {
  const { id } = useParams()
  const [pedido, setPedido] = useState(null)
  const [erro, setErro] = useState('')

  useEffect(() => {
    buscarPedido(id).then(setPedido).catch(err => setErro(mensagemErro(err)))
  }, [id])

  async function cancelar() {
    if (!confirm('Deseja realmente cancelar este pedido?')) return
    try {
      setPedido(await cancelarPedido(id))
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  if (!pedido) return erro ? <div className="alerta erro">{erro}</div> : <p>Carregando...</p>
  const podeCancelar = pedido.status === 'PENDENTE' || pedido.status === 'EM_ANALISE'

  return (
    <section>
      <div className="cabecalho">
        <h2>Pedido #{pedido.id} <StatusBadge status={pedido.status} /></h2>
        <div className="acoes">
          {pedido.status === 'PENDENTE' && <Link to={`/pedidos/${id}/editar`} className="btn">Modificar</Link>}
          {podeCancelar && <button className="btn-perigo" onClick={cancelar}>Cancelar pedido</button>}
          <Link to="/pedidos" className="btn-secundario">Voltar</Link>
        </div>
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <div className="cartao grade info">
        <dl>
          <dt>Automóvel</dt><dd>{pedido.automovel.marca} {pedido.automovel.modelo} ({pedido.automovel.ano})</dd>
          <dt>Placa</dt><dd>{pedido.automovel.placa}</dd>
          <dt>Matrícula</dt><dd>{pedido.automovel.matricula}</dd>
        </dl>
        <dl>
          <dt>Período</dt><dd>{data(pedido.dataInicio)} a {data(pedido.dataFim)} ({pedido.dias} diárias)</dd>
          <dt>Valor estimado</dt><dd>{moeda(pedido.valorEstimado)}</dd>
          <dt>Criado em</dt><dd>{new Date(pedido.criadoEm).toLocaleString('pt-BR')}</dd>
        </dl>
      </div>
    </section>
  )
}
