import React, { useMemo } from "react";
import { AbsoluteFill, Audio, Img, getInputProps, staticFile, useCurrentFrame } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import timeline from "./timeline.json";
import icon from "./assets/icon.png";
// Google's official badge, unaltered (marketing/assets/badges/google-play): moved and scaled whole, never redrawn.
import badge from "../../../../../assets/badges/google-play/en_badge_web_generic.png";
import { B, FPS, clamp, enter, leave, mix, pop, ramp, smooth, standard } from "./lib/anim";
import { CONFETTI, FONT, NEUTRAL, WORLDS } from "./theme";
import type { World } from "./theme";
import { Chip, ConfettiBurst, Decor, ELEVATION, Headline, MorphPair, Phone, Receipt, Ripples, Ruler, ShapeWipe, ShareCard, SoftWaves, AppWheel, APP_WHEEL, burstPieces, confettoAt, morphTurn, speakers } from "./motifs";
import type { PhonePose, Screen, UnitFace } from "./motifs";
import { morphPath } from "./motifs/morph";
import { CHOICES, FOCUS, HERO_BOX, WINNER, wheelState } from "./wheelMotion";
import { throughEdge } from "./turn";

/**
 * "One app. Many everyday moments." The app's name, then a story in which one useful form turns into the next:
 * the tone that pushes water out of a speaker, the last wave flattening into a ruler, the ruler's reading cut to a conversion,
 * the conversion folding into a bill, the bill's rest cut to a choice, the choice's confetti becoming the
 * app's tools, and the tools gathering into the icon. Every scene keeps its own shot and its own way in; there
 * is no shared camera and no per-tool title: the tools show what they are. All times are seconds in timeline.json,
 * read by the soundtrack too.
 */

const GRID = { left: 144, right: 1776, top: 108, bottom: 972 };
const TITLE = 104;
const SCENE = Object.fromEntries(timeline.scenes.map((s) => [s.id, s])) as Record<string, (typeof timeline.scenes)[number]>;

// The real screens, in the states the store listings show.
const SCREENS: Record<string, Screen> = {
  water: ["quick-tools", { activeToolId: "water_eject" }],
  protractor: ["numbers", { activeSheet: "protractor", protractorAngle: 45 }],
  bill: ["numbers", { activeSheet: "bill_splitter", billAmount: "52.00", billCurrency: "EUR", billTipEnabled: true, tipPercent: 10, splitPeople: 2 }],
  wheel: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 0, wheelWinner: "" }],
  result: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 1530, wheelWinner: "Music" }],
};
/** Water Eject opens from its card in Quick Tools: the list is scrolled to it, as on the listing. */
const WATER_SCROLL = 672;
/** The Surface Ruler's reading for a marker position: the sheet's own sum (window height over 160 dpi in mm). */
const rulerMm = (marker: number) => (marker * 957) / (160 / 25.4);

const hex = (c: string) => [1, 3, 5].map((i) => parseInt(c.slice(i, i + 2), 16));
const mixColor = (a: string, b: string, p: number) => `rgb(${hex(a).map((v, i) => Math.round(v + (hex(b)[i] - v) * clamp(p))).join(",")})`;

/** Decoration placed by hand, as on the listing screenshots: a few MaterialShapes ([x, y, size]) at the frame's edges. */
const DECOR_NAMES = ["sunny", "flower", "gem", "clover_4leaf", "soft_burst", "cookie_6sided", "very_sunny", "clover_8leaf"];
const place = (spots: [number, number, number][], colors: string[], seed: number) =>
  spots.map(([x, y, size], i) => ({ x, y, size, name: DECOR_NAMES[(i * 3 + seed) % DECOR_NAMES.length], color: colors[(i + seed) % colors.length], spin: ((i % 2) * 2 - 1) * (8 + i * 2), delay: i * 0.07 }));

/** A large tonal shape that breathes slowly behind the phone. */
const Behind: React.FC<{ t: number; name: string; x: number; y: number; size: number; color: string; seed: number; start: number }> = ({ t, name, x, y, size, color, seed, start }) => {
  // From nothing on its cue: drawn early at part size, a wipe passing over it cut it into odd shapes.
  const p = pop(t, start, 14, 120), s = size * p;
  return <div style={{ position: "absolute", inset: 0, zIndex: 1 }}><Shape name={name} x={x - s / 2} y={y - s / 2} size={s} color={color} rotate={(t - start) * 6 + seed * 20} /></div>;
};
const Ground: React.FC<{ color: string; opacity?: number }> = ({ color, opacity = 1 }) => <AbsoluteFill style={{ background: color, opacity }} />;
const Title: React.FC<{ t: number; at: number[]; text: string; x?: number; y?: number; color: string; size?: number; sub?: string; subColor?: string; align?: "left" | "center"; width?: number }> = ({ t, at, text, x = GRID.left, y = GRID.top, color, size = TITLE, sub, subColor, align = "left", width = 1400 }) => (
  <Headline t={t} at={at} title={text} sub={sub} x={x} y={y} color={color} subColor={subColor} size={size} subScale={0.5} align={align} width={width} />
);

