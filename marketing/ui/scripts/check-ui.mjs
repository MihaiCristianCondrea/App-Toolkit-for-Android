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
    await page.screenshot({path: fileURLToPath(new URL(`../captures/gallery-${width}.png`, import.meta.url))});
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
  // Chance belongs to Quick Tools, with Compose range controls and six-sided dice.
  for (const window of ['phone', 'fold', 'desktop']) {
    for (const theme of ['light', 'dark']) {
      await page.goto(`${base}/screen.html?id=quick-tools&window=${window}&theme=${theme}&panel=none`);
      await page.locator('.qt-chance').first().waitFor();
      assert.equal(await page.locator('.qt-chance').count(), 3);
      for (const id of ['random_number', 'dice_roll', 'coin_flip']) {
        await page.locator(`[data-tool="${id}"] .qt-live__label`).click();
        const sheet = page.locator('.tk-sheet-surface');
        await sheet.waitFor();
        if (id === 'random_number') {
          await sheet.locator('md-outlined-segmented-button[label="1–2"]').click();
          await sheet.locator('md-filled-button').filter({hasText:'Generate'}).click();
          assert.match(await sheet.locator('.chance-number').innerText(), /^[12]$/);
          await sheet.locator('md-filled-text-field').first().locator('input').fill('3');
          await page.waitForFunction(() => document.querySelector('.chance-body md-filled-button')?.disabled === true);
          assert.equal(await sheet.locator('md-filled-button').filter({hasText:'Generate'}).evaluate(el => el.disabled), true);
          assert.equal(await sheet.locator('[role="alert"]').count(), 1);
        } else {
          assert.equal(await sheet.locator('[aria-label="Die sides"]').count(), 0);
          const action = sheet.locator('md-filled-button');
          await action.click();
          await page.waitForFunction(() => !document.querySelector('.chance-body md-filled-button')?.disabled);
          assert.match(await sheet.locator('[role="img"]').getAttribute('aria-label'), id === 'dice_roll' ? /^Die showing [1-6]$/ : /^(Heads|Tails)$/);
        }
        await sheet.locator('[aria-label="Close"]').click();
        checks++;
      }
    }
  }
  // Direct object taps animate in the widget; the surrounding card opens a stable sheet.
  for (const id of ['dice_roll','coin_flip']) {
    await page.goto(`${base}/screen.html?id=quick-tools&panel=none`);
    const card = page.locator(`[data-tool="${id}"]`);
    await card.locator('.qt-chance__play').click();
    assert.equal(await page.locator('.tk-sheet-surface').count(),0);
    await page.waitForFunction(id=>!document.querySelector(`[data-tool="${id}"]`).dataset.tossing,id);
    await card.locator('.qt-live__label').click();
    const sheet = page.locator('.tk-sheet-surface');
    await sheet.waitFor();
    await page.waitForFunction(()=>document.querySelector('.tk-sheet-surface').getAnimations().every(a=>a.playState==='finished'));
    for(let repeat=0;repeat<2;repeat++) {
      const before=await sheet.boundingBox();
      await sheet.locator('.chance-body md-filled-button').click();
      const samples=await sheet.evaluate(el=>new Promise(resolve=>{
        const heights=[];const sample=()=>{heights.push(el.getBoundingClientRect().height);if(el.querySelector('[data-tossing]'))requestAnimationFrame(sample);else resolve(heights);};sample();
      }));
      assert.ok(samples.every(h=>Math.abs(h-before.height)<1),'sheet height remains stable during motion');
      assert.equal(await sheet.locator('.chance-body [data-tossing]').count(),0);
    }
    checks++;
  }
  await page.goto(`${base}/screen.html?id=numbers&panel=none`);
  await page.locator('.numbers-card').first().waitFor();
  assert.equal(await page.locator('h2').filter({hasText:'Chance'}).count(), 0);
  checks++;
  // Compose Measure presentations at every supported window and theme.
  for(const window of ['phone','fold','desktop']) for(const theme of ['light','dark']) {
    for(const activeSheet of ['ruler','protractor','counter','bill_splitter','unit_converter','reaction_test']) {
      await page.goto(`${base}/screen.html?id=numbers&window=${window}&theme=${theme}&panel=none&state=${encodeURIComponent(JSON.stringify({activeSheet}))}`);
      await page.locator('[role="dialog"]').waitFor();
      if(activeSheet==='ruler') {assert.equal(await page.locator('.tk-sheet-surface').count(),0);assert.equal(await page.locator('md-slider[range]').count(),1);}
      if(activeSheet==='counter') assert.equal(await page.locator('[aria-label="Step increment"]').count(),0);
      checks++;
    }
  }
  const openMeasure=async(activeSheet,extra={})=>{
    await page.goto(`${base}/screen.html?id=numbers&panel=none&state=${encodeURIComponent(JSON.stringify({activeSheet,...extra}))}`);
    await page.locator('[role="dialog"]').waitFor();
  };
  await openMeasure('counter',{counterValue:0});
  assert.equal(await page.locator('[aria-label="Decrease"]').evaluate(el=>el.disabled),true);
  await page.locator('.measure-actions md-filled-button').click();
  await page.waitForFunction(()=>document.querySelector('.measure-counter__value').textContent.trim()==='1');
  await page.locator('.measure-actions md-outlined-button').click();
  await page.waitForFunction(()=>document.querySelector('.measure-counter__value').textContent.trim()==='0');checks++;
  await openMeasure('ruler');
  await page.locator('md-outlined-segmented-button[label="in"]').click();
  await page.waitForFunction(()=>document.querySelector('.measure-ruler .measure-display').textContent.includes('in'));
  await page.locator('.measure-calibration md-slider').evaluate(el=>{el.value=2;el.dispatchEvent(new Event('input',{bubbles:true}));});
  await page.waitForFunction(()=>document.querySelector('.measure-calibration md-slider').value===2);checks++;
  await openMeasure('protractor');
  await page.locator('.measure-body md-slider').locator('input').focus();
  await page.keyboard.press('ArrowRight');
  await page.waitForFunction(()=>document.querySelector('.measure-display').textContent.includes('46'));checks++;
  await openMeasure('bill_splitter',{billAmount:'10.00',tipPercent:15,splitPeople:'3'});
  assert.equal((await page.locator('.measure-bill > strong').innerText()).replaceAll(',',''),'3.84');
  await page.locator('md-outlined-text-field').first().locator('input').fill('1.999');
  await page.locator('.chance-error').waitFor();
  assert.equal(await page.locator('.measure-bill').count(),0);checks++;
  await openMeasure('unit_converter',{converterCategory:'Temperature',converterFrom:'Celsius',converterTo:'Fahrenheit',converterInput:'0'});
  assert.equal(await page.locator('.measure-conversion-row md-outlined-text-field').nth(1).evaluate(el=>el.value),'32');
  await page.locator('.measure-conversion-row md-outlined-text-field').nth(1).locator('input').fill('212');
  await page.waitForFunction(()=>document.querySelector('.measure-conversion-row md-outlined-text-field').value==='100');checks++;
  await openMeasure('reaction_test',{reactionBest:0,reactionAverage:0});
  await page.locator('.measure-actions md-filled-button').click();
  await page.locator('.measure-reaction__pad--waiting').click();
  await page.locator('.measure-reaction__pad--early').waitFor();
  await page.locator('.measure-actions md-filled-button').click();
  await page.locator('.measure-reaction__pad--signal').waitFor({timeout:7000});
  await page.locator('.measure-reaction__pad--signal').click();
  await page.locator('.measure-reaction__pad--result').waitFor();
  assert.equal(await page.locator('.measure-history-chips md-assist-chip').count(),1);checks++;
  await page.goto(`${base}/screen.html?id=numbers&theme=light`);
  await page.locator('html[data-ready]').waitFor();
  assert.equal(await page.locator('mnw-device-frame').count(), 1);
  await page.locator('.numbers-card').first().click();
  await page.locator('.measure-ruler').waitFor();
  assert.equal(await page.locator('.measure-ruler').isVisible(), true);
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
  // Deterministic catalog data makes FAB geometry independent of the live API.
  for (const window of ['phone', 'fold', 'desktop']) {
    for (const navType of ['gesture', 'buttons']) {
      for (const direction of ['ltr', 'rtl']) {
        const geometry = await page.evaluate(async ({window, navType, direction}) => {
          const api = await import('/src/embed.js');
          document.documentElement.dir = direction;
          const target = document.createElement('div');
          document.body.replaceChildren(target);
          await api.mount(target, 'apps', {
            navType,
            apps: Array.from({length: 24}, (_, i) => ({id: `example.app${i}`, name: `Example ${i}`, summary: 'Fixture', icon: '', category: ''})),
          }, {frame: false, window});
          await target.querySelector('md-fab').updateComplete;
          const measure = () => {
            const screen = target.querySelector('.sc-screen');
            const pane = screen.querySelector('.sc-frame__card') ?? screen;
            const fab = screen.querySelector('.sc-fab').getBoundingClientRect();
            const bounds = pane.getBoundingClientRect();
            const nav = screen.querySelector('.sc-navbar');
            const system = screen.querySelector('.sc-sysnav');
            return {
              end: direction === 'rtl' ? fab.left - bounds.left : bounds.right - fab.right,
              bottom: (nav?.getBoundingClientRect().top ?? system?.getBoundingClientRect().top ?? bounds.bottom) - fab.bottom,
              inside: fab.left >= bounds.left && fab.right <= bounds.right && fab.top >= bounds.top,
            };
          };
          const before = measure();
          const scroller = target.querySelector('.sc-content');
          scroller.scrollTop = 100;
          return {before, after: measure(), scrollTop: scroller.scrollTop};
        }, {window, navType, direction});
        assert.ok(geometry.scrollTop > 0, `${window}/${navType}/${direction}: fixture actually scrolls`);
        for (const bounds of [geometry.before, geometry.after]) {
          assert.ok(bounds.inside, `${window}/${navType}/${direction}: FAB inside pane`);
          assert.ok(Math.abs(bounds.end - 16) < 1, `${window}/${navType}/${direction}: end gap ${bounds.end}`);
          assert.ok(Math.abs(bounds.bottom - 16) < 1, `${window}/${navType}/${direction}: bottom gap ${bounds.bottom}`);
        }
        checks++;
      }
    }
  }
  await page.evaluate(() => { document.documentElement.dir = 'ltr'; });
  assert.deepEqual(errors, []);
  await page.close();
  console.log(`${checks} marketing UI checks passed`);
} finally {
  await browser.close();
  await server.close();
}
