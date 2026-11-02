import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarContratos } from '../services/contratoService'
import { data, mensagemErro, moeda } from '../utils/erro'

export default function Contratos() {
  const [contratos, setContratos] = useState([])
  const [erro, setErro] = useState('')

  useEffect(() => {
    listarContratos().then(setContratos).catch(err => setErro(mensagemErro(err)))
  }, [])

  return (
    <section>
      <h2>Contratos</h2>
      {erro && <div className="alerta erro">{erro}</div>}
      <table>
        <thead>
          <tr><th>#</th><th>Cliente</th><th>Automóvel</th><th>Assinado em</th><th>Valor</th><th>Crédito</th><th></th></tr>
        </thead>
        <tbody>
          {contratos.map(c => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.pedido.clienteNome}</td>
              <td>{c.pedido.automovel.marca} {c.pedido.automovel.modelo}</td>
              <td>{data(c.dataAssinatura)}</td>
              <td>{moeda(c.pedido.valorEstimado)}</td>
              <td>{c.credito ? `${c.credito.banco} (${c.credito.numeroParcelas}x)` : 'Não'}</td>
              <td><Link to={`/contratos/${c.id}`}>Ver</Link></td>
            </tr>
          ))}
          {contratos.length === 0 && <tr><td colSpan="7" className="vazio">Nenhum contrato ainda</td></tr>}
        </tbody>
      </table>
    </section>
  )
}
