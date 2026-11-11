/**
 * The background's liquid-glass ribbons as geometry: each ribbon's centreline across a strip and the
 * half-width its band and rims cover. src/Glass.tsx draws them from this; the layout keeps shapes clear of
 * them and the validator measures how close they come to captions. A slow sweep rising and falling once
 * per screenshot or two, computed across the whole strip so every seam meets.
 */
export const RIBBONS = [
  { y0: 0.62, amp: 0.2, period: 1.7, phase: 2.4, w: 220, o: 0.9 },
  { y0: 0.92, amp: 0.1, period: 2.6, phase: 4.1, w: 160, o: 0.8 },
];

export type Ribbon = (typeof RIBBONS)[number] & { half: number; pts: [number, number][] };

export const ribbonLines = (width: number, height: number, panels: number): Ribbon[] => {
  const panel = width / panels;
  return RIBBONS.map((r) => ({
    ...r,
    half: r.w / 2,
    pts: Array.from({ length: Math.floor((width + 400) / 20) + 1 }, (_, i) => {
      const x = -200 + i * 20, y0 = height * r.y0, amp = height * r.amp;
      return [x, y0 + amp * Math.sin((x / (panel * r.period)) * Math.PI * 2 + r.phase) + amp * 0.35 * Math.sin((x / (panel * r.period * 0.37)) * Math.PI * 2 + r.phase * 2.3)] as [number, number];
    }),
  }));
};

/**
 * The fade behind text: the ribbons keep `FADE.floor` of their opacity inside a caption's text block grown
 * by `FADE.core`, then return over `FADE.feather` px, in `FADE.steps` stacked steps (no blur filter, which
 * seams on a strip this wide).
 */
export const FADE = { core: 34, feather: 89, steps: 12, floor: 0.1 };
