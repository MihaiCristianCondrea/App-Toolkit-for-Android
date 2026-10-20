import { setup, mount } from '../../../../../../ui/src/embed.js';
import { render, html } from '../../../../../../ui/node_modules/lit/index.js';
import './store.css';

// What each screenshot mounts from the UI kit (real screens, real states). Copy comes from strings/<locale>.json.
const noop = { go: () => {}, set: () => {} };
const context = { id: '20', title: 'Implementing Parallax Scrolling in Jetpack Compose', type: 'lesson' };
const SCREENS = {
  news: ['home-feed', { section: 'news', filter: 'all', query: '' }],
  guides: ['home-feed', { section: 'guides', filter: 'all', query: '' }],
  reader: ['article-detail', { articleId: '20', section: 'guides' }],
  'studio-bot': ['studio-bot', { conversationId: 1 }],
  attached: ['studio-bot', { messages: [], articleContext: context, returnArticleState: {}, draft: '' }],
  search: ['home-feed', { section: 'guides', query: 'Compose', filter: 'all' }],
  favorites: ['home-feed', { section: 'guides', filter: 'favorites', query: '' }],
  conversations: ['conversations', { showAds: false, conversations: [
    { id: 1, title: 'Parallax scrolling in Compose', snippet: 'Use graphicsLayer with translationY = scrollState.value * 0.5f to achieve 120 FPS depth without recomposition.', messages: '2 messages', time: '10 min. ago' },
    { id: 2, title: 'Material 3 Date Picker', snippet: 'Wrap DatePicker inside DatePickerDialog with confirm and dismiss buttons to handle locale formatting.', messages: '4 messages', time: '2 hr. ago' },
    { id: 3, title: 'Chips for complex filters', snippet: 'Use FilterChip inside a FlowRow so the chips wrap on small screens and keep their selected state.', messages: '6 messages', time: 'Yesterday' },
    { id: 4, title: 'View Binding setup', snippet: 'Enable viewBinding in build.gradle.kts, then inflate the generated binding class in onCreate.', messages: '3 messages', time: 'Oct 12' },
  ] }],
};

const params = new URLSearchParams(location.search);
const only = params.get('shot');
// One source for every language: the captions and alt text come from strings/<locale>.json.
const strings = await (await fetch(`/strings/${params.get('locale') || 'en-US'}.json`)).json();
const target = document.querySelector('#store');
await setup({ palette: 'android', theme: 'light' });

const shape = (name, cls) => html`<i class="shape ${cls}" style="--m:url('/assets/shapes/${name}.svg')"></i>`;

if (!only || only === 'feature-graphic') {
  const fg = document.createElement('article');
  fg.className = 'feature';
  fg.dataset.shot = 'feature-graphic';
  render(html`
    ${shape('cookie_12sided', 'f1')}${shape('clover_4leaf', 'f2')}${shape('soft_burst', 'f3')}
    <div class="feature__text"><h1>${strings.featureGraphic.title}</h1><p>${strings.featureGraphic.tagline}</p></div>
    <div class="feature__ui feature__ui--left"><div data-product></div></div>
    <div class="feature__ui feature__ui--right"><div data-product></div></div>`, fg);
  target.append(fg);
  const [left, right] = fg.querySelectorAll('[data-product]');
  await mount(left, 'home-feed', { ...noop, section: 'guides', filter: 'all', query: '' });
  await mount(right, 'studio-bot', { ...noop });
}

