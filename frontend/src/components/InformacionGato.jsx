import { RESULTADOS_TEST } from '../constants'
import './InformacionGato.css'

const CUIDADOS = [
  { clave: 'esterilizado', texto: 'Esterilizado/a' },
  { clave: 'desparasitado', texto: 'Desparasitado/a' },
  { clave: 'vacunado', texto: 'Vacunado/a' },
]

const TESTS = [
  { clave: 'testFiv', texto: 'Test FIV' },
  { clave: 'testFelv', texto: 'Test FeLV' },
]

export default function InformacionGato({ gato }) {
  return (
    <div className="informacion-gato">
      <div className="informacion-gato__fila">
        <div className="informacion-gato__grupo">
          <span className="informacion-gato__etiqueta">Cuidados veterinarios</span>
          <div className="informacion-gato__chips">
            {CUIDADOS.map(({ clave, texto }) => (
              <span
                key={clave}
                className={`informacion-gato__chip ${
                  gato[clave] ? 'informacion-gato__chip--si' : 'informacion-gato__chip--no'
                }`}
              >
                {gato[clave] ? '✓' : '✕'} {texto}
              </span>
            ))}
          </div>
        </div>

        <div className="informacion-gato__grupo">
          <span className="informacion-gato__etiqueta">Tests</span>
          <div className="informacion-gato__chips">
            {TESTS.map(({ clave, texto }) => (
              <span
                key={clave}
                className={`informacion-gato__chip informacion-gato__chip--test-${gato[clave]?.toLowerCase()}`}
              >
                {texto}: {RESULTADOS_TEST[gato[clave]] ?? gato[clave]}
              </span>
            ))}
          </div>
        </div>
      </div>

      {gato.enfermedad && (
        <div className="informacion-gato__enfermedad">
          <strong>Necesita cuidados:</strong> {gato.enfermedad}
        </div>
      )}

      {gato.observaciones && <p className="informacion-gato__observaciones">{gato.observaciones}</p>}
    </div>
  )
}
