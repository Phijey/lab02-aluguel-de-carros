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
            ? <>
                <NavLink to="/analise">Pedidos</NavLink>
                <NavLink to="/clientes">Clientes</NavLink>
                <NavLink to="/automoveis">Automóveis</NavLink>
              </>
            : <>
                <NavLink to="/pedidos">Meus pedidos</NavLink>
                <NavLink to="/perfil">Meus dados</NavLink>
              </>}
          <NavLink to="/contratos">Contratos</NavLink>
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
