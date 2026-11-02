export function mensagemErro(err, padrao = 'Ocorreu um erro, tente novamente') {
  const data = err?.response?.data
  if (!data) return padrao
  if (data.campos) {
    const detalhes = Object.entries(data.campos).map(([c, m]) => `${c}: ${m}`).join(' | ')
    return `${data.mensagem} - ${detalhes}`
  }
  return data.mensagem || padrao
}

export const moeda = v => Number(v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })

export const data = d => d ? new Date(d + (d.length === 10 ? 'T00:00:00' : '')).toLocaleDateString('pt-BR') : '-'
