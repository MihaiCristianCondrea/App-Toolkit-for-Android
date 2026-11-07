import React from "react";

/**
 * The campaign's ground: a light aqua field crossed by ribbons of liquid glass, the way Material 3's
 * own hero art draws them. Each ribbon is a wide translucent band whose colour shifts between aqua
 * and white along its length, with a bright rim and a cyan refracting edge. The ribbons are
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

/** A point on a Catmull-Rom curve through `pts`, at `t` from 0 to 1. */
const along = (pts: number[][], t: number) => {
  const n = pts.length - 1, f = Math.min(t * n, n - 1e-6), i = Math.floor(f), u = f - i;
  const p0 = pts[Math.max(i - 1, 0)], p1 = pts[i], p2 = pts[i + 1], p3 = pts[Math.min(i + 2, n)];
  return [0, 1].map((k) => 0.5 * (2 * p1[k] + (-p0[k] + p2[k]) * u + (2 * p0[k] - 5 * p1[k] + 4 * p2[k] - p3[k]) * u * u + (-p0[k] + 3 * p1[k] - 3 * p2[k] + p3[k]) * u * u * u));
};
const mix = (a: number[], b: number[], t: number) => a.map((v, k) => Math.round(v + (b[k] - v) * t));
const rgb = (c: number[]) => `rgb(${c.join(",")})`;

/**
 * A wave of water thrown out of a speaker grille at (x, y), turned by `angle`: a ribbon of liquid that
 * leaves the grille narrow, widens as it sweeps down and away, and twists over into a curl, so its light
 * face and its deeper underside both show. Built from short lit segments with bright rims; a handful of
 * drops leave the crest. An illustration of the tone's effect, not a claim.
 */
export const Wave: React.FC<{ x: number; y: number; angle: number; size: number; width: number; height: number }> = ({ x, y, angle, size, width, height }) => {
  const spine = [[0, 10], [-6, 90], [-60, 190], [-190, 270], [-360, 290], [-510, 240], [-600, 150], [-590, 60], [-520, 30], [-460, 70]].map(([px, py]) => [px * 0.8, py]);
  const N = 160;
  const light = [214, 251, 250], mid = [64, 210, 218], deep = [8, 128, 144];
  const segments = Array.from({ length: N }, (_, i) => {
    const t0 = i / N, t1 = (i + 1) / N;
    const edge = (t: number) => {
      const [cx, cy] = along(spine, t), [ax, ay] = along(spine, Math.max(t - 0.004, 0)), [bx, by] = along(spine, Math.min(t + 0.004, 1));
      const len = Math.hypot(bx - ax, by - ay) || 1, nx = -(by - ay) / len, ny = (bx - ax) / len;
      const w = (8 + 230 * Math.pow(Math.sin(Math.PI * Math.min(t * 1.15, 1)), 0.9)) / 2;
      const twist = Math.cos(Math.PI * 1.35 * t);
      return { a: [cx + nx * w * twist, cy + ny * w * twist], b: [cx - nx * w * twist, cy - ny * w * twist], twist };
    };
    const e0 = edge(t0), e1 = edge(t1);
    const face = Math.abs(e0.twist), front = e0.twist >= 0;
    const fill = front ? mix(mix(mid, light, face), deep, t0 * 0.35) : mix(deep, mid, face * 0.5);
    return { key: i, d: `M ${e0.a} L ${e1.a} L ${e1.b} L ${e0.b} Z`, fill: rgb(fill), a: [e0.a, e1.a], b: [e0.b, e1.b], t: t0 };
  });
  const rim = (side: "a" | "b") => `M ${segments.map((s) => s[side][0]).join(" L ")}`;
  const drops = [[-400, -40, 10], [-450, -75, 6], [-350, -60, 5], [-490, -30, 7], [-300, -20, 4], [36, 50, 6], [52, 22, 4]];
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <defs>
        <radialGradient id="wave-drop" cx=".35" cy=".3" r=".8">
          <stop offset="0" stopColor="#FFFFFF" />
          <stop offset=".45" stopColor="#B8F3F4" />
          <stop offset="1" stopColor="#1FB3C2" />
        </radialGradient>
        <filter id="wave-shadow" x="-20%" y="-20%" width="140%" height="150%"><feDropShadow dx="0" dy="28" stdDeviation="24" floodColor="#00444E" floodOpacity=".25" /></filter>
        <filter id="wave-soft" x="-10%" y="-10%" width="120%" height="120%"><feGaussianBlur stdDeviation="5" /></filter>
      </defs>
      <g transform={`translate(${x} ${y}) rotate(${angle}) scale(${size})`}>
        <g filter="url(#wave-shadow)" opacity=".94">
          {segments.map((s) => <path key={s.key} d={s.d} fill={s.fill} stroke={s.fill} strokeWidth="1.2" strokeLinejoin="round" />)}
        </g>
        <path d={rim("a")} fill="none" stroke="#FFFFFF" strokeWidth="10" strokeLinecap="round" opacity=".55" filter="url(#wave-soft)" />
        <path d={rim("a")} fill="none" stroke="#FFFFFF" strokeWidth="3.5" strokeLinecap="round" opacity=".95" />
        <path d={rim("b")} fill="none" stroke="#E8FFFF" strokeWidth="2.5" strokeLinecap="round" opacity=".8" />
        {drops.map(([cx, cy, r], i) => (
          <g key={i}>
            <circle cx={cx} cy={cy} r={r} fill="url(#wave-drop)" />
            <ellipse cx={cx - r * 0.3} cy={cy - r * 0.35} rx={r * 0.3} ry={r * 0.2} fill="#FFFFFF" />
          </g>
        ))}
      </g>
    </svg>
  );
};
