import EtiquetaCollar from './EtiquetaCollar'
import SiluetaGato from './SiluetaGato'
import { SEXOS } from '../constants'
import './GatoCard.css'

export default function GatoCard({ gato }) {
  return (
    <article className="gato-card">
      <div className="gato-card__foto">
        <EtiquetaCollar estado={gato.estado} />
        <div className="gato-card__foto-interior">
          {gato.fotoPrincipalUrl ? (
            <img src={gato.fotoPrincipalUrl} alt={gato.nombre} className="gato-card__foto-imagen" />
          ) : (
            <SiluetaGato />
          )}
        </div>
      </div>

      <div className="gato-card__cuerpo">
        <h3 className="gato-card__nombre">{gato.nombre}</h3>
        <p className="gato-card__meta">
          {gato.edadTexto} · {SEXOS[gato.sexo] ?? gato.sexo}
        </p>
      </div>
    </article>
  )
}
