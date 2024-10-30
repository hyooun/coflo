import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import svgr from 'vite-plugin-svgr';
import tsconfigPaths from 'vite-tsconfig-paths';
import path from 'path';
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths(), svgr()],
  publicDir: 'public',
  assetsInclude: ['**/*.png'],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});
