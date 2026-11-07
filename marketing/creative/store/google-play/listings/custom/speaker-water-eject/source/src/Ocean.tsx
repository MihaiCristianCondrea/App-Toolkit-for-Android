import React from "react";
import { seededRandom } from "./random";

/**
 * One ocean across the whole strip: a deep-to-bright sky of water, and three layers of waves whose
 * shape is computed over the full width, so every seam between two screenshots meets exactly. The
 * swell is strongest at `peak` (the hero) and settles away from it. `front` draws only the nearest
 * wave, for laying over the bottom of the phones so they float in the water.
 */
export const Ocean: React.FC<{ width: number; height: number; peak: number; front?: boolean; spray?: { x: number; y: number } }> = ({ width, height, peak, front, spray }) => {
  // Swell height at x: a broad envelope around the peak over a gentle floor.
  const swell = (x: number) => 0.45 + 0.55 * Math.exp(-(((x - peak) / 900) ** 2));
  const layer = (base: number, amp: number, wave: number, phase: number) => {
    const points: string[] = [];
    for (let x = -40; x <= width + 40; x += 12) {
      const s = swell(x);
      const y = base - s * amp * (0.62 * Math.sin(x / wave + phase) + 0.38 * Math.sin(x / (wave * 0.47) + phase * 1.7))
        - (s - 0.45) * amp * 1.1;
      points.push(`${x.toFixed(0)},${y.toFixed(1)}`);
    }
    return { line: `M ${points.join(" L ")}`, fill: `M ${points.join(" L ")} L ${width + 40},${height + 40} L -40,${height + 40} Z` };
  };
  const back = layer(height * 0.74, 120, 260, 0.4);
  const mid = layer(height * 0.8, 95, 210, 2.1);
  const near = layer(height * 0.94, 30, 170, 4.2);
  const rand = seededRandom(29);
  const drops = spray
    ? Array.from({ length: 30 }, (_, i) => {
        const t = rand();
        const side = rand() < 0.5 ? -1 : 1;
        // Thrown out to either side of the phone, never over its screen.
        return { key: i, cx: spray.x + side * (330 + t * 170), cy: spray.y - 20 - Math.sin(t * Math.PI) * 260 - rand() * 80, r: 3 + (1 - t) * 9 * rand() + 2 };
      })
    : [];
  if (front)
    return (
      <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
        <defs>
          <linearGradient id="near" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0" stopColor="#7FE6E4" />
            <stop offset=".25" stopColor="#1FB2C0" />
            <stop offset="1" stopColor="#077C8C" />
          </linearGradient>
        </defs>
        <path d={near.fill} fill="url(#near)" />
        <path d={near.line} fill="none" stroke="#FFFFFF" strokeOpacity=".85" strokeWidth="4" />
        {drops.map((d) => (
          <g key={d.key}>
            <circle cx={d.cx} cy={d.cy} r={d.r} fill="#E9FFFD" fillOpacity=".9" />
            <circle cx={d.cx - d.r * 0.3} cy={d.cy - d.r * 0.35} r={d.r * 0.3} fill="#FFFFFF" />
          </g>
        ))}
      </svg>
    );
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <defs>
        <linearGradient id="sky" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#003E4C" />
          <stop offset=".55" stopColor="#006577" />
          <stop offset="1" stopColor="#0A8E9C" />
        </linearGradient>
        <radialGradient id="sun" cx=".5" cy="0" r=".9">
          <stop offset="0" stopColor="#5FE0E0" stopOpacity=".35" />
          <stop offset="1" stopColor="#5FE0E0" stopOpacity="0" />
        </radialGradient>
        <linearGradient id="back" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#2BB7C2" stopOpacity=".55" />
          <stop offset="1" stopColor="#0B6E7D" stopOpacity=".7" />
        </linearGradient>
        <linearGradient id="mid" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#5AD5D8" stopOpacity=".85" />
          <stop offset=".4" stopColor="#139FAE" />
          <stop offset="1" stopColor="#086F7F" />
        </linearGradient>
      </defs>
      <rect width={width} height={height} fill="url(#sky)" />
      <ellipse cx={peak} cy={0} rx={1400} ry={1300} fill="url(#sun)" />
      <path d={back.fill} fill="url(#back)" />
      <path d={back.line} fill="none" stroke="#9FF0EE" strokeOpacity=".45" strokeWidth="3" />
      <path d={mid.fill} fill="url(#mid)" />
      <path d={mid.line} fill="none" stroke="#FFFFFF" strokeOpacity=".6" strokeWidth="3.5" />
    </svg>
  );
};
