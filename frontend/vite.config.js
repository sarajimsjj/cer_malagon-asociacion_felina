import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
 
// Redirige /api/* al backend Spring Boot en desarrollo, así el frontend
// simplemente llama a fetch('/api/gatos') sin preocuparse de CORS ni de
// escribir la URL completa de localhost:8080 en el código.
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://127.0.0.1:8080',
      '/uploads': 'http://127.0.0.1:8080',
    },
  },
})