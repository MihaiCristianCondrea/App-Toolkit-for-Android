// Renders this listing's visuals from one source into every language: each strings/<locale>.json becomes
// ../listing/<locale>/graphics/ (phone, tablet and desktop screenshots, feature graphic, icon) and
// ../listing/<locale>/alt-text.txt. Review strips of the phone, tablet and desktop sets go to output/ (not committed).
// Usage: node scripts/render.mjs [locale ...] [--preview]      CHROME_PATH selects the browser.
import { createServer } from '../../../../../../ui/node_modules/vite/dist/node/index.js';
import { chromium } from '../../../../../../ui/node_modules/playwright-core/index.mjs';
import { fileURLToPath } from 'node:url';
import { mkdir, writeFile, readFile, rm } from 'node:fs/promises';
import { readdirSync, readFileSync, existsSync, mkdirSync } from 'node:fs';
import { createHash } from 'node:crypto';
import { execFileSync } from 'node:child_process';
import path from 'node:path';
import assert from 'node:assert/strict';

const root = fileURLToPath(new URL('../', import.meta.url));
const listing = path.resolve(root, '..', 'listing');
const marketing = path.resolve(root, '../../../../..');
const wanted = process.argv.slice(2).filter((a) => !a.startsWith('--'));
const locales = readdirSync(path.join(root, 'strings')).filter((f) => f.endsWith('.json')).map((f) => f.slice(0, -5))
  .filter((l) => !wanted.length || wanted.includes(l));

// News stories show their publishers' artwork from the live API, as the app does. A headless browser may not reach it,
// so each image is downloaded once with curl (which honours the proxy) into output/images/ and served from there.
const imageCache = path.join(root, 'output', 'images');
const remoteImage = (url) => {
  mkdirSync(imageCache, { recursive: true });
  const file = path.join(imageCache, createHash('sha1').update(url).digest('hex'));
  if (!existsSync(file)) execFileSync('curl', ['-sSfL', '-m', '60', '-o', file, url]);
  const body = readFileSync(file);
  const type = body.subarray(0, 4).toString('latin1') === 'RIFF' ? 'image/webp' : body[0] === 0x89 ? 'image/png' : 'image/jpeg';
  return { body, type };
};

const server = await createServer({ configFile: false, root, publicDir: path.join(marketing, 'ui/public'),
  server: { host: '127.0.0.1', port: 0, fs: { allow: [marketing] } } });
