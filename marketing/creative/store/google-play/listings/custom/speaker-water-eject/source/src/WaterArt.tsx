import React from "react";
import { RIPPLE, SURFACE_DEEP, SURFACE_TOP } from "./theme";

/** A set of ripple rings: where the tone leaves the speaker, how far it travels and how present it is. */
export type Ripple = { x: number; y: number; reach: number; strength: number };

/**
 * The campaign's one water idea: the tone leaves the speaker as rings on a calm surface. A tonal
 * Material surface, a soft pool of light under each ripple and thin concentric strokes whose spacing
 * widens and whose ink fades as they travel. No texture, glow or caustic noise competes with the UI.
 */
export const WaterArt: React.FC<{ width: number; height: number; ripples?: Ripple[] }> = ({ width, height, ripples }) => {
  const sets = ripples ?? [{ x: width / 2, y: height * 0.98, reach: Math.max(width, height) * 0.7, strength: 0.8 }];
  return (
    <svg aria-hidden width={width} height={height} viewBox={`0 0 ${width} ${height}`} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <defs>
        <linearGradient id="surface" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0" stopColor={SURFACE_TOP} />
          <stop offset="1" stopColor={SURFACE_DEEP} />
        </linearGradient>
        <radialGradient id="pool">
          <stop offset="0" stopColor="#FFFFFF" stopOpacity=".75" />
          <stop offset=".55" stopColor="#FFFFFF" stopOpacity=".18" />
          <stop offset="1" stopColor="#FFFFFF" stopOpacity="0" />
        </radialGradient>
      </defs>
      <rect width={width} height={height} fill="url(#surface)" />
      {sets.map((r, i) => (
        <g key={i}>
          <circle cx={r.x} cy={r.y} r={r.reach * 0.42} fill="url(#pool)" opacity={r.strength} />
          {Array.from({ length: 7 }, (_, k) => {
            // Rings spread out as they travel, the way a real ripple's wavelength grows.
            const t = (k + 1) / 7;
            const radius = r.reach * Math.pow(t, 1.35);
            return (
              <circle key={k} cx={r.x} cy={r.y} r={radius} fill="none" stroke={RIPPLE}
                strokeWidth={3.2 - t * 1.6} strokeOpacity={r.strength * (0.46 - t * 0.36)} />
            );
          })}
        </g>
      ))}
    </svg>
  );
};
