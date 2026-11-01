import { createServer } from 'vite';
import { chromium } from 'playwright-core';
const [root, out, ...specs] = process.argv.slice(2);
const server = await createServer({ root, server: { host: '127.0.0.1', port: 0, open: false }, logLevel: 'error' });
await server.listen();
const base = `http://127.0.0.1:${server.httpServer.address().port}`;
const b = await chromium.launch({executablePath: '/opt/pw-browsers/chromium'});
const p = await b.newPage({ viewport: { width: 430, height: 1500 }, deviceScaleFactor: 1 });
for (const spec of specs) {
  const [name, id, theme, state] = spec.split('@');
  await p.goto(`${base}/screen.html?id=${id}&panel=none&frame=none&theme=${theme}${state ? '&state=' + encodeURIComponent(state) : ''}`);
  await p.waitForTimeout(1500);
  await p.screenshot({ path: `${out}/${name}.png`, fullPage: true });
}
await b.close(); await server.close();
