# Histórias de Usuário - Sistema de Aluguel de Carros

## Autenticação

**HU01 - Cadastro de cliente**
Como cliente, quero me cadastrar informando meus dados pessoais, para poder usar o sistema.
- Deve pedir login, senha, nome, RG, CPF, endereço e profissão
- Pode informar até 3 entidades empregadoras com o rendimento de cada uma
- Não pode existir dois clientes com o mesmo CPF ou login

**HU02 - Cadastro de agente**
Como empresa ou banco, quero me cadastrar como agente, para poder avaliar pedidos de aluguel.
- Deve pedir login, senha, nome, CNPJ e o tipo (empresa ou banco)

**HU03 - Login**
Como usuário cadastrado, quero entrar com usuário e senha, para acessar as funcionalidades do meu perfil.
- Sem login nenhuma tela além de login/cadastro pode ser acessada
- Usuário ou senha errados mostram mensagem de erro

## Cliente

**HU04 - Manter meus dados**
Como cliente, quero consultar, alterar e excluir meus dados cadastrais, para manter meu cadastro atualizado.
- O limite de 3 rendimentos continua valendo na edição

**HU05 - Introduzir pedido**
Como cliente, quero criar um pedido de aluguel escolhendo um automóvel e o período, para alugar um carro.
- O pedido nasce com status PENDENTE
- A data de fim não pode ser antes da data de início

**HU06 - Consultar pedidos**
Como cliente, quero ver a lista dos meus pedidos e o status de cada um, para acompanhar a análise.

**HU07 - Modificar pedido**
Como cliente, quero alterar um pedido que ainda não foi avaliado, para corrigir período ou automóvel.
- Só é possível com status PENDENTE

**HU08 - Cancelar pedido**
Como cliente, quero cancelar um pedido, para desistir do aluguel.
- Só é possível antes da aprovação (PENDENTE ou EM_ANALISE)

## Agente (empresa / banco)

**HU09 - Avaliar pedido**
Como agente, quero fazer a análise financeira de um pedido e dar um parecer, para aprovar ou reprovar o aluguel.
- Ao abrir para análise o pedido passa para EM_ANALISE
- O parecer final muda o status para APROVADO ou REPROVADO
- Posso ver os dados e rendimentos do cliente durante a análise

**HU10 - Modificar pedido (agente)**
Como agente, quero ajustar o valor e o período de um pedido em análise, para adequar à política da empresa.

**HU11 - Executar contrato**
Como agente, quero executar o contrato de um pedido aprovado, para efetivar o aluguel.
- O pedido passa para CONTRATADO
- Deve ser informado em nome de quem o automóvel fica registrado (cliente, empresa ou banco)

**HU12 - Contrato de crédito**
Como banco, quero associar um contrato de crédito ao aluguel, para financiar o cliente.
- Informar valor, taxa de juros e número de parcelas
- Só bancos podem conceder crédito

**HU13 - Manter automóveis**
Como agente, quero cadastrar, alterar e remover automóveis, para que fiquem disponíveis nos pedidos.
- Dados: matrícula, ano, marca, modelo e placa
- Placa e matrícula são únicas

**HU14 - Consultar clientes**
Como agente, quero consultar os clientes cadastrados, para analisar os dados deles.
