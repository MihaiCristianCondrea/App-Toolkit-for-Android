import React from "react";
import { LitScreen } from "../LitScreen";
import { Shape } from "../Shape";
import { shapeUrl } from "../../../../../../../ui/src/kit/assets.js";
import { FRAME, measure } from "./geometry";
import { scatterConfetti } from "./confetti";
import { morphPath } from "./morph";
import type { Confetto } from "./confetti";
import { clamp, enter, leave, mix, ramp, pop, standard } from "../lib/anim";
import { FONT } from "../theme";

// ---------------------------------------------------------------------------------------------------
// The phone: the real kit screen in Material-Next's 3D Pixel, placed by its frame's centre.

export type PhonePose = { x: number; y: number; scale: number; roll?: number; yaw?: number; pitch?: number };
export type Screen = [string, Record<string, unknown>];

/** The phone. `children` are drawn over the screen in the frame's own pixels (465 x 989, the screen at 17.6, 15.8), so they move with the phone; they line up only while the phone faces the camera (yaw and pitch 0). */
/**
 * `shadow` scales the phone's drop shadow (1 is the listings' own). The phone is placed by its transform alone,
 * from the origin: Blink rounds a box's left and top to whole pixels, which would make a slow phone step a pixel
 * at a time, while a transform's translation is drawn exactly.
 */
export const Phone: React.FC<{ screen: Screen; pose: PhonePose; scroll?: number; dx?: number; dy?: number; grow?: number; opacity?: number; z?: number; shadow?: number; children?: React.ReactNode }> = ({ screen, pose, scroll, dx = 0, dy = 0, grow = 1, opacity = 1, z = 2, shadow = 1, children }) => (
  <div style={{ position: "absolute", left: 0, top: 0, width: FRAME.width, height: FRAME.height, zIndex: z, opacity, transformOrigin: "50% 50%", transform: `translate(${pose.x - FRAME.width / 2 + dx}px, ${pose.y - FRAME.height / 2 + dy}px) rotate(${pose.roll ?? 0}deg) scale(${pose.scale * grow})`, filter: shadow > 0.002 ? `drop-shadow(0 4px 6px rgba(30,24,16,${(0.1 * shadow).toFixed(3)})) drop-shadow(0 30px 40px rgba(30,24,16,${(0.16 * shadow).toFixed(3)}))` : undefined }}>
    <LitScreen id={screen[0]} state={screen[1]} pose={{ yaw: pose.yaw ?? 0, pitch: pose.pitch ?? 0, perspective: 3000 }} style={{ width: FRAME.width }} scroll={scroll} />
    {children && <svg aria-hidden width={FRAME.width} height={FRAME.height} style={{ position: "absolute", left: 0, top: 0, overflow: "visible" }}>{children}</svg>}
  </div>
);

/** Where the phone's speaker holes are, and which way sound leaves them, in this pose. */
export const speakers = (pose: PhonePose) =>
  measure({ pitch: pose.pitch ?? 0, yaw: pose.yaw ?? 0, roll: pose.roll ?? 0, perspective: 3000, scale: pose.scale, x: pose.x, y: pose.y }, { critical: {}, secondary: {} }).speakers;

// ---------------------------------------------------------------------------------------------------
// Type: lines rise out of a mask, one per beat.

