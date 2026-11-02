import { moeda } from '../utils/erro'

export default function DadosCliente({ cliente }) {
  const e = cliente.endereco || {}
  const total = cliente.rendimentos.reduce((s, r) => s + Number(r.valor), 0)

  return (
    <div className="grade info">
      <dl>
        <dt>Nome</dt><dd>{cliente.nome}</dd>
        <dt>CPF</dt><dd>{cliente.cpf}</dd>
        <dt>RG</dt><dd>{cliente.rg}</dd>
        <dt>Profissão</dt><dd>{cliente.profissao || '-'}</dd>
      </dl>
      <dl>
        <dt>Endereço</dt>
        <dd>
          {e.logradouro ? `${e.logradouro}, ${e.numero || 's/n'} - ${e.bairro || ''}` : '-'}<br />
          {e.cidade && `${e.cidade}/${e.uf || ''} ${e.cep || ''}`}
        </dd>
      </dl>
      <dl>
        <dt>Rendimentos</dt>
        {cliente.rendimentos.length === 0 && <dd>Nenhum informado</dd>}
        {cliente.rendimentos.map((r, i) => <dd key={i}>{r.entidadeEmpregadora}: {moeda(r.valor)}</dd>)}
        <dt>Renda total</dt><dd><strong>{moeda(total)}</strong></dd>
      </dl>
    </div>
  )
}
