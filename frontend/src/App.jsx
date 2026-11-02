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

const AGENTES = ['EMPRESA', 'BANCO']

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />

      <Route element={<RotaProtegida><Layout /></RotaProtegida>}>
        <Route path="/" element={<Inicio />} />
        <Route path="/pedidos" element={<RotaProtegida perfis={['CLIENTE']}><MeusPedidos /></RotaProtegida>} />
        <Route path="/pedidos/novo" element={<RotaProtegida perfis={['CLIENTE']}><PedidoForm /></RotaProtegida>} />
        <Route path="/pedidos/:id/editar" element={<RotaProtegida perfis={['CLIENTE']}><PedidoForm /></RotaProtegida>} />
        <Route path="/pedidos/:id" element={<DetalhePedido />} />
        <Route path="/perfil" element={<RotaProtegida perfis={['CLIENTE']}><PerfilCliente /></RotaProtegida>} />
        <Route path="/clientes" element={<RotaProtegida perfis={AGENTES}><ListaClientes /></RotaProtegida>} />
        <Route path="/clientes/:id" element={<RotaProtegida perfis={AGENTES}><DetalheCliente /></RotaProtegida>} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
