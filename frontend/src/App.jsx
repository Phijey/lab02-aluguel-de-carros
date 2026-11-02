import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import RotaProtegida from './components/RotaProtegida'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import Inicio from './pages/Inicio'
import PerfilCliente from './pages/PerfilCliente'
import ListaClientes from './pages/ListaClientes'
import DetalheCliente from './pages/DetalheCliente'

const AGENTES = ['EMPRESA', 'BANCO']

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />

      <Route element={<RotaProtegida><Layout /></RotaProtegida>}>
        <Route path="/" element={<Inicio />} />
        <Route path="/perfil" element={<RotaProtegida perfis={['CLIENTE']}><PerfilCliente /></RotaProtegida>} />
        <Route path="/clientes" element={<RotaProtegida perfis={AGENTES}><ListaClientes /></RotaProtegida>} />
        <Route path="/clientes/:id" element={<RotaProtegida perfis={AGENTES}><DetalheCliente /></RotaProtegida>} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