// ================================================================================================ 0 to 3.2: the name
// One shape per tool arrives on each note in its world's colour, the icon pops, the shapes breathe round it and
// the name and line land. Then the others swap out while the blue shape travels into the water world.
const INTRO_SHAPES: { world: World; name: string; x: number; y: number; size: number }[] = [
  { world: WORLDS.water, name: "cookie_9sided", x: 1590, y: 270, size: 240 },
  { world: WORLDS.ruler, name: "soft_burst", x: 330, y: 810, size: 260 },
  { world: WORLDS.unit, name: "cookie_6sided", x: 1600, y: 820, size: 230 },
  { world: WORLDS.bill, name: "pill", x: 340, y: 280, size: 230 },
  { world: WORLDS.wheel, name: "flower", x: 960, y: 120, size: 150 },
];
const introTurn = (t: number, i: number) => i * 23 + t * 10 + ramp(t, B.swing, 0.8, enter) * 40;
/** Every backdrop shape after the intro turns at the same slow rate, so a shape handed from one scene to the next never jumps. */
const spin = (t: number) => t * 8;
const Intro: React.FC<{ t: number }> = ({ t }) => {
  const swing = ramp(t, B.swing, 0.8, enter), iconP = pop(t, B.icon, 10, 190), [m0] = B.waterMorph;
  const out = (delay: number) => leave(clamp((t - m0 - delay) / 0.35));
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {INTRO_SHAPES.map((s, i) => {
        if (i === 0 && t >= m0) return null; // the blue one travels on (WaterMorph)
        const gone = i ? out(0.04 * i) : 0, p = pop(t, B.introShapes[i], 12, 200), size = s.size * p * (1 + 0.05 * Math.sin(swing * Math.PI)) * (1 - gone);
        return <Shape key={i} name={s.name} x={s.x - size / 2} y={s.y - size / 2} size={size} color={s.world.shape} rotate={introTurn(t, i) + gone * 90} />;
      })}
      {/* The name and icon clear first, so the blue shape travels over a clean frame. */}
      <div style={{ position: "absolute", inset: 0, opacity: 1 - standard(clamp((t - m0) / 0.22)), transform: `scale(${1 - 0.06 * standard(clamp((t - m0) / 0.22))})`, transformOrigin: "960px 540px", zIndex: 4 }}>
        <Img src={icon} style={{ position: "absolute", left: 960 - 150, top: 410 - 150 - (1 - iconP) * 30, width: 300, height: 300, transform: `scale(${iconP})` }} />
        <Headline t={t} at={[B.title, B.tagline]} title="App Toolkit" sub="Everyday tools in one app" x={960} y={590} width={1200} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={150} subScale={0.42} />
      </div>
    </AbsoluteFill>
  );
};
/** The blue shape's journey: from its place in the intro to the phone's, growing and morphing into the water backdrop. */
const WATER_BACKDROP_AT = { x: 960, y: 465 };
const waterMorph = (t: number) => {
  const [m0, m1] = B.waterMorph, p = standard(clamp((t - m0) / (m1 - m0)));
  return { p, x: mix(1590, WATER_BACKDROP_AT.x, p), y: mix(270, WATER_BACKDROP_AT.y, p), size: mix(240, WATER_BACKDROP, p), turn: mix(introTurn(t, 0), spin(t), p) };
};
const WaterMorph: React.FC<{ t: number }> = ({ t }) => {
  const m = waterMorph(t);
  return <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0 }}><path d={morphPath("cookie_9sided", "cookie_12sided", m.p, m.x, m.y, m.size * 0.98, m.turn)} fill={WORLDS.water.shape} /></svg>;
};

