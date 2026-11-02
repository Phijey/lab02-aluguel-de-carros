import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import DadosCliente from '../components/DadosCliente'
import { buscarCliente } from '../services/clienteService'
import { mensagemErro } from '../utils/erro'

export default function DetalheCliente() {
  const { id } = useParams()
  const [cliente, setCliente] = useState(null)
  const [erro, setErro] = useState('')

  useEffect(() => {
    buscarCliente(id).then(setCliente).catch(err => setErro(mensagemErro(err)))
  }, [id])

  if (erro) return <div className="alerta erro">{erro}</div>
  if (!cliente) return <p>Carregando...</p>

  return (
    <section>
      <div className="cabecalho">
        <h2>{cliente.nome}</h2>
        <Link to="/clientes" className="btn-secundario">Voltar</Link>
      </div>
      <div className="cartao"><DadosCliente cliente={cliente} /></div>
    </section>
  )
}
