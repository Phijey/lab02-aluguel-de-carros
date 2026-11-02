const nomes = {
  PENDENTE: 'Pendente',
  EM_ANALISE: 'Em análise',
  APROVADO: 'Aprovado',
  REPROVADO: 'Reprovado',
  CANCELADO: 'Cancelado',
  CONTRATADO: 'Contratado'
}

export default function StatusBadge({ status }) {
  return <span className={`badge ${status}`}>{nomes[status] || status}</span>
}