// ================================================================================================ 3 to 6.85: Water Eject
// The phone rises into the blue backdrop with the store listing's dramatic hero orientation (pitch 28, yaw -13),
// running off the top so its speaker edge faces the camera; below each speaker group, concentric arcs and glass
// bubbles fan out from small to big.
const WATER_POSE: PhonePose = { x: 945, y: 245, scale: 1.12, pitch: 28, yaw: -13, roll: 0 };
/** The water backdrop's size, scaled with the phone. */
const WATER_BACKDROP = 920;
const Water: React.FC<{ t: number; part: "back" | "front" }> = ({ t, part }) => {
  const w = WORLDS.water, rise = pop(t, B.waterPhone, 13, 140), dy = (1 - rise) * 1150; // from wholly below the frame
  const decor = useMemo(() => place([[300, 210, 84], [1640, 160, 92], [1790, 860, 60], [150, 900, 58], [1770, 500, 44], [380, 640, 40]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  if (part === "back") return (
    <AbsoluteFill>
      <Ground color={w.field} />
      {t >= B.waterMorph[1] && t < B.shapeMorph[0] && <Shape name="cookie_12sided" x={WATER_BACKDROP_AT.x - WATER_BACKDROP / 2} y={WATER_BACKDROP_AT.y - WATER_BACKDROP / 2} size={WATER_BACKDROP} color={w.shape} rotate={spin(t)} />}
      <Decor t={t} start={B.waterMorph[1] - 0.1} pieces={decor} />
    </AbsoluteFill>
  );
  const sources = speakers(WATER_POSE).map((s) => ({ at: [s.at[0], s.at[1] + dy] as [number, number], direction: s.direction }));
  return (
    <AbsoluteFill>
      {t < B.phoneTurn[0] && <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={WATER_POSE} dy={dy} />}
      <SoftWaves t={t} onsets={B.waterWaves} drops={B.waterDrops} sources={sources} tones={[w.tones[2], w.tones[1], w.tones[0]]} rim={w.bright} tint={w.tones[0]} bubbles={[[3.55, 0], [3.75, 1], [4.05, 0], [4.55, 1], [4.75, 0], [5.05, 1], [5.55, 0], [5.75, 1], [6.0, 0]]} visible={ramp(t, B.waterPhone + 0.25, 0.35) * (1 - clamp((t - B.phoneTurn[0] + 0.2) / 0.3))} />
    </AbsoluteFill>
  );
};

// ================================================================================================ 6.15 to 7: the wave becomes a ruler
// The last wave keeps travelling and loses its curve until it is a straight line across the frame; graduations
// grow out of it, the yellow world opens outward from it, and the line thickens into the ruler. Meanwhile the
// blue backdrop morphs and moves into the yellow one, and the phone turns: past edge-on it comes round as the
// Surface Ruler.
const RULER_Y = 690, RULER_PX = 14;
const WaveLine: React.FC<{ t: number }> = ({ t }) => {
  const [l0, l1] = B.waveLine, p = standard(clamp((t - l0) / (l1 - l0))), fade = 1 - clamp((t - B.rulerBody[0]) / (B.rulerBody[1] - B.rulerBody[0]));
  if (t < l0 || fade <= 0) return null;
  const cx = WATER_BACKDROP_AT.x, yb = mix(915, RULER_Y, p), xl = mix(cx - 210, -80, p), xr = mix(cx + 210, 2000, p), sag = mix(42, 0, p);
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <path d={`M ${xl} ${yb} Q ${cx} ${yb + 2 * sag} ${xr} ${yb}`} fill="none" stroke={mixColor(WORLDS.water.tones[2], "#654000", p)} strokeWidth={mix(2.4, 3, p)} strokeLinecap="round" opacity={fade} />
    </svg>
  );
};

// ================================================================================================ 6.15 to 10.95: Ruler & Protractor
// The app leads: the phone that turned in is the Surface Ruler, the Protractor joins it, and the ruler the wave
// became pulls back to a strip at the lower left. An arm rises from the ruler's zero to 45 degrees while the
// Protractor sweeps with it and holds, then a marker measures 76.0 mm while the Surface Ruler reads the same, so
// both phones end on their results. The reading lifts onto a shape the size of the next scene's first card,
// which keeps growing until it is the converter's ground.
const RULER_A: PhonePose = { x: 1090, y: 520, scale: 0.8, yaw: 12, roll: 0 };
const RULER_B: PhonePose = { x: 1565, y: 520, scale: 0.8, yaw: -12, roll: 0 };
/** Which phone leads: the Protractor while the arm sweeps, the Surface Ruler while it measures (0 = Ruler, 1 = Protractor). */
const rulerFocus = (t: number) => ramp(t, B.protractorSweep[0] - 0.3, 0.45, standard) * (1 - ramp(t, B.rulerMeasure[0] - 0.35, 0.45, standard));
const BACKDROP = { x: 1325, y: 520, size: 980 };
const STRIP = { y: 810, width: 760, px: 8 };
const MATCH = { x: 560, y: 520, size: 330 };
const rulerArm = (t: number) => {
  const s = B.protractorSweep, up = s.reduce((acc, at, i) => acc + ramp(t, at, 0.3) * [0.3, 0.25, 0.25, 0.2][i], 0);
  return 45 * up;
};
/** The water phone's turn into the Surface Ruler: one continuous turn through edge-on (src/turn.ts), the ruler facing us past it. */
const turnPose = (t: number): { pose: PhonePose; ruler: boolean } => {
  const [a, b] = B.phoneTurn, u = clamp((t - a) / (b - a)), e = standard(u), turn = throughEdge(u, WATER_POSE.yaw ?? 0, RULER_A.yaw ?? 0);
  return { pose: { x: mix(WATER_POSE.x, RULER_A.x, e), y: mix(WATER_POSE.y, RULER_A.y, e), scale: mix(WATER_POSE.scale, RULER_A.scale, e), pitch: mix(WATER_POSE.pitch ?? 0, 0, e), yaw: turn.yaw, roll: 0 }, ruler: turn.second };
};
const RulerScene: React.FC<{ t: number; part: "back" | "shape" | "phones" | "front" }> = ({ t, part }) => {
  const w = WORLDS.ruler, [m0, m1] = B.rulerMeasure, measured = ramp(t, m0, m1 - m0, standard), [p0, p1] = B.rulerPull, pull = standard(clamp((t - p0) / (p1 - p0)));
  const marker = 0.5 * measured, mm = rulerMm(marker), angle = rulerArm(t), shownAngle = Math.round(angle);
  const decor = useMemo(() => place([[760, 170, 80], [1840, 130, 56], [1860, 960, 62], [1060, 1000, 50], [70, 330, 46]], [w.tones[0], w.tones[1], w.container, "#EE9B00"], 2), []);
  if (part === "back") return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Decor t={t} start={p0} pieces={decor} />
    </AbsoluteFill>
  );
  if (part === "shape") {
    // The blue backdrop moves over and morphs into the yellow one; the yellow fills it from the centre out, so the
    // colour never passes through a muddy mix of the two.
    const [s0, s1] = B.shapeMorph, q = standard(clamp((t - s0) / (s1 - s0))), cx = mix(WATER_BACKDROP_AT.x, BACKDROP.x, q), cy = mix(WATER_BACKDROP_AT.y, BACKDROP.y, q);
    const size = mix(WATER_BACKDROP, BACKDROP.size, q), d = morphPath("cookie_12sided", "soft_burst", q, cx, cy, size * 0.98, spin(t)), fill = size * 0.6 * enter(clamp((t - s0 - 0.1) / (s1 - s0 - 0.15)));
    return (
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0 }}>
        <defs><clipPath id="tt-yellow-fill"><circle cx={cx} cy={cy} r={fill} /></clipPath></defs>
        {fill < size * 0.6 && <path d={d} fill={WORLDS.water.shape} />}
        <path d={d} fill={w.shape} clipPath={fill < size * 0.6 ? "url(#tt-yellow-fill)" : undefined} />
      </svg>
    );
  }
  if (part === "phones") {
    const turning = t < B.phoneTurn[1], a = turning ? turnPose(t) : { pose: RULER_A, ruler: true }, inB = 1 - ramp(t, p0 + 0.1, 0.75, enter), f = rulerFocus(t);
    return (
      <AbsoluteFill>
        {a.ruler ? <Phone screen={["numbers", { activeSheet: "ruler", rulerAdvanced: false, rulerMarker: marker }]} pose={a.pose} grow={mix(1, 0.86, f)} z={3} /> : <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={a.pose} z={3} />}
        {t >= p0 && <Phone screen={["numbers", { activeSheet: "protractor", protractorAngle: shownAngle }]} pose={RULER_B} dx={inB * 1150} grow={mix(0.86, 1, f)} z={4} />}
      </AbsoluteFill>
    );
  }
  const ruler = { y: mix(RULER_Y, STRIP.y, pull), width: mix(GRID.right - GRID.left, STRIP.width, pull), px: mix(RULER_PX, STRIP.px, pull) };
  const R_PRO = 210, proPivot = { x: GRID.left + 28 + R_PRO, y: ruler.y };
  const proArm = (a: number, r: number) => [proPivot.x + r * Math.cos((-a * Math.PI) / 180), proPivot.y + r * Math.sin((-a * Math.PI) / 180)];
  const proIn = ramp(t, p0 + 0.1, 0.45, standard), proOut = 1 - ramp(t, m0 - 0.15, 0.35, standard), proAlpha = proIn * proOut;
  const [hx, hy] = proArm(angle, R_PRO), [lx, ly] = proArm(Math.max(22, angle * 0.52), R_PRO * 0.62);
  const lift = ramp(t, B.rulerLift[0], B.rulerLift[1] - B.rulerLift[0], standard), markerX = GRID.left + 28 + mm * ruler.px;
  // After the lift, the shape under the reading keeps growing until it is the converter's ground.
  const [e0, e1] = B.rulerExpand, grow = leave(clamp((t - e0) / (e1 - e0)));
  const chip = { x: mix(markerX, MATCH.x, lift), y: mix(ruler.y - 110, MATCH.y, lift) }, unit = WORLDS.unit;
  return (
    <AbsoluteFill>
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
        {proAlpha > 0.01 && <g opacity={proAlpha}>
          <path d={`M ${proPivot.x - R_PRO} ${proPivot.y} A ${R_PRO} ${R_PRO} 0 0 1 ${proPivot.x + R_PRO} ${proPivot.y} Z`} fill="#FFF8E6" fillOpacity=".95" stroke="#C88200" strokeWidth="2.5" />
          {angle > 0.2 && <path d={`M ${proPivot.x} ${proPivot.y} L ${proPivot.x + R_PRO} ${proPivot.y} A ${R_PRO} ${R_PRO} 0 0 0 ${hx} ${hy} Z`} fill={w.container} fillOpacity=".92" />}
          <path d={`M ${proPivot.x - 48} ${proPivot.y} A 48 48 0 0 1 ${proPivot.x + 48} ${proPivot.y} Z`} fill={w.field} fillOpacity=".92" stroke="#E0A82E" strokeWidth="1.5" />
          {Array.from({ length: 37 }, (_, i) => {
            const deg = i * 5, major = deg % 15 === 0, len = deg % 30 === 0 ? 18 : major ? 13 : 8;
            const [x1, y1] = proArm(deg, R_PRO), [x2, y2] = proArm(deg, R_PRO - len), [tx, ty] = proArm(deg, R_PRO - 31);
            return (
              <g key={i}>
                <line x1={x1} y1={y1} x2={x2} y2={y2} stroke="#8F5700" strokeWidth={major ? 2.2 : 1.3} opacity={deg % 10 === 0 ? 0.9 : 0.45} strokeLinecap="round" />
                {deg > 0 && deg < 180 && deg % 30 === 0 && <text x={tx} y={ty} textAnchor="middle" dominantBaseline="central" fontFamily={FONT} fontWeight={600} fontSize="15" fill="#654000">{deg}</text>}
              </g>
            );
          })}
          <path d={`M ${proPivot.x + R_PRO} ${proPivot.y} H ${proPivot.x} L ${hx} ${hy}`} fill="none" stroke="#8F5700" strokeWidth="4.5" strokeLinecap="round" strokeLinejoin="round" />
          <circle cx={proPivot.x} cy={proPivot.y} r="8" fill="#8F5700" />
          <circle cx={hx} cy={hy} r="8" fill="#8F5700" stroke="#FFF8E6" strokeWidth="3" />
          {angle > 2 && <g>
            <rect x={lx - 38} y={ly - 20} width="76" height="40" rx="20" fill="#8F5700" />
            <text x={lx} y={ly} textAnchor="middle" dominantBaseline="central" fontFamily={FONT} fontWeight={650} fontSize="23" fill="#FFFFFF">{shownAngle}°</text>
          </g>}
        </g>}
        <Ruler x={GRID.left} y={ruler.y} width={ruler.width} px={ruler.px} ticks={ramp(t, B.waveTicks[0], B.waveTicks[1] - B.waveTicks[0], (x) => x)} body={ramp(t, B.rulerBody[0], B.rulerBody[1] - B.rulerBody[0], standard)} mm={t >= m0 ? mm : 0} accent={w.accent} />
        {lift > 0 && <path d={morphPath("soft_burst", "soft_burst", 1, chip.x, chip.y, MATCH.size * lift * (1 + 13 * grow), morphTurn(t, 0, 0))} fill={mixColor(w.shape, unit.field, grow)} />}
      </svg>
      {t >= m0 && (
        // The reading: a chip over the marker, then the hero of the change (sized so it lands exactly on the card's text).
        <div style={{ position: "absolute", left: chip.x, top: chip.y, transform: `translate(-50%, -50%) scale(${1 + 1.05 * lift})`, zIndex: 6, display: "flex", flexDirection: "column", alignItems: "center", fontFamily: FONT, color: lift > 0.5 ? mixColor(w.ink, unit.ink, grow) : "#FFFFFF", background: `rgba(143,87,0,${1 - lift})`, borderRadius: 30, padding: `${10 * (1 - lift)}px ${22 * (1 - lift)}px` }}>
          <div style={{ fontSize: 37.02, fontWeight: 650, letterSpacing: -0.976, lineHeight: 1 }}>{(Math.round(mm * 10) / 10).toFixed(1)}</div>
          <div style={{ fontSize: 20.93, fontWeight: 560, marginTop: 2.93 }}>mm</div>
        </div>
      )}
    </AbsoluteFill>
  );
};