await server.listen();
const url = server.resolvedUrls.local[0];
if (process.argv.includes('--preview')) {
  console.log(`Preview: ${url}?locale=${locales[0]}`);
} else {
  let browser;
  try {
    browser = await chromium.launch({ headless: true, executablePath: process.env.CHROME_PATH || 'C:/Program Files/Google/Chrome/Application/chrome.exe' });
    const page = await browser.newPage({ viewport: { width: 8640, height: 1920 }, deviceScaleFactor: 1 });
    const errors = [];
    page.on('pageerror', (e) => errors.push(e.message));
    await page.route(/^https?:\/\//, async (r) => {
      if (r.request().url().startsWith(url)) return r.continue();
      if (r.request().resourceType() === 'image') { const img = remoteImage(r.request().url()); return r.fulfill({ status: 200, contentType: img.type, body: img.body }); }
      return r.abort();
    });
    const open = async (locale, shot) => {
      await page.goto(`${url}?locale=${locale}&shot=${shot}`);
      await page.waitForFunction(() => window.storeReady === true);
      await page.evaluate(() => document.documentElement.setAttribute('data-export', ''));
    };

    for (const locale of locales) {
      const strings = JSON.parse(await readFile(path.join(root, 'strings', `${locale}.json`), 'utf8'));
      const out = path.join(listing, locale, 'graphics');
      for (const dir of ['phone-screenshots', 'tablet-screenshots', 'desktop-screenshots', 'feature-graphic', 'icon']) await rm(path.join(out, dir), { recursive: true, force: true });
      const flatten = [];

      await open(locale, 'panorama');
      await mkdir(path.join(out, 'phone-screenshots'), { recursive: true });
      for (const [i, s] of strings.screenshots.entries()) {
        const file = path.join(out, 'phone-screenshots', `${i + 1}-${s.id}.png`);
        const el = page.locator(`[data-shot="${i + 1}"]`);
        await el.screenshot({ path: file, animations: 'disabled' });
        // Play: taglines take no more than 20% of the image.
        const share = await el.evaluate((node) => {
          const r = node.getBoundingClientRect();
          const c = node.querySelector('.caption').getBoundingClientRect();
          return (c.width * c.height) / (r.width * r.height);
        });
        assert.ok(share <= 0.2, `${file}: caption covers ${(share * 100).toFixed(1)}% of the image (Play allows 20%)`);
        flatten.push(file);
      }
      await mkdir(path.join(root, 'output'), { recursive: true });
      await page.locator('.pano').screenshot({ path: path.join(root, 'output', `panorama-${locale}.png`) });

      for (const [shot, dir] of [['feature-graphic', 'feature-graphic'], ['icon', 'icon']]) {
        await open(locale, shot);
        await mkdir(path.join(out, dir), { recursive: true });
        const file = path.join(out, dir, `${dir}.png`);
        // The icon keeps its alpha: no page background behind it.
        if (dir === 'icon') await page.evaluate(() => { document.documentElement.style.background = 'transparent'; document.body.style.background = 'transparent'; });
        await page.locator(`[data-shot="${shot}"]`).screenshot({ path: file, omitBackground: dir === 'icon' });
        if (dir !== 'icon') flatten.push(file);
      }
      // Tablet and desktop screenshots at 2x: 3840 x 2160 each (16:9), on the Fold and the Pixelbook Go.
      const wide = await browser.newPage({ viewport: { width: 1920 * strings.tabletScreenshots.length, height: 1080 }, deviceScaleFactor: 2 });
      wide.on('pageerror', (e) => errors.push(e.message));
      await wide.route(/^https?:\/\//, async (r) => {
        if (r.request().url().startsWith(url)) return r.continue();
        if (r.request().resourceType() === 'image') { const img = remoteImage(r.request().url()); return r.fulfill({ status: 200, contentType: img.type, body: img.body }); }
        return r.abort();
      });
      for (const [kind, list] of [['tablet', strings.tabletScreenshots], ['desktop', strings.desktopScreenshots]]) {
        await wide.goto(`${url}?locale=${locale}&shot=${kind}`);
        await wide.waitForFunction(() => window.storeReady === true);
        await wide.evaluate(() => document.documentElement.setAttribute('data-export', ''));
        await mkdir(path.join(out, `${kind}-screenshots`), { recursive: true });
        for (const [i, s] of list.entries()) {
          const file = path.join(out, `${kind}-screenshots`, `${i + 1}-${s.id}.png`);
          await wide.locator(`[data-shot="${kind}-${i + 1}"]`).screenshot({ path: file, animations: 'disabled' });
          flatten.push(file);
        }
        await wide.locator('.strip').screenshot({ path: path.join(root, 'output', `${kind}-strip-${locale}.png`), scale: 'css' });
      }
      await wide.close();
      assert.deepEqual(errors, []);
      // Play wants screenshots and the feature graphic as 24-bit PNG without alpha; the icon as 32-bit PNG.
      execFileSync(process.platform === 'win32' ? 'python' : 'python3', ['-c',
        'import sys\nfrom PIL import Image\nfor p in sys.argv[2:]: Image.open(p).convert("RGB").save(p, optimize=True)\nImage.open(sys.argv[1]).convert("RGBA").save(sys.argv[1])',
        path.join(out, 'icon', 'icon.png'), ...flatten]);
      // One line per screenshot: its path under graphics/, a tab, its alt text (Play allows 140 characters).
      const alt = [['phone', strings.screenshots], ['tablet', strings.tabletScreenshots], ['desktop', strings.desktopScreenshots]]
        .flatMap(([kind, list]) => list.map((s, i) => `${kind}-screenshots/${i + 1}-${s.id}.png\t${s.alt}`));
      await writeFile(path.join(listing, locale, 'alt-text.txt'), alt.join('\n') + '\n');
      console.log(`${locale}: ${strings.screenshots.length} phone, ${strings.tabletScreenshots.length} tablet and ${strings.desktopScreenshots.length} desktop screenshots, feature graphic, icon. No page errors.`);
    }
  } finally {
    await browser?.close();
    await server.close();
  }
}
