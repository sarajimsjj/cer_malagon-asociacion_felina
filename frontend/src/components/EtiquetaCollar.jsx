import { ESTADOS } from '../constants'
import './EtiquetaCollar.css'

export default function EtiquetaCollar({ estado }) {
  const info = ESTADOS[estado] ?? { texto: estado, color: 'adoptado' }

  return (
    <div className={`etiqueta-collar etiqueta-collar--${info.color}`}>
      <span className="etiqueta-collar__anilla" aria-hidden="true" />
      {info.texto}
    </div>
  )
}
