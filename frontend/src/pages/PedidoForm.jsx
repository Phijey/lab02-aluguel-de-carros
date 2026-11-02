import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { listarAutomoveis } from '../services/automovelService'
import { alterarPedido, buscarPedido, criarPedido } from '../services/pedidoService'
import { mensagemErro, moeda } from '../utils/erro'

const hoje = () => new Date().toISOString().slice(0, 10)

export default function PedidoForm() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [automoveis, setAutomoveis] = useState([])
  const [form, setForm] = useState({ automovelId: '', dataInicio: hoje(), dataFim: '' })
  const [erro, setErro] = useState('')

  useEffect(() => {
    listarAutomoveis().then(setAutomoveis).catch(err => setErro(mensagemErro(err)))
    if (id) {
      buscarPedido(id).then(p => setForm({ automovelId: p.automovel.id, dataInicio: p.dataInicio, dataFim: p.dataFim }))
    }
  }, [id])

  const carro = automoveis.find(a => a.id === Number(form.automovelId))
  let dias = 0
  if (form.dataInicio && form.dataFim) {
    dias = Math.max(1, Math.round((new Date(form.dataFim) - new Date(form.dataInicio)) / 86400000))
  }

  async function salvar(e) {
    e.preventDefault()
    setErro('')
    try {
      const dados = { ...form, automovelId: Number(form.automovelId) }
      const p = id ? await alterarPedido(id, dados) : await criarPedido(dados)
      navigate(`/pedidos/${p.id}`)
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  return (
    <section>
      <div className="cabecalho">
        <h2>{id ? `Modificar pedido #${id}` : 'Novo pedido de aluguel'}</h2>
        <Link to={id ? `/pedidos/${id}` : '/pedidos'} className="btn-secundario">Voltar</Link>
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <form className="cartao form" onSubmit={salvar}>
        <label>Automóvel
          <select value={form.automovelId} onChange={e => setForm({ ...form, automovelId: e.target.value })} required>
            <option value="">Selecione...</option>
            {automoveis.map(a => (
              <option key={a.id} value={a.id}>
                {a.marca} {a.modelo} {a.ano} - {a.placa} ({moeda(a.valorDiaria)}/dia)
              </option>
            ))}
          </select>
        </label>
        <div className="linha">
          <label>Retirada
            <input type="date" min={hoje()} value={form.dataInicio} onChange={e => setForm({ ...form, dataInicio: e.target.value })} required />
          </label>
          <label>Devolução
            <input type="date" min={form.dataInicio} value={form.dataFim} onChange={e => setForm({ ...form, dataFim: e.target.value })} required />
          </label>
        </div>

        {carro && dias > 0 &&
          <div className="alerta ok">
            {dias} diária(s) x {moeda(carro.valorDiaria)} = <strong>{moeda(dias * carro.valorDiaria)}</strong>
          </div>}

        <button className="btn">{id ? 'Salvar alterações' : 'Enviar pedido'}</button>
      </form>
    </section>
  )
}