// ================================================================================================ 10.95 to 13.5: Unit Converter
// The reading's shape has become the converter's ground: under 76.0 mm the first card grows, then 7.6 cm beside
// it. One more conversion follows, 20 °C to 68 °F, and each result holds for a full second. Then the two cards
// slide together and fold into one surface, which the receipt grows out of.
const CATEGORIES: { name: string; from: string; to: string; input: string; a: UnitFace; b: UnitFace; shapes: [string, string] }[] = [
  { name: "Length", from: "Millimetre", to: "Centimetre", input: "76.0", a: { value: "76.0", label: "mm" }, b: { value: "7.6", label: "cm" }, shapes: ["soft_burst", "cookie_6sided"] },
  { name: "Temperature", from: "Celsius", to: "Fahrenheit", input: "20", a: { value: "20", label: "°C" }, b: { value: "68", label: "°F" }, shapes: ["pentagon", "clover_4leaf"] },
];
const PAIR = { cx: MATCH.x + 230, cy: MATCH.y, gap: 460, size: MATCH.size };
const RECEIPT = { cx: PAIR.cx, cy: 540, scale: 1.3 };
const UNIT_POSE: PhonePose = { x: 1580, y: 540, scale: 0.8, yaw: -10, roll: 0 };
const Unit: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.unit, at = B.unitCategories, i = Math.max(0, at.filter((c) => t >= c + 0.2).length - 1), c = CATEGORIES[i];
  const [f0, f1] = B.unitFold, fold = clamp((t - f0) / (f1 - f0)), folding = t >= f0, grown = pop(t, SCENE.unit.start, 13, 150);
  const decor = useMemo(() => place([[1260, 150, 96], [1840, 120, 50], [1800, 980, 60], [96, 990, 54], [1000, 990, 46]], [w.tones[0], w.tones[1], w.tones[2], w.container], 2), []);
  const rest = 1 - clamp((fold - 0.05) / 0.3);
  // The fold: both shapes slide to the middle and round into one surface, which stretches and whitens into the receipt.
  const turns = at.length - 1, slide = standard(clamp(fold / 0.55)), merge = standard(clamp((fold - 0.45) / 0.55));
  const rw = 292 * RECEIPT.scale, rh = 372 * RECEIPT.scale;
  return (
    <AbsoluteFill>
      <Ground color={w.field} opacity={rest} />
      <div style={{ position: "absolute", inset: 0, opacity: rest }}>
        <Decor t={t} start={SCENE.unit.start - 0.3} pieces={decor} />
        <Phone screen={["numbers", { activeSheet: "unit_converter", converterCategory: c.name, converterFrom: c.from, converterTo: c.to, converterInput: c.input, converterEditingLeft: true }]} pose={UNIT_POSE} dx={(1 - ramp(t, SCENE.unit.start + 0.15, 0.75, enter)) * 900} />
      </div>
      {!folding && <MorphPair t={t} cx={PAIR.cx} cy={PAIR.cy} gap={PAIR.gap} size={PAIR.size} changes={at} shapes={CATEGORIES.map((k) => k.shapes)} colors={[w.tones[0], w.tones[1]]} ink={w.ink} arrows={w.accent} appear={grown} faceIn={[1, clamp(grown * 2)]} faces={CATEGORIES.map((k) => [k.a, k.b])} />}
      {folding && (
        <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 6 }}>
          {/* The surface grows under the two shapes: the first is its own colour, the second fades off it. */}
          {merge > 0 && <g transform={`translate(${PAIR.cx} ${mix(PAIR.cy, RECEIPT.cy, merge)}) scale(${mix(PAIR.size * 0.86, rw, merge) / 100} ${mix(PAIR.size * 0.86, rh, merge) / 100})`}>
            <path d={morphPath("squircle", "squircle", 1, 0, 0, 100)} fill={mixColor(w.tones[0], "#FFFBF5", merge)} />
          </g>}
          {merge < 1 && ([0, 1] as const).map((side) => {
            const x = mix(PAIR.cx + (side ? 1 : -1) * PAIR.gap / 2, PAIR.cx, slide);
            return <path key={side} d={morphPath(CATEGORIES[CATEGORIES.length - 1].shapes[side], "squircle", slide, x, PAIR.cy, PAIR.size * mix(1, 0.86, slide), morphTurn(t, side, turns) * (1 - slide))} fill={mixColor([w.tones[0], w.tones[1]][side], "#FFFBF5", merge)} opacity={side ? 1 - merge : 1} />;
          })}
        </svg>
      )}
    </AbsoluteFill>
  );
};

