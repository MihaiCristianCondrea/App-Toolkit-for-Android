import assert from 'node:assert/strict';
import {mkdir} from 'node:fs/promises';
import {fileURLToPath} from 'node:url';
import {createServer} from 'vite';
import {chromium} from 'playwright-core';

const root = fileURLToPath(new URL('../', import.meta.url));
const server = await createServer({root, server: {host: '127.0.0.1', port: 0, open: false}});
const browser = await chromium.launch(process.env.CHROMIUM_PATH ? {executablePath: process.env.CHROMIUM_PATH} : {});
let checks = 0;
try {
  await server.listen();
  const base = `http://127.0.0.1:${server.httpServer.address().port}`;
  await mkdir(new URL('../captures/', import.meta.url), {recursive: true});
  for (const width of [360, 1280]) {
    const page = await browser.newPage({viewport: {width, height: 900}});
    const errors = [];
    page.on('pageerror', e => errors.push(e.message));
    await page.goto(base);
    await page.locator('mnw-screen-card').first().waitFor();
    assert.equal(await page.locator('mnw-screen-card').count(), 3);
    assert.deepEqual(await page.locator('[data-screen]').evaluateAll(nodes => nodes.map(node => node.dataset.screen)), ['quick-tools', 'numbers', 'apps']);
    assert.equal(await page.locator('mnw-marketing-shell .catalog-header, mnw-marketing-shell [aria-label="Filter screens"]').count(), 0);
    assert.equal(await page.locator('mnw-device-frame').count(), 0);
    assert.equal(await page.locator('.sc-screen').count(), 3);
    // A marketing page: no footer, no drawer, no menu button.
    assert.equal(await page.locator('mnw-site-footer, .g-footer').count(), 0);
    const shell = page.locator('mnw-marketing-shell mnw-app-shell');
    assert.equal(await shell.evaluate(el => el.navigationMode), 'none');
    assert.equal(await shell.locator('.drawer').isVisible(), false);
    assert.equal(await shell.locator('.bar > md-icon-button.leading').count(), 0);
    assert.equal(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth + 1), true);
    await page.screenshot({path: new URL(`../captures/gallery-${width}.png`, import.meta.url).pathname});
    assert.deepEqual(errors, []);
    await page.close();
    checks++;
  }
  {
    // Opening a screen and going back stays in one page.
    const page = await browser.newPage({viewport: {width: 1280, height: 900}});
    await page.goto(base);
    await page.locator('mnw-screen-card').first().waitFor();
    await page.evaluate(() => { window.__hub = true; });
    const href = await page.locator('mnw-screen-card').first().getAttribute('href');
    assert.match(href, /[?&]id=quick-tools/);
    await page.locator('mnw-screen-card').first().locator('md-filled-button, a').last().click();
    await page.locator('mnw-prototype-layout').waitFor();
    assert.equal(await page.evaluate(() => window.__hub), true);
    await page.locator('mnw-screen-inspector').locator('.back').click();
    await page.locator('mnw-marketing-shell').waitFor();
    assert.equal(await page.evaluate(() => window.__hub && !location.search), true);
    await page.goForward();
    await page.locator('mnw-prototype-layout').waitFor();
    assert.equal(await page.evaluate(() => window.__hub), true);
    await page.close();
    checks++;
  }
  const page = await browser.newPage({viewport: {width: 1280, height: 1000}});
  const errors = [];
  page.on('pageerror', e => errors.push(e.message));
  for (const id of ['quick-tools', 'numbers', 'apps']) {
    for (const window of ['phone', 'fold', 'desktop']) {
      for (const theme of ['light', 'dark']) {
        await page.goto(`${base}/screen.html?id=${id}&window=${window}&theme=${theme}&frame=device&panel=none`);
        await page.locator('html[data-ready]').waitFor();
        assert.equal(await page.locator('.sc-screen').count(), 1);
        const expected = {phone: [430, 957], fold: [852, 883], desktop: [1218, 720]}[window];
        const size = await page.locator('.sc-screen').evaluate(el => [el.offsetWidth, el.offsetHeight]);
        assert.deepEqual(size, expected, `${id} ${window} ${theme}`);
        const images = await page.locator('mnw-device-frame img').evaluateAll(async images => {
          await Promise.all(images.map(image => image.decode()));
          return images.every(image => image.naturalWidth > 0);
        });
        assert.equal(images, true);
        checks++;
      }
    }
  }
  await page.goto(`${base}/screen.html?id=numbers&theme=light`);
  await page.locator('html[data-ready]').waitFor();
  assert.equal(await page.locator('mnw-device-frame').count(), 1);
  await page.locator('.numbers-card').first().click();
  await page.locator('.tk-sheet-surface').waitFor();
  assert.equal(await page.locator('.tk-sheet-surface').isVisible(), true);
  checks++;
  await page.goto(`${base}/screen.html?id=numbers&mode=compare&compare=theme`);
  await page.locator('html[data-ready]').waitFor();
  assert.equal(await page.locator('.sc-screen').count(), 2);
  checks++;
  for (const file of ['motion', 'specs']) {
    await page.goto(`${base}/${file}.html`);
    await page.locator('mnw-app-shell').waitFor();
    assert.equal(await page.locator('mnw-app-shell').evaluate(el => el.navigationMode), 'none');
    assert.equal(await page.locator('mnw-app-shell').evaluate(el => el.leading), 'back');
    assert.equal(await page.locator('mnw-site-footer').count(), 0);
    checks++;
  }
  // Exercise the public embed API without a separate test-only product page.
  await page.goto(`${base}/screen.html?id=numbers&panel=none`);
  const embedded = await page.evaluate(async () => {
    const api = await import('/src/embed.js');
    await api.setup();
    const target = document.createElement('div');
    document.body.replaceChildren(target);
    await api.mount(target, 'numbers', {activeSheet: 'counter'}, {frame: true, window: 'fold'});
    return {screen: !!target.querySelector('.sc-screen'), sheet: !!target.querySelector('.tk-sheet-surface'), frame: target.querySelector('mnw-device-frame')?.device};
  });
  assert.deepEqual(embedded, {screen: true, sheet: true, frame: 'pixel_9_pro_fold'});
  checks++;
  assert.deepEqual(errors, []);
  await page.close();
  console.log(`${checks} marketing UI checks passed`);
} finally {
  await browser.close();
  await server.close();
}
