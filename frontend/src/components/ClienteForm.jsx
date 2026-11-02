import { useState } from 'react'

const MAX_RENDIMENTOS = 3

const vazio = {
  login: '', senha: '', nome: '', rg: '', cpf: '', profissao: '',
  endereco: { logradouro: '', numero: '', bairro: '', cidade: '', uf: '', cep: '' },
  rendimentos: []
}

export default function ClienteForm({ inicial, onSubmit, senhaObrigatoria = true, textoBotao = 'Salvar', bloquearLogin = false }) {
  const [form, setForm] = useState(() => ({
    ...vazio,
    ...inicial,
    senha: '',
    endereco: { ...vazio.endereco, ...(inicial?.endereco || {}) },
    rendimentos: inicial?.rendimentos || []
  }))
  const [enviando, setEnviando] = useState(false)

  const campo = e => setForm({ ...form, [e.target.name]: e.target.value })
  const campoEndereco = e => setForm({ ...form, endereco: { ...form.endereco, [e.target.name]: e.target.value } })

  function campoRendimento(i, nome, valor) {
    const lista = form.rendimentos.map((r, idx) => idx === i ? { ...r, [nome]: valor } : r)
    setForm({ ...form, rendimentos: lista })
  }

  function addRendimento() {
    if (form.rendimentos.length >= MAX_RENDIMENTOS) return
    setForm({ ...form, rendimentos: [...form.rendimentos, { entidadeEmpregadora: '', valor: '' }] })
  }

  const removerRendimento = i => setForm({ ...form, rendimentos: form.rendimentos.filter((_, idx) => idx !== i) })

  async function enviar(e) {
    e.preventDefault()
    setEnviando(true)
    try {
      await onSubmit({
        ...form,
        senha: form.senha || null,
        rendimentos: form.rendimentos.map(r => ({ ...r, valor: Number(r.valor) }))
      })
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="form" onSubmit={enviar}>
      <fieldset>
        <legend>Acesso</legend>
        <div className="linha">
          <label>Usuário
            <input name="login" value={form.login} onChange={campo} required disabled={bloquearLogin} />
          </label>
          <label>{senhaObrigatoria ? 'Senha' : 'Nova senha (opcional)'}
            <input type="password" name="senha" value={form.senha} onChange={campo}
              required={senhaObrigatoria} minLength={6} />
          </label>
        </div>
      </fieldset>

      <fieldset>
        <legend>Dados pessoais</legend>
        <label>Nome completo
          <input name="nome" value={form.nome} onChange={campo} required />
        </label>
        <div className="linha">
          <label>RG<input name="rg" value={form.rg} onChange={campo} required /></label>
          <label>CPF<input name="cpf" value={form.cpf} onChange={campo} required placeholder="000.000.000-00" /></label>
          <label>Profissão<input name="profissao" value={form.profissao || ''} onChange={campo} /></label>
        </div>
      </fieldset>

      <fieldset>
        <legend>Endereço</legend>
        <div className="linha">
          <label className="grande">Logradouro<input name="logradouro" value={form.endereco.logradouro || ''} onChange={campoEndereco} /></label>
          <label className="pequeno">Número<input name="numero" value={form.endereco.numero || ''} onChange={campoEndereco} /></label>
        </div>
        <div className="linha">
          <label>Bairro<input name="bairro" value={form.endereco.bairro || ''} onChange={campoEndereco} /></label>
          <label>Cidade<input name="cidade" value={form.endereco.cidade || ''} onChange={campoEndereco} /></label>
          <label className="pequeno">UF<input name="uf" maxLength={2} value={form.endereco.uf || ''} onChange={campoEndereco} /></label>
          <label className="pequeno">CEP<input name="cep" value={form.endereco.cep || ''} onChange={campoEndereco} /></label>
        </div>
      </fieldset>

      <fieldset>
        <legend>Empregos e rendimentos ({form.rendimentos.length}/{MAX_RENDIMENTOS})</legend>
        {form.rendimentos.map((r, i) => (
          <div className="linha" key={i}>
            <label className="grande">Entidade empregadora
              <input value={r.entidadeEmpregadora} onChange={e => campoRendimento(i, 'entidadeEmpregadora', e.target.value)} required />
            </label>
            <label>Rendimento (R$)
              <input type="number" min="0" step="0.01" value={r.valor} onChange={e => campoRendimento(i, 'valor', e.target.value)} required />
            </label>
            <button type="button" className="btn-perigo btn-pequeno" onClick={() => removerRendimento(i)}>Remover</button>
          </div>
        ))}
        {form.rendimentos.length < MAX_RENDIMENTOS &&
          <button type="button" className="btn-secundario" onClick={addRendimento}>+ Adicionar rendimento</button>}
      </fieldset>

      <button className="btn" disabled={enviando}>{enviando ? 'Salvando...' : textoBotao}</button>
    </form>
  )
}
