import { useAuth } from '../context/AuthContext'

export default function Inicio() {
  const { usuario, isAgente } = useAuth()

  return (
    <section>
      <h2>Olá, {usuario.nome}!</h2>
      <p className="sub">
        {isAgente
          ? 'Aqui você acompanha e avalia os pedidos de aluguel dos clientes.'
          : 'Aqui você faz e acompanha seus pedidos de aluguel.'}
      </p>
    </section>
  )
}