// ================================================================================================ 13.15 to 17: Tip & Bill Splitter
// The receipt leads: it prints the bill and the tip, the share lands, and the total divides into two people's
// cards. The phone rises in beside it as the proof. Then the phone zooms in and glides to the centre, turning
// into the Decision Wheel phone.
const BILL_POSE: PhonePose = { x: 1575, y: 540, scale: 0.8, yaw: -10, roll: 2 };
const BILL_ZOOM: [number, number] = [16.25, 17.0];
const WHEEL_ENTRY_POSE = wheelState(SCENE.wheel.start).phone;
const billToWheelPose = (t: number, target3d = false): { pose: PhonePose; grow: number; p: number } => {
  const [z0, z1] = BILL_ZOOM, p = smooth(clamp((t - z0) / (z1 - z0)));
  return {
    p,
    grow: 1 + 0.14 * Math.sin(p * Math.PI),
    pose: {
      x: mix(BILL_POSE.x, WHEEL_ENTRY_POSE.x, p),
      y: mix(BILL_POSE.y, WHEEL_ENTRY_POSE.y, p),
      scale: mix(BILL_POSE.scale, WHEEL_ENTRY_POSE.scale, p),
      yaw: target3d ? WHEEL_ENTRY_POSE.yaw : (BILL_POSE.yaw ?? 0),
      pitch: target3d ? WHEEL_ENTRY_POSE.pitch : (BILL_POSE.pitch ?? 0),
      roll: mix(BILL_POSE.roll ?? 0, WHEEL_ENTRY_POSE.roll, p),
    },
  };
};
const Bill: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.bill, rise = 1 - ramp(t, B.billPhone, 0.75, enter);
  const { pose, grow, p: zP } = billToWheelPose(t), leftFade = 1 - smooth(clamp((t - BILL_ZOOM[0]) / 0.5));
  const decor = useMemo(() => place([[1300, 170, 84], [1830, 130, 92], [1850, 760, 58], [90, 960, 54], [140, 380, 46]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <div style={{ position: "absolute", inset: 0 }}>
        <div style={{ position: "absolute", inset: 0, opacity: 1 - zP * 0.85, transform: `translate(${(pose.x - BILL_POSE.x) * 0.6}px, 0) scale(${1 + 0.25 * zP})`, transformOrigin: `${BILL_POSE.x}px ${BILL_POSE.y}px` }}>
          <Behind t={t} name="soft_burst" x={1600} y={520} size={640} color={w.shape} seed={4} start={B.billPhone - 0.1} />
          <Decor t={t} start={SCENE.bill.start + 0.2} pieces={decor} />
        </div>
        <Phone screen={SCREENS.bill} pose={pose} dy={rise * 1000} grow={grow} z={3} />
        <div style={{ position: "absolute", inset: 0, opacity: leftFade, transform: `translate(${-240 * zP}px, 0) scale(${1 - 0.12 * zP})`, transformOrigin: `${RECEIPT.cx}px ${RECEIPT.cy}px`, zIndex: 4 }}>
          {[0, 1].map((k) => {
            const p = pop(t, B.billShares[k], 12, 170), side = k ? 1 : -1;
            return p > 0 && <div key={k} style={{ position: "absolute", inset: 0, opacity: clamp(p * 2), transform: `translate(${(1 - p) * -side * 200}px, ${(1 - p) * -220}px)` }}><ShareCard x={RECEIPT.cx - 190 + side * 205} y={830} who={String(k + 1)} amount="€28.60" ink={w.ink} container={w.container} avatar={w.tones[k]} shape={k ? "cookie_6sided" : "sunny"} /></div>;
          })}
          <Receipt x={RECEIPT.cx - 146} y={RECEIPT.cy - 186} angle={-2 * ramp(t, SCENE.bill.start + 0.4, 0.6)} scale={RECEIPT.scale} t={t} lines={B.billLines} ink={w.ink} accent={w.accent} />
        </div>
      </div>
    </AbsoluteFill>
  );
};

// ================================================================================================ 17 to 23.5: Decision Wheel
// One wheel and one camera move (src/wheelMotion.ts). The zoomed phone arrives with the real Decision Wheel
// sheet, turned a little; the spin starts inside the app; the camera pushes towards the wheel while the phone
// turns face on, and passes through it, so the phone leaves by framing alone and its sheet is never emptied. The
// lilac grows from behind the wheel, which settles to fill the scene and slows to Music; the result holds for a
// second, then the confetti.
const HERO = { x: FOCUS.hero[0], y: FOCUS.hero[1], box: HERO_BOX };
const HERO_POINTER_TOP = HERO.y - HERO.box / 2 - (2 * HERO.box) / APP_WHEEL.box;
const CHIP = { x: 1665, y: 320 };
const WHEEL_CONFETTI = { seed: 41, count: 34, from: [HERO.x, HERO_POINTER_TOP - 20] as [number, number] };
const wheelPieces = () => burstPieces({ ...WHEEL_CONFETTI, colors: WORLDS.wheel.tones.concat(["#D9C67D", "#9ABDA9"]), avoid: [{ x: HERO.x, y: HERO.y, r: HERO.box / 2 + 34 }], boxes: [{ left: HERO.x - 40, top: HERO_POINTER_TOP - 40, right: HERO.x + 40, bottom: HERO_POINTER_TOP + 40 }, { left: CHIP.x - 170, top: CHIP.y - 60, right: CHIP.x + 170, bottom: CHIP.y + 60 }] });
const TOOL_SPECS: { name: string; shape: string; bg: string; fg: string; icon: string }[] = [
  { name: "Water Eject", shape: "cookie_9sided", bg: "#B8CDE3", fg: "#0B4FA8", icon: "water_drop" },
  { name: "Surface Ruler", shape: "cookie_12sided", bg: "#FDE7A2", fg: "#6B5E00", icon: "square_foot" },
  { name: "Protractor", shape: "cookie_9sided", bg: "#FDE7A2", fg: "#6B5E00", icon: "architecture" },
  { name: "Unit Converter", shape: "cookie_9sided", bg: "#B6E5DF", fg: "#153F3C", icon: "straighten" },
  { name: "Tip & Bill Splitter", shape: "soft_burst", bg: "#F7D8C9", fg: "#91005A", icon: "calculate" },
  { name: "Decision Wheel", shape: "cookie_12sided", bg: "#E7D9EF", fg: "#684379", icon: "rotate_right" },
  { name: "Compass", shape: "sunny", bg: "#B6E5DF", fg: "#006A60", icon: "explore" },
  { name: "Bubble Level", shape: "pill", bg: "#B6E5DF", fg: "#006A60", icon: "straighten" },
  { name: "Flash Dimmer", shape: "burst", bg: "#FDE7A2", fg: "#6B5E00", icon: "flashlight_on" },
  { name: "Morse & SOS", shape: "cookie_7sided", bg: "#FFDAD6", fg: "#B10000", icon: "emergency" },
  { name: "Coin Flip", shape: "cookie_12sided", bg: "#FDE7A2", fg: "#6B5E00", icon: "monetization_on" },
  { name: "Dice Roll", shape: "cookie_12sided", bg: "#E7D9EF", fg: "#684379", icon: "casino" },
];
/**
 * Check renders (scripts/verify-wheel.mjs): "phone" hides the replica and keeps the app's own wheel turning, so the
 * two can be measured against each other on the same frame; any other value is the film.
 */
const CHECK = (getInputProps() as { check?: string }).check;
const WheelScene: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.wheel, m = wheelState(Math.max(t, SCENE.wheel.start)), [, r1] = B.wheelReveal, pieces = useMemo(wheelPieces, []);
  const entry = t < SCENE.wheel.start ? billToWheelPose(t, true) : { pose: m.phone, grow: 1, p: 1 };
  const blendIn = t < SCENE.wheel.start ? smooth(clamp((t - 16.45) / 0.55)) : 1;
  const decor = useMemo(() => place([[250, 250, 92], [1720, 170, 64], [1780, 920, 56], [290, 880, 60], [560, 560, 44]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  const heroDecor = useMemo(() => place([[230, 220, 92], [1760, 150, 60], [1760, 900, 70], [270, 880, 62], [300, 560, 44], [1610, 1000, 46]], [w.tones[0], w.tones[1], w.tones[2], w.container], 1), []);
  const lift = pop(t, B.wheelPick, 10, 200), chip = pop(t, B.wheelPick + 0.1, 11, 210), check = CHECK === "phone";
  const { zoom, from, to } = m.camera, { cx, cy, box, rotation } = m.replica;
  // The lilac grows from behind the wheel once the phone has left the frame: a ring of the container colour, then the ground.
  const [lead, trail] = m.revealSizes;
  return (
    <AbsoluteFill style={{ opacity: blendIn }}>
      <Ground color={w.field} />
      {/* The decoration rides the same camera as the phone, so it streams out past the frame's edges. */}
      {t < r1 && <div style={{ position: "absolute", inset: 0, transformOrigin: "0 0", transform: `translate(${to[0] - zoom * from[0]}px, ${to[1] - zoom * from[1]}px) scale(${zoom})` }}><Decor t={t} start={16.5} pieces={decor} /></div>}
      {(t < r1 || check) && <Phone screen={["play", { activeToolId: "decision_wheel", wheelText: CHOICES.join("\n"), wheelRotation: check ? rotation : m.inApp, wheelWinner: "" }]} pose={entry.pose} grow={entry.grow} z={3} shadow={m.shadow} />}
      {m.reveal > 0 && !check && (
        <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 4 }}>
          <path d={morphPath("cookie_12sided", "cookie_12sided", 1, cx, cy, lead, spin(t))} fill={w.container} />
          <path d={morphPath("cookie_12sided", "cookie_12sided", 1, cx, cy, trail, spin(t))} fill={w.field} />
        </svg>
      )}
      {t >= r1 - 0.2 && !check && (
        <div style={{ position: "absolute", inset: 0, zIndex: 4 }}>
          <Behind t={t} name="flower" x={HERO.x} y={HERO.y} size={HERO.box + 150} color={w.container} seed={6} start={r1 - 0.15} />
          <Decor t={t} start={r1 - 0.2} pieces={heroDecor} />
        </div>
      )}
      {m.handed && !check && (
        <div style={{ position: "absolute", inset: 0, zIndex: 5 }}>
          <AppWheel cx={cx} cy={cy} box={box} rotation={rotation} items={CHOICES} winner={t >= B.wheelPick ? WINNER : -1} lift={lift} shadow={m.reveal} />
        </div>
      )}
      <div style={{ position: "absolute", left: CHIP.x, top: CHIP.y, transform: `translate(-50%, -50%) scale(${chip})`, opacity: clamp(chip * 2), zIndex: 6, boxShadow: ELEVATION[2], borderRadius: 32 }}>
        <Chip label="Music" selected color="#FFFFFF" container={w.accent} outline={w.accent} size={52} />
      </div>
      {/* The first pieces leave with the montage, which turns them into the app's tools. */}
      <ConfettiBurst t={t} at={B.wheelBurst} from={WHEEL_CONFETTI.from} pieces={pieces} skip={t >= SCENE.montage.start ? TOOL_SPECS.length : 0} />
    </AbsoluteFill>
  );
};

// ================================================================================================ 22.75 to 29.1: the toolkit
// The payoff: twelve pieces of the wheel's confetti fly to a grid and become the app's tools, each a
// MaterialShape badge with its icon and name, while the lilac gives way to the neutral ground. The named grid
// holds long enough to read; then the badges rise into a band and the five tools' real screens track in from
// the right on the beat, and everything gathers into the icon on the final downbeat.
const CARDS: { screen: Screen; scroll?: number; world: World }[] = [
  { screen: SCREENS.water, scroll: WATER_SCROLL, world: WORLDS.water }, { screen: SCREENS.protractor, world: WORLDS.ruler },
  { screen: ["numbers", { activeSheet: "unit_converter", converterCategory: "Length", converterFrom: "Millimetre", converterTo: "Centimetre", converterInput: "76.0" }], world: WORLDS.unit },
  { screen: SCREENS.bill, world: WORLDS.bill }, { screen: SCREENS.result, world: WORLDS.wheel },
];
const ICON = { x: 960, y: 380 };
const ScreenCard: React.FC<{ screen: Screen; scroll?: number; world: World; x: number; y: number; scale: number; tilt: number; opacity: number }> = ({ screen, scroll, world, x, y, scale, tilt, opacity }) => {
  const k = 0.58, w = 430 * k, h = 957 * k;
  return (
    <div style={{ position: "absolute", left: x - w / 2, top: y - h / 2, width: w, height: h, transform: `rotate(${tilt}deg) scale(${scale})`, opacity, zIndex: 4 }}>
      <div style={{ position: "absolute", inset: -16, borderRadius: 52, background: world.container, transform: `rotate(${-tilt * 1.6}deg)` }} />
      <div style={{ position: "absolute", inset: 0, borderRadius: 40, overflow: "hidden", boxShadow: ELEVATION[3], background: "#FFFFFF" }}>
        <div style={{ width: 430, height: 957, transform: `scale(${k})`, transformOrigin: "0 0" }}>
          <LitScreen id={screen[0]} state={screen[1]} frame={false} scroll={scroll} style={{ width: 430, height: 957 }} />
        </div>
      </div>
    </div>
  );
};
const Montage: React.FC<{ t: number }> = ({ t }) => {
  const start = SCENE.montage.start, pieces = useMemo(wheelPieces, []), [g0, g1] = B.converge;
  const ground = clamp((t - start - 0.1) / 0.6), [b0] = B.montageBadges, [band0, band1] = B.montageBand;
  const band = standard(clamp((t - band0) / (band1 - band0))), gone = (delay: number) => leave(clamp((t - g0 - delay) / (g1 - g0 - delay)));
  const slot = (i: number) => ({ x: 960 + ((i % 6) - 2.5) * 250, y: i < 6 ? 360 : 650 }), bandSlot = (i: number) => ({ x: 960 + (i - 5.5) * 128, y: 150 });
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} opacity={ground} />
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
        {TOOL_SPECS.map((spec, i) => {
          const q = pieces[i], c = confettoAt(q, i, t, B.wheelBurst, WHEEL_CONFETTI.from), fly = standard(clamp((t - b0 - i * 0.05) / 0.6));
          const s = slot(i), bs = bandSlot(i), g = gone(0.02 * (TOOL_SPECS.length - 1 - i));
          const x = mix(mix(c.x, s.x, fly), bs.x, band), y = mix(mix(c.y, s.y, fly), bs.y, band), size = mix(Math.max(q.w, q.h) * c.k, 120, fly) * mix(1, 0.55, band);
          const fx = mix(x, ICON.x, g), fy = mix(y, ICON.y, g);
          return <path key={i} d={morphPath("pill", spec.shape, fly, fx, fy, size * (1 - 0.85 * g), c.rot * (1 - fly))} fill={mixColor(q.color, spec.bg, fly)} opacity={1 - clamp((g - 0.85) / 0.15)} />;
        })}
      </svg>
      <div style={{ position: "absolute", inset: 0, zIndex: 5, pointerEvents: "none" }}>
        {TOOL_SPECS.map((spec, i) => {
          const q = pieces[i], c = confettoAt(q, i, t, B.wheelBurst, WHEEL_CONFETTI.from), fly = standard(clamp((t - b0 - i * 0.05) / 0.6));
          const s = slot(i), bs = bandSlot(i), g = gone(0.02 * (TOOL_SPECS.length - 1 - i));
          const x = mix(mix(c.x, s.x, fly), bs.x, band), y = mix(mix(c.y, s.y, fly), bs.y, band);
          const fx = mix(x, ICON.x, g), fy = mix(y, ICON.y, g), scale = mix(1, 0.55, band) * (1 - 0.85 * g);
          const iconAlpha = clamp((fly - 0.45) / 0.45) * (1 - clamp((g - 0.85) / 0.15));
          return iconAlpha > 0 && (
            <span key={spec.name} className="material-symbols-outlined" style={{ position: "absolute", left: fx, top: fy, transform: `translate(-50%, -50%) scale(${scale})`, fontSize: 52, color: spec.fg, opacity: iconAlpha }}>
              {spec.icon}
            </span>
          );
        })}
      </div>
      {/* The tools' names, under their badges while they are a grid. */}
      <div style={{ position: "absolute", inset: 0, zIndex: 5, opacity: clamp((t - b0 - 0.55) / 0.3) * (1 - clamp(band * 3)) }}>
        {TOOL_SPECS.map((spec, i) => { const s = slot(i); return <div key={spec.name} style={{ position: "absolute", left: s.x - 120, top: s.y + 76, width: 240, textAlign: "center", fontFamily: FONT, fontSize: 29, fontWeight: 560, color: NEUTRAL.ink }}>{spec.name}</div>; })}
      </div>
      {CARDS.map((card, i) => {
        const land = B.montageCards[i], p = enter(clamp((t - land + 0.35) / 0.55)), track = -46 * ramp(t, B.montageCards[0], g0 - B.montageCards[0], (x) => x);
        const g = gone(0.03 * i), x = 960 + (i - 2) * 300 + (1 - p) * 1300 + track, y = 590 + Math.sin(clamp((t - land) / 0.4) * Math.PI) * -16;
        return p > 0 && <ScreenCard key={i} {...card} x={mix(x, ICON.x, g)} y={mix(y, ICON.y, g)} scale={1 - 0.88 * g} tilt={(i - 2) * 2 * (1 - g)} opacity={1 - clamp((g - 0.85) / 0.15)} />;
      })}
    </AbsoluteFill>
  );
};

