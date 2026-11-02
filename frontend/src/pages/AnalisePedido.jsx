import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import DadosCliente from '../components/DadosCliente'
import StatusBadge from '../components/StatusBadge'
import { useAuth } from '../context/AuthContext'
import { buscarCliente } from '../services/clienteService'
import { executarContrato, listarBancos } from '../services/contratoService'
import { alterarPeloAgente, avaliarPedido, buscarPedido, iniciarAnalise } from '../services/pedidoService'
import { data, mensagemErro, moeda } from '../utils/erro'

export default function AnalisePedido() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { usuario } = useAuth()
  const [pedido, setPedido] = useState(null)
  const [cliente, setCliente] = useState(null)
  const [bancos, setBancos] = useState([])
  const [erro, setErro] = useState('')

  const [editando, setEditando] = useState(false)
  const [edicao, setEdicao] = useState({})
  const [parecer, setParecer] = useState('')
  const [contrato, setContrato] = useState({ tipoProprietario: 'EMPRESA', comCredito: false, bancoId: '', valor: '', taxaJuros: '1.5', numeroParcelas: '12' })

  useEffect(() => {
    buscarPedido(id).then(p => {
      setPedido(p)
      buscarCliente(p.clienteId).then(setCliente)
    }).catch(err => setErro(mensagemErro(err)))
    listarBancos().then(setBancos)
  }, [id])

  async function executar(acao) {
    setErro('')
    try {
      setPedido(await acao())
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  function abrirEdicao() {
    setEdicao({ dataInicio: pedido.dataInicio, dataFim: pedido.dataFim, valorEstimado: pedido.valorEstimado })
    setEditando(true)
  }

  async function salvarEdicao(e) {
    e.preventDefault()
    await executar(() => alterarPeloAgente(id, { ...edicao, valorEstimado: Number(edicao.valorEstimado) }))
    setEditando(false)
  }

  async function avaliar(aprovado) {
    if (!parecer.trim()) { setErro('Escreva o parecer financeiro antes de avaliar'); return }
    await executar(() => avaliarPedido(id, aprovado, parecer))
  }

  async function gerarContrato(e) {
    e.preventDefault()
    setErro('')
    const dados = { pedidoId: Number(id), tipoProprietario: contrato.tipoProprietario }
    if (contrato.comCredito) {
      dados.credito = {
        bancoId: contrato.bancoId ? Number(contrato.bancoId) : null,
        valor: Number(contrato.valor || pedido.valorEstimado),
        taxaJuros: Number(contrato.taxaJuros),
        numeroParcelas: Number(contrato.numeroParcelas)
      }
    }
    try {
      const c = await executarContrato(dados)
      navigate(`/contratos/${c.id}`)
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  if (!pedido) return erro ? <div className="alerta erro">{erro}</div> : <p>Carregando...</p>
  const s = pedido.status
  const ehBanco = usuario.perfil === 'BANCO'

  return (
    <section>
      <div className="cabecalho">
        <h2>Pedido #{pedido.id} <StatusBadge status={s} /></h2>
        <div className="acoes">
          {s === 'PENDENTE' && <button className="btn" onClick={() => executar(() => iniciarAnalise(id))}>Iniciar análise</button>}
          {(s === 'PENDENTE' || s === 'EM_ANALISE') && !editando && <button className="btn-secundario" onClick={abrirEdicao}>Modificar</button>}
          <Link to="/analise" className="btn-secundario">Voltar</Link>
        </div>
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      <div className="cartao">
        <h3>Dados do pedido</h3>
        {editando ? (
          <form className="form" onSubmit={salvarEdicao}>
            <div className="linha">
              <label>Retirada<input type="date" value={edicao.dataInicio} onChange={e => setEdicao({ ...edicao, dataInicio: e.target.value })} /></label>
              <label>Devolução<input type="date" value={edicao.dataFim} onChange={e => setEdicao({ ...edicao, dataFim: e.target.value })} /></label>
              <label>Valor (R$)<input type="number" step="0.01" min="0" value={edicao.valorEstimado} onChange={e => setEdicao({ ...edicao, valorEstimado: e.target.value })} /></label>
            </div>
            <div className="acoes">
              <button className="btn">Salvar</button>
              <button type="button" className="btn-secundario" onClick={() => setEditando(false)}>Cancelar</button>
            </div>
          </form>
        ) : (
          <div className="grade info">
            <dl>
              <dt>Automóvel</dt><dd>{pedido.automovel.marca} {pedido.automovel.modelo} ({pedido.automovel.placa})</dd>
              <dt>Proprietário atual</dt><dd>{pedido.automovel.proprietario || '-'}</dd>
            </dl>
            <dl>
              <dt>Período</dt><dd>{data(pedido.dataInicio)} a {data(pedido.dataFim)} ({pedido.dias} diárias)</dd>
              <dt>Valor</dt><dd>{moeda(pedido.valorEstimado)}</dd>
            </dl>
            <dl>
              <dt>Avaliado por</dt><dd>{pedido.agenteAvaliador || '-'}</dd>
              <dt>Parecer</dt><dd>{pedido.parecer || '-'}</dd>
            </dl>
          </div>
        )}
      </div>

      {cliente && (
        <div className="cartao">
          <h3>Cliente</h3>
          <DadosCliente cliente={cliente} />
        </div>
      )}

      {s === 'EM_ANALISE' && (
        <div className="cartao form">
          <h3>Análise financeira</h3>
          <label>Parecer
            <textarea rows="3" value={parecer} onChange={e => setParecer(e.target.value)} placeholder="Ex.: renda compatível com o valor do aluguel" />
          </label>
          <div className="acoes">
            <button className="btn-sucesso" onClick={() => avaliar(true)}>Aprovar</button>
            <button className="btn-perigo" onClick={() => avaliar(false)}>Reprovar</button>
          </div>
        </div>
      )}

      {s === 'APROVADO' && (
        <form className="cartao form" onSubmit={gerarContrato}>
          <h3>Executar contrato</h3>
          <label>Automóvel será registrado em nome de
            <select value={contrato.tipoProprietario} onChange={e => setContrato({ ...contrato, tipoProprietario: e.target.value })}>
              <option value="EMPRESA">Empresa</option>
              <option value="CLIENTE">Cliente</option>
              <option value="BANCO">Banco (exige crédito)</option>
            </select>
          </label>
          <label style={{ flexDirection: 'row', alignItems: 'center' }}>
            <input type="checkbox" checked={contrato.comCredito} onChange={e => setContrato({ ...contrato, comCredito: e.target.checked })} />
            Associar contrato de crédito
          </label>
          {contrato.comCredito && (
            <div className="linha">
              {!ehBanco &&
                <label>Banco
                  <select value={contrato.bancoId} onChange={e => setContrato({ ...contrato, bancoId: e.target.value })} required>
                    <option value="">Selecione...</option>
                    {bancos.map(b => <option key={b.id} value={b.id}>{b.nome}</option>)}
                  </select>
                </label>}
              <label>Valor (R$)
                <input type="number" step="0.01" min="0" placeholder={pedido.valorEstimado} value={contrato.valor} onChange={e => setContrato({ ...contrato, valor: e.target.value })} />
              </label>
              <label>Juros (% a.m.)
                <input type="number" step="0.01" min="0" value={contrato.taxaJuros} onChange={e => setContrato({ ...contrato, taxaJuros: e.target.value })} />
              </label>
              <label>Parcelas
                <input type="number" min="1" value={contrato.numeroParcelas} onChange={e => setContrato({ ...contrato, numeroParcelas: e.target.value })} />
              </label>
            </div>
          )}
          <button className="btn">Gerar contrato</button>
        </form>
      )}
    </section>
  )
}