export const Headline: React.FC<{ t: number; at: number[]; title: string; sub?: string; x: number; y: number; color: string; subColor?: string; size?: number; subScale?: number; align?: "left" | "center"; width?: number }> = ({ t, at, title, sub, x, y, color, subColor, size = 104, subScale = 0.4, align = "left", width = 900 }) => {
  const line = (text: string, start: number, fontSize: number, weight: number, tone: string, spacing: number, stagger: number) => {
    const words = text.split(" ");
    const padX = fontSize * 0.08, padBottom = fontSize * 0.16;
    return (
      <div style={{ display: "flex", flexWrap: "wrap", justifyContent: align === "center" ? "center" : "flex-start", columnGap: fontSize * 0.24, rowGap: fontSize * 0.08, paddingBottom: padBottom }}>
        {words.map((word, i) => {
          const wStart = start + i * stagger;
          const r = ramp(t, wStart, 0.48, enter);
          const s = pop(t, wStart, 11, 185);
          const wght = Math.round(mix(weight * 0.72, weight, r));
          const wdth = Math.round(mix(92, 100, r));
          return (
            <span key={i} style={{ display: "inline-block", overflow: "hidden", padding: `0 ${padX}px ${padBottom}px`, margin: `0 ${-padX}px ${-padBottom}px` }}>
              <span style={{
                display: "inline-block",
                transform: `translateY(${(1 - r) * 95}%) scale(${0.86 + 0.14 * s})`,
                transformOrigin: "50% 80%",
                opacity: clamp(r * 1.85),
                fontSize,
                fontWeight: wght,
                fontVariationSettings: `"wght" ${wght}, "wdth" ${wdth}`,
                lineHeight: 1.04,
                letterSpacing: spacing,
                color: tone,
              }}>
                {word}
              </span>
            </span>
          );
        })}
      </div>
    );
  };
  return (
    <div style={{ position: "absolute", left: align === "center" ? x - width / 2 : x, top: y, width, textAlign: align, fontFamily: FONT, zIndex: 6 }}>
      {line(title, at[0], size, 650, color, -size * 0.035, 0.09)}
      {sub && <div style={{ marginTop: size * 0.08 }}>{line(sub, at[1] ?? at[0] + 0.3, size * subScale, 500, subColor ?? color, -0.4, 0.05)}</div>}
    </div>
  );
};

// ---------------------------------------------------------------------------------------------------
// Water, as the Water Eject listing draws it: on every note a set of three thin arcs with a soft glow leaves
// the speaker edge, glass bubbles (a clear centre, a tinted rim, a highlight and a crescent) rise off the
// fronts, and on the strong notes droplets are thrown out of the grille and fall.

/** Material elevation, used by every lifted surface in the film (phones, receipt, tiles, chips). */
export const ELEVATION = {
  1: "0 1px 2px rgba(30,24,16,.10), 0 4px 10px rgba(30,24,16,.08)",
  2: "0 1px 3px rgba(30,24,16,.10), 0 10px 24px rgba(30,24,16,.10)",
  3: "0 2px 6px rgba(30,24,16,.10), 0 22px 44px rgba(30,24,16,.14)",
};

/** The listing's glass bubble; `id` names the shared gradient in the enclosing svg. */
export const Bubble: React.FC<{ x: number; y: number; r: number; rim: string; id?: string; opacity?: number }> = ({ x, y, r, rim, id = "tt-glass", opacity = 1 }) => (
  <g opacity={opacity}>
    <circle cx={x} cy={y} r={r} fill={`url(#${id})`} stroke={rim} strokeOpacity=".65" strokeWidth={Math.max(1.6, r * 0.09)} />
    <ellipse cx={x - r * 0.36} cy={y - r * 0.4} rx={r * 0.26} ry={r * 0.16} fill="#FFFFFF" transform={`rotate(-35 ${x - r * 0.36} ${y - r * 0.4})`} />
    <path d={`M ${x + r * 0.55} ${y + r * 0.2} A ${r * 0.7} ${r * 0.7} 0 0 1 ${x + r * 0.1} ${y + r * 0.62}`} fill="none" stroke="#FFFFFF" strokeWidth={Math.max(1.2, r * 0.08)} strokeOpacity=".85" strokeLinecap="round" />
  </g>
);
export const GlassDefs: React.FC<{ tint: string; id?: string }> = ({ tint, id = "tt-glass" }) => (
  <defs>
    <radialGradient id={id} cx=".5" cy=".5" r=".5">
      <stop offset=".62" stopColor="#FFFFFF" stopOpacity=".18" />
      <stop offset=".9" stopColor={tint} stopOpacity=".6" />
      <stop offset="1" stopColor="#FFFFFF" stopOpacity=".95" />
    </radialGradient>
    <filter id={`${id}-glow`} x="-30%" y="-30%" width="160%" height="160%"><feGaussianBlur stdDeviation="7" /></filter>
  </defs>
);

/**
 * Water Eject waves and bubbles, as the store listing draws them (custom/speaker-water-eject): for each bottom
 * speaker group, concentric arcs centered on the projected speaker holes open along the bottom face's outward
 * normal across +-44 degrees, growing from small to big on the Fibonacci scale (34, 55, 89, 144, 233). Glass
 * bubbles ride outside the wavefronts from small to large and travel outward away from the bottom of the phone.
 */
