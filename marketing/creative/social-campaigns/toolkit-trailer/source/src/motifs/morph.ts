/**
 * MaterialShape morphing, as Material 3 Expressive's loading indicator does it: a shape turns while its outline
 * flows into the next shape. The curves are the UI kit's own (marketing/ui/src/kit/assets.js draws every
 * MaterialShape as a polar curve r(θ)); a morph blends two curves' radii at every angle, each normalised to the
 * same box first, so the outline never jumps.
 */
const polygon = (n: number, t: number) => {
  const seg = (2 * Math.PI) / n, u = t + seg / 2, m = (((u % seg) + seg) % seg) - seg / 2;
  return Math.cos(Math.PI / n) / Math.cos(m);
};
const cookie = (n: number) => (t: number) => 0.9 + 0.1 * Math.cos(n * t);
const CURVES: Record<string, (t: number) => number> = {
  circle: () => 1,
  pill: (t) => 1 / Math.pow(Math.pow(Math.abs(Math.cos(t)), 3.2) + Math.pow(Math.abs(Math.sin(t)), 3.2), 1 / 3.2),
  pentagon: (t) => 0.5 * polygon(5, t - Math.PI / 2) + 0.5,
  gem: (t) => 0.55 * polygon(6, t) + 0.4,
  flower: (t) => 0.8 + 0.2 * Math.cos(8 * t),
  soft_burst: (t) => 0.86 + 0.14 * Math.cos(10 * t),
  sunny: (t) => 0.82 + 0.18 * Math.cos(12 * t),
  very_sunny: (t) => 0.78 + 0.22 * Math.cos(16 * t),
  clover_4leaf: (t) => 0.62 + 0.38 * Math.pow(Math.abs(Math.cos(2 * t)), 0.8),
  clover_8leaf: (t) => 0.7 + 0.3 * Math.pow(Math.abs(Math.cos(4 * t)), 0.8),
  bun: (t) => 0.88 + 0.12 * Math.cos(2 * t),
};
const curve = (name: string) => { const c = /^cookie_(\d+)sided$/.exec(name); return c ? cookie(Number(c[1])) : CURVES[name] ?? cookie(9); };

const N = 180;
const cache = new Map<string, number[]>();
/** The shape's radius at N angles, scaled so its outline just fits a unit box (as the kit's masks are). */
const radii = (name: string) => {
  if (cache.has(name)) return cache.get(name)!;
  const r = curve(name), rs = Array.from({ length: N }, (_, i) => r((i / N) * 2 * Math.PI));
  const max = Math.max(...rs.map((v, i) => { const t = (i / N) * 2 * Math.PI; return Math.max(Math.abs(v * Math.cos(t)), Math.abs(v * Math.sin(t))); }));
  const out = rs.map((v) => v / max);
  cache.set(name, out);
  return out;
};

/** An SVG path for shape `a` flowing into shape `b` (p from 0 to 1), centred at (cx, cy), `size` across, turned by `turn` degrees. */
export const morphPath = (a: string, b: string, p: number, cx: number, cy: number, size: number, turn = 0) => {
  const ra = radii(a), rb = radii(b), half = size / 2, off = (turn * Math.PI) / 180;
  return Array.from({ length: N }, (_, i) => {
    const t = (i / N) * 2 * Math.PI, r = (ra[i] + (rb[i] - ra[i]) * p) * half;
    return `${i ? "L" : "M"}${(cx + r * Math.cos(t + off)).toFixed(1)} ${(cy + r * Math.sin(t + off)).toFixed(1)}`;
  }).join("") + "Z";
};
