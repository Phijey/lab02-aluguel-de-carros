import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { listarPedidos, meusPedidos } from '../services/pedidoService'

export default function Inicio() {
  const { usuario, isAgente } = useAuth()
  const [pedidos, setPedidos] = useState([])

  useEffect(() => {
    (isAgente ? listarPedidos() : meusPedidos()).then(setPedidos).catch(() => {})
  }, [isAgente])

  const conta = s => pedidos.filter(p => p.status === s).length
  const destino = s => isAgente ? `/analise?status=${s}` : '/pedidos'

  return (
    <section>
      <div className="cabecalho">
        <div>
          <h2>Olá, {usuario.nome}!</h2>
          <p className="sub">
            {isAgente
              ? 'Aqui você acompanha e avalia os pedidos de aluguel dos clientes.'
              : 'Aqui você faz e acompanha seus pedidos de aluguel.'}
          </p>
        </div>
        {!isAgente && <Link to="/pedidos/novo" className="btn">+ Novo pedido</Link>}
      </div>

      <div className="cards">
        {['PENDENTE', 'EM_ANALISE', 'APROVADO', 'CONTRATADO', 'REPROVADO'].map(s => (
          <Link key={s} to={destino(s)} className="card-num">
            <strong>{conta(s)}</strong>
            {s === 'EM_ANALISE' ? 'Em análise' : s.charAt(0) + s.slice(1).toLowerCase()}
          </Link>
        ))}
      </div>
    </section>
  )
}
