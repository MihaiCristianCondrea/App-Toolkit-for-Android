import React from "react";
import { FADE, ribbonLines } from "./composition/ribbons";
import { BUBBLE_TINT, FIELD, RIBBON_EDGE, RIBBON_STOPS, SHEET_BLUE, SHEET_BLUE_BRIGHT } from "./theme";
export { ribbonLines };

/** A box the ribbons fade behind, in the strip's coordinates. */
export type TextBox = { left: number; top: number; right: number; bottom: number };

/**
 * The campaign's ground: a light field in the Water Eject sheet's blues crossed by ribbons of liquid glass,
 * the way Material 3's own hero art draws them. Each ribbon is a wide translucent band whose colour shifts
 * between blue and white along its length, with a bright rim and a blue refracting edge. The ribbons are
 * computed across the whole strip, so every seam between two screenshots meets.
 */
export const Glass: React.FC<{ width: number; height: number; panels: number; id?: string; text?: TextBox[]; ribbons?: boolean; field?: [string, string] }> = ({ width, height, panels, id = "g", text = [], ribbons: withRibbons = true, field = FIELD }) => {
  const panel = width / panels;
  const ribbons = (withRibbons ? ribbonLines(width, height, panels) : []).map((r) => ({ d: `M ${r.pts.map(([x, y]) => `${x},${y.toFixed(1)}`).join(" L ")}`, w: r.w, o: r.o }));
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <defs>
        <linearGradient id={`${id}-field`} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor={field[0]} />
          <stop offset="1" stopColor={field[1]} />
        </linearGradient>
        <linearGradient id={`${id}-iris`} x1="0" y1="0" x2={panel * 1.4} y2="0" gradientUnits="userSpaceOnUse" spreadMethod="reflect">
          <stop offset="0" stopColor={RIBBON_STOPS[0]} />
          <stop offset=".45" stopColor={RIBBON_STOPS[1]} />
          <stop offset=".75" stopColor={RIBBON_STOPS[2]} />
          <stop offset="1" stopColor={RIBBON_STOPS[3]} />
        </linearGradient>
        <filter id={`${id}-soft`}><feGaussianBlur stdDeviation="3" /></filter>
        {text.length > 0 && (
          <mask id={`${id}-text`} maskUnits="userSpaceOnUse" x={0} y={0} width={width} height={height}>
            <rect width={width} height={height} fill="#fff" />
            {/* Stacked rounded boxes, each a little smaller, darken the mask towards each caption's core. */}
            {text.flatMap((b, k) => Array.from({ length: FADE.steps }, (_, s) => {
              const grow = FADE.core + FADE.feather * (1 - s / (FADE.steps - 1));
              const step = 1 - Math.pow(FADE.floor, 1 / FADE.steps);
              return <rect key={`${k}-${s}`} x={b.left - grow} y={b.top - grow} width={b.right - b.left + 2 * grow} height={b.bottom - b.top + 2 * grow} rx={grow + 21} fill="#000" fillOpacity={step} />;
            }))}
          </mask>
        )}
      </defs>
      <rect width={width} height={height} fill={`url(#${id}-field)`} />
      <g mask={text.length ? `url(#${id}-text)` : undefined}>
      {ribbons.map((r, i) => (
        <g key={i} opacity={r.o}>
          {/* The glass band, a blue edge where it refracts, a broad soft sheen and two sharp white rims. */}
          <path d={r.d} fill="none" stroke={`url(#${id}-iris)`} strokeWidth={r.w} strokeLinecap="round" opacity=".8" />
          <path d={r.d} fill="none" stroke={RIBBON_EDGE} strokeWidth={r.w * 0.12} transform={`translate(0 ${r.w * 0.36})`} opacity=".45" />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth={r.w * 0.34} strokeLinecap="round" transform={`translate(0 ${-r.w * 0.12})`} opacity=".35" filter={`url(#${id}-soft)`} />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth="6" transform={`translate(0 ${-r.w * 0.45})`} opacity=".95" />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth="3" transform={`translate(0 ${r.w * 0.47})`} opacity=".8" />
        </g>
      ))}
      </g>
    </svg>
  );
};

/**
 * Sound leaving the speakers and the bubbles it carries: `arcs` are point lists with their ring, 0 nearest, drawn as soft
 * lines in the sheet's blue that fade as they spread; `bubbles` are [x, y, r], clear spheres with
 * a rim and a highlight. An illustration of the tone, not a claim.
 */
export const SoundBubbles: React.FC<{ arcs: { pts: number[][]; ring: number }[]; bubbles: number[][]; width: number; height: number }> = ({ arcs, bubbles, width, height }) => (
  <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
    <defs>
      <radialGradient id="sound-bubble" cx=".5" cy=".5" r=".5">
        <stop offset=".62" stopColor="#FFFFFF" stopOpacity=".08" />
        <stop offset=".9" stopColor={BUBBLE_TINT} stopOpacity=".6" />
        <stop offset="1" stopColor="#FFFFFF" stopOpacity=".95" />
      </radialGradient>
      <filter id="sound-glow" x="-20%" y="-20%" width="140%" height="140%"><feGaussianBlur stdDeviation="4" /></filter>
    </defs>
    {arcs.map(({ pts, ring: i }, key) => {
      const d = `M ${pts.map((p) => p.map((n) => n.toFixed(1)).join(" ")).join(" L ")}`, fade = 1 - i * 0.15;
      return (
        <g key={key} opacity={fade}>
          <path d={d} fill="none" stroke={SHEET_BLUE_BRIGHT} strokeWidth={7} strokeLinecap="round" opacity=".16" filter="url(#sound-glow)" />
          <path d={d} fill="none" stroke={SHEET_BLUE} strokeWidth={Math.max(1.4, 2.6 - i * 0.3)} strokeLinecap="round" opacity=".5" />
        </g>
      );
    })}
    {bubbles.map(([x, y, r], i) => (
      <g key={i}>
        <circle cx={x} cy={y} r={r} fill="url(#sound-bubble)" stroke={SHEET_BLUE_BRIGHT} strokeOpacity=".65" strokeWidth={Math.max(1.5, r * 0.09)} />
        <ellipse cx={x - r * 0.36} cy={y - r * 0.4} rx={r * 0.26} ry={r * 0.16} fill="#FFFFFF" transform={`rotate(-35 ${x - r * 0.36} ${y - r * 0.4})`} />
        <path d={`M ${x + r * 0.55} ${y + r * 0.2} A ${r * 0.7} ${r * 0.7} 0 0 1 ${x + r * 0.1} ${y + r * 0.62}`} fill="none" stroke="#FFFFFF" strokeWidth={Math.max(1.2, r * 0.08)} strokeOpacity=".85" strokeLinecap="round" />
      </g>
    ))}
  </svg>
);