const WAVE_SPREAD = 44;
const WAVE_RINGS = [34, 55, 89, 144, 233];
const WAVE_RIDERS: [number, number, number][] = [
  [1, 22, 8],
  [2, -30, 13],
  [2, 34, 8],
  [3, -38, 13],
  [3, 12, 21],
  [4, -20, 21],
  [4, 30, 13],
];
export const SoftWaves: React.FC<{ t: number; onsets: number[]; sources: { at: [number, number]; direction: [number, number] }[]; tones: string[]; rim: string; tint: string; drops?: number[]; bubbles?: [number, 0 | 1][]; visible?: number }> = ({ t, onsets, sources, tones, rim, tint, drops = [], bubbles = [], visible = 1 }) => {
  if (visible <= 0) return null;
  const turn = (d: [number, number], deg: number): [number, number] => { const a = (deg * Math.PI) / 180; return [d[0] * Math.cos(a) - d[1] * Math.sin(a), d[0] * Math.sin(a) + d[1] * Math.cos(a)]; };
  const sorted = [...sources].sort((a, b) => a.at[0] - b.at[0]);
  const swell = onsets.reduce((acc, o) => (t >= o ? acc + Math.exp(-(t - o) * 3.0) * Math.min(1, (t - o) / 0.08) : acc), 0);
  const onArc = (at: [number, number], d: [number, number], radius: number, degrees: number): [number, number] => {
    const n: [number, number] = [-d[1], d[0]], a = (degrees * Math.PI) / 180;
    return [at[0] + radius * (d[0] * Math.cos(a) + n[0] * Math.sin(a)), at[1] + radius * (d[1] * Math.cos(a) + n[1] * Math.sin(a))];
  };
  const arcPath = (at: [number, number], d: [number, number], radius: number) =>
    `M ${Array.from({ length: 41 }, (_, i) => onArc(at, d, radius, -WAVE_SPREAD + (2 * WAVE_SPREAD * i) / 40).map((v) => v.toFixed(1)).join(" ")).join(" L ")}`;
  const t0 = onsets[0] ?? 3.3;
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, pointerEvents: "none", overflow: "visible", opacity: visible }}>
      <GlassDefs tint={tint} />
      <defs><filter id="tt-soft" x="-20%" y="-20%" width="140%" height="140%"><feGaussianBlur stdDeviation="4" /></filter></defs>
      {sorted.map(({ at, direction: d }, side) => (
        <g key={side}>
          {WAVE_RINGS.map((baseR, ring) => {
            const appear = ramp(t, t0 - 0.1 + ring * 0.07, 0.38, enter);
            if (appear <= 0) return null;
            const r = baseR * (0.82 + 0.18 * appear) + swell * (3 + ring * 2.5);
            const path = arcPath(at, d, r);
            const fade = (1 - ring * 0.15) * appear;
            return (
              <g key={ring} opacity={fade}>
                <path d={path} fill="none" stroke={rim} strokeWidth={7} strokeLinecap="round" opacity=".18" filter="url(#tt-soft)" />
                <path d={path} fill="none" stroke={tones[0]} strokeWidth={Math.max(1.4, 2.6 - ring * 0.3)} strokeLinecap="round" opacity=".58" />
              </g>
            );
          })}
          {onsets.map((o, k) => {
            const life = t - o;
            if (life < 0 || life > 1.15) return null;
            const p = enter(clamp(life / 1.15));
            const r = mix(WAVE_RINGS[0], WAVE_RINGS[WAVE_RINGS.length - 1], p);
            const path = arcPath(at, d, r);
            return (
              <g key={`p${k}`} opacity={(1 - p) * 0.75}>
                <path d={path} fill="none" stroke={rim} strokeWidth={6} strokeLinecap="round" opacity=".16" filter="url(#tt-soft)" />
                <path d={path} fill="none" stroke={tones[0]} strokeWidth={mix(2.6, 1.4, p)} strokeLinecap="round" opacity=".6" />
              </g>
            );
          })}
          {WAVE_RIDERS.map(([ring, degrees, baseR], rIdx) => {
            const riderStart = t0 + ring * 0.08 + rIdx * 0.04;
            const appear = pop(t, riderStart, 11, 165);
            if (appear <= 0) return null;
            const cycle = ((Math.max(0, t - riderStart) * 0.55 + rIdx * 0.17) % 1);
            const drift = cycle * 28 + swell * (3 + ring * 1.8);
            const radius = WAVE_RINGS[ring] + baseR * 0.7 + drift;
            const deg = (side ? -degrees : degrees) + (side ? 6 : 0) + Math.sin((t - riderStart) * 2.4 + rIdx) * 2.5;
            const [x, y] = onArc(at, d, radius, deg);
            const fadeOut = 1 - clamp((cycle - 0.78) / 0.22);
            const fadeIn = clamp(cycle / 0.15);
            const bScale = Math.min(1, appear) * (0.85 + 0.15 * fadeIn);
            return <Bubble key={`r${side}-${rIdx}`} x={x} y={y} r={baseR * bScale} rim={rim} opacity={0.9 * Math.min(1, appear) * fadeIn * fadeOut} />;
          })}
        </g>
      ))}
      {bubbles.map(([o, side], k) => {
        const life = t - o;
        if (life < 0 || life > 1.6) return null;
        const src = sorted[Math.min(side, sorted.length - 1)], d = src.direction;
        const travel = enter(clamp(life / 1.55));
        const radius = mix(WAVE_RINGS[0], WAVE_RINGS[WAVE_RINGS.length - 1] + 24, travel);
        const deg = ((k % 5) - 2) * 16 + (side ? 5 : -5);
        const [x, y] = onArc(src.at, d, radius, deg);
        const r = mix(8, 18, travel) * Math.min(1, life / 0.18);
        return <Bubble key={`b${k}`} x={x} y={y} r={r} rim={rim} opacity={0.85 * Math.min(1, life / 0.18) * (1 - clamp((life - 1.2) / 0.4))} />;
      })}
      {drops.flatMap((o, k) => {
        if (t < o || t > o + 1.1) return [];
        return sorted.map(({ at, direction: d }, side) => {
          const life = t - o, [ux, uy] = turn(d, side ? 14 : -14), v = 250, x = at[0] + ux * v * life, y = at[1] + uy * v * life + 620 * life * life, size = 4.5;
          const angle = (Math.atan2(uy * v + 1240 * life, ux * v) * 180) / Math.PI + 90;
          return <path key={`d${k}-${side}`} d={`M 0 ${-size * 1.5} C ${size * 0.55} ${-size * 0.4} ${size} 0 ${size} ${size * 0.35} A ${size} ${size} 0 0 1 ${-size} ${size * 0.35} C ${-size} 0 ${-size * 0.55} ${-size * 0.4} 0 ${-size * 1.5} Z`} fill={tones[2] ?? tones[0]} opacity={0.55 * (1 - clamp((life - 0.6) / 0.5))} transform={`translate(${x.toFixed(1)} ${y.toFixed(1)}) rotate(${angle.toFixed(1)})`} />;
        });
      })}
    </svg>
  );
};

