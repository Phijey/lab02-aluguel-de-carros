# Histórico de versões dos modelos UML

Os modelos ficam em pastas por versão para dar para comparar o que foi pensado no início com o que foi implementado.

| Versão | Pasta | Tag do repositório | Momento |
|---|---|---|---|
| v1 | [`v1/`](v1/) | `v0.1.0` | Modelagem inicial, antes do código |
| v2 | [`v2/`](v2/) | `v1.0.0` | Revisão após a implementação (modelo alinhado com o código) |

Cada pasta tem os fontes `.puml` e as imagens `.png` geradas a partir deles. Na tag `v1.0.2` os PNGs foram adicionados e o diagrama de pacotes das duas versões teve um ajuste de notação (nomes dos pacotes exibidos corretamente), sem mudança de conteúdo.

## v1 → v2

### Diagrama de classes
- **Automovel** ganhou o atributo `valorDiaria`. Sem ele não tinha como calcular o valor estimado do pedido.
- **Automovel** ganhou o método `registrarPropriedade()`, usado quando o contrato define se o carro fica com o cliente, a empresa ou o banco.
- **PedidoAluguel**
  - novos atributos `atualizadoEm` e `agenteAvaliador` (antes era só uma associação sem nome);
  - os métodos foram detalhados conforme as regras de status: `alterarPeloCliente()`, `iniciarAnalise()`, `alterarPeloAgente()`, `avaliar()`, `marcarContratado()`, `pertenceA()` e `dias()`.
- **Contrato** passou a guardar o **agente** que executou o contrato.
- **ContratoCredito** ganhou o método `valorParcela()` (tabela Price) e a associação com o banco foi nomeada `banco`.
- **Cliente** ganhou a constante `MAX_RENDIMENTOS = 3` e o método `substituirRendimentos()`, usado na edição do cadastro.
- **StatusPedido** ganhou `podeSerCancelado()`.
- A multiplicidade de **Endereco** mudou de `1` para `0..1`, porque o endereço é opcional no cadastro.
- O relacionamento Pedido → Cliente passou a ser navegável a partir do pedido, como está no JPA.

### Diagrama de pacotes
- Entraram os pacotes `exception`, `config`, `security` e `db.migration` (Flyway), que surgiram na implementação.
- No frontend entraram `utils` e `context` (estado de autenticação).
- Ficou explícito que o frontend fala com o backend por HTTP/JSON com token JWT.

### Casos de uso
- Novo caso **Iniciar análise do pedido**: o pedido passa a ter o status `EM_ANALISE` antes do parecer.
- Novo caso **Consultar contratos** (cliente e agentes).
- Novo caso incluído **Verificar disponibilidade do automóvel**, para impedir duas reservas aprovadas do mesmo carro no mesmo período.
- O include "pedido → login" saiu do diagrama, porque o login é exigido em todas as telas pelo Spring Security.

### Novo diagrama
- `v2/estados-pedido.puml`: diagrama de estados do pedido, documentando as transições validadas no código.

### Banco de dados
O schema também é versionado, pelas migrações do Flyway em `backend/src/main/resources/db/migration`:
- `V1__usuarios.sql`: usuários, clientes, rendimentos e agentes
- `V2__automoveis_pedidos.sql`: automóveis e pedidos
- `V3__avaliacao_contratos.sql`: avaliação dos pedidos, contratos e crédito
