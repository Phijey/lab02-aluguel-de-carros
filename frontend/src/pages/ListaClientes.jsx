import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarClientes } from '../services/clienteService'
import { mensagemErro, moeda } from '../utils/erro'

export default function ListaClientes() {
  const [clientes, setClientes] = useState([])
  const [busca, setBusca] = useState('')
  const [erro, setErro] = useState('')

  useEffect(() => {
    listarClientes().then(setClientes).catch(err => setErro(mensagemErro(err)))
  }, [])

  const filtrados = clientes.filter(c =>
    c.nome.toLowerCase().includes(busca.toLowerCase()) || c.cpf.includes(busca.replace(/\D/g, '') || '#'))

  return (
    <section>
      <div className="cabecalho">
        <h2>Clientes</h2>
        <input placeholder="Buscar por nome ou CPF" value={busca} onChange={e => setBusca(e.target.value)} />
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <table>
        <thead>
          <tr><th>Nome</th><th>CPF</th><th>Profissão</th><th>Renda total</th><th></th></tr>
        </thead>
        <tbody>
          {filtrados.map(c => (
            <tr key={c.id}>
              <td>{c.nome}</td>
              <td>{c.cpf}</td>
              <td>{c.profissao || '-'}</td>
              <td>{moeda(c.rendimentos.reduce((s, r) => s + Number(r.valor), 0))}</td>
              <td><Link to={`/clientes/${c.id}`}>Ver</Link></td>
            </tr>
          ))}
          {filtrados.length === 0 && <tr><td colSpan="5" className="vazio">Nenhum cliente encontrado</td></tr>}
        </tbody>
      </table>
    </section>
  )
}
