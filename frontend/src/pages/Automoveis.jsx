import { useEffect, useState } from 'react'
import { atualizarAutomovel, criarAutomovel, excluirAutomovel, listarAutomoveis } from '../services/automovelService'
import { mensagemErro, moeda } from '../utils/erro'

const vazio = { matricula: '', ano: new Date().getFullYear(), marca: '', modelo: '', placa: '', valorDiaria: '' }

export default function Automoveis() {
  const [lista, setLista] = useState([])
  const [form, setForm] = useState(vazio)
  const [editandoId, setEditandoId] = useState(null)
  const [aberto, setAberto] = useState(false)
  const [erro, setErro] = useState('')

  const carregar = () => listarAutomoveis().then(setLista).catch(err => setErro(mensagemErro(err)))
  useEffect(() => { carregar() }, [])

  const campo = e => setForm({ ...form, [e.target.name]: e.target.value })

  function novo() {
    setForm(vazio); setEditandoId(null); setAberto(true)
  }

  function editar(a) {
    setForm({ matricula: a.matricula, ano: a.ano, marca: a.marca, modelo: a.modelo, placa: a.placa, valorDiaria: a.valorDiaria })
    setEditandoId(a.id)
    setAberto(true)
  }

  async function salvar(e) {
    e.preventDefault()
    setErro('')
    const dados = { ...form, ano: Number(form.ano), valorDiaria: Number(form.valorDiaria) }
    try {
      if (editandoId) await atualizarAutomovel(editandoId, dados)
      else await criarAutomovel(dados)
      setAberto(false)
      carregar()
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  async function remover(a) {
    if (!confirm(`Excluir ${a.marca} ${a.modelo} (${a.placa})?`)) return
    try {
      await excluirAutomovel(a.id)
      carregar()
    } catch (err) {
      setErro(mensagemErro(err))
    }
  }

  return (
    <section>
      <div className="cabecalho">
        <h2>Automóveis</h2>
        {!aberto && <button className="btn" onClick={novo}>+ Novo automóvel</button>}
      </div>
      {erro && <div className="alerta erro">{erro}</div>}

      {aberto && (
        <form className="cartao form" onSubmit={salvar}>
          <h3>{editandoId ? 'Editar automóvel' : 'Novo automóvel'}</h3>
          <div className="linha">
            <label>Matrícula<input name="matricula" value={form.matricula} onChange={campo} required /></label>
            <label>Placa<input name="placa" value={form.placa} onChange={campo} required maxLength={8} /></label>
            <label className="pequeno">Ano<input type="number" name="ano" value={form.ano} onChange={campo} required /></label>
          </div>
          <div className="linha">
            <label>Marca<input name="marca" value={form.marca} onChange={campo} required /></label>
            <label>Modelo<input name="modelo" value={form.modelo} onChange={campo} required /></label>
            <label>Diária (R$)<input type="number" step="0.01" min="0" name="valorDiaria" value={form.valorDiaria} onChange={campo} required /></label>
          </div>
          <div className="acoes">
            <button className="btn">Salvar</button>
            <button type="button" className="btn-secundario" onClick={() => setAberto(false)}>Cancelar</button>
          </div>
        </form>
      )}

      <table>
        <thead>
          <tr><th>Matrícula</th><th>Placa</th><th>Marca/Modelo</th><th>Ano</th><th>Diária</th><th>Proprietário</th><th></th></tr>
        </thead>
        <tbody>
          {lista.map(a => (
            <tr key={a.id}>
              <td>{a.matricula}</td>
              <td>{a.placa}</td>
              <td>{a.marca} {a.modelo}</td>
              <td>{a.ano}</td>
              <td>{moeda(a.valorDiaria)}</td>
              <td>{a.proprietario || '-'} <small>({a.tipoProprietario?.toLowerCase()})</small></td>
              <td className="acoes">
                <button className="btn-secundario btn-pequeno" onClick={() => editar(a)}>Editar</button>
                <button className="btn-perigo btn-pequeno" onClick={() => remover(a)}>Excluir</button>
              </td>
            </tr>
          ))}
          {lista.length === 0 && <tr><td colSpan="7" className="vazio">Nenhum automóvel cadastrado</td></tr>}
        </tbody>
      </table>
    </section>
  )
}
