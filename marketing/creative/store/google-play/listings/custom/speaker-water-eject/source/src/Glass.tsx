import React from "react";

/**
 * The campaign's ground: a light aqua field crossed by ribbons of liquid glass, the way Material 3's
 * own hero art draws them. Each ribbon is a wide translucent band whose colour shifts between aqua
 * and white along its length, with a bright rim and a cyan refracting edge. The ribbons are
 * computed across the whole strip, so every seam between two screenshots meets.
 */
/** The ribbons: centre height, swing and period (in screenshots) as shares, band width in px. */
const RIBBONS = [
  { y0: 0.62, amp: 0.2, period: 1.7, phase: 2.4, w: 220, o: 0.9 },
  { y0: 0.92, amp: 0.1, period: 2.6, phase: 4.1, w: 160, o: 0.8 },
];

/**
 * Each ribbon's centreline across a strip, with the half-width its band and rims cover, so other art
 * can keep clear of it. A slow sweep rising and falling once per screenshot or two.
 */
export const ribbonLines = (width: number, height: number, panels: number) => {
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

export const Glass: React.FC<{ width: number; height: number; panels: number; id?: string }> = ({ width, height, panels, id = "g" }) => {
  const panel = width / panels;
  const ribbons = ribbonLines(width, height, panels).map((r) => ({ d: `M ${r.pts.map(([x, y]) => `${x},${y.toFixed(1)}`).join(" L ")}`, w: r.w, o: r.o }));
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <defs>
        <linearGradient id={`${id}-field`} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#EAF9FA" />
          <stop offset="1" stopColor="#CDEFF2" />
        </linearGradient>
        <linearGradient id={`${id}-iris`} x1="0" y1="0" x2={panel * 1.4} y2="0" gradientUnits="userSpaceOnUse" spreadMethod="reflect">
          <stop offset="0" stopColor="#8FE6EC" />
          <stop offset=".45" stopColor="#F4FFFF" />
          <stop offset=".75" stopColor="#B9EEF2" />
          <stop offset="1" stopColor="#6FD3DE" />
        </linearGradient>
        <filter id={`${id}-soft`}><feGaussianBlur stdDeviation="3" /></filter>
      </defs>
      <rect width={width} height={height} fill={`url(#${id}-field)`} />
      {ribbons.map((r, i) => (
        <g key={i} opacity={r.o}>
          {/* The glass band, a cyan edge where it refracts, a broad soft sheen and two sharp white rims. */}
          <path d={r.d} fill="none" stroke={`url(#${id}-iris)`} strokeWidth={r.w} strokeLinecap="round" opacity=".8" />
          <path d={r.d} fill="none" stroke="#3FC6D3" strokeWidth={r.w * 0.12} transform={`translate(0 ${r.w * 0.36})`} opacity=".45" />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth={r.w * 0.34} strokeLinecap="round" transform={`translate(0 ${-r.w * 0.12})`} opacity=".35" filter={`url(#${id}-soft)`} />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth="6" transform={`translate(0 ${-r.w * 0.45})`} opacity=".95" />
          <path d={r.d} fill="none" stroke="#FFFFFF" strokeWidth="3" transform={`translate(0 ${r.w * 0.47})`} opacity=".8" />
        </g>
      ))}
    </svg>
  );
};

/**
 * Sound leaving the speakers and the bubbles it carries: `arcs` are point lists with their ring, 0 nearest, drawn as soft
 * turquoise lines that fade as they spread; `bubbles` are [x, y, r], clear spheres with
 * a rim and a highlight. An illustration of the tone, not a claim.
 */
export const SoundBubbles: React.FC<{ arcs: { pts: number[][]; ring: number }[]; bubbles: number[][]; width: number; height: number }> = ({ arcs, bubbles, width, height }) => (
  <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
    <defs>
      <radialGradient id="sound-bubble" cx=".5" cy=".5" r=".5">
        <stop offset=".62" stopColor="#FFFFFF" stopOpacity=".08" />
        <stop offset=".9" stopColor="#7FDDE4" stopOpacity=".6" />
        <stop offset="1" stopColor="#FFFFFF" stopOpacity=".95" />
      </radialGradient>
      <filter id="sound-glow" x="-20%" y="-20%" width="140%" height="140%"><feGaussianBlur stdDeviation="4" /></filter>
    </defs>
    {arcs.map(({ pts, ring: i }, key) => {
      const d = `M ${pts.map((p) => p.map((n) => n.toFixed(1)).join(" ")).join(" L ")}`, fade = 1 - i * 0.15;
      return (
        <g key={key} opacity={fade}>
          <path d={d} fill="none" stroke="#3FC6D3" strokeWidth={7} strokeLinecap="round" opacity=".18" filter="url(#sound-glow)" />
          <path d={d} fill="none" stroke="#1AA5B5" strokeWidth={Math.max(1.4, 2.6 - i * 0.3)} strokeLinecap="round" opacity=".6" />
        </g>
      );
    })}
    {bubbles.map(([x, y, r], i) => (
      <g key={i}>
        <circle cx={x} cy={y} r={r} fill="url(#sound-bubble)" stroke="#1FA9B8" strokeOpacity=".65" strokeWidth={Math.max(1.5, r * 0.09)} />
        <ellipse cx={x - r * 0.36} cy={y - r * 0.4} rx={r * 0.26} ry={r * 0.16} fill="#FFFFFF" transform={`rotate(-35 ${x - r * 0.36} ${y - r * 0.4})`} />
        <path d={`M ${x + r * 0.55} ${y + r * 0.2} A ${r * 0.7} ${r * 0.7} 0 0 1 ${x + r * 0.1} ${y + r * 0.62}`} fill="none" stroke="#FFFFFF" strokeWidth={Math.max(1.2, r * 0.08)} strokeOpacity=".85" strokeLinecap="round" />
      </g>
    ))}
  </svg>
);
