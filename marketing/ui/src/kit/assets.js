import { IDENTITY } from "../config/identity.js";

/**
 * Base URL for fonts, illustrations and logos: the identity's, so its logo and fonts move with everything else.
 * Vite serves public/ at "/"; other hosts (e.g. Remotion's staticFile) call setAssetBase() before rendering.
 */
export const setAssetBase = (b) => IDENTITY.setAssetBase(b);
export const asset = (path) => IDENTITY.asset(path);

/**
 * MaterialShapes are drawn as polar curves and served as data-URI masks, so every tool shape
 * (cookie, flower, burst, gem, clover, pill...) renders without shipping per-shape SVG files.
 */
const polygon = (n, t) => {
  const seg = (2 * Math.PI) / n;
  const u = t + seg / 2;
  const m = (((u % seg) + seg) % seg) - seg / 2;
  return Math.cos(Math.PI / n) / Math.cos(m);
};
const SHAPES = {
  circle: () => 1,
  pill: (t) => 1 / Math.pow(Math.pow(Math.abs(Math.cos(t)), 3.2) + Math.pow(Math.abs(Math.sin(t)), 3.2), 1 / 3.2),
  arch: (t) => 1 / Math.pow(Math.pow(Math.abs(Math.cos(t)), 4) + Math.pow(Math.abs(Math.sin(t)), 4), 1 / 4),
  pentagon: (t) => 0.5 * polygon(5, t - Math.PI / 2) + 0.5,
  gem: (t) => 0.55 * polygon(6, t) + 0.4,
  flower: (t) => 0.8 + 0.2 * Math.cos(8 * t),
  soft_burst: (t) => 0.86 + 0.14 * Math.cos(10 * t),
  burst: (t) => 0.74 + 0.26 * Math.pow(Math.abs(Math.cos(6 * t)), 0.7),
  soft_boom: (t) => 0.84 + 0.16 * Math.cos(7 * t),
  sunny: (t) => 0.82 + 0.18 * Math.cos(12 * t),
  very_sunny: (t) => 0.78 + 0.22 * Math.cos(16 * t),
  clover_4leaf: (t) => 0.62 + 0.38 * Math.pow(Math.abs(Math.cos(2 * t)), 0.8),
  clover_8leaf: (t) => 0.7 + 0.3 * Math.pow(Math.abs(Math.cos(4 * t)), 0.8),
  bun: (t) => 0.88 + 0.12 * Math.cos(2 * t),
};
const cookie = (n) => (t) => 0.9 + 0.1 * Math.cos(n * t);
const cache = new Map();
export const shapeUrl = (name) => {
  if (cache.has(name)) return cache.get(name);
  const cm = /^cookie_(\d+)sided$/.exec(name);
  const r = cm ? cookie(Number(cm[1])) : SHAPES[name] || cookie(9);
  const pts = [];
  for (let i = 0; i < 180; i++) {
    const t = (i / 180) * 2 * Math.PI;
    pts.push([r(t) * Math.cos(t), r(t) * Math.sin(t)]);
  }
  const max = Math.max(...pts.map(([x, y]) => Math.max(Math.abs(x), Math.abs(y))));
  const d = pts.map(([x, y], i) => (i ? 'L' : 'M') + (50 + (x / max) * 49).toFixed(2) + ' ' + (50 + (y / max) * 49).toFixed(2)).join('') + 'Z';
  const url = 'data:image/svg+xml,' + encodeURIComponent('<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path d="' + d + '"/></svg>');
  cache.set(name, url);
  return url;
};
export const shapeMaskStyle = (url) =>
  `-webkit-mask:url("${url}") center / contain no-repeat;mask:url("${url}") center / contain no-repeat;`;

/** Guide artwork (hosted on i.ibb.co) is synced into public/images; other remote images stay remote. */
export const resolveImageUrl = (url) => {
  if (!url) return null;
  if (/^https?:/.test(url) && !/^https:\/\/i\.ibb\.co\//.test(url)) return url;
  const match = url.match(/\/([^/?#]+\.(png|jpg|jpeg|webp))$/i);
  if (match) return asset(`images/${match[1]}`);
  return url;
};
