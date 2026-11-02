import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import RotaProtegida from './components/RotaProtegida'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import Inicio from './pages/Inicio'
import PerfilCliente from './pages/PerfilCliente'
import ListaClientes from './pages/ListaClientes'
import DetalheCliente from './pages/DetalheCliente'
import MeusPedidos from './pages/MeusPedidos'
import PedidoForm from './pages/PedidoForm'
import DetalhePedido from './pages/DetalhePedido'
import PedidosAgente from './pages/PedidosAgente'
import AnalisePedido from './pages/AnalisePedido'
import Contratos from './pages/Contratos'
import DetalheContrato from './pages/DetalheContrato'
import Automoveis from './pages/Automoveis'

const AGENTES = ['EMPRESA', 'BANCO']
const CLIENTE = ['CLIENTE']

const so = (perfis, elemento) => <RotaProtegida perfis={perfis}>{elemento}</RotaProtegida>

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />

      <Route element={<RotaProtegida><Layout /></RotaProtegida>}>
        <Route path="/" element={<Inicio />} />

        <Route path="/pedidos" element={so(CLIENTE, <MeusPedidos />)} />
        <Route path="/pedidos/novo" element={so(CLIENTE, <PedidoForm />)} />
        <Route path="/pedidos/:id/editar" element={so(CLIENTE, <PedidoForm />)} />
        <Route path="/pedidos/:id" element={so(CLIENTE, <DetalhePedido />)} />
        <Route path="/perfil" element={so(CLIENTE, <PerfilCliente />)} />

        <Route path="/analise" element={so(AGENTES, <PedidosAgente />)} />
        <Route path="/analise/:id" element={so(AGENTES, <AnalisePedido />)} />
        <Route path="/clientes" element={so(AGENTES, <ListaClientes />)} />
        <Route path="/clientes/:id" element={so(AGENTES, <DetalheCliente />)} />
        <Route path="/automoveis" element={so(AGENTES, <Automoveis />)} />

        <Route path="/contratos" element={<Contratos />} />
        <Route path="/contratos/:id" element={<DetalheContrato />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
