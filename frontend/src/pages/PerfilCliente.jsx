import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import ClienteForm from '../components/ClienteForm'
import DadosCliente from '../components/DadosCliente'
import { useAuth } from '../context/AuthContext'
import { atualizarMeusDados, excluirMinhaConta, meusDados } from '../services/clienteService'
import { mensagemErro } from '../utils/erro'

export default function PerfilCliente() {
  const { sair } = useAuth()
  const navigate = useNavigate()
  const [cliente, setCliente] = useState(null)
  const [editando, setEditando] = useState(false)
  const [erro, setErro] = useState('')
  const [ok, setOk] = useState('')

  useEffect(() => {
    meusDados().then(setCliente).catch(err => setErro(mensagemErro(err)))
  }, [])

  async function salvar(dados) {
    setErro(''); setOk('')
    try {
      const atualizado = await atualizarMeusDados(dados)
      setCliente(atualizado)
      setEditando(false)
      setOk('Dados atualizados com sucesso')
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  async function excluir() {
    if (!confirm('Tem certeza que deseja excluir sua conta? Essa ação não pode ser desfeita.')) return
    try {
      await excluirMinhaConta()
      sair()
      navigate('/login')
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  if (!cliente) return erro ? <div className="alerta erro">{erro}</div> : <p>Carregando...</p>

  return (
    <section>
      <div className="cabecalho">
        <h2>Meus dados</h2>
        {!editando &&
          <div className="acoes">
            <button className="btn" onClick={() => setEditando(true)}>Editar</button>
            <button className="btn-perigo" onClick={excluir}>Excluir conta</button>
          </div>}
      </div>

      {erro && <div className="alerta erro">{erro}</div>}
      {ok && <div className="alerta ok">{ok}</div>}

      <div className="cartao">
        {editando
          ? <>
              <ClienteForm inicial={cliente} onSubmit={salvar} senhaObrigatoria={false} bloquearLogin textoBotao="Salvar alterações" />
              <button className="btn-secundario" style={{ marginTop: '.8rem' }} onClick={() => setEditando(false)}>Cancelar</button>
            </>
          : <DadosCliente cliente={cliente} />}
      </div>
    </section>
  )
}
