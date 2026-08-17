import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { obtenerGato, actualizarGato } from '../api/gatos'
import FormularioGato from '../components/FormularioGato'
import './NuevoGato.css'

export default function EditarGato() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [valoresIniciales, setValoresIniciales] = useState(null)
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'

  useEffect(() => {
    obtenerGato(id)
      .then((gato) => {
        setValoresIniciales({
          nombre: gato.nombre,
          fechaNacimientoEstim: gato.fechaNacimientoEstim,
          sexo: gato.sexo,
          esterilizado: gato.esterilizado,
          desparasitado: gato.desparasitado,
          vacunado: gato.vacunado,
          enfermedad: gato.enfermedad ?? '',
          estado: gato.estado,
          testFiv: gato.testFiv,
          testFelv: gato.testFelv,
          observaciones: gato.observaciones ?? '',
        })
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [id])

  async function manejarEnvio(datos) {
    await actualizarGato(id, datos)
    navigate(`/gatos/${id}`)
  }

  return (
    <div className="pagina">
      <header className="cabecera">
        <h1 className="cabecera__titulo">Editar gato</h1>
        <p className="cabecera__subtitulo">Actualiza la ficha del gato.</p>
      </header>

      <main className="contenido contenido--formulario">
        {estado === 'cargando' && <p className="mensaje-estado">Cargando…</p>}

        {estado === 'error' && (
          <p className="mensaje-estado mensaje-estado--error">No se ha podido cargar este gato.</p>
        )}

        {estado === 'listo' && (
          <FormularioGato
            valoresIniciales={valoresIniciales}
            onEnviar={manejarEnvio}
            textoBoton="Guardar cambios"
            textoBotonEnviando="Guardando…"
            enlaceCancelar={`/gatos/${id}`}
          />
        )}
      </main>
    </div>
  )
}