if (!only || only === 'icon') {
  const icon = document.createElement('article');
  icon.className = 'icon';
  icon.dataset.shot = 'icon';
  // The app's launcher foreground vector (app/src/main/res/drawable-anydpi/ic_launcher_foreground.xml) on a transparent
  // ground: Google Play keeps the alpha, and the robot over the book stands on its own without the launcher's white.
  icon.innerHTML = `<svg viewBox="0 0 432 432" width="512" height="512">
    <g transform="translate(123.6 108.3) scale(0.5)">
      <path fill="#00A86B" d="M22.8,253.9c0,-10 5,-13.7 15,-11l127,35c8,2 12,8.3 12,19v123c0,9.3 -4.7,12.7 -14,10l-122,-33c-12,-3.3 -18,-10.7 -18,-22L22.8,253.9z"/>
      <path fill="none" stroke="#FFFFFF" stroke-linecap="round" stroke-width="16" d="M54.8,345.9l83,22"/>
      <path fill="none" stroke="#FFFFFF" stroke-linecap="round" stroke-width="16" d="M54.8,304.9l83,22"/>
      <path fill="#8BE8A8" d="M346.8,253.9c0,-10 -5,-13.7 -15,-11l-127,35c-8,2 -12,8.3 -12,19v123c0,9.3 4.7,12.7 14,10l122,-33c12,-3.3 18,-10.7 18,-22L346.8,253.9z"/>
      <path fill="#3DDC84" d="M368.3,218.5h1.3c-2.8,-29.4 -11.8,-56.1 -26.9,-80.2s-34.6,-43.7 -58.4,-58.8l32.3,-54.2c2.2,-3.9 2.8,-8.1 1.7,-12.6c-1.1,-4.5 -3.6,-7.8 -7.6,-10.1c-3.9,-2.5 -8.1,-3.2 -12.6,-2.1c-4.5,1.1 -7.8,3.6 -10.1,7.6l-33.6,55.9c-10.6,-4.5 -21.8,-7.8 -33.4,-10.1c-11.6,-2.2 -23.7,-3.4 -36.3,-3.4s-24.7,1.1 -36.3,3.4s-22.8,5.6 -33.4,10.1l-33.6,-55.9c-2.2,-3.9 -5.6,-6.4 -10.1,-7.6c-4.5,-1.1 -8.7,-0.4 -12.6,2.1c-3.9,2.2 -6.5,5.6 -7.8,10.1c-1.3,4.5 -0.6,8.7 1.9,12.6l32.3,54.2c-23.8,15.1 -43.3,34.7 -58.4,58.8S2.8,189.1 0,218.5h1.1l173,45.5c7,1.8 14.3,1.8 21.3,0L368.3,218.5zM252,143.7c0.6,-4.8 2.7,-8.4 6.3,-10.9c3.6,-2.5 7.7,-3.1 12.2,-1.7c4.5,1.4 8.3,4.5 11.3,9.2c3.1,4.8 4.4,9.6 4,14.5c-0.4,4.9 -2.5,8.6 -6.1,11.1c-3.6,2.5 -7.8,3 -12.4,1.5c-4.6,-1.5 -8.5,-4.7 -11.5,-9.5C252.7,153.2 251.4,148.5 252,143.7zM87.8,140.4c3.1,-4.8 6.9,-7.9 11.6,-9.5c4.6,-1.5 8.8,-1 12.4,1.5c3.6,2.5 5.7,6.2 6.1,11.1c0.4,4.9 -0.9,9.7 -4,14.5s-6.9,7.8 -11.3,9.2c-4.5,1.4 -8.5,0.8 -12.2,-1.7c-3.6,-2.5 -5.7,-6.2 -6.3,-10.9S84.7,145.1 87.8,140.4z"/>
    </g></svg>`;
  target.append(icon);
}

