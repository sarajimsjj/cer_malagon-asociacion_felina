import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { obtenerGatos } from '../api/gatos'
import GatoCard from '../components/GatoCard'

export default function ListaGatos() {
  const [gatos, setGatos] = useState([])
  const [estado, setEstado] = useState('cargando') // 'cargando' | 'listo' | 'error'
  const [mostrarLeyenda, setMostrarLeyenda] = useState(false)

  useEffect(() => {
    obtenerGatos()
      .then((datos) => {
        setGatos(datos)
        setEstado('listo')
      })
      .catch(() => setEstado('error'))
  }, [])

  return (
    <div className="pagina">
      <header className="cabecera">
        <div className="cabecera__fila">
         <div>
            <h1 className="cabecera__titulo">Conoce a nuestros gatos</h1>

            <p className="cabecera__descripcion">
                Cada uno de nuestros gatos tiene su propia historia, personalidad y necesidades.
                Algunos están esperando encontrar una familia, mientras que otros necesitan un
                poco más de tiempo y cuidados antes de estar preparados para ser adoptados.
                En cada ficha encontrarás una etiqueta que indica la situación actual del gato, 
                para que puedas identificarla de un vistazo.{' '}
            

            <button
              type="button"
              className="cabecera__leyenda-toggle"
              onClick={() => setMostrarLeyenda((anterior) => !anterior)}
              aria-expanded={mostrarLeyenda}
            >
              {mostrarLeyenda ? 'Ocultar el significado de las etiquetas' : '¿Qué significa cada etiqueta?'}
            </button>

            {mostrarLeyenda && (
            <div className="cabecera__leyenda">
                <p>
                <span className="cabecera__leyenda-circulo cabecera__leyenda-circulo--urgente"></span>
                <strong> Adopción urgente</strong> — Necesita encontrar un hogar con especial prioridad. 
                Puede llevar mucho tiempo esperando, encontrarse en una situación que requiere una solución
                rápida o necesitar un entorno familiar cuanto antes. Si estás pensando en adoptar,
                necesita especialmente que alguien le dé una oportunidad.
                </p>

                <p>
                <span className="cabecera__leyenda-circulo cabecera__leyenda-circulo--disponible"></span>
                <strong> Disponible</strong> — Está preparado para encontrar 
                una familia y comenzar una nueva etapa de su vida. Si te enamora, 
                puedes conocer más sobre él y ponerte en contacto con la asociación para iniciar 
                el proceso de adopción.
                </p>

                <p>
                <span className="cabecera__leyenda-circulo cabecera__leyenda-circulo--reservado"></span>
                <strong> Reservado</strong> — Tiene una familia interesada
                y cuyo proceso de adopción está en marcha. Por el momento, no está disponible
                para nuevas solicitudes de adopción
                </p>

                <p>
                <span className="cabecera__leyenda-circulo cabecera__leyenda-circulo--tratamiento"></span>
                <strong> En tratamiento</strong> — Necesita atención veterinaria, 
                se está recuperando de alguna enfermedad o requiere cuidados especiales. 
                Por este motivo, no está disponible para adopción temporalmente. 
                Cuando esté recuperado y preparado para encontrar una familia, se pondrá en adopción.
                </p>

                <p>
                <span className="cabecera__leyenda-circulo cabecera__leyenda-circulo--adoptado"></span>
                <strong> Adoptado</strong> — Ya ha encontrado una familia y
                un hogar definitivo. Aunque ya no esté disponible, nos gusta mantenerlo 
                aquí porque cada adopción es una historia con final feliz y una oportunidad 
                para recordar todo lo que puede cambiar la vida de un animal cuando encuentra su hogar.
                </p>
            </div>
            )}
            </p>

            <p className="cabecera__cierre">
                Todos ellos merecen una oportunidad, independientemente de la etiqueta que tengan.
                Si estás pensando en adoptar, te animamos a conocer sus historias, descubrir sus personalidades 
                y encontrar al compañero que encaje contigo. 
            </p>
            
            <p className="cabecera__cierre">
                Quizás uno de ellos lleve tiempo esperando precisamente a alguien como tú.
            </p>
            </div>
        </div>
      </header>

      <main className="contenido">
        {estado === 'cargando' && <p className="mensaje-estado">Cargando gatos…</p>}

        {estado === 'error' && (
          <p className="mensaje-estado mensaje-estado--error">
            No se ha podido conectar con el servidor. Comprueba que el backend esté arrancado en
            el puerto 8080 e inténtalo de nuevo.
          </p>
        )}

        {estado === 'listo' && gatos.length === 0 && (
          <p className="mensaje-estado">
            Todavía no hay gatos registrados. Añade el primero desde el botón de arriba.
          </p>
        )}

        {estado === 'listo' && gatos.length > 0 && (
          <div className="grid-gatos">
            {gatos.map((gato) => (
              <Link key={gato.id} to={`/gatos/${gato.id}`} className="gato-card-enlace">
                <GatoCard gato={gato} />
              </Link>
            ))}
          </div>
        )}
      </main>
    </div>
  )
}