// ================================================================================================ 28.95 to 32: the icon
// The tools land in the icon on the final downbeat: it pops with a ripple in each world's colour and a little
// confetti; the name rises and the official badge comes in whole, then everything holds.
const End: React.FC<{ t: number }> = ({ t }) => {
  const start = B.endBurst, iconP = pop(t, start, 10, 190), badgeP = pop(t, B.endPlay, 13, 170), legal = ramp(t, B.endPlay + 0.4, 0.6);
  const pieces = useMemo(() => burstPieces({ seed: 7, count: 36, from: [ICON.x, ICON.y], colors: CONFETTI, avoid: [{ x: ICON.x, y: ICON.y, r: 190 }], boxes: [{ left: 480, top: 500, right: 1440, bottom: 1010 }] }), []);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} opacity={clamp((t - start - 0.12) / 0.1)} />
      <Ripples t={t} onsets={B.endRings} at={[ICON.x, ICON.y]} colors={[WORLDS.water.container, WORLDS.ruler.container, WORLDS.unit.container, WORLDS.bill.container, WORLDS.wheel.container]} from={160} reach={720} />
      <ConfettiBurst t={t} at={start} from={[ICON.x, ICON.y]} pieces={pieces} z={3} />
      <Img src={icon} style={{ position: "absolute", left: ICON.x - 150, top: ICON.y - 150, width: 300, height: 300, transform: `scale(${iconP})`, zIndex: 5 }} />
      <Title t={t} at={[B.endTitle]} text="App Toolkit" x={960} y={560} width={1300} align="center" color={NEUTRAL.ink} size={150} />
      <Img src={badge} style={{ position: "absolute", left: 960 - 210, top: 765, width: 420, transform: `scale(${0.85 + 0.15 * badgeP})`, opacity: clamp(badgeP * 1.6), zIndex: 6 }} />
      <div style={{ position: "absolute", left: 0, right: 0, top: 990, textAlign: "center", fontFamily: FONT, fontSize: 22, color: NEUTRAL.accent, opacity: legal * 0.8, zIndex: 6 }}>Google Play and the Google Play logo are trademarks of Google LLC.</div>
    </AbsoluteFill>
  );
};

