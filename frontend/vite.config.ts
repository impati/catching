import path from 'node:path'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const __dirname = path.dirname(fileURLToPath(import.meta.url))

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/v1': {
        target: 'http://localhost:8001',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: path.resolve(__dirname, '../external/src/main/resources/static'),
    emptyOutDir: true,
  },
})
