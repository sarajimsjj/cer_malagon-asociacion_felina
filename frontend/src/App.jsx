import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar'
import AvisoSolicitudes from './components/AvisoSolicitudes'
import RutaProtegida from './components/RutaProtegida'
import ListaGatos from './pages/ListaGatos'
import Equipo from './pages/Equipo'
import NuevoGato from './pages/NuevoGato'
import GatoDetalle from './pages/GatoDetalle'
import EditarGato from './pages/EditarGato'
import Login from './pages/Login'
import NuevaAdministradora from './pages/NuevaAdministradora'
import './App.css'

export default function App() {
  return (
    <>
      <Navbar />
      <AvisoSolicitudes />
      <Routes>
        <Route path="/" element={<ListaGatos />} />
        <Route path="/equipo" element={<Equipo />} />
        <Route path="/login" element={<Login />} />
        <Route
          path="/gatos/nuevo"
          element={
            <RutaProtegida soloPrincipal>
              <NuevoGato />
            </RutaProtegida>
          }
        />
        <Route path="/gatos/:id" element={<GatoDetalle />} />
        <Route
          path="/gatos/:id/editar"
          element={
            <RutaProtegida soloPrincipal>
              <EditarGato />
            </RutaProtegida>
          }
        />
        <Route
          path="/admin/nueva-administradora"
          element={
            <RutaProtegida soloPrincipal>
              <NuevaAdministradora />
            </RutaProtegida>
          }
        />
      </Routes>
    </>
  )
}