// The phone screenshots are cut from one panorama (8 x 1080 px wide): one ground, and shapes placed across the seams,
// so the images read as one continuous strip when Play shows them side by side.
const WIDTH = 1080;
// Where the framed phone sits in each screenshot (store.css .phone: 469 x 992 at scale 1.48, centred, from y 404).
const PHONE = { x0: 540 - (469 * 1.48) / 2, x1: 540 + (469 * 1.48) / 2, y0: 404 };
const SEAMS = [
  // [shape, size, y, colour token, rotation]: shapes centred on the seams between two screenshots, a large one low and a
  // small one high, alternating, so every pair of neighbours shares a shape.
  ['cookie_12sided', 1040, 1300, 'primary', 12], ['clover_4leaf', 300, 330, 'surface', -8],
  ['soft_burst', 980, 1560, 'surface', 20], ['flower', 280, 260, 'primary', -14],
  ['clover_8leaf', 1040, 1240, 'secondary-container', 6], ['puffy', 320, 340, 'surface', -10],
  ['cookie_9sided', 1000, 1500, 'primary', 16], ['burst', 300, 280, 'secondary-container', 8],
  ['flower', 1040, 1280, 'surface', -6], ['cookie_12sided', 280, 320, 'primary', 10],
  ['clover_4leaf', 980, 1540, 'secondary-container', 14], ['soft_burst', 300, 270, 'surface', -12],
  ['puffy', 1040, 1300, 'primary', -4], ['clover_8leaf', 300, 330, 'secondary-container', 18],
];
const shots = strings.screenshots.filter((_, i) => !only || only === 'panorama' || only === String(i + 1));
if (shots.length) {
  const pano = document.createElement('section');
  pano.className = 'pano';
  pano.style.width = `${strings.screenshots.length * WIDTH}px`;
  // Scattered shapes fill every gap (the top band included): a seeded layout, so every render is the same. They keep
  // clear of the captions and may run behind the phones and off the edges.
  let seed = 7;
  const rand = () => ((seed = (seed * 16807) % 2147483647) / 2147483647);
  const NAMES = ['diamond', 'cookie_4sided', 'gem', 'triangle', 'semi_circle', 'slanted', 'cookie_6sided', 'fan', 'boom', 'pill', 'sunny', 'arch', 'clam_shell'];
  // No near-black tone (on-primary-container): small dark shapes read as holes in the ground.
  const COLORS = ['primary', 'primary-container', 'inverse-primary', 'secondary', 'secondary-container'];
  const captionClear = (x, y, size) => { const lx = x % WIDTH; return !(y + size > 60 && y < 330 && lx + size > 40 && lx < 860); };
  // Shapes never overlap: every placed shape (seam shapes, waves, dot grids, the scatter) is an obstacle, measured as a
  // circle around its centre with a margin.
  const WAVES = [[880, 40, 340, 8], [2980, 1700, 380, -12], [5100, 60, 320, 4], [7300, 1760, 360, 10], [8380, 420, 300, -80]];
  const DOTS = [[960, 1840], [3150, 380], [4300, 1820], [6420, 400], [7580, 1830]];
  const taken = [
    ...SEAMS.map(([, size, y], i) => ({ x: (Math.floor(i / 2) + 1) * WIDTH - (i % 2 ? 130 : 0), y, r: size / 2 })),
    ...WAVES.map(([x, y, w]) => ({ x: x + w / 2, y: y + 30, r: w / 2 })),
    ...DOTS.map(([x, y]) => ({ x: x + 36, y: y + 36, r: 50 })),
  ];
  const scatter = [];
  for (let n = 0; scatter.length < strings.screenshots.length * 7 && n < 4000; n++) {
    const size = 90 + rand() * 230;
    const x = rand() * (strings.screenshots.length * WIDTH) - size / 2;
    const y = rand() * 1920 - size / 2;
    if (!captionClear(x, y, size)) continue;
    // A scattered shape sits whole inside one screenshot (only seam shapes cross a seam or an edge) and never touches
    // the phone: a shape half hidden behind the device or cut by the image's edge looks like a mistake.
    const lx = x - Math.floor((x + size / 2) / WIDTH) * WIDTH;
    if (lx < 24 || lx + size > WIDTH - 24 || y < 24 || y + size > 1920 - 24) continue;
    if (lx + size > PHONE.x0 - 32 && lx < PHONE.x1 + 32 && y + size > PHONE.y0 - 32) continue;
    const c = { x: x + size / 2, y: y + size / 2, r: size / 2 };
    if (taken.some((o) => Math.hypot(o.x - c.x, o.y - c.y) < o.r + c.r + 40)) continue;
    taken.push(c);
    scatter.push({ x, y, size, name: NAMES[scatter.length % NAMES.length], color: COLORS[(scatter.length * 5) % COLORS.length], rot: rand() * 360 });
  }
  const wave = (x, y, w, rot) => html`<svg class="wave" style="left:${x}px;top:${y}px;width:${w}px;transform:rotate(${rot}deg)" viewBox="0 0 400 60" fill="none"><path d="M0 30 q25 -30 50 0 t50 0 t50 0 t50 0 t50 0 t50 0 t50 0 t50 0" stroke="currentColor" stroke-width="5" stroke-linecap="round"/></svg>`;
  const dots = (x, y) => html`<svg class="dots" style="left:${x}px;top:${y}px" width="72" height="72" viewBox="0 0 72 72">${[0, 1, 2].flatMap((r) => [0, 1, 2].map((c) => html`<circle cx=${12 + c * 24} cy=${12 + r * 24} r="7"/>`))}</svg>`;
  render(html`
    ${scatter.map((o) => html`<i class="shape" style="--m:url('/assets/shapes/${o.name}.svg');--c:var(--md-sys-color-${o.color});width:${o.size}px;height:${o.size}px;left:${o.x}px;top:${o.y}px;transform:rotate(${o.rot}deg)"></i>`)}
    ${WAVES.map(([x, y, w, r]) => wave(x, y, w, r))}
    ${DOTS.map(([x, y]) => dots(x, y))}
    ${SEAMS.map(([name, size, y, color, rot], i) => html`<i class="shape seam" style="--m:url('/assets/shapes/${name}.svg');--c:var(--md-sys-color-${color});width:${size}px;height:${size}px;left:${(Math.floor(i / 2) + 1) * WIDTH - size / 2 - (i % 2 ? 130 : 0)}px;top:${y - size / 2}px;transform:rotate(${rot}deg)"></i>`)}
    ${strings.screenshots.map((s, index) => html`
      <article class="shot" data-shot=${String(index + 1)} data-id=${s.id} style="left:${index * WIDTH}px">
        <h1 class="caption">${s.lines.map((line, i) => html`<span class="line ${i === s.pill ? 'line--pill' : ''}">${i === s.pill ? html`<b>${line}</b>` : line}</span>`)}</h1>
        <div class="phone"><div data-product></div></div>
      </article>`)}`, pano);
  target.append(pano);
  for (const [index, s] of strings.screenshots.entries()) {
    const [id, state] = SCREENS[s.id];
    await mount(pano.querySelectorAll('[data-product]')[index], id, { ...noop, ...state }, { frame: true });
  }
}

