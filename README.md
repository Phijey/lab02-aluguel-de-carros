# 🚗 RodaFácil - Sistema de Aluguel de Carros

> [!NOTE]
> Sistema web para gestão de aluguéis de automóveis. Clientes fazem, modificam, consultam e cancelam pedidos pela internet; agentes (empresas e bancos) analisam os pedidos financeiramente, executam os contratos e podem conceder crédito.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Projeto desenvolvido para o <b>Laboratório 02</b> da disciplina <i>Desenvolvimento e Integração de Aplicações Web (DIAW)</i> do curso de Engenharia de Software da <b>PUC Minas</b>. O sistema só pode ser usado após cadastro e login. O <b>cliente</b> informa seus dados pessoais e até 3 rendimentos e faz pedidos de aluguel. O <b>agente</b> (empresa ou banco) faz a análise financeira e dá o parecer. Pedidos aprovados viram <b>contratos</b>, que podem ter um <b>contrato de crédito</b> concedido por um banco. Dependendo do contrato, o automóvel fica registrado em nome do cliente, da empresa ou do banco.
      </div>
    </td>
    <td>
      <div>
        <img src="frontend/public/favicon.svg" alt="Logo do Projeto" width="120px"/>
      </div>
    </td>
  </tr>
</table>

---

## 🚧 Status do Projeto

