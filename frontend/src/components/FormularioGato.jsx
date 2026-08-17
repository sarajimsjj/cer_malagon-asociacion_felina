import { useState } from 'react'
import { Link } from 'react-router-dom'
import { OPCIONES_ESTADO, OPCIONES_SEXO, OPCIONES_RESULTADO_TEST } from '../constants'
import '../pages/NuevoGato.css'

export default function FormularioGato({
  valoresIniciales,
  onEnviar,
  textoBoton,
  textoBotonEnviando,
  enlaceCancelar,
}) {
  const [valores, setValores] = useState(valoresIniciales)
  const [erroresPorCampo, setErroresPorCampo] = useState({})
  const [errorGeneral, setErrorGeneral] = useState(null)
  const [enviando, setEnviando] = useState(false)

  function actualizarCampo(campo, valor) {
    setValores((anteriores) => ({ ...anteriores, [campo]: valor }))
  }

  async function manejarEnvio(evento) {
    evento.preventDefault()
    setEnviando(true)
    setErrorGeneral(null)
    setErroresPorCampo({})

    try {
      await onEnviar({
        ...valores,
        enfermedad: valores.enfermedad.trim() || null,
        observaciones: valores.observaciones.trim() || null,
      })
    } catch (error) {
      if (error?.erroresPorCampo) {
        setErroresPorCampo(error.erroresPorCampo)
      } else {
        setErrorGeneral(
          error?.message ??
            'No se ha podido guardar el gato. Comprueba que el backend esté arrancado e inténtalo de nuevo.',
        )
      }
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="formulario" onSubmit={manejarEnvio} noValidate>
      {errorGeneral && <p className="formulario__error-general">{errorGeneral}</p>}

      <div className="formulario__campo">
        <label htmlFor="nombre">Nombre</label>
        <input
          id="nombre"
          type="text"
          value={valores.nombre}
          onChange={(e) => actualizarCampo('nombre', e.target.value)}
        />
        {erroresPorCampo.nombre && <p className="formulario__error">{erroresPorCampo.nombre}</p>}
      </div>

      <div className="formulario__fila">
        <div className="formulario__campo">
          <label htmlFor="fechaNacimientoEstim">Fecha de nacimiento estimada</label>
          <input
            id="fechaNacimientoEstim"
            type="date"
            value={valores.fechaNacimientoEstim}
            onChange={(e) => actualizarCampo('fechaNacimientoEstim', e.target.value)}
          />
          {erroresPorCampo.fechaNacimientoEstim && (
            <p className="formulario__error">{erroresPorCampo.fechaNacimientoEstim}</p>
          )}
        </div>

        <div className="formulario__campo">
          <label htmlFor="sexo">Sexo</label>
          <select
            id="sexo"
            value={valores.sexo}
            onChange={(e) => actualizarCampo('sexo', e.target.value)}
          >
            {OPCIONES_SEXO.map(([clave, texto]) => (
              <option key={clave} value={clave}>
                {texto}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="formulario__campo">
        <span className="formulario__etiqueta-grupo">Cuidados veterinarios</span>
        <div className="formulario__checkboxes">
          <label className="formulario__checkbox">
            <input
              type="checkbox"
              checked={valores.esterilizado}
              onChange={(e) => actualizarCampo('esterilizado', e.target.checked)}
            />
            Esterilizado/a
          </label>
          <label className="formulario__checkbox">
            <input
              type="checkbox"
              checked={valores.desparasitado}
              onChange={(e) => actualizarCampo('desparasitado', e.target.checked)}
            />
            Desparasitado/a
          </label>
          <label className="formulario__checkbox">
            <input
              type="checkbox"
              checked={valores.vacunado}
              onChange={(e) => actualizarCampo('vacunado', e.target.checked)}
            />
            Vacunado/a
          </label>
        </div>
      </div>

      <div className="formulario__fila">
        <div className="formulario__campo">
          <label htmlFor="testFiv">Test FIV</label>
          <select
            id="testFiv"
            value={valores.testFiv}
            onChange={(e) => actualizarCampo('testFiv', e.target.value)}
          >
            {OPCIONES_RESULTADO_TEST.map(([clave, texto]) => (
              <option key={clave} value={clave}>
                {texto}
              </option>
            ))}
          </select>
        </div>

        <div className="formulario__campo">
          <label htmlFor="testFelv">Test FeLV</label>
          <select
            id="testFelv"
            value={valores.testFelv}
            onChange={(e) => actualizarCampo('testFelv', e.target.value)}
          >
            {OPCIONES_RESULTADO_TEST.map(([clave, texto]) => (
              <option key={clave} value={clave}>
                {texto}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="formulario__campo">
        <label htmlFor="estado">Estado</label>
        <select
          id="estado"
          value={valores.estado}
          onChange={(e) => actualizarCampo('estado', e.target.value)}
        >
          {OPCIONES_ESTADO.map(([clave, texto]) => (
            <option key={clave} value={clave}>
              {texto}
            </option>
          ))}
        </select>
      </div>

      <div className="formulario__campo">
        <label htmlFor="enfermedad">Enfermedad (opcional)</label>
        <input
          id="enfermedad"
          type="text"
          value={valores.enfermedad}
          onChange={(e) => actualizarCampo('enfermedad', e.target.value)}
          placeholder="Déjalo en blanco si no tiene ninguna"
        />
      </div>

      <div className="formulario__campo">
        <label htmlFor="observaciones">Observaciones</label>
        <textarea
          id="observaciones"
          rows={4}
          value={valores.observaciones}
          onChange={(e) => actualizarCampo('observaciones', e.target.value)}
        />
      </div>

      <div className="formulario__acciones">
        <Link to={enlaceCancelar} className="boton boton--secundario">
          Cancelar
        </Link>
        <button type="submit" className="boton boton--primario" disabled={enviando}>
          {enviando ? textoBotonEnviando : textoBoton}
        </button>
      </div>
    </form>
  )
}
