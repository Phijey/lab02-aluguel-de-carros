import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout() {
  const { usuario, sair, isAgente } = useAuth()
  const navigate = useNavigate()

  function handleSair() {
    sair()
    navigate('/login')
  }

  return (
    <div className="app">
      <header className="topo">
        <div className="marca">🚗 RodaFácil</div>
        <nav>
          <NavLink to="/" end>Início</NavLink>
          {isAgente
            ? <NavLink to="/clientes">Clientes</NavLink>
            : <NavLink to="/perfil">Meus dados</NavLink>}
        </nav>
        <div className="usuario-topo">
          <span>{usuario.nome} <small>({isAgente ? usuario.perfil.toLowerCase() : 'cliente'})</small></span>
          <button className="btn-link" onClick={handleSair}>Sair</button>
        </div>
      </header>
      <main className="conteudo">
        <Outlet />
      </main>
    </div>
  )
}