![Versão](https://img.shields.io/github/v/tag/Phijey/lab02-aluguel-de-carros?label=vers%C3%A3o)
![Java](https://img.shields.io/badge/Java-25-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.9-6DB33F?logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-8-646CFF?logo=vite&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3FCF8E?logo=supabase&logoColor=white)

✅ Concluído - versão `v1.0.0`

---

## 📚 Índice
- [Links Úteis](#-links-úteis)
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Modelagem e Versões](#-modelagem-e-versões)
- [Instalação e Execução](#-instalação-e-execução)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Endpoints da API](#-endpoints-da-api)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)

---

## 🔗 Links Úteis
- 📄 [Histórias de usuário](docs/historias-de-usuario.md)
- 📐 [Diagramas UML (v1 e v2)](docs/uml)
- 📝 [Histórico de mudanças dos modelos](docs/uml/CHANGELOG.md)

---

## 📝 Sobre o Projeto

O sistema foi dividido nos dois subsistemas pedidos na especificação:

1. **Gestão de pedidos e contratos**: API REST em Spring Boot (arquitetura MVC em camadas) com as regras de negócio, a autenticação e a persistência no Supabase.
2. **Construção dinâmica das páginas web**: SPA em React + Vite que consome a API e monta as telas conforme o perfil de quem está logado.

**Fluxo de um pedido:**

```
PENDENTE ──► EM_ANALISE ──► APROVADO ──► CONTRATADO
   │              │    └──► REPROVADO
   └──────────────┴──► CANCELADO (pelo cliente)
```

---

## ✨ Funcionalidades Principais

**Autenticação**
- 🔐 Cadastro de clientes e agentes (empresa ou banco)
- 🔑 Login com usuário e senha (senha com BCrypt, sessão com token JWT)
- 🚫 Nenhuma rota acessível sem login; cada rota é liberada conforme o perfil

**Cliente**
- 👤 CRUD dos próprios dados (RG, CPF, nome, endereço, profissão e até 3 empregadores/rendimentos)
- ➕ Introduzir pedido de aluguel (cálculo automático do valor pelas diárias)
- ✏️ Modificar pedido enquanto estiver pendente
- 👀 Consultar pedidos e acompanhar o status
- ❌ Cancelar pedido antes da aprovação
- 📃 Consultar os próprios contratos

**Agente (empresa / banco)**
- 📋 Listar pedidos com filtro por status
- 🔎 Iniciar a análise e ver os dados e a renda do cliente
- ✏️ Modificar período e valor de pedidos em análise
- ✅ Aprovar ou reprovar com parecer financeiro
- 📝 Executar o contrato e definir em nome de quem o automóvel fica registrado
- 🏦 Associar contrato de crédito (valor, juros, parcelas, com cálculo da parcela)
- 🚘 CRUD de automóveis (matrícula, ano, marca, modelo, placa)
- 👥 Consultar clientes

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end
- React 19
- Vite 8
- React Router
- Axios

### 🖥️ Back-end
- Java 25 (JDK 25)
- Spring Boot 4.0.9 (Web MVC, Data JPA, Validation)
- Spring Security + OAuth2 Resource Server (JWT HS256)
- Flyway (versionamento do banco)
- Apache Maven
- JUnit 5, MockMvc e H2 (testes)

### ⚙️ Infraestrutura
- Supabase (PostgreSQL gerenciado, conexão via Session Pooler)

---

## 🏗 Arquitetura

Arquitetura **MVC em camadas**:

- **Model**: entidades JPA em `model` com as regras de domínio (transições de status, limite de rendimentos, cálculo de valores)
- **View**: frontend React em `frontend/src/pages` e `components`
- **Controller**: `controller` expõe a API REST; `service` orquestra as regras; `repository` acessa o banco

```
React (View) ──HTTP/JSON + JWT──► Controller ──► Service ──► Repository ──► Supabase (PostgreSQL)
                                       │            │
                                      DTO         Model
```

### Diagrama de Pacotes (v2)
![Diagrama de pacotes](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/Phijey/lab02-aluguel-de-carros/main/docs/uml/v2/pacotes.puml)

### Diagrama de Classes (v2)
![Diagrama de classes](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/Phijey/lab02-aluguel-de-carros/main/docs/uml/v2/classes.puml)

### Diagrama de Casos de Uso (v2)
![Diagrama de casos de uso](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/Phijey/lab02-aluguel-de-carros/main/docs/uml/v2/casos-de-uso.puml)

### Estados do Pedido (v2)
![Estados do pedido](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/Phijey/lab02-aluguel-de-carros/main/docs/uml/v2/estados-pedido.puml)

---

## 📐 Modelagem e Versões

Os diagramas foram feitos em **PlantUML** e ficam versionados em `docs/uml`:

| Versão do modelo | Conteúdo | Tag |
|---|---|---|
| [v1](docs/uml/v1) | Modelagem inicial: casos de uso, classes e pacotes | `v0.1.0` |
| [v2](docs/uml/v2) | Revisão após a implementação, alinhada ao código + diagrama de estados | `v1.0.0` |

As diferenças entre as versões estão em [docs/uml/CHANGELOG.md](docs/uml/CHANGELOG.md).

**Versões do sistema (tags do Git):**

| Tag | Entrega |
|---|---|
| `v0.1.0` | Modelagem inicial (UML v1 e histórias de usuário) |
| `v0.2.0` | Estrutura do projeto e autenticação |
| `v0.3.0` | CRUD de cliente |
| `v0.4.0` | Protótipo: criar e acompanhar pedidos de aluguel |
| `v0.5.0` | Funcionalidades dos agentes (avaliação, contratos, crédito, automóveis) |
| `v1.0.0` | Revisão dos diagramas (UML v2) e documentação final |

---

## 🔧 Instalação e Execução

### Pré-requisitos
- JDK 25
- Maven 3.9+
- Node.js 20+ e npm
- Um projeto no [Supabase](https://supabase.com)

### 🔑 Variáveis de Ambiente

#### 1. Back-end (Spring Boot)
Copie `backend/.env.example` para `backend/.env` e preencha com os dados do Supabase (**Connect → Session pooler**):

| Variável | Descrição |
|---|---|
| `SUPABASE_DB_URL` | `jdbc:postgresql://<host-do-pooler>:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USER` | `postgres.<project-ref>` |
| `SUPABASE_DB_PASSWORD` | senha do banco definida na criação do projeto |
| `JWT_SECRET` | chave com pelo menos 32 caracteres |

> O arquivo `.env` está no `.gitignore` e não vai para o repositório.

#### 2. Front-end (React, Vite)
Não precisa de variáveis em desenvolvimento: o Vite faz proxy de `/api` para `http://localhost:8080`. Para apontar para outra API, use `VITE_API_URL`.

### 💾 Banco de Dados
As tabelas são criadas automaticamente pelo **Flyway** quando o backend sobe pela primeira vez. Também são criados dados de demonstração:

| Usuário | Senha | Perfil |
|---|---|---|
| `locadora` | `123456` | Empresa (dona de 5 automóveis) |
| `banco` | `123456` | Banco |

Clientes são criados pela tela de cadastro.

### ⚡ Como Executar

#### Terminal 1: Back-end
```bash
cd backend
mvn spring-boot:run
```
A API sobe em `http://localhost:8080`.

#### Terminal 2: Front-end
```bash
cd frontend
npm install
npm run dev
```
Acesse `http://localhost:5173`.

---

## 📂 Estrutura de Pastas

```
.
├── backend/
│   ├── pom.xml
│   ├── .env.example
│   └── src/
│       ├── main/java/br/pucminas/aluguelcarros/
│       │   ├── config/        # SecurityConfig (JWT, CORS) e DataSeeder
│       │   ├── controller/    # endpoints REST
│       │   ├── dto/           # objetos de entrada/saída da API
│       │   ├── exception/     # exceções de negócio e handler global
│       │   ├── model/         # entidades JPA e enums
│       │   ├── repository/    # Spring Data JPA
│       │   ├── security/      # geração de token e UserDetailsService
│       │   └── service/       # regras de negócio
│       ├── main/resources/
│       │   ├── application.properties
│       │   └── db/migration/  # V1, V2, V3 (Flyway)
│       └── test/              # testes unitários e de integração
├── frontend/
│   ├── index.html
│   ├── vite.config.js
│   └── src/
│       ├── components/        # Layout, formulários, badges, rota protegida
│       ├── context/           # AuthContext (usuário logado)
│       ├── pages/             # telas do cliente e do agente
│       ├── services/          # chamadas à API (axios)
│       └── utils/
└── docs/
    ├── historias-de-usuario.md
    └── uml/
        ├── v1/                # modelagem inicial
        ├── v2/                # modelagem revisada
        └── CHANGELOG.md
```

---

## 🌐 Endpoints da API

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/auth/login` | público | login, retorna o token JWT |
| POST | `/api/auth/registro/cliente` | público | cadastro de cliente |
| POST | `/api/auth/registro/agente` | público | cadastro de empresa/banco |
| GET | `/api/auth/me` | logado | dados do usuário logado |
| GET/PUT/DELETE | `/api/clientes/me` | cliente | CRUD dos próprios dados |
| GET | `/api/clientes`, `/api/clientes/{id}` | agente | consulta de clientes |
| GET | `/api/automoveis` | logado | lista de automóveis |
| POST/PUT/DELETE | `/api/automoveis[/{id}]` | agente | manter automóveis |
| POST | `/api/pedidos` | cliente | introduzir pedido |
| GET | `/api/pedidos/meus` | cliente | pedidos do cliente |
| GET | `/api/pedidos?status=` | agente | todos os pedidos |
| GET | `/api/pedidos/{id}` | logado | detalhe (cliente só vê os seus) |
| PUT | `/api/pedidos/{id}` | cliente | modificar pedido pendente |
| PATCH | `/api/pedidos/{id}/cancelar` | cliente | cancelar pedido |
| PATCH | `/api/pedidos/{id}/analise` | agente | iniciar análise |
| PUT | `/api/pedidos/{id}/agente` | agente | modificar período/valor |
| PATCH | `/api/pedidos/{id}/avaliacao` | agente | aprovar/reprovar com parecer |
| POST | `/api/contratos` | agente | executar contrato (com crédito opcional) |
| GET | `/api/contratos[/{id}]` | logado | consultar contratos |
| GET | `/api/agentes?perfil=BANCO` | agente | lista de bancos |

Exemplo:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"locadora","senha":"123456"}'
```

---

## 🧪 Testes

```bash
cd backend
mvn test
```

Os testes rodam em um banco H2 em memória (as mesmas migrações do Flyway), sem depender do Supabase:

- `PedidoAluguelTest`: regras de domínio (cálculo de diárias, cancelamento, limite de 3 rendimentos)
- `AuthControllerTest`: cadastro, login, senha inválida e acesso sem token
- `ClienteControllerTest`: CRUD do cliente e restrição de acesso por perfil
- `FluxoPedidoTest`: fluxo completo pedido → análise → aprovação → contrato com crédito do banco

---

## 🔗 Documentações utilizadas
- [Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Spring Security - OAuth2 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
- [Flyway](https://documentation.red-gate.com/flyway)
- [Supabase - Connecting to Postgres](https://supabase.com/docs/guides/database/connecting-to-postgres)
- [React](https://react.dev)
- [Vite](https://vite.dev)
- [PlantUML](https://plantuml.com)

---

## 👥 Autores

| 👤 Nome | :octocat: GitHub |
|---|---|
| Pedro José Camilo | [Phijey](https://github.com/Phijey) |

---

## 🙏 Agradecimentos
- Prof. Dr. João Paulo Carneiro Aramuni, pelo template de README e pelos projetos de exemplo da disciplina.
