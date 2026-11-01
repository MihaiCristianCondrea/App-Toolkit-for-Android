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
    assert.deepEqual(await page.locator('[data-screen]').evaluateAll(nodes => nodes.map(node => node.dataset.screen)), ['quick-tools', 'numbers', 'play']);
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
  for (const id of ['quick-tools', 'numbers', 'play']) {
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
      await page.goto(`${base}/screen.html?id=play&window=${window}&theme=${theme}&panel=none`);
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
    await page.goto(`${base}/screen.html?id=play&panel=none`);
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
  {
    const waterPage=await browser.newPage({viewport:{width:1280,height:1100}});
    await waterPage.addInitScript(()=>{window.__audio={started:0,stopped:0,closed:0,frequency:0};window.AudioContext=class{destination={};createOscillator(){const frequency={value:0};return {frequency,connect(){},start(){window.__audio.started++;window.__audio.frequency=frequency.value;},stop(){window.__audio.stopped++;}};}createGain(){return {gain:{value:0},connect(){}};}resume(){return Promise.resolve();}close(){window.__audio.closed++;return Promise.resolve();}};});
    await waterPage.clock.install();
    await waterPage.goto(`${base}/screen.html?id=quick-tools&panel=none`);
    assert.equal(await waterPage.locator('.qt-chance').count(),0);
    await waterPage.locator('[data-tool="water_eject"]').click();
    const action=waterPage.locator('.water-eject md-filled-button');
    assert.equal(await waterPage.evaluate(()=>window.__audio.started),0);
    await action.click();assert.equal(await waterPage.evaluate(()=>window.__audio.frequency),165);
    await action.click();assert.equal(await waterPage.evaluate(()=>window.__audio.closed),1);
    await action.click();await waterPage.clock.fastForward(31000);
    assert.equal(await waterPage.evaluate(()=>window.__audio.closed),2);
    await action.click();await waterPage.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    await waterPage.waitForFunction(()=>window.__audio.closed===3);
    await waterPage.close();checks++;
  }
  {
    const playPage=await browser.newPage({viewport:{width:1280,height:1100},reducedMotion:'reduce',hasTouch:true});
    await playPage.goto(`${base}/screen.html?id=play&panel=none`);
    await playPage.locator('[data-tool="decision_wheel"]').click();
    await playPage.locator('.wheel-body md-filled-button').click();
    await playPage.waitForFunction(()=>['Option 1','Option 2'].includes(document.querySelector('.wheel-result')?.textContent.trim()));
    assert.equal(await playPage.locator('.wheel-body md-chip-set').count(),0);
    await playPage.locator('.wheel-body md-outlined-text-field').locator('textarea').fill('Only one');
    assert.equal(await playPage.locator('.wheel-body md-filled-button').evaluate(el=>el.disabled),true);
    await playPage.locator('.wheel-body md-outlined-text-field').locator('textarea').fill('Alpha\nBeta');
    await playPage.locator('.wheel-body md-filled-button').click();
    await playPage.waitForFunction(()=>['Alpha','Beta'].includes(document.querySelector('.wheel-result')?.textContent.trim()));
    await playPage.locator('.wheel-body md-outlined-text-field').locator('textarea').fill(Array.from({length:13},(_,i)=>String(i)).join('\n'));
    assert.equal(await playPage.locator('.wheel-body md-filled-button').evaluate(el=>el.disabled),true);
    checks++;
    for(const scroll of [0,100,200,300,400]) {
      await playPage.goto(`${base}/?id=quick-tools`);
      await playPage.locator('mnw-android-screen').evaluate((el,y)=>{el.shadowRoot.querySelector('.content').scrollTop=y;},scroll);
      await playPage.getByRole('button',{name:'Play & Decide',exact:true}).tap();
      await playPage.waitForURL(/id=play/);
      assert.equal(await playPage.locator('.tk-sheet-title').count(),0,'tab tap cannot open SOS');
      checks++;
    }
    await playPage.close();
  }
  {
    await page.goto(`${base}/screen.html?id=quick-tools&panel=none&state=${encodeURIComponent(JSON.stringify({activeToolId:'morse_sos'}))}`);
    await page.locator('.sos-primary').click();
    assert.equal(await page.locator('.sos-signal').getAttribute('data-active'),'true');
    await page.locator('.sos-primary').click();
    await page.locator('md-outlined-segmented-button[label="Morse message"]').click();
    await page.locator('md-outlined-text-field[label="Message"]').locator('input').fill('HELP1');
    assert.equal((await page.locator('.sos-code-panel .tk-morse').innerText()).trim(),'.... . .-.. .--. .----');
    await page.locator('.sos-primary').click();
    assert.equal(await page.locator('md-outlined-text-field[label="Message"]').evaluate(el=>el.disabled),true);
    await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    checks++;
  }
  // Material Colors is the last tools section, then More apps ends the page; the colours sheet names every role with
  // the hex the palette gives it.
  await page.goto(`${base}/screen.html?id=quick-tools&panel=none`);
  await page.locator('.qt-colors').waitFor();
  assert.equal(await page.locator('.qt-section:not(.qt-more-apps)').last().locator('.qt-colors').count(), 1);
  assert.equal(await page.locator('.qt-section').last().evaluate(el => el.classList.contains('qt-more-apps')), true);
  assert.equal(await page.locator('.qt-app-card').count(), 2);
    await page.goto(base + '/screen.html?' + new URLSearchParams({id: 'quick-tools', panel: 'none', state: JSON.stringify({moreApps: []})}));
    await page.locator('.qt-colors').waitFor();
    assert.equal(await page.locator('.qt-more-apps').count(), 0);
    assert.equal(await page.locator('.qt-section').last().locator('.qt-colors').count(), 1);
    checks++;
    await page.goto(base + '/screen.html?id=quick-tools&panel=none');
    await page.locator('.qt-colors').waitFor();
  await page.locator('.qt-colors').click();
  await page.waitForFunction(() => document.querySelectorAll('.mc-swatch__hex').length === 12 && [...document.querySelectorAll('.mc-swatch__hex')].every((el) => /^#[0-9A-F]{8}$/.test(el.textContent)));
  checks++;
    assert.equal(await page.locator('button.mc-swatch').count(), 12);
    await page.context().grantPermissions(['clipboard-read', 'clipboard-write']);
    const primaryHex = await page.locator('.mc-swatch__hex').first().textContent();
    await page.locator('.mc-swatch').first().press('Enter');
    await page.waitForFunction(hex => document.querySelector('.mc-sheet__feedback').textContent === `Copied ${hex}`, primaryHex);
    assert.equal(await page.evaluate(() => navigator.clipboard.readText()), primaryHex);
    // The component must reflow inside a narrow sheet, independent of the preview's fixed phone frame.
    for (const width of [320, 430, 560]) {
      await page.locator('.mc-sheet').evaluate((el, width) => { el.style.width = `${width}px`; }, width);
      assert.equal(await page.locator('.mc-sheet__body').evaluate(el => el.scrollWidth <= el.clientWidth + 1), true);
      assert.equal(await page.locator('.mc-swatch').evaluateAll(nodes => nodes.every(el => el.scrollWidth <= el.clientWidth + 1)), true);
      checks++;
    }
    await page.locator('.mc-sheet').evaluate(el => { el.style.width = ''; });
    const headerY = (await page.locator('.mc-sheet__head').boundingBox()).y;
    await page.locator('.mc-swatch').last().scrollIntoViewIfNeeded();
    assert.equal((await page.locator('.mc-sheet__head').boundingBox()).y, headerY);
    checks++;
    for (const theme of ['light', 'dark']) {
      const state = encodeURIComponent(JSON.stringify({activeToolId: 'material_colors'}));
      await page.goto(`${base}/screen.html?id=quick-tools&panel=none&theme=${theme}&state=${state}`);
      await page.waitForFunction(() => [...document.querySelectorAll('.mc-swatch__hex')].length === 12 && [...document.querySelectorAll('.mc-swatch__hex')].every(el => /^#[0-9A-F]{8}$/.test(el.textContent)));
      // Both black and white labels must meet normal-text contrast on the real scheme colours.
      const contrast = await page.locator('.mc-swatch').evaluateAll(nodes => {
        const luminance = css => {
          const ctx = document.createElement('canvas').getContext('2d');
          ctx.fillStyle = css; ctx.fillRect(0, 0, 1, 1);
          const rgb = [...ctx.getImageData(0, 0, 1, 1).data].slice(0, 3).map(v => {
            const c = v / 255; return c <= .04045 ? c / 12.92 : ((c + .055) / 1.055) ** 2.4;
          });
          return .2126 * rgb[0] + .7152 * rgb[1] + .0722 * rgb[2];
        };
        return nodes.every(el => {
          const style = getComputedStyle(el), bg = luminance(style.backgroundColor), ink = luminance(style.color);
          return (Math.max(bg, ink) + .05) / (Math.min(bg, ink) + .05) >= 4.5;
        });
      });
      assert.equal(contrast, true, `${theme} swatch text contrast`);
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
    await counter.locator('[aria-label="Add"]').click();
    assert.equal((await counter.locator('.mw-count').innerText()).trim(), '1');
    assert.equal(await page.locator('[role="dialog"]').count(), 0);
    await counter.locator('[aria-label="Subtract"]').click();
    assert.equal((await counter.locator('.mw-count').innerText()).trim(), '0');
    assert.equal(await counter.locator('[aria-label="Subtract"]').evaluate(el => el.disabled), true);
    await counter.locator('.mw-open').focus();
    await page.keyboard.press('Enter');
    await page.locator('.measure-counter .measure-counter__value').waitFor();
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
    assert.equal((await page.locator('.mw-share strong').innerText()).trim(), '$57.50');
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
  // Counter keeps its weighted shape and single-row controls as the count grows.
  for (const width of [320, 360, 430, 852]) {
    const sizes = [];
    for (const count of [0, 999, 1000000, 2147483647]) {
      sizes.push(await page.evaluate(async ({width, count}) => {
        const api = await import('/src/embed.js');
        await api.setup();
        const target = document.createElement('div');
        document.body.append(target);
        try {
          await api.mount(target, 'numbers', {counterValue: count}, {window: {width, height: 957, system: 'android'}, frame: false});
          const number = target.querySelector('.mw-count');
          const minus = target.querySelector('.mw-counter-panel > md-outlined-icon-button');
          const plus = target.querySelector('.mw-counter-panel > md-filled-icon-button');
          const style = getComputedStyle(number), bounds = number.getBoundingClientRect();
          const range = document.createRange(); range.selectNodeContents(number);
          return {fits: range.getBoundingClientRect().width <= number.clientWidth - parseFloat(style.paddingLeft) - parseFloat(style.paddingRight),
            startGap: bounds.left - minus.getBoundingClientRect().right, endGap: plus.getBoundingClientRect().left - bounds.right,
            font: parseFloat(style.fontSize), height: bounds.height};
        } finally { target.remove(); }
      }, {width, count}));
    }
    for (const size of sizes) {
      assert.ok(size.fits, 'counter digits fit inside the shape');
      assert.ok(Math.abs(size.startGap - 8) < 1 && Math.abs(size.endGap - 8) < 1, 'shape fills space between controls');
      assert.equal(size.height, sizes[0].height, 'shape height stays fixed');
    }
    assert.ok(sizes[3].font <= sizes[0].font, 'larger counts never grow the font');
    checks++;
  }
  // Currency and optional tip are shared by the sheet and overview.
  await page.goto(`${base}/screen.html?id=numbers&panel=none`);
  for (const [currency, withTip, withoutTip] of [['USD','$28.75','$25.00'],['EUR','€28.75','€25.00'],['GBP','£28.75','£25.00'],['RON','28.75 lei','25.00 lei'],['JPY','¥29','¥25'],['INR','₹28.75','₹25.00']]) {
    await page.locator('[data-tool="bill_splitter"] .mw-open').click();
    const picker = page.locator('.bill-currency');
    await picker.click();
    await picker.locator('md-select-option').filter({hasText: currency}).click();
    const checkbox = page.locator('.bill-tip-toggle md-checkbox');
    if (!await checkbox.evaluate(el => el.checked)) await checkbox.click();
    assert.equal((await page.locator('.measure-bill > strong').innerText()).trim(), withTip);
    await checkbox.click();
    assert.equal((await page.locator('.measure-bill > strong').innerText()).trim(), withoutTip);
    assert.equal(await page.locator('.bill-tip-expansion').evaluate(el => el.inert), true);
    await page.locator(SHEET_CONTENT).locator('[aria-label="Close"]').click();
    assert.equal((await page.locator('.mw-share strong').innerText()).trim(), withoutTip);
    assert.match(await page.locator('[data-tool="bill_splitter"] .mw-detail').innerText(), /No tip/);
    checks++;
  }
  await page.goto(`${base}/screen.html?id=numbers&panel=none`);
  const measureSearch = page.getByRole('searchbox', {name:'Search tools'});
  for (const [query, ids] of [['RULER',['ruler']],['tip',['bill_splitter']],['reflex',['reaction_test']],['zzzz',[]],['',['ruler','protractor','unit_converter','bill_splitter','counter','reaction_test']]]) {
    await measureSearch.fill(query);
    assert.deepEqual(await page.locator('.mw-widget').evaluateAll(nodes=>nodes.map(n=>n.dataset.tool)),ids);
    if(ids.length===1) assert.equal(await page.locator('.qt-section').count(),1);
    if(!ids.length) assert.equal(await page.locator('.measure-empty').count(),1);
  }
  checks++;
  // Compose Measure presentations at every supported window and theme.
  for(const window of ['phone','fold','desktop']) for(const theme of ['light','dark']) {
    for(const activeSheet of ['ruler','protractor','counter','bill_splitter','unit_converter','reaction_test']) {
      await page.goto(`${base}/screen.html?id=numbers&window=${window}&theme=${theme}&panel=none&state=${encodeURIComponent(JSON.stringify({activeSheet}))}`);
      await page.locator('[role="dialog"]').waitFor();
      if(activeSheet==='ruler') {assert.equal(await page.locator(SHEET).count(),0);assert.equal(await page.locator('mnw-android-screen .fullscreen').count(),1);assert.equal(await page.locator('.measure-ruler__settings[aria-hidden="true"]').count(),1);assert.equal(await page.locator('.measure-ruler__card').count(),1);}
      if(activeSheet==='protractor') { assert.equal(await page.locator(SHEET).count(),0); assert.equal(await page.locator('mnw-android-screen .fullscreen').count(),1); }
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
  await page.waitForFunction(()=>document.querySelector('.measure-counter .measure-counter__value').textContent.trim()==='1');
  await page.locator('.measure-actions md-outlined-button').click();
  await page.waitForFunction(()=>document.querySelector('.measure-counter .measure-counter__value').textContent.trim()==='0');checks++;
  // The simple ruler reads from zero to wherever it is dragged; advanced shows the range and the settings.
  await openMeasure('ruler');
  const rulerBox=await page.locator('.measure-ruler__scale').boundingBox();
  await page.mouse.move(rulerBox.x+8,rulerBox.y+rulerBox.height/4);
  await page.mouse.down();await page.mouse.up();
  await page.waitForFunction(()=>Math.abs(parseFloat(document.querySelector('.measure-ruler__card .measure-display').textContent)-parseFloat(document.querySelector('.measure-ruler__scale').clientHeight)/4/(160/25.4))<0.2);
  await page.locator('md-outlined-segmented-button[label="Advanced"]').click();
  await page.locator('.measure-ruler__panel[data-advanced] .tk-slider--range').waitFor();
  await page.locator('md-outlined-segmented-button[label="Imperial"]').click();
  await page.waitForFunction(()=>document.querySelector('.measure-ruler .measure-display').textContent.includes('in'));
  // Calibrating rescales the ruler itself, so the slider stays where it is: nothing in the panel grows.
  const calibration=page.locator('.measure-calibration .tk-slider');
  await calibration.locator('input').focus();
  await calibration.scrollIntoViewIfNeeded();
  const calibrationTop=(await calibration.boundingBox()).y;
  await page.keyboard.press('End');
  await page.waitForFunction(()=>document.querySelector('.measure-calibration .tk-slider input').value==='2');
  assert.equal((await calibration.boundingBox()).y,calibrationTop);checks++;
  await openMeasure('protractor');
  await page.locator('.measure-body .tk-slider input').focus();
  await page.keyboard.press('ArrowRight');
  await page.waitForFunction(()=>document.querySelector('.measure-display').textContent.includes('46'));checks++;
  const dial = page.locator('.measure-protractor');
  const geometry = await dial.evaluate(el => ({box: el.getBoundingClientRect().toJSON(), width: el.viewBox.baseVal.width, height: el.viewBox.baseVal.height}));
  const centerY = Math.max(140,(geometry.height-204)/2);
  await page.mouse.click(geometry.box.x + 100 * geometry.box.width / geometry.width, geometry.box.y + centerY * geometry.box.height / geometry.height);
  await page.waitForFunction(()=>document.querySelector('.measure-display').textContent.includes('90'));
  await page.locator('[aria-label="Set angle to 180 degrees"]').click();
  assert.equal((await page.locator('.protractor-kind').innerText()).trim(),'Straight');
  await page.locator('.measure-protractor-dialog [aria-label="Close"]').click();
  assert.equal(await page.locator('[role="dialog"]').count(),0);
  checks++;
  await openMeasure('bill_splitter',{billAmount:'10.00',tipPercent:15,splitPeople:'3'});
  assert.equal((await page.locator('.measure-bill > strong').innerText()).replaceAll(',',''),'$3.84');
  await page.locator('md-outlined-text-field').first().locator('input').fill('1.999');
  await page.locator('.chance-error').waitFor();
  assert.equal(await page.locator('.measure-bill').count(),0);checks++;
  await openMeasure('unit_converter',{converterCategory:'Temperature',converterFrom:'Celsius',converterTo:'Fahrenheit',converterInput:'0'});
  assert.equal(await page.locator('.measure-conversion-row md-outlined-text-field').nth(1).evaluate(el=>el.value),'32');
  await page.locator('.measure-conversion-row md-outlined-text-field').nth(1).locator('input').fill('212');
  await page.waitForFunction(()=>document.querySelector('.measure-conversion-row md-outlined-text-field').value==='100');checks++;
  await page.locator('[aria-label="Swap units"]').click();
  assert.deepEqual(await page.locator('.measure-conversion-row md-outlined-text-field').evaluateAll(nodes=>nodes.map(n=>n.value)),['212','100']);
  await page.locator('.measure-conversion-row md-outlined-text-field').first().locator('input').fill('32');
  await page.waitForFunction(()=>document.querySelectorAll('.measure-conversion-row md-outlined-text-field')[1].value==='0');
  checks++;
  const unitSelect = page.locator('.measure-unit-picker md-outlined-select').first();
  await unitSelect.click();
  await unitSelect.locator('md-select-option').filter({hasText:'Kelvin'}).click();
  assert.equal(await unitSelect.evaluate(el=>el.value),'Kelvin');
  await page.locator('.converter-swap').hover();
  await page.locator('.converter-swap').click();
  await page.mouse.move(0,0);
  assert.equal(await page.locator('.converter-swap').evaluate(el=>el.matches(':active')),false);
  checks++;
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
      for (const activeToolId of ['compass', 'bubble_level', 'flash_dimmer', 'morse_sos', 'breathing', 'water_eject']) states.push(['quick-tools', {activeToolId}]);
      for(const activeToolId of ['random_number','dice_roll','coin_flip','decision_wheel']) states.push(['play',{activeToolId}]);
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
  {
    const landscape = await page.evaluate(async()=>{
      const api=await import('/src/embed.js');await api.setup();const target=document.createElement('div');document.body.append(target);
      try {await api.mount(target,'numbers',{activeSheet:'protractor'},{window:{width:957,height:430,system:'android'},frame:false});
        const dialog=target.querySelector('.measure-protractor-dialog'),controls=dialog.querySelector('.measure-protractor-controls'),bounds=dialog.getBoundingClientRect(),box=controls.getBoundingClientRect();
        return {landscape:dialog.hasAttribute('data-landscape'),titleIcons:dialog.querySelectorAll('header > md-icon').length,inside:box.right<=bounds.right&&box.bottom<=bounds.bottom&&box.top>=bounds.top};
      }finally{target.remove();}
    });
    assert.deepEqual(landscape,{landscape:true,titleIcons:0,inside:true});checks++;
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
  assert.deepEqual(errors, []);
  await page.close();
  // Every overview uses the same ad preview and suppresses it during search and clean captures.
  {
    const adPage = await browser.newPage({viewport: {width: 430, height: 1000}});
    const adErrors = [];
    adPage.on('pageerror', error => adErrors.push(error.message));
    for (const theme of ['light', 'dark']) {
      for (const id of ['quick-tools', 'numbers', 'play']) {
        for (const mode of ['on', 'off', 'search']) {
          const params = new URLSearchParams({id, theme, window: 'phone', frame: 'none', panel: 'none'});
          if (mode !== 'off') params.set('ads', 'on');
          if (mode === 'search') params.set('state', JSON.stringify({searchQuery: id === 'numbers' ? 'unit' : id === 'play' ? 'dice' : 'compass'}));
          await adPage.goto(base + '/screen.html?' + params);
          await adPage.locator('mnw-android-screen').waitFor();
          assert.equal(await adPage.locator('[data-native-ad]').count(), mode === 'on' ? 1 : 0, id + ' ' + theme + ' ' + mode);
          if (mode === 'on') {
            const order = await adPage.locator('[data-native-ad]').evaluate(el => ({
              before: el.previousElementSibling?.querySelector('h2')?.textContent ?? '',
              after: el.nextElementSibling?.querySelector('h2')?.textContent ?? '',
            }));
            assert.match(order.before, id === 'numbers' ? /Convert & split/ : id === 'play' ? /Chance/ : /Light & signals/);
            if (id === 'numbers') assert.match(order.after, /Count & time/);
              if (id === 'quick-tools') assert.match(order.after, /Hardware & sound/);
          }
          checks++;
        }
      }
    }
    assert.deepEqual(adErrors, []);
    await adPage.close();
  }
  console.log(`${checks} marketing UI checks passed`);
} finally {
  await browser.close();
  await server.close();
}