// ================================================================================================ the edit
/**
 * The edit, scene by scene, each with its own way in: the water shape growing out of the intro, a wave
 * flattening into the ruler, a match cut on the reading, the cards folding into the receipt (with a soft burst
 * opening the coral world), the phone zooming into the Decision Wheel, the confetti becoming the tools, and the
 * tools converging into the icon.
 */
export const Trailer: React.FC = () => {
  useFonts();
  const t = useCurrentFrame() / FPS;
  const [r0, r1] = B.waveReveal, reveal = 900 * enter(clamp((t - r0) / (r1 - r0)));
  // The blue grows out of the travelling shape over half a second, easing in and out, so it is watched growing and reads as a morph, not a cut.
  const [m0, m1] = B.waterMorph, flood = 1750 * smooth(clamp((t - m0 - 0.1) / (m1 - m0 - 0.1))), wm = waterMorph(t);
  const rulerEnd = SCENE.unit.start;
  const shots: [boolean, React.ReactNode][] = [
    [t < m1 + 0.05, <Intro t={t} />],
    // The blue floods in from the travelling shape; the shape itself is drawn over it until it is the backdrop.
    [t >= m0 + 0.15 && t < SCENE.water.end, <div style={{ position: "absolute", inset: 0, clipPath: t < m1 ? `circle(${flood}px at ${wm.x}px ${wm.y}px)` : undefined }}><Water t={t} part="back" /></div>],
    [t >= m0 && t < m1, <WaterMorph t={t} />],
    [t >= SCENE.water.start && t < SCENE.water.end, <Water t={t} part="front" />],
    // The yellow world opens outward from the line the wave became.
    [t >= r0 && t < rulerEnd, <div style={{ position: "absolute", inset: 0, clipPath: reveal < 900 ? `inset(${Math.max(0, RULER_Y - reveal)}px 0 ${Math.max(0, 1080 - RULER_Y - reveal)}px 0)` : undefined }}><RulerScene t={t} part="back" /></div>],
    [t >= B.shapeMorph[0] && t < rulerEnd, <RulerScene t={t} part="shape" />],
    [t >= B.waveLine[0] && t < B.rulerBody[1] + 0.05, <WaveLine t={t} />],
    [t >= B.phoneTurn[0] && t < rulerEnd, <RulerScene t={t} part="phones" />],
    [t >= r0 && t < rulerEnd, <RulerScene t={t} part="front" />],
    [t >= SCENE.unit.start && t < SCENE.bill.start, <Unit t={t} />],
    // The coral world opens out of the folded surface, a soft burst the shape of the bill's world.
    [t >= SCENE.bill.start && t < SCENE.bill.end, t < B.unitFold[1] ? <ShapeWipe t={t} start={SCENE.bill.start} dur={B.unitFold[1] - SCENE.bill.start + 0.15} at={[RECEIPT.cx, RECEIPT.cy]} shape="soft_burst"><Bill t={t} /></ShapeWipe> : <Bill t={t} />],
    // The fold's surface stays over the opening world until the receipt under it takes its place.
    [t >= SCENE.bill.start && t < B.unitFold[1], <Unit t={t} />],
    [t >= 16.45 && t < SCENE.wheel.end, <WheelScene t={t} />],
    [t >= SCENE.montage.start && t < SCENE.montage.end, <Montage t={t} />],
    [t >= SCENE.end.start, <End t={t} />],
  ];
  return (
    <AbsoluteFill style={{ background: NEUTRAL.field, fontFamily: FONT, overflow: "hidden" }}>
      {shots.map(([on, node], i) => on && <div key={i} style={{ position: "absolute", inset: 0, zIndex: i }}>{node}</div>)}
      <Audio src={staticFile("soundtrack.mp3")} />
    </AbsoluteFill>
  );
};
