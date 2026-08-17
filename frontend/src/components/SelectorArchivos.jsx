import './SelectorArchivos.css'

const TIPOS_ACEPTADOS = 'image/jpeg,image/png,image/webp,video/mp4,video/webm'

export default function SelectorArchivos({
  onSeleccionar,
  multiple = false,
  disabled = false,
  compacto = false,
  texto,
}) {
  function manejarCambio(evento) {
    const archivos = Array.from(evento.target.files)
    evento.target.value = ''
    if (archivos.length === 0) return
    onSeleccionar(multiple ? archivos : archivos[0])
  }

  return (
    <label
      className={`selector-archivos${compacto ? ' selector-archivos--compacto' : ''}${
        disabled ? ' selector-archivos--disabled' : ''
      }`}
    >
      <span className="selector-archivos__icono" aria-hidden="true">
        +
      </span>
      <span className="selector-archivos__texto">{texto}</span>
      <input
        type="file"
        accept={TIPOS_ACEPTADOS}
        multiple={multiple}
        onChange={manejarCambio}
        disabled={disabled}
        hidden
      />
    </label>
  )
}
