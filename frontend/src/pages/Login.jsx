import { useState } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { mensagemErro } from '../utils/erro'

export default function Login() {
  const { usuario, entrar } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [login, setLogin] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)

  if (usuario) return <Navigate to="/" replace />

  async function handleSubmit(e) {
    e.preventDefault()
    setErro('')
    setCarregando(true)
    try {
      await entrar(login, senha)
      navigate('/')
    } catch (err) {
      setErro(mensagemErro(err, 'Não foi possível entrar'))
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="tela-auth">
      <form className="cartao auth" onSubmit={handleSubmit}>
        <h1>🚗 RodaFácil</h1>
        <p className="sub">Sistema de aluguel de carros</p>

        {location.state?.cadastrado && !erro && <div className="alerta ok">Cadastro realizado! Faça login.</div>}
        {erro && <div className="alerta erro">{erro}</div>}

        <label>Usuário
          <input value={login} onChange={e => setLogin(e.target.value)} required autoFocus />
        </label>
        <label>Senha
          <input type="password" value={senha} onChange={e => setSenha(e.target.value)} required />
        </label>
        <button className="btn" disabled={carregando}>{carregando ? 'Entrando...' : 'Entrar'}</button>

        <p className="rodape-auth">Não tem conta? <Link to="/cadastro">Cadastre-se</Link></p>
      </form>
    </div>
  )
}
