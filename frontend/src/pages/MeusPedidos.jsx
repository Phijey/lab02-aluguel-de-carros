import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import StatusBadge from '../components/StatusBadge'
import { meusPedidos } from '../services/pedidoService'
import { data, mensagemErro, moeda } from '../utils/erro'

export default function MeusPedidos() {
  const [pedidos, setPedidos] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    meusPedidos()
      .then(setPedidos)
      .catch(err => setErro(mensagemErro(err)))
      .finally(() => setCarregando(false))
  }, [])

  return (
    <section>
      <div className="cabecalho">
        <h2>Meus pedidos</h2>
        <Link to="/pedidos/novo" className="btn">+ Novo pedido</Link>
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <table>
        <thead>
          <tr><th>#</th><th>Automóvel</th><th>Período</th><th>Valor estimado</th><th>Status</th><th></th></tr>
        </thead>
        <tbody>
          {pedidos.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.automovel.marca} {p.automovel.modelo} <small>({p.automovel.placa})</small></td>
              <td>{data(p.dataInicio)} a {data(p.dataFim)}</td>
              <td>{moeda(p.valorEstimado)}</td>
              <td><StatusBadge status={p.status} /></td>
              <td><Link to={`/pedidos/${p.id}`}>Detalhes</Link></td>
            </tr>
          ))}
          {!carregando && pedidos.length === 0 &&
            <tr><td colSpan="6" className="vazio">Você ainda não fez nenhum pedido</td></tr>}
        </tbody>
      </table>
    </section>
  )
}
