import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { buscarContrato } from '../services/contratoService'
import { data, mensagemErro, moeda } from '../utils/erro'

const donos = { CLIENTE: 'Cliente', EMPRESA: 'Empresa', BANCO: 'Banco' }

export default function DetalheContrato() {
  const { id } = useParams()
  const [c, setC] = useState(null)
  const [erro, setErro] = useState('')

  useEffect(() => {
    buscarContrato(id).then(setC).catch(err => setErro(mensagemErro(err)))
  }, [id])

  if (erro) return <div className="alerta erro">{erro}</div>
  if (!c) return <p>Carregando...</p>
  const p = c.pedido

  return (
    <section>
      <div className="cabecalho">
        <h2>Contrato #{c.id}</h2>
        <Link to="/contratos" className="btn-secundario">Voltar</Link>
      </div>

      <div className="cartao grade info">
        <dl>
          <dt>Cliente</dt><dd>{p.clienteNome}</dd>
          <dt>Pedido</dt><dd>#{p.id}</dd>
          <dt>Executado por</dt><dd>{c.agente}</dd>
          <dt>Data de assinatura</dt><dd>{data(c.dataAssinatura)}</dd>
        </dl>
        <dl>
          <dt>Automóvel</dt><dd>{p.automovel.marca} {p.automovel.modelo} ({p.automovel.placa})</dd>
          <dt>Período</dt><dd>{data(p.dataInicio)} a {data(p.dataFim)}</dd>
          <dt>Valor do aluguel</dt><dd>{moeda(p.valorEstimado)}</dd>
          <dt>Registrado em nome de</dt><dd>{donos[c.tipoProprietario]} - {p.automovel.proprietario}</dd>
        </dl>
        <dl>
          <dt>Contrato de crédito</dt>
          {c.credito ? <>
            <dd>{c.credito.banco}</dd>
            <dt>Valor financiado</dt><dd>{moeda(c.credito.valor)}</dd>
            <dt>Condições</dt><dd>{c.credito.numeroParcelas}x de {moeda(c.credito.valorParcela)} ({c.credito.taxaJuros}% a.m.)</dd>
          </> : <dd>Sem crédito associado</dd>}
        </dl>
      </div>
    </section>
  )
}
