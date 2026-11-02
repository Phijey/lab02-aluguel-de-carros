import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import ClienteForm from '../components/ClienteForm'
import { registrarAgente, registrarCliente } from '../services/authService'
import { mensagemErro } from '../utils/erro'

export default function Cadastro() {
  const navigate = useNavigate()
  const [tipo, setTipo] = useState('CLIENTE')
  const [erro, setErro] = useState('')
  const [agente, setAgente] = useState({ login: '', senha: '', nome: '', cnpj: '', tipo: 'EMPRESA' })

  async function salvarCliente(dados) {
    setErro('')
    try {
      await registrarCliente(dados)
      navigate('/login', { state: { cadastrado: true } })
    } catch (err) {
      setErro(mensagemErro(err))
      window.scrollTo(0, 0)
    }
  }

  async function salvarAgente(e) {
    e.preventDefault()
    setErro('')
    try {
      await registrarAgente(agente)
      navigate('/login', { state: { cadastrado: true } })
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  const campoAgente = e => setAgente({ ...agente, [e.target.name]: e.target.value })

  return (
    <div className="tela-auth">
      <div className="cartao largo">
        <h1>Criar conta</h1>
        <div className="abas">
          <button className={tipo === 'CLIENTE' ? 'ativa' : ''} onClick={() => setTipo('CLIENTE')}>Sou cliente</button>
          <button className={tipo === 'AGENTE' ? 'ativa' : ''} onClick={() => setTipo('AGENTE')}>Sou agente (empresa/banco)</button>
        </div>

        {erro && <div className="alerta erro">{erro}</div>}

        {tipo === 'CLIENTE'
          ? <ClienteForm onSubmit={salvarCliente} textoBotao="Cadastrar" />
          : (
            <form className="form" onSubmit={salvarAgente}>
              <div className="linha">
                <label>Usuário<input name="login" value={agente.login} onChange={campoAgente} required /></label>
                <label>Senha<input type="password" name="senha" minLength={6} value={agente.senha} onChange={campoAgente} required /></label>
              </div>
              <label>Razão social<input name="nome" value={agente.nome} onChange={campoAgente} required /></label>
              <div className="linha">
                <label>CNPJ<input name="cnpj" value={agente.cnpj} onChange={campoAgente} required placeholder="00.000.000/0000-00" /></label>
                <label>Tipo
                  <select name="tipo" value={agente.tipo} onChange={campoAgente}>
                    <option value="EMPRESA">Empresa</option>
                    <option value="BANCO">Banco</option>
                  </select>
                </label>
              </div>
              <button className="btn">Cadastrar</button>
            </form>
          )}

        <p className="rodape-auth">Já tem conta? <Link to="/login">Entrar</Link></p>
      </div>
    </div>
  )
}