/**
 * The Decision Wheel exactly as the app draws it in its sheet (marketing/ui/src/kit/decision-wheel.js and kit.css
 * .wheel-stage): a 248-px disc of the surface colour with an 8-px margin round a 300-unit plate of six segments
 * at radius 144 (primary, primary container and a 24% mix, in turn), labels at radius 88 in Google Sans Flex 600
 * 12, a hub, and the pointer at the top. Colours are the Play screen's, read from the rendered sheet. `box` is
 * the disc's size in pixels; the plate turns by `rotation` degrees as the app's does. `winner` and `lift` mark
 * the pick once it has settled.
 */
export const APP_WHEEL = { box: 248, fills: ["#6B5E00", "#FFF0B3", "#D5D5B9"], inks: ["#FFFFFF", "#211B00", "#171D19"], surface: "#F6FBF4", primary: "#6B5E00", card: "#F2E8C9" };
export const AppWheel: React.FC<{ cx: number; cy: number; box: number; rotation: number; items: string[]; winner?: number; lift?: number; shadow?: number }> = ({ cx, cy, box, rotation, items, winner = -1, lift = 0, shadow = 0 }) => {
  const k = box / APP_WHEEL.box, u = (232 * k) / 300, n = items.length, top = cy - box / 2 - 2 * k;
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, overflow: "visible", pointerEvents: "none", filter: shadow > 0 ? `drop-shadow(0 ${14 * shadow}px ${26 * shadow}px rgba(53,35,62,${0.28 * shadow}))` : undefined }}>
      <circle cx={cx} cy={cy} r={box / 2} fill={APP_WHEEL.surface} />
      <g transform={`translate(${cx} ${cy}) rotate(${rotation}) scale(${u})`}>
        {items.map((item, i) => {
          const a = ((i / n) * 360 - 90) * (Math.PI / 180), b = (((i + 1) / n) * 360 - 90) * (Math.PI / 180), mid = (a + b) / 2, win = i === winner, out = win ? lift * 10 : 0;
          return (
            <g key={i} transform={`translate(${(Math.cos(mid) * out).toFixed(2)} ${(Math.sin(mid) * out).toFixed(2)})`}>
              <path d={`M0 0L${144 * Math.cos(a)} ${144 * Math.sin(a)} A144 144 0 ${1 / n > 0.5 ? 1 : 0} 1 ${144 * Math.cos(b)} ${144 * Math.sin(b)}Z`} fill={APP_WHEEL.fills[i % 3]} stroke={win && lift > 0 ? "#FFFFFF" : APP_WHEEL.surface} strokeWidth={win ? 2 + 2.5 * lift : 2} />
              <text x={88 * Math.cos(mid)} y={88 * Math.sin(mid)} textAnchor="middle" dominantBaseline="middle" fill={APP_WHEEL.inks[i % 3]} fontFamily={FONT} fontWeight={600} fontSize={12 * (win ? 1 + 0.18 * lift : 1)}>{item.length > 12 ? item.slice(0, 11) + "…" : item}</text>
            </g>
          );
        })}
        <circle r="18" fill={APP_WHEEL.surface} />
        <circle r="6" fill={APP_WHEEL.primary} />
      </g>
      <path d={`M ${cx - 10 * k} ${top} L ${cx + 10 * k} ${top} L ${cx} ${top + 20 * k} Z`} fill={APP_WHEEL.primary} />
    </svg>
  );
};

