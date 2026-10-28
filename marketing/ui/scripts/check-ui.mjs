import assert from 'node:assert/strict';
import {mkdir} from 'node:fs/promises';
import {fileURLToPath} from 'node:url';
import {createServer} from 'vite';
import {chromium} from 'playwright-core';

const root = fileURLToPath(new URL('../', import.meta.url));
const server = await createServer({root, server: {host: '127.0.0.1', port: 0, open: false}});
const browser = await chromium.launch(process.env.CHROMIUM_PATH ? {executablePath: process.env.CHROMIUM_PATH} : {});
let checks = 0;
// Material Next's Android preview draws the shell in a shadow root: `.sheet` is its surface, and what a sheet
// contains is the screen's own content, slotted as [slot="overlay"].
const SHEET = 'mnw-android-screen .sheet';
const SHEET_CONTENT = 'mnw-android-screen > [slot="overlay"]';
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
    assert.equal(await page.locator('mnw-android-screen').count(), 3);
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
        assert.equal(await page.locator('mnw-android-screen').count(), 1);
        const expected = {phone: [430, 957], fold: [852, 883], desktop: [1218, 720]}[window];
        const size = await page.locator('mnw-android-screen').evaluate(el => [el.offsetWidth, el.offsetHeight]);
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
        await page.locator(SHEET).waitFor();
        const sheet = page.locator(SHEET_CONTENT);
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
    assert.equal(await page.locator(SHEET).count(),0);
    await page.waitForFunction(id=>!document.querySelector(`[data-tool="${id}"]`).dataset.tossing,id);
    await card.locator('.qt-live__label').click();
    const surface = page.locator(SHEET);
    await surface.waitFor();
    await page.waitForFunction(()=>document.querySelector('mnw-android-screen').shadowRoot.querySelector('.sheet').getAnimations().every(a=>a.playState==='finished'));
    const sheet = page.locator(SHEET_CONTENT);
    for(let repeat=0;repeat<2;repeat++) {
      const before=await surface.boundingBox();
      await sheet.locator('.chance-body md-filled-button').click();
      const samples=await surface.evaluate(el=>new Promise(resolve=>{
        const host=el.getRootNode().host;
        const heights=[];const sample=()=>{heights.push(el.getBoundingClientRect().height);if(host.querySelector('[data-tossing]'))requestAnimationFrame(sample);else resolve(heights);};sample();
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
  // Overview actions do not open a sheet, and its summaries follow edits made in the tools.
  for (const window of ['phone', 'fold', 'desktop']) {
    await page.goto(`${base}/screen.html?id=numbers&window=${window}&panel=none&state=${encodeURIComponent(JSON.stringify({counterValue: 0}))}`);
    const counter = page.locator('[data-tool="counter"]');
    await counter.locator('md-filled-button').click();
    assert.equal((await counter.locator('.mw-count').innerText()).trim(), '1');
    assert.equal(await page.locator('[role="dialog"]').count(), 0);
    await counter.locator('[aria-label="Subtract"]').click();
    assert.equal((await counter.locator('.mw-count').innerText()).trim(), '0');
    assert.equal(await counter.locator('[aria-label="Subtract"]').evaluate(el => el.disabled), true);
    await counter.locator('.mw-open').focus();
    await page.keyboard.press('Enter');
    await page.locator('.measure-counter__value').waitFor();
    await page.locator('.measure-actions md-filled-button').click();
    await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    assert.equal((await counter.locator('.mw-count').innerText()).trim(), '1');
    await page.locator('[data-tool="unit_converter"] .mw-open').click();
    await page.locator('.measure-conversion-row md-outlined-text-field').first().locator('input').fill('2');
    await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    assert.match(await page.locator('.mw-conversion').innerText(), /2\s*m[\s\S]*200\s*cm/);
    await page.locator('[data-tool="bill_splitter"] .mw-open').click();
    await page.locator('md-outlined-text-field[label="Bill amount"]').locator('input').fill('200');
    await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    assert.equal((await page.locator('.mw-share strong').innerText()).trim(), '57.50');
    checks++;
  }
  // The reaction card only opens; its Start action opens and begins a timed round.
  await page.goto(`${base}/screen.html?id=numbers&panel=none`);
  const reactionCard = page.locator('[data-tool="reaction_test"]');
  await reactionCard.locator('.mw-open').click();
  await page.locator('.measure-reaction__pad--idle').waitFor();
  await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
  await reactionCard.locator('md-filled-tonal-button').click();
  await page.locator('.measure-reaction__pad--waiting').waitFor();
  await page.locator('.measure-reaction__pad--signal').waitFor({timeout: 7000});
  await page.locator('.measure-reaction__pad').click();
  await page.locator('.measure-reaction__pad--result').waitFor();
  assert.equal(await page.locator('.measure-history-chips md-assist-chip').count(), 1);
  await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
  await reactionCard.locator('md-filled-tonal-button').focus();
  await page.keyboard.press('Enter');
  await page.locator('.measure-reaction__pad--waiting').waitFor();
  await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
  await reactionCard.locator('.mw-open').click();
  await page.locator('.measure-reaction__pad--idle').waitFor();
  await page.waitForTimeout(5200);
  assert.equal(await page.locator('.measure-reaction__pad--idle').count(), 1, 'closing cancels the running round');
  await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
  checks++;
  // Compose Measure presentations at every supported window and theme.
  for(const window of ['phone','fold','desktop']) for(const theme of ['light','dark']) {
    for(const activeSheet of ['ruler','protractor','counter','bill_splitter','unit_converter','reaction_test']) {
      await page.goto(`${base}/screen.html?id=numbers&window=${window}&theme=${theme}&panel=none&state=${encodeURIComponent(JSON.stringify({activeSheet}))}`);
      await page.locator('[role="dialog"]').waitFor();
      if(activeSheet==='ruler') {assert.equal(await page.locator(SHEET).count(),0);assert.equal(await page.locator('mnw-android-screen .fullscreen').count(),1);assert.equal(await page.locator('.tk-slider--range').count(),1);}
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
  // The calibration slider stays where it is while the reference above it grows (the card keeps its height).
  const calibration=page.locator('.measure-calibration .tk-slider');
  const calibrationTop=(await calibration.boundingBox()).y;
  await calibration.locator('input').focus();
  await page.keyboard.press('End');
  await page.waitForFunction(()=>document.querySelector('.measure-calibration .tk-slider input').value==='2');
  assert.equal((await calibration.boundingBox()).y,calibrationTop);checks++;
  await openMeasure('protractor');
  await page.locator('.measure-body .tk-slider input').focus();
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
  assert.equal(await page.locator('mnw-android-screen').count(), 2);
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
    const shell = target.querySelector('mnw-android-screen');
    return {screen: !!shell, sheet: !!shell?.shadowRoot.querySelector('.sheet'), frame: target.querySelector('mnw-device-frame')?.device};
  });
  assert.deepEqual(embedded, {screen: true, sheet: true, frame: 'pixel_9_pro_fold'});
  checks++;
  // One identity names the app everywhere: the drawer header, the hub and the pickers read it, and a host that
  // moves the asset base (Remotion's staticFile root) moves the drawer's mark with it.
  {
    const identity = await page.evaluate(async () => {
      const api = await import('/src/embed.js');
      const {PRODUCT_CONFIG} = await import('/src/config/product.js');
      const {PALETTES} = await import('/src/controls.js');
      const header = async (assetBase) => {
        await api.setup({assetBase});
        const target = document.createElement('div');
        document.body.replaceChildren(target);
        await api.mount(target, 'numbers', {drawerOpen: true}, {frame: false, window: 'phone'});
        const el = target.querySelector('mnw-android-screen').shadowRoot.querySelector('.drawer__header');
        return {name: el.textContent.trim(), logo: el.querySelector('img').getAttribute('src')};
      };
      const moved = await header('/kit-base/');
      const home = await header('/');
      return {
        moved, home,
        name: api.IDENTITY.config.name,
        product: PRODUCT_CONFIG.name === api.IDENTITY.config.name && PRODUCT_CONFIG.icon === '/logos/icon.svg',
        palettes: PALETTES === api.IDENTITY.palettes,
        typeface: getComputedStyle(document.documentElement).getPropertyValue('--md-ref-typeface-plain').trim(),
      };
    });
    assert.deepEqual(identity, {
      moved: {name: 'App Toolkit', logo: '/kit-base/logos/app_logo.svg'},
      home: {name: 'App Toolkit', logo: '/logos/app_logo.svg'},
      name: 'App Toolkit',
      product: true,
      palettes: true,
      typeface: '"Google Sans Flex", system-ui, sans-serif',
    });
    checks++;
  }
  // Videos mount screens without set(): every preset, every sheet and the navigation surfaces must render
  // without errors, including tools that measure themselves after rendering (Ruler, Protractor).
  {
    const failures = await page.evaluate(async () => {
      const api = await import('/src/embed.js');
      const failures = [];
      const states = [];
      for (const screen of api.SCREENS) {
        const presets = screen.controls?.filter((c) => c.type === 'presets').flatMap((c) => c.options.map(([, patch]) => patch)) ?? [];
        for (const patch of [{}, ...presets, {drawerOpen: true}, {railOpen: true}, {overflowOpen: true}]) states.push([screen.id, patch]);
      }
      for (const activeSheet of ['ruler', 'protractor', 'counter', 'bill_splitter', 'unit_converter', 'reaction_test']) states.push(['numbers', {activeSheet}]);
      for (const activeToolId of ['compass', 'bubble_level', 'flash_dimmer', 'morse_sos', 'breathing', 'random_number', 'dice_roll', 'coin_flip']) states.push(['quick-tools', {activeToolId}]);
      const onError = (e) => failures.push(String(e.message ?? e.reason));
      addEventListener('error', onError);
      addEventListener('unhandledrejection', onError);
      for (const [id, state] of states) {
        for (const window of ['phone', 'fold']) {
          const target = document.createElement('div');
          document.body.replaceChildren(target);
          try {
            await api.mount(target, id, state, {frame: false, window});
            // Let measurement callbacks (requestAnimationFrame) run, as a video frame would.
            await new Promise((r) => requestAnimationFrame(() => requestAnimationFrame(r)));
            const shell = target.querySelector('mnw-android-screen');
            if (!shell || shell.interactive) failures.push(`${id} ${JSON.stringify(state)} ${window}: no static shell`);
          } catch (e) {
            failures.push(`${id} ${JSON.stringify(state)} ${window}: ${e.message}`);
          }
        }
      }
      removeEventListener('error', onError);
      removeEventListener('unhandledrejection', onError);
      return failures;
    });
    assert.deepEqual(failures, []);
    checks++;
  }
  // Without set() nothing measures the page: the ruler's scale and the protractor still have to be drawn
  // at their real size, or a capture shows the wrong millimetres.
  for (const window of ['phone', 'fold', 'desktop']) {
    const sizes = await page.evaluate(async (window) => {
      const api = await import('/src/embed.js');
      const measure = async (activeSheet, selector) => {
        const target = document.createElement('div');
        document.body.replaceChildren(target);
        await api.mount(target, 'numbers', {activeSheet}, {frame: false, window});
        const el = target.querySelector(selector);
        const [, , w, h] = el.getAttribute('viewBox').split(' ').map(Number);
        return {drawn: [w, h], shown: [el.clientWidth, el.clientHeight]};
      };
      return {ruler: await measure('ruler', '.measure-ruler__scale'), protractor: await measure('protractor', '.measure-protractor')};
    }, window);
    assert.ok(Math.abs(sizes.ruler.drawn[1] - sizes.ruler.shown[1]) < 1, `${window}: ruler drawn ${sizes.ruler.drawn[1]}px tall, shown ${sizes.ruler.shown[1]}px`);
    assert.ok(Math.abs(sizes.protractor.drawn[0] - sizes.protractor.shown[0]) < 1, `${window}: protractor drawn ${sizes.protractor.drawn[0]}px wide, shown ${sizes.protractor.shown[0]}px`);
    checks++;
  }
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
            const screen = target.querySelector('mnw-android-screen');
            const root = screen.shadowRoot;
            const pane = root.querySelector('.frame__card') ?? screen;
            const fab = screen.querySelector('[slot="fab"] md-fab').getBoundingClientRect();
            const bounds = pane.getBoundingClientRect();
            const nav = root.querySelector('.navbar');
            const system = root.querySelector('.sysnav');
            return {
              end: direction === 'rtl' ? fab.left - bounds.left : bounds.right - fab.right,
              bottom: (nav?.getBoundingClientRect().top ?? system?.getBoundingClientRect().top ?? bounds.bottom) - fab.bottom,
              inside: fab.left >= bounds.left && fab.right <= bounds.right && fab.top >= bounds.top,
            };
          };
          const before = measure();
          const scroller = target.querySelector('mnw-android-screen').shadowRoot.querySelector('.content');
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
