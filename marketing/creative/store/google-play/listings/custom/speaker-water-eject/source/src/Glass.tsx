import React from "react";

/**
 * The campaign's ground: a light aqua field crossed by ribbons of liquid glass, the way Material 3's
 * own hero art draws them. Each ribbon is a wide translucent band whose colour shifts through aqua,
 * white, lemon and lilac along its length, with a bright rim and a soft glow. The ribbons are
 * computed across the whole strip, so every seam between two screenshots meets.
 */
export const Glass: React.FC<{ width: number; height: number; panels: number; id?: string }> = ({ width, height, panels, id = "g" }) => {
  const panel = width / panels;
  // A ribbon's centreline: a slow sweep across the strip, rising and falling once per screenshot or two.
  const ribbon = (y0: number, amp: number, period: number, phase: number) => {
    const pts: string[] = [];
    for (let x = -200; x <= width + 200; x += 20) {
      const y = y0 + amp * Math.sin((x / (panel * period)) * Math.PI * 2 + phase) + amp * 0.35 * Math.sin((x / (panel * period * 0.37)) * Math.PI * 2 + phase * 2.3);
      pts.push(`${x},${y.toFixed(1)}`);
    }
    return `M ${pts.join(" L ")}`;
  };
  const ribbons = [
        { d: ribbon(height * 0.62, height * 0.2, 1.7, 2.4), w: 220, o: 0.9 },
    { d: ribbon(height * 0.92, height * 0.1, 2.6, 4.1), w: 160, o: 0.8 },
  ];
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
 * Water leaving a speaker at (x, y): a curving stream of clear water with a bright core and edges,
 * breaking into droplets that catch the light. An illustration of the tone's effect, not a claim.
 */
export const Stream: React.FC<{ x: number; y: number; width: number; height: number; id?: string }> = ({ x, y, width, height, id = "s" }) => {
  const body = `M ${x - 26} ${y} C ${x - 40} ${y + 120} ${x - 160} ${y + 190} ${x - 330} ${y + 260}
    C ${x - 470} ${y + 320} ${x - 560} ${y + 330} ${x - 640} ${y + 400}
    L ${x - 600} ${y + 430} C ${x - 520} ${y + 380} ${x - 400} ${y + 360} ${x - 270} ${y + 300}
    C ${x - 120} ${y + 230} ${x + 30} ${y + 150} ${x + 30} ${y} Z`;
  const core = `M ${x} ${y + 10} C ${x - 10} ${y + 120} ${x - 140} ${y + 210} ${x - 300} ${y + 275} C ${x - 440} ${y + 330} ${x - 540} ${y + 350} ${x - 620} ${y + 410}`;
  let seed = 11;
  const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
  const drops = Array.from({ length: 26 }, (_, i) => {
    const t = rand();
    return { key: i, cx: x - 80 - t * 520 + (rand() - 0.5) * 140, cy: y + 60 + t * 300 + (rand() - 0.5) * 200, r: 4 + rand() * 11 };
  });
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <g transform={`translate(${x} ${y}) scale(1.5) translate(${-x} ${-y})`}>
      <defs>
        <linearGradient id={`${id}-water`} x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stopColor="#E9FFFF" stopOpacity=".95" />
          <stop offset=".5" stopColor="#7FE2EA" stopOpacity=".75" />
          <stop offset="1" stopColor="#3CC3D2" stopOpacity=".55" />
        </linearGradient>
        <radialGradient id={`${id}-drop`} cx=".35" cy=".3" r=".8">
          <stop offset="0" stopColor="#FFFFFF" />
          <stop offset=".4" stopColor="#D9FAFB" stopOpacity=".9" />
          <stop offset="1" stopColor="#53CBD6" stopOpacity=".6" />
        </radialGradient>
        <filter id={`${id}-soft`}><feGaussianBlur stdDeviation="2.5" /></filter>
      </defs>
      <path d={body} fill={`url(#${id}-water)`} />
      <path d={core} fill="none" stroke="#FFFFFF" strokeWidth="9" strokeLinecap="round" opacity=".85" filter={`url(#${id}-soft)`} />
      <path d={core} fill="none" stroke="#FFFFFF" strokeWidth="3" strokeLinecap="round" transform="translate(-22 14)" opacity=".7" />
      {drops.map((d) => (
        <g key={d.key}>
          <circle cx={d.cx} cy={d.cy} r={d.r} fill={`url(#${id}-drop)`} stroke="#FFFFFF" strokeOpacity=".7" strokeWidth="1.2" />
          <ellipse cx={d.cx - d.r * 0.3} cy={d.cy - d.r * 0.35} rx={d.r * 0.32} ry={d.r * 0.22} fill="#FFFFFF" />
        </g>
      ))}
      </g>
    </svg>
  );
};

/**
 * Bubbles rising from a speaker just below the image's edge at (x, y): clear spheres with a bright
 * rim and a highlight, larger and denser near the speaker, thinning as they rise.
 */
export const Bubbles: React.FC<{ x: number; y: number; width: number; height: number }> = ({ x, y, width, height }) => {
  let seed = 23;
  const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
  const bubbles = Array.from({ length: 34 }, (_, i) => {
    const rise = Math.pow(rand(), 0.8);
    const r = 34 - rise * 26 + rand() * 10;
    const side = rand() < 0.5 ? -1 : 1;
    return { key: i, cx: x + side * (60 + rand() * 300 + rise * 160), cy: y - 60 - rise * 1150, r };
  });
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none", zIndex: 10 }}>
      <defs>
        <radialGradient id="bubble" cx=".5" cy=".5" r=".5">
          <stop offset=".7" stopColor="#FFFFFF" stopOpacity=".05" />
          <stop offset=".92" stopColor="#BFF4F6" stopOpacity=".55" />
          <stop offset="1" stopColor="#FFFFFF" stopOpacity=".95" />
        </radialGradient>
      </defs>
      {bubbles.map((b) => (
        <g key={b.key}>
          <circle cx={b.cx} cy={b.cy} r={b.r} fill="url(#bubble)" stroke="#5FCAD6" strokeOpacity=".35" strokeWidth="1.2" />
          <ellipse cx={b.cx - b.r * 0.38} cy={b.cy - b.r * 0.42} rx={b.r * 0.26} ry={b.r * 0.16} fill="#FFFFFF" transform={`rotate(-35 ${b.cx - b.r * 0.38} ${b.cy - b.r * 0.42})`} />
          <path d={`M ${b.cx + b.r * 0.55} ${b.cy + b.r * 0.2} A ${b.r * 0.7} ${b.r * 0.7} 0 0 1 ${b.cx + b.r * 0.1} ${b.cy + b.r * 0.62}`} fill="none" stroke="#FFFFFF" strokeWidth={Math.max(1.5, b.r * 0.08)} strokeOpacity=".8" strokeLinecap="round" />
        </g>
      ))}
    </svg>
  );
};