// ---------------------------------------------------------------------------------------------------
// Decoration, as every listing screenshot has it: small MaterialShapes in the world's tones scattered over
// the open ground, each popping in on its own spring, then turning slowly in place.

export type Box = { left: number; top: number; right: number; bottom: number };
export type DecorPiece = { x: number; y: number; size: number; name: string; color: string; spin: number; delay: number };
export const Decor: React.FC<{ t: number; start: number; pieces: DecorPiece[]; z?: number }> = ({ t, start, pieces, z = 1 }) => (
  <div style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none" }}>
    {pieces.map((q, i) => {
      const p = pop(t, start + 0.15 + q.delay, 10, 170), size = q.size * p;
      if (size <= 0.5) return null;
      // Still in place once they have landed: only a slow turn, so nothing jitters.
      return <Shape key={i} name={q.name} x={q.x - size / 2} y={q.y - size / 2} size={size} color={q.color} rotate={i * 37 + (t - start) * q.spin * 0.5 + (1 - p) * -90} />;
    })}
  </div>
);

// ---------------------------------------------------------------------------------------------------
// Material chips: filter chips (selected shows a check) and assist chips, drawn as the app draws them.

export const Chip: React.FC<{ label: string; selected?: boolean; color: string; container: string; outline: string; size?: number; style?: React.CSSProperties }> = ({ label, selected, color, container, outline, size = 30, style }) => (
  <div style={{ display: "inline-flex", alignItems: "center", gap: size * 0.3, height: size * 1.9, padding: `0 ${size * 0.62}px 0 ${selected ? size * 0.42 : size * 0.62}px`, borderRadius: size * 0.5, boxSizing: "border-box", background: selected ? container : "transparent", border: selected ? "none" : `2px solid ${outline}`, color, fontFamily: FONT, fontSize: size, fontWeight: 560, whiteSpace: "nowrap", ...style }}>
    {selected && <svg width={size * 0.9} height={size * 0.9} viewBox="0 0 24 24"><path d="M5 12.5 l4.5 4.5 L19 7.5" fill="none" stroke={color} strokeWidth="2.6" strokeLinecap="round" strokeLinejoin="round" /></svg>}
    {label}
  </div>
);

/**
 * The worlds gathering: a Material ripple per onset, a tonal disc in a world's container colour that grows out
 * of the icon and fades as it spreads, the way a pressed surface's state layer does.
 */
export const Ripples: React.FC<{ t: number; onsets: number[]; at: [number, number]; colors: string[]; from: number; reach: number; z?: number }> = ({ t, onsets, at, colors, from, reach, z = 2 }) => (
  <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none" }}>
    {onsets.map((o, k) => {
      if (t < o) return null;
      // Smooth fade to zero opacity so the disc clears without an abrupt cut-off jump.
      const p = enter(clamp((t - o) / 1.1));
      const u = clamp((t - o - 0.15) / 0.95);
      const fade = (1 - u) * (1 - u);
      return fade > 0.001 && <circle key={k} cx={at[0]} cy={at[1]} r={from + p * reach} fill={colors[k % colors.length]} opacity={0.9 * fade} />;
    })}
  </svg>
);

