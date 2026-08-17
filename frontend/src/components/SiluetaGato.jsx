export default function SiluetaGato() {
  return (
    <svg viewBox="0 0 100 100" className="silueta-gato" aria-hidden="true">
      <path
        d="M28 32 L20 12 L38 24 Q50 20 62 24 L80 12 L72 32 Q80 42 80 56 Q80 78 50 82 Q20 78 20 56 Q20 42 28 32 Z"
        fill="currentColor"
      />
      <circle cx="40" cy="54" r="3.5" fill="var(--color-surface)" />
      <circle cx="60" cy="54" r="3.5" fill="var(--color-surface)" />
      <path
        d="M46 64 Q50 68 54 64"
        stroke="var(--color-surface)"
        strokeWidth="2.5"
        fill="none"
        strokeLinecap="round"
      />
    </svg>
  )
}