// Tablet and desktop screenshots: the app's wide layout (kit windows "fold" and "desktop") in its device, the Pixel 9
// Pro Fold open or the Pixelbook Go, centred and unscaled on 1920 x 1080 canvases captured at 2x (3840 x 2160, 16:9).
// Each set is cut from one strip like the phone set: one ground, shapes on the seams, and a seeded scatter that sits
// whole inside one image, clear of the device and of every other shape. No captions: Play wants large-screen
// screenshots to show the app itself.
const CANVAS = { w: 1920, h: 1080 };
const STRIPS = {
  tablet: { list: strings.tabletScreenshots ?? [], window: 'fold', device: [904.53, 933.27], seed: 11 },
  desktop: { list: strings.desktopScreenshots ?? [], window: 'desktop', device: [1459.47, 973.2], seed: 23 },
};
const SHAPE_NAMES = ['diamond', 'cookie_4sided', 'gem', 'triangle', 'semi_circle', 'slanted', 'cookie_6sided', 'fan', 'pill', 'sunny', 'arch', 'clam_shell'];
const SHAPE_COLORS = ['primary', 'primary-container', 'inverse-primary', 'secondary', 'secondary-container'];
for (const [kind, set] of Object.entries(STRIPS)) {
  if ((only && only !== kind) || !set.list.length) continue;
  const n = set.list.length;
  const [dw, dh] = set.device;
  const dx = (CANVAS.w - dw) / 2;
  const dy = (CANVAS.h - dh) / 2;
  let seed = set.seed;
  const rand = () => ((seed = (seed * 16807) % 2147483647) / 2147483647);
  // Seam shapes fill the gap between two devices, a large one and a small one, never reaching a device.
  const gap = CANVAS.w - dw;
  const seams = Array.from({ length: n - 1 }, (_, i) => {
    const big = Math.min(640, gap - 80);
    return [
      { x: (i + 1) * CANVAS.w, y: i % 2 ? 300 : 760, size: big, name: ['cookie_12sided', 'clover_8leaf', 'soft_burst'][i % 3], color: i % 2 ? 'primary' : 'secondary-container', rot: 10 * (i + 1) },
      { x: (i + 1) * CANVAS.w + (i % 2 ? 40 : -60), y: i % 2 ? 880 : 170, size: Math.min(220, gap / 2 - 40), name: ['flower', 'puffy', 'burst'][i % 3], color: 'surface', rot: -12 },
    ];
  }).flat();
  const devices = set.list.map((_, i) => ({ x0: i * CANVAS.w + dx - 32, x1: i * CANVAS.w + dx + dw + 32, y0: dy - 32, y1: dy + dh + 32 }));
  const taken = seams.map((o) => ({ x: o.x, y: o.y, r: o.size / 2 }));
  const scatter = [];
  for (let k = 0; scatter.length < n * 6 && k < 6000; k++) {
    const size = 80 + rand() * 160;
    const x = rand() * n * CANVAS.w - size / 2;
    const y = rand() * CANVAS.h - size / 2;
    const lx = x - Math.floor((x + size / 2) / CANVAS.w) * CANVAS.w;
    if (lx < 24 || lx + size > CANVAS.w - 24 || y < 24 || y + size > CANVAS.h - 24) continue;
    const c = { x: x + size / 2, y: y + size / 2, r: size / 2 };
    if (devices.some((d) => c.x + c.r > d.x0 && c.x - c.r < d.x1 && c.y + c.r > d.y0 && c.y - c.r < d.y1)) continue;
    if (taken.some((o) => Math.hypot(o.x - c.x, o.y - c.y) < o.r + c.r + 40)) continue;
    taken.push(c);
    scatter.push({ x, y, size, name: SHAPE_NAMES[scatter.length % SHAPE_NAMES.length], color: SHAPE_COLORS[(scatter.length * 3) % SHAPE_COLORS.length], rot: rand() * 360 });
  }
  const piece = (o, centred = false) => html`<i class="shape ${centred ? 'seam' : ''}" style="--m:url('/assets/shapes/${o.name}.svg');--c:var(--md-sys-color-${o.color});width:${o.size}px;height:${o.size}px;left:${o.x - (centred ? o.size / 2 : 0)}px;top:${o.y - (centred ? o.size / 2 : 0)}px;transform:rotate(${o.rot}deg)"></i>`;
  const strip = document.createElement('section');
  strip.className = `strip strip--${kind}`;
  strip.style.width = `${n * CANVAS.w}px`;
  render(html`
    ${scatter.map((o) => piece(o))}
    ${seams.map((o) => piece(o, true))}
    ${set.list.map((t, index) => html`
      <article class="wide-shot" data-shot=${`${kind}-${index + 1}`} data-id=${t.id} style="left:${index * CANVAS.w}px">
        <div class="wide-device" style="left:${dx}px;top:${dy}px"><div data-product></div></div>
      </article>`)}`, strip);
  target.append(strip);
  for (const [index, t] of set.list.entries()) {
    const [id, state] = SCREENS[t.id];
    await mount(strip.querySelectorAll('[data-product]')[index], id, { ...noop, ...state, conversations: SCREENS.conversations[1].conversations }, { frame: true, window: set.window });
  }
}

await document.fonts.ready;
await Promise.all([...document.images].map((img) => img.decode().catch(() => { throw new Error(`Cannot decode image: ${img.src}`); })));
window.storeReady = true;