// ---------------------------------------------------------------------------------------------------
// Ruler and Protractor: the listing's artwork, with a slide and a live arm.

/**
 * The listing's yellow ruler at 8 px per millimetre, with centimetre labels; `mm` is the measured length,
 * shown as a band and a line, the same reading the Surface Ruler phone shows.
 */
export const Ruler: React.FC<{ x: number; y: number; width: number; angle?: number; ticks?: number; mm?: number; accent?: string; px?: number; body?: number }> = ({ x, y, width, angle = 0, ticks = 1, mm = 0, accent = "#8F5700", px = 8, body = 1 }) => {
  const pad = 28, n = Math.floor((width - 2 * pad) / px), shown = Math.floor((n + 1) * clamp(ticks)), band = Math.min(mm, n) * px, h = 136 * body;
  return (
    <g transform={`translate(${x} ${y}) rotate(${angle})`}>
      {body > 0 && <rect x="0" y="12" width={width} height={h} rx={Math.min(36, h / 2)} fill="#B56D00" opacity={0.16 * body} />}
      {body > 0 && <rect width={width} height={h} rx={Math.min(36, h / 2)} fill="#F6B91D" />}
      {mm > 0 && <rect x={pad} y="0" width={band} height={h} fill={accent} opacity=".16" />}
      {Array.from({ length: shown }, (_, i) => <line key={i} x1={pad + i * px} x2={pad + i * px} y1="0" y2={i % 10 === 0 ? 62 : i % 5 === 0 ? 44 : 26} stroke="#654000" strokeWidth={i % 5 ? 1.6 : 2.8} strokeLinecap="round" />)}
      {body > 0.6 && Array.from({ length: Math.floor((shown - 1) / 10) + 1 }, (_, k) => k > 0 && <text key={k} x={pad + k * 10 * px} y="96" textAnchor="middle" fontFamily={FONT} fontWeight={600} fontSize="26" fill="#654000" opacity={clamp((body - 0.6) / 0.4)}>{k}</text>)}
      {mm > 0 && <rect x={pad + band - 3} y="-16" width="6" height={h + 32} rx="3" fill={accent} />}
    </g>
  );
};

// ---------------------------------------------------------------------------------------------------
// Tip & Bill Splitter: one card per person, each with the share the app works out.

export const ShareCard: React.FC<{ x: number; y: number; who: string; amount: string; ink: string; container: string; avatar: string; shape: string }> = ({ x, y, who, amount, ink, container, avatar, shape }) => (
  <div style={{ position: "absolute", left: x, top: y, width: 380, height: 128, borderRadius: 32, background: container, boxShadow: ELEVATION[1], display: "flex", alignItems: "center", gap: 22, padding: "0 30px", boxSizing: "border-box", fontFamily: FONT, color: ink, zIndex: 4 }}>
    <div style={{ position: "relative", width: 72, height: 72, flex: "none" }}>
      <Shape name={shape} x={0} y={0} size={72} color={avatar} rotate={0} />
      <div style={{ position: "absolute", inset: 0, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 32, fontWeight: 650 }}>{who}</div>
    </div>
    <div style={{ display: "flex", flexDirection: "column" }}>
      <div style={{ fontSize: 24, fontWeight: 500, opacity: 0.8 }}>Pays</div>
      <div style={{ fontSize: 50, fontWeight: 650, letterSpacing: -1.5, lineHeight: 1 }}>{amount}</div>
    </div>
  </div>
);

// ---------------------------------------------------------------------------------------------------
// Unit Converter: the pair as two Material 3 Expressive loading-indicator shapes. They turn all the time, in
// opposite directions; on each category change both spin on and their outlines flow into the next pair's
// shapes while the values swap, and the swap arrows between them turn over.

