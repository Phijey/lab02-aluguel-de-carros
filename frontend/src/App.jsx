import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import RotaProtegida from './components/RotaProtegida'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import Inicio from './pages/Inicio'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/cadastro" element={<Cadastro />} />

      <Route element={<RotaProtegida><Layout /></RotaProtegida>}>
        <Route path="/" element={<Inicio />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
