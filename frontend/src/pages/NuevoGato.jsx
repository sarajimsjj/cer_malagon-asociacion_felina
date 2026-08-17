import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { crearGato } from '../api/gatos'
import { subirFoto } from '../api/fotos'
import FormularioGato from '../components/FormularioGato'
import SelectorArchivos from '../components/SelectorArchivos'
import './NuevoGato.css'

const VALORES_INICIALES = {
  nombre: '',
  fechaNacimientoEstim: '',
  sexo: 'DESCONOCIDO',
  esterilizado: false,
  desparasitado: false,
  vacunado: false,
  enfermedad: '',
  estado: 'DISPONIBLE',
  testFiv: 'NO_TESTADO',
  testFelv: 'NO_TESTADO',
  observaciones: '',
}

export default function NuevoGato() {
  const navigate = useNavigate()
  const [archivos, setArchivos] = useState([])

  function agregarArchivos(nuevos) {
    setArchivos((anteriores) => [...anteriores, ...nuevos])
  }

  function quitarArchivo(indice) {
    setArchivos((anteriores) => anteriores.filter((_, i) => i !== indice))
  }

  async function manejarEnvio(datos) {
    const creado = await crearGato(datos)

    let fotosFallidas = 0
    for (const archivo of archivos) {
      try {
        await subirFoto(creado.id, archivo)
      } catch {
        fotosFallidas += 1
      }
    }

    navigate(`/gatos/${creado.id}`, {
      state:
        fotosFallidas > 0
          ? {
              avisoFotos: `${fotosFallidas} de ${archivos.length} archivo${archivos.length === 1 ? '' : 's'} no se pudo subir. Puedes intentarlo de nuevo aquí abajo.`,
            }
          : undefined,
    })
  }

  return (
    <div className="pagina">
      <header className="cabecera">
        <h1 className="cabecera__titulo">Añadir gato</h1>
        <p className="cabecera__subtitulo">Rellena la ficha con los datos del gato.</p>
      </header>

      <main className="contenido contenido--formulario">
        <div className="nuevo-gato__fotos">
          <span className="formulario__etiqueta-grupo">Fotos y vídeos</span>
          <SelectorArchivos
            multiple
            onSeleccionar={agregarArchivos}
            texto="Arrastra fotos o vídeos aquí, o haz clic para seleccionarlos"
          />

          {archivos.length > 0 && (
            <ul className="nuevo-gato__archivos">
              {archivos.map((archivo, indice) => (
                <li key={`${archivo.name}-${indice}`} className="nuevo-gato__archivo">
                  <span className="nuevo-gato__archivo-nombre">{archivo.name}</span>
                  <button
                    type="button"
                    className="nuevo-gato__archivo-quitar"
                    onClick={() => quitarArchivo(indice)}
                    aria-label={`Quitar ${archivo.name}`}
                  >
                    ✕
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        <FormularioGato
          valoresIniciales={VALORES_INICIALES}
          onEnviar={manejarEnvio}
          textoBoton="Guardar gato"
          textoBotonEnviando={archivos.length > 0 ? 'Guardando y subiendo fotos…' : 'Guardando…'}
          enlaceCancelar="/"
        />
      </main>
    </div>
  )
}