export type UnitFace = { value: string; label: string };
const MORPH = 0.55;
/** The turn of one of the pair's shapes at time t (side 0 turns clockwise, side 1 the other way). */
export const morphTurn = (t: number, side: 0 | 1, turns: number) => (side ? -1 : 1) * (t * 36 + turns * 150) + side * 20;
export const MorphPair: React.FC<{ t: number; cx: number; cy: number; gap: number; size: number; changes: number[]; shapes: [string, string][]; faces: [UnitFace, UnitFace][]; colors: [string, string]; ink: string; arrows: string; appear?: number; faceIn?: [number, number] }> = ({ t, cx, cy, gap, size, changes, shapes, faces, colors, ink, arrows, appear: shown, faceIn }) => {
  const idx = Math.max(0, changes.filter((c) => t >= c).length - 1), tc = changes[idx] ?? 0;
  const morphs = changes.map((c, j) => (j === 0 ? 0 : enter(clamp((t - c) / MORPH))));
  const turns = morphs.reduce((a, v) => a + v, 0), p = idx > 0 ? morphs[idx] : 1, appear = shown ?? pop(t, changes[0] - 0.15, 12, 170);
  const swapOut = clamp((t - tc) / 0.18), swapIn = clamp((t - tc - 0.16) / 0.3);
  const face = (side: 0 | 1, x: number) => {
    const prev = faces[Math.max(0, idx - 1)][side], cur = faces[idx][side], show = (f: UnitFace, o: number, k: number) => (
      <div style={{ position: "absolute", inset: 0, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", opacity: o, transform: `scale(${k})` }}>
        <div style={{ fontSize: size * (f.value.length > 4 ? 0.2 : f.value.length > 3 ? 0.23 : 0.27), fontWeight: 650, letterSpacing: -2, lineHeight: 1 }}>{f.value}</div>
        <div style={{ fontSize: size * 0.13, fontWeight: 560, marginTop: 6 }}>{f.label}</div>
      </div>
    );
    return (
      <div key={side} style={{ position: "absolute", left: x - size / 2, top: cy - size / 2, width: size, height: size, zIndex: 5, fontFamily: FONT, color: ink }}>
        {idx > 0 && swapOut < 1 && show(prev, 1 - swapOut, 1 - 0.3 * swapOut)}
        {show(cur, idx > 0 ? enter(swapIn) : faceIn ? faceIn[side] : clamp(appear * 2), idx > 0 ? 0.7 + 0.3 * enter(swapIn) : 1)}
      </div>
    );
  };
  const xs = [cx - gap / 2, cx + gap / 2];
  return (
    <>
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 4, overflow: "visible" }}>
        {[0, 1].map((side) => {
          const from = shapes[Math.max(0, idx - 1)][side], to = shapes[idx][side], dir = side ? -1 : 1;
          const turn = morphTurn(t, side as 0 | 1, turns), k = appear * (1 + 0.08 * Math.sin(p * Math.PI)); void dir;
          return <path key={side} d={morphPath(from, to, p, xs[side], cy, size * k, turn)} fill={colors[side]} />;
        })}
        <g transform={`translate(${cx} ${cy}) rotate(${turns * 180}) scale(${appear})`}>
          <circle r="56" fill="#FFFFFF" />
          <g fill="none" stroke={arrows} strokeWidth="9" strokeLinecap="round" strokeLinejoin="round">
            <path d="M -28 -11 H 26 l -13 -13" /><path d="M 28 11 H -26 l 13 13" />
          </g>
        </g>
      </svg>
      {face(0, xs[0])}
      {face(1, xs[1])}
    </>
  );
};

// ---------------------------------------------------------------------------------------------------
// Tip & Bill Splitter: the listing's receipt, printing line by line.

export const Receipt: React.FC<{ x: number; y: number; angle: number; t: number; lines: number[]; ink: string; accent: string; scale?: number }> = ({ x, y, angle, t, lines, ink, accent, scale = 1.35 }) => {
  const show = (i: number) => { const p = ramp(t, lines[i], 0.45); return { opacity: p, transform: `translateY(${(1 - p) * 18}px)` }; };
  const big = pop(t, lines[3], 9, 200);
  return (
    <div style={{ position: "absolute", left: x, top: y, width: 292, height: 372, transform: `rotate(${angle}deg) scale(${scale})`, transformOrigin: "50% 50%", borderRadius: 28, background: "#FFFBF5", boxShadow: ELEVATION[3], padding: "28px 30px", boxSizing: "border-box", color: ink, fontFamily: FONT, zIndex: 4 }}>
      <div style={{ fontSize: 20, fontWeight: 550 }}>Dinner for two</div>
      <div style={{ ...show(0), display: "flex", justifyContent: "space-between", fontSize: 22, marginTop: 24 }}><span>Bill</span><b>€52.00</b></div>
      <div style={{ ...show(1), display: "flex", justifyContent: "space-between", fontSize: 22, marginTop: 14 }}><span>Tip 10%</span><b>€5.20</b></div>
      <div style={{ ...show(2), borderTop: "3px dotted #D9A08C", marginTop: 22, paddingTop: 16, fontSize: 17 }}>Per person</div>
      <div style={{ opacity: clamp(big * 2), transform: `scale(${0.6 + 0.4 * big})`, transformOrigin: "0 50%", fontSize: 54, fontWeight: 650, letterSpacing: -2, marginTop: 3 }}>€28.60</div>
      <div style={{ ...show(4), fontSize: 17, marginTop: 9, color: accent }}>Total €57.20</div>
    </div>
  );
};

