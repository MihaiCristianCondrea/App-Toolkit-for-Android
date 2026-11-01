import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import { defineConfig } from "vite";

const __dirname = dirname(fileURLToPath(import.meta.url));

export default defineConfig({
  root: __dirname,
  publicDir: "public",
  server: {
    port: 5173,
    open: false,
  },
  build: {
    outDir: "dist",
    emptyOutDir: true,
    rollupOptions: {
      input: {
        gallery: resolve(__dirname, "index.html"),
        screen: resolve(__dirname, "screen.html"),
        motion: resolve(__dirname, "motion.html"),
        specs: resolve(__dirname, "specs.html"),
      },
    },
  },
});
