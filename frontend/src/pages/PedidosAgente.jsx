import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import StatusBadge from '../components/StatusBadge'
import { listarPedidos } from '../services/pedidoService'
import { data, mensagemErro, moeda } from '../utils/erro'

const filtros = ['', 'PENDENTE', 'EM_ANALISE', 'APROVADO', 'REPROVADO', 'CONTRATADO', 'CANCELADO']

export default function PedidosAgente() {
  const [params, setParams] = useSearchParams()
  const status = params.get('status') || ''
  const [pedidos, setPedidos] = useState([])
  const [erro, setErro] = useState('')

  useEffect(() => {
    listarPedidos(status).then(setPedidos).catch(err => setErro(mensagemErro(err)))
  }, [status])

  return (
    <section>
      <div className="cabecalho">
        <h2>Pedidos de aluguel</h2>
        <select value={status} onChange={e => setParams(e.target.value ? { status: e.target.value } : {})}>
          {filtros.map(f => <option key={f} value={f}>{f ? f.replace('_', ' ') : 'Todos os status'}</option>)}
        </select>
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <table>
        <thead>
          <tr><th>#</th><th>Cliente</th><th>Automóvel</th><th>Período</th><th>Valor</th><th>Status</th><th></th></tr>
        </thead>
        <tbody>
          {pedidos.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.clienteNome}</td>
              <td>{p.automovel.marca} {p.automovel.modelo}</td>
              <td>{data(p.dataInicio)} a {data(p.dataFim)}</td>
              <td>{moeda(p.valorEstimado)}</td>
              <td><StatusBadge status={p.status} /></td>
              <td><Link to={`/analise/${p.id}`}>Abrir</Link></td>
            </tr>
          ))}
          {pedidos.length === 0 && <tr><td colSpan="7" className="vazio">Nenhum pedido encontrado</td></tr>}
        </tbody>
      </table>
    </section>
  )
}