// ---------------------------------------------------------------------------------------------------
// Confetti: the Decision Wheel listing's blue-noise scatter, thrown out from a point and settling.

export const burstPieces = (o: { seed: number; count: number; from: [number, number]; colors: string[]; avoid?: { x: number; y: number; r: number }[]; boxes?: Box[] }): Confetto[] =>
  scatterConfetti({
    width: 1920, height: 1080, seed: o.seed, count: o.count, margin: 30, colors: o.colors,
    focus: { left: o.from[0] - 80, top: o.from[1] - 80, right: o.from[0] + 80, bottom: o.from[1] + 80 },
    free: (x, y, r) => (o.avoid ?? []).every((a) => Math.hypot(a.x - x, a.y - y) > a.r + r * 1.6) && (o.boxes ?? []).every((b) => x + r * 1.6 < b.left || x - r * 1.6 > b.right || y + r * 1.6 < b.top || y - r * 1.6 > b.bottom),
  }).map((q) => ({ ...q, w: q.w * 1.6, h: q.h * 1.6 }));

/** Where confetto `i` is at time t: out on its own spring, then a flutter (a slow fall, a sway, a turn that never stops). */
export const confettoAt = (q: Confetto, i: number, t: number, at: number, from: [number, number]) => {
  const delay = (i % 7) * 0.012, p = enter(clamp((t - at - delay) / 0.95)), after = Math.max(0, t - at - 0.7);
  return {
    x: mix(from[0], q.x, p) + Math.sin(after * 2.4 + i) * 10 * clamp(after), y: mix(from[1], q.y, p) + after * 22,
    flip: Math.cos(after * (2 + (i % 5) * 0.4) + i), rot: q.rotate + (1 - p) * 420 + after * 50 * ((i % 2) * 2 - 1), k: 0.35 + 0.65 * p,
  };
};
export const ConfettiBurst: React.FC<{ t: number; at: number; from: [number, number]; pieces: Confetto[]; z?: number; skip?: number; fade?: number }> = ({ t, at, from, pieces, z = 7, skip = 0, fade = 1 }) => {
  if (t < at) return null;
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: z, pointerEvents: "none", opacity: fade }}>
      {pieces.map((q, i) => {
        if (i < skip) return null;
        const { x, y, flip, rot, k } = confettoAt(q, i, t, at, from);
        return q.kind === "dot"
          ? <circle key={i} cx={x} cy={y} r={(q.w / 2) * k} fill={q.color} />
          : <rect key={i} x={x - (q.w * k) / 2} y={y - (q.h * k * (0.45 + 0.55 * Math.abs(flip))) / 2} width={q.w * k} height={q.h * k * (0.45 + 0.55 * Math.abs(flip))} rx={(Math.min(q.w, q.h) * k) / 2} fill={q.color} transform={`rotate(${rot.toFixed(1)} ${x.toFixed(1)} ${y.toFixed(1)})`} />;
      })}
    </svg>
  );
};

// ---------------------------------------------------------------------------------------------------
// The hand-off: the next world grows out of a MaterialShape until it fills the frame.

export const ShapeWipe: React.FC<{ t: number; start: number; dur: number; at: [number, number]; shape: string; children: React.ReactNode }> = ({ t, start, dur, at, shape, children }) => {
  const p = clamp((t - start) / dur);
  if (p >= 1) return <div style={{ position: "absolute", inset: 0 }}>{children}</div>;
  // From rest: an entering curve starts at full speed, which grew the mask by 1600 px in one frame.
  const size = 20 + standard(p) * 5200, mask = `url("${shapeUrl(shape)}") ${at[0] - size / 2}px ${at[1] - size / 2}px / ${size}px ${size}px no-repeat`;
  return <div style={{ position: "absolute", inset: 0, WebkitMask: mask, mask }}>{children}</div>;
};
