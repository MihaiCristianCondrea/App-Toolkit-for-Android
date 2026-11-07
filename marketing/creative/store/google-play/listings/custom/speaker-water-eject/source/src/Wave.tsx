import React from "react";
import { seededRandom } from "./random";

/**
 * An illustrated wave rising from a phone's speaker at (x, y), in one screenshot's coordinates.
 * Layered translucent water (a deep body, a lit face, a curling crest), a soft highlight along the
 * crest, foam, spray droplets catching the light, and a mirror streak on the surface below. A
 * turbulence filter gives the edges the irregularity of moving water. It is a metaphor for the
 * tone, not a depiction of what the app does to a phone.
 */
export const Wave: React.FC<{ x: number; y: number; width: number; height: number; id: string }> = ({ x, y, width, height, id }) => {
  const rand = seededRandom(17);
  // One swell across the bottom of the image that rises into a curling crest behind the phone, the speaker at (x, y)
  // sitting in its trough. Spray arcs from the speaker into the crest.
  // The crest stands clear of the phone on its right, curling back towards it.
  const c = x + 330;
  const body = `M -40 ${y + 130} C ${x - 340} ${y + 110} ${x - 120} ${y + 70} ${x + 40} ${y - 10}
    C ${x + 160} ${y - 80} ${c - 150} ${y - 300} ${c - 20} ${y - 400}
    C ${c + 80} ${y - 470} ${c + 210} ${y - 420} ${c + 200} ${y - 330}
    C ${c + 192} ${y - 270} ${c + 130} ${y - 255} ${c + 100} ${y - 290}
    C ${c + 140} ${y - 200} ${c + 200} ${y - 60} ${width + 40} ${y + 10}
    L ${width + 40} ${height + 40} L -40 ${height + 40} Z`;
  const face = `M -40 ${y + 190} C ${x - 320} ${y + 170} ${x - 100} ${y + 130} ${x + 60} ${y + 60}
    C ${x + 170} ${y + 10} ${c - 120} ${y - 220} ${c - 20} ${y - 330}
    C ${c + 20} ${y - 200} ${c + 120} ${y - 40} ${width + 40} ${y + 70}
    L ${width + 40} ${height + 40} L -40 ${height + 40} Z`;
  const crest = `M ${x + 40} ${y - 10} C ${x + 160} ${y - 80} ${c - 150} ${y - 300} ${c - 20} ${y - 400}
    C ${c + 80} ${y - 470} ${c + 210} ${y - 420} ${c + 200} ${y - 330}`;
  const drops = Array.from({ length: 34 }, (_, i) => {
    // Spray thrown from the speaker up into the crest, finer as it travels.
    const t = rand();
    const cx = x + 30 + t * 320 + (rand() - 0.5) * 70;
    const cy = y - 30 - Math.sin(t * Math.PI * 0.9) * 360 - rand() * 70;
    const r = 3 + (1 - t) * 10 * rand() + 2;
    return { cx, cy, r, key: i };
  });
  const foam = Array.from({ length: 36 }, (_, i) => {
    const t = i / 35;
    const cx = c - 40 + t * 230 + (rand() - 0.5) * 20;
    const cy = y - 380 - Math.sin(t * Math.PI) * 70 + t * 40 + (rand() - 0.5) * 18;
    return { cx, cy, r: 2 + rand() * 5, key: i };
  });
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none", overflow: "visible" }}>
      <defs>
        <linearGradient id={`${id}-body`} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor="#2FC4CF" />
          <stop offset=".45" stopColor="#0C8E9C" />
          <stop offset="1" stopColor="#05535E" />
        </linearGradient>
        <linearGradient id={`${id}-face`} x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stopColor="#B9F3F0" stopOpacity=".95" />
          <stop offset=".35" stopColor="#5BD7DA" stopOpacity=".75" />
          <stop offset="1" stopColor="#0E8F9D" stopOpacity=".2" />
        </linearGradient>
        <radialGradient id={`${id}-drop`} cx=".35" cy=".3" r=".75">
          <stop offset="0" stopColor="#FFFFFF" />
          <stop offset=".35" stopColor="#CFF7F5" />
          <stop offset="1" stopColor="#27B8C4" stopOpacity=".55" />
        </radialGradient>
        <linearGradient id={`${id}-mirror`} x1="0" y1="0" x2="1" y2="0">
          <stop offset="0" stopColor="#FFFFFF" stopOpacity="0" />
          <stop offset=".5" stopColor="#FFFFFF" stopOpacity=".55" />
          <stop offset="1" stopColor="#FFFFFF" stopOpacity="0" />
        </linearGradient>
        <filter id={`${id}-move`} x="-10%" y="-10%" width="120%" height="120%">
          <feTurbulence type="fractalNoise" baseFrequency=".006 .014" numOctaves="1" seed="4" />
          <feDisplacementMap in="SourceGraphic" scale="7" />
        </filter>
        <filter id={`${id}-soft`}><feGaussianBlur stdDeviation="5" /></filter>
        <filter id={`${id}-glow`}><feGaussianBlur stdDeviation="14" /></filter>
      </defs>
      <g filter={`url(#${id}-move)`}>
        <path d={body} fill={`url(#${id}-body)`} />
        <path d={face} fill={`url(#${id}-face)`} />
        <path d={crest} fill="none" stroke="#FFFFFF" strokeOpacity=".55" strokeWidth="22" strokeLinecap="round" filter={`url(#${id}-glow)`} />
        <path d={crest} fill="none" stroke="#FFFFFF" strokeOpacity=".9" strokeWidth="5" strokeLinecap="round" filter={`url(#${id}-soft)`} />
      </g>
      {/* The sky's light lying on the surface, broken by the swell. */}
      {[0, 1, 2].map((k) => (
        <rect key={k} x={x - 420 + k * 120} y={y + 200 + k * 50} width={700 - k * 170} height={6 - k * 1.5} rx="3" fill={`url(#${id}-mirror)`} />
      ))}
      {foam.map((f) => <circle key={f.key} cx={f.cx} cy={f.cy} r={f.r} fill="#FFFFFF" fillOpacity=".7" />)}
      {drops.map((d) => (
        <g key={d.key}>
          <circle cx={d.cx} cy={d.cy} r={d.r} fill={`url(#${id}-drop)`} />
          <circle cx={d.cx - d.r * 0.3} cy={d.cy - d.r * 0.35} r={d.r * 0.28} fill="#FFFFFF" />
        </g>
      ))}
    </svg>
  );
};
