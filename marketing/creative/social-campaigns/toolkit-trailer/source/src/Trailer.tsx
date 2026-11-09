import React, { useMemo } from "react";
import { AbsoluteFill, Audio, Img, staticFile, useCurrentFrame } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import timeline from "./timeline.json";
import icon from "./assets/icon.png";
// Google's official badge, unaltered (marketing/assets/badges/google-play): moved and scaled whole, never redrawn.
import badge from "../../../../../assets/badges/google-play/en_badge_web_generic.png";
import { B, FPS, clamp, drift, enter, leave, mix, pop, ramp, standard } from "./lib/anim";
import { CONFETTI, FONT, NEUTRAL, WORLDS } from "./theme";
import type { World } from "./theme";
import { Chip, ConfettiBurst, Decor, ELEVATION, Headline, MorphPair, Phone, Receipt, Ripples, Ruler, ShapeWipe, ShareCard, SpeakerWaves, Waveform, Wheel, burstPieces, confettoAt, morphTurn, speakers } from "./motifs";
import type { PhonePose, Screen, UnitFace } from "./motifs";
import { morphPath } from "./motifs/morph";

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
  const p = pop(t, start, 14, 120), s = size * (0.7 + 0.3 * p);
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
      <div style={{ position: "absolute", inset: 0, opacity: 1 - out(0.02), transform: `scale(${1 - 0.08 * out(0.02)})`, transformOrigin: "960px 540px", zIndex: 4 }}>
        <Img src={icon} style={{ position: "absolute", left: 960 - 150, top: 410 - 150 - (1 - iconP) * 30, width: 300, height: 300, transform: `scale(${iconP})` }} />
        <Headline t={t} at={[B.title, B.tagline]} title="App Toolkit" sub="Everyday tools in one app" x={960} y={590} width={1200} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={150} subScale={0.42} />
      </div>
    </AbsoluteFill>
  );
};
/** The blue shape's journey: from its place in the intro to the phone's, growing and morphing into the water backdrop. */
const waterMorph = (t: number) => {
  const [m0, m1] = B.waterMorph, p = standard(clamp((t - m0) / (m1 - m0)));
  return { p, x: mix(1590, WATER_POSE.x, p), y: mix(270, WATER_POSE.y, p), size: mix(240, 780, p), turn: mix(introTurn(t, 0), spin(t), p) };
};
const WaterMorph: React.FC<{ t: number }> = ({ t }) => {
  const m = waterMorph(t);
  return <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0 }}><path d={morphPath("cookie_9sided", "cookie_12sided", m.p, m.x, m.y, m.size * 0.98, m.turn)} fill={WORLDS.water.shape} /></svg>;
};

// ================================================================================================ 3 to 6.85: Water Eject
// The phone rises into the blue backdrop, speaker down as the sheet asks; each speaker sends out its own wave on
// every note, with a few bubbles and droplets, and the tone itself, 165 Hz, pulses beside it. No title.
const WATER_POSE: PhonePose = { x: 1100, y: 440, scale: 0.7, pitch: 12, yaw: -10, roll: 0 };
const Water: React.FC<{ t: number; part: "back" | "front" }> = ({ t, part }) => {
  const w = WORLDS.water, rise = pop(t, B.waterPhone, 13, 140), dy = (1 - rise) * 640;
  const decor = useMemo(() => place([[620, 190, 76], [1520, 150, 92], [1830, 860, 58], [110, 930, 56], [1800, 470, 44]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  if (part === "back") return (
    <AbsoluteFill>
      <Ground color={w.field} />
      {t >= B.waterMorph[1] && t < B.shapeMorph[0] && <Shape name="cookie_12sided" x={WATER_POSE.x - 390} y={WATER_POSE.y - 390} size={780} color={w.shape} rotate={spin(t)} />}
      <Decor t={t} start={B.waterMorph[1] - 0.1} pieces={decor} />
      <Waveform t={t} start={B.waterHz} onsets={B.waterWaves} x={180} y={640} width={560} label="165 Hz" line={w.accent} glow={w.bright} ink={w.accent} />
    </AbsoluteFill>
  );
  const sources = speakers(WATER_POSE).map((s) => ({ at: [s.at[0], s.at[1] + dy] as [number, number], direction: s.direction }));
  return (
    <AbsoluteFill>
      {t < B.phoneTurn[0] && <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={WATER_POSE} dy={dy} />}
      <SpeakerWaves t={t} onsets={B.waterWaves} drops={B.waterDrops} sources={sources} line={w.accent} glow={w.bright} tint={w.tones[0]} />
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
  const cx = WATER_POSE.x, yb = mix(905, RULER_Y, p), xl = mix(cx - 225, -80, p), xr = mix(cx + 225, 2000, p), sag = mix(130, 0, p);
  return (
    <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <path d={`M ${xl} ${yb} Q ${cx} ${yb + 2 * sag} ${xr} ${yb}`} fill="none" stroke={mixColor(WORLDS.water.accent, "#654000", p)} strokeWidth={mix(6, 3, p)} strokeLinecap="round" opacity={fade} />
    </svg>
  );
};

// ================================================================================================ 6.15 to 10.95: Ruler & Protractor
// The app leads: the phone that turned in is the Surface Ruler, the Protractor joins it, and the ruler the wave
// became pulls back to a strip at the lower left. An arm rises from the ruler's zero to 45 degrees while the
// Protractor sweeps with it and holds, then a marker measures 76.0 mm while the Surface Ruler reads the same, so
// both phones end on their results. The reading lifts onto a shape the size of the next scene's first card,
// which keeps growing until it is the converter's ground.
const RULER_A: PhonePose = { x: 1130, y: 520, scale: 0.72, yaw: 12, roll: 0 };
const RULER_B: PhonePose = { x: 1530, y: 520, scale: 0.72, yaw: -12, roll: 0 };
const BACKDROP = { x: 1330, y: 520, size: 900 };
const STRIP = { y: 810, width: 760, px: 8 };
const MATCH = { x: 560, y: 520, size: 330 };
const rulerArm = (t: number) => {
  const s = B.protractorSweep, up = s.reduce((acc, at, i) => acc + ramp(t, at, 0.3) * [0.3, 0.25, 0.25, 0.2][i], 0);
  return 45 * up;
};
/** The water phone's turn into the Surface Ruler: up to edge-on with the Water Eject screen, then round from the other edge with the ruler. */
const turnPose = (t: number): { pose: PhonePose; ruler: boolean } => {
  const [a, b] = B.phoneTurn, u = clamp((t - a) / (b - a)), e = standard(u), first = u < 0.5;
  const yaw = first ? mix(WATER_POSE.yaw ?? 0, 90, leave(u / 0.5)) : mix(-90, RULER_A.yaw ?? 0, enter((u - 0.5) / 0.5));
  return { pose: { x: mix(WATER_POSE.x, RULER_A.x, e), y: mix(WATER_POSE.y, RULER_A.y, e), scale: mix(WATER_POSE.scale, RULER_A.scale, e), pitch: Math.round(mix(WATER_POSE.pitch ?? 0, 0, e) * 4) / 4, yaw: Math.round(yaw * 4) / 4, roll: 0 }, ruler: !first };
};
const RulerScene: React.FC<{ t: number; part: "back" | "shape" | "phones" | "front" }> = ({ t, part }) => {
  const w = WORLDS.ruler, [m0, m1] = B.rulerMeasure, measured = ramp(t, m0, m1 - m0, standard), [p0, p1] = B.rulerPull, pull = standard(clamp((t - p0) / (p1 - p0)));
  const marker = Math.round(0.5 * measured * 200) / 200, mm = rulerMm(marker), angle = rulerArm(t), shownAngle = Math.round(angle);
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
    const [s0, s1] = B.shapeMorph, q = standard(clamp((t - s0) / (s1 - s0))), cx = mix(WATER_POSE.x, BACKDROP.x, q), cy = mix(WATER_POSE.y, BACKDROP.y, q);
    const size = mix(780, BACKDROP.size, q), d = morphPath("cookie_12sided", "soft_burst", q, cx, cy, size * 0.98, spin(t)), fill = size * 0.6 * enter(clamp((t - s0 - 0.1) / (s1 - s0 - 0.15)));
    return (
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0 }}>
        <defs><clipPath id="tt-yellow-fill"><circle cx={cx} cy={cy} r={fill} /></clipPath></defs>
        {fill < size * 0.6 && <path d={d} fill={WORLDS.water.shape} />}
        <path d={d} fill={w.shape} clipPath={fill < size * 0.6 ? "url(#tt-yellow-fill)" : undefined} />
      </svg>
    );
  }
  if (part === "phones") {
    const turning = t < B.phoneTurn[1], a = turning ? turnPose(t) : { pose: RULER_A, ruler: true }, inB = 1 - ramp(t, p0 + 0.1, 0.75, enter);
    return (
      <AbsoluteFill>
        {a.ruler ? <Phone screen={["numbers", { activeSheet: "ruler", rulerAdvanced: false, rulerMarker: marker }]} pose={a.pose} z={3} /> : <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={a.pose} z={3} />}
        {t >= p0 && <Phone screen={["numbers", { activeSheet: "protractor", protractorAngle: shownAngle }]} pose={RULER_B} dx={inB * 1150} z={4} />}
      </AbsoluteFill>
    );
  }
  const ruler = { y: mix(RULER_Y, STRIP.y, pull), width: mix(GRID.right - GRID.left, STRIP.width, pull), px: mix(RULER_PX, STRIP.px, pull) };
  const pivot = { x: GRID.left + 28, y: ruler.y }, arm = (a: number, r: number) => [pivot.x + r * Math.cos((-a * Math.PI) / 180), pivot.y + r * Math.sin((-a * Math.PI) / 180)];
  const lift = ramp(t, B.rulerLift[0], B.rulerLift[1] - B.rulerLift[0], standard), markerX = pivot.x + mm * ruler.px, armFade = 1 - clamp(lift * 2);
  // After the lift, the shape under the reading keeps growing until it is the converter's ground.
  const [e0, e1] = B.rulerExpand, grow = leave(clamp((t - e0) / (e1 - e0)));
  const chip = { x: mix(markerX, MATCH.x, lift), y: mix(ruler.y - 110, MATCH.y, lift) }, unit = WORLDS.unit;
  return (
    <AbsoluteFill>
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
        {angle > 0.2 && armFade > 0 && <g opacity={armFade}>
          <path d={`M ${pivot.x} ${pivot.y} L ${arm(0, 230).join(" ")} A 230 230 0 0 0 ${arm(angle, 230).join(" ")} Z`} fill={w.container} opacity=".9" />
          {Array.from({ length: Math.floor(angle / 5) + 1 }, (_, i) => { const [x1, y1] = arm(i * 5, 230), [x2, y2] = arm(i * 5, i % 3 === 0 ? 198 : 212); return <line key={i} x1={x1} y1={y1} x2={x2} y2={y2} stroke="#8F5700" strokeWidth={i % 3 === 0 ? 3 : 2} strokeLinecap="round" />; })}
          <line x1={pivot.x} y1={pivot.y} x2={arm(angle, 480)[0]} y2={arm(angle, 480)[1]} stroke="#654000" strokeWidth="8" strokeLinecap="round" />
          <text x={arm(angle / 2, 290)[0]} y={arm(angle / 2, 290)[1]} fontFamily={FONT} fontWeight={650} fontSize="64" fill="#654000" dominantBaseline="central">{shownAngle}°</text>
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
// it. Four more categories follow on syncopated beats. Then the two cards slide together and fold into one
// surface, which the receipt grows out of.
const CATEGORIES: { name: string; from: string; to: string; input: string; a: UnitFace; b: UnitFace; shapes: [string, string] }[] = [
  { name: "Length", from: "Millimetre", to: "Centimetre", input: "76.0", a: { value: "76.0", label: "mm" }, b: { value: "7.6", label: "cm" }, shapes: ["soft_burst", "cookie_6sided"] },
  { name: "Mass", from: "Gram", to: "Kilogram", input: "500", a: { value: "500", label: "g" }, b: { value: "0.5", label: "kg" }, shapes: ["cookie_9sided", "flower"] },
  { name: "Temperature", from: "Celsius", to: "Fahrenheit", input: "20", a: { value: "20", label: "°C" }, b: { value: "68", label: "°F" }, shapes: ["pentagon", "clover_4leaf"] },
  { name: "Volume", from: "Litre", to: "Millilitre", input: "2", a: { value: "2", label: "L" }, b: { value: "2000", label: "mL" }, shapes: ["pill", "gem"] },
  { name: "Data", from: "Gigabyte", to: "Megabyte", input: "1", a: { value: "1", label: "GB" }, b: { value: "1000", label: "MB" }, shapes: ["sunny", "very_sunny"] },
];
const PAIR = { cx: MATCH.x + 230, cy: MATCH.y, gap: 460, size: MATCH.size };
const RECEIPT = { cx: PAIR.cx, cy: 540, scale: 1.3 };
const UNIT_POSE: PhonePose = { x: 1590, y: 540, scale: 0.62, yaw: -10, roll: 0 };
const Unit: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.unit, at = B.unitCategories, i = Math.max(0, at.filter((c) => t >= c + 0.2).length - 1), shown = Math.max(0, at.filter((c) => t >= c).length - 1), c = CATEGORIES[i];
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
        <div style={{ position: "absolute", left: PAIR.cx - 480, width: 960, top: 760, display: "flex", justifyContent: "center", gap: 10, zIndex: 5 }}>
          {CATEGORIES.map((k, j) => { const p = pop(t, B.unitChips + j * 0.05, 12, 200); return <div key={k.name} style={{ transform: `scale(${p})`, opacity: clamp(p * 2) }}><Chip label={k.name} selected={j === shown} color={w.ink} container={w.container} outline={w.tones[2]} size={28} /></div>; })}
        </div>
      </div>
      {!folding && <MorphPair t={t} cx={PAIR.cx} cy={PAIR.cy} gap={PAIR.gap} size={PAIR.size} changes={at} shapes={CATEGORIES.map((k) => k.shapes)} colors={[w.tones[0], w.tones[1]]} ink={w.ink} arrows={w.accent} appear={grown} faceIn={[1, clamp(grown * 2)]} faces={CATEGORIES.map((k) => [k.a, k.b])} />}
      {folding && (
        <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 6 }}>
          {merge < 1 && ([0, 1] as const).map((side) => {
            const x = mix(PAIR.cx + (side ? 1 : -1) * PAIR.gap / 2, PAIR.cx, slide);
            return <path key={side} d={morphPath(CATEGORIES[4].shapes[side], "squircle", slide, x, PAIR.cy, PAIR.size * mix(1, 0.86, slide), morphTurn(t, side, turns) * (1 - slide))} fill={mixColor([w.tones[0], w.tones[1]][side], "#FFFBF5", merge)} opacity={side ? 1 - merge : 1} />;
          })}
          {merge > 0 && <g transform={`translate(${PAIR.cx} ${mix(PAIR.cy, RECEIPT.cy, merge)}) scale(${mix(PAIR.size * 0.86, rw, merge) / 100} ${mix(PAIR.size * 0.86, rh, merge) / 100})`}>
            <path d={morphPath("squircle", "squircle", 1, 0, 0, 100)} fill={mixColor(w.tones[0], "#FFFBF5", merge)} />
          </g>}
        </svg>
      )}
    </AbsoluteFill>
  );
};

// ================================================================================================ 13.15 to 17: Tip & Bill Splitter
// The receipt leads: it prints the bill and the tip, the share lands, and the total divides into two people's
// cards. The phone rises in beside it as the proof. Then a rest: everything holds while the camera barely moves.
const BILL_POSE: PhonePose = { x: 1600, y: 540, scale: 0.6, yaw: -10, roll: 2 };
const Bill: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.bill, rise = 1 - ramp(t, B.billPhone, 0.75, enter), [r0, r1] = B.billRest, rest = ramp(t, r0, r1 - r0, (x) => x);
  const decor = useMemo(() => place([[1300, 170, 84], [1830, 130, 92], [1850, 760, 58], [90, 960, 54], [140, 380, 46]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <div style={{ position: "absolute", inset: 0, transform: `scale(${1 + 0.025 * rest})`, transformOrigin: `${RECEIPT.cx}px ${RECEIPT.cy}px` }}>
        <Behind t={t} name="soft_burst" x={1600} y={520} size={640} color={w.shape} seed={4} start={B.billPhone - 0.1} />
        <Decor t={t} start={SCENE.bill.start + 0.2} pieces={decor} />
        <Phone screen={SCREENS.bill} pose={BILL_POSE} dy={rise * 700} z={3} />
        {[0, 1].map((k) => {
          const p = pop(t, B.billShares[k], 12, 170), side = k ? 1 : -1;
          return p > 0 && <div key={k} style={{ position: "absolute", inset: 0, zIndex: 3, opacity: clamp(p * 2), transform: `translate(${(1 - p) * -side * 200}px, ${(1 - p) * -220}px)` }}><ShareCard x={RECEIPT.cx - 190 + side * 205} y={830} who={String(k + 1)} amount="€28.60" ink={w.ink} container={w.container} avatar={w.tones[k]} shape={k ? "cookie_6sided" : "sunny"} /></div>;
        })}
        <Receipt x={RECEIPT.cx - 146} y={RECEIPT.cy - 186} angle={-2 * ramp(t, SCENE.bill.start + 0.4, 0.6)} scale={RECEIPT.scale} t={t} lines={B.billLines} ink={w.ink} accent={w.accent} />
      </div>
    </AbsoluteFill>
  );
};

// ================================================================================================ 17 to 21.9: Decision Wheel
// A hard cut on a strong accent into a frame the wheel fills. The choices land, a quiet beat, a small wind-up
// backwards, the spin, Music, and one restrained burst of confetti.
const CHOICES = ["Walk", "Read", "Cook", "Draw", "Music", "Rest"];
const WHEEL = { cx: 860, cy: 560, r: 360 };
const WHEEL_POSE: PhonePose = { x: 1640, y: 560, scale: 0.55, yaw: -10, roll: 0 };
const wheelRotation = (t: number) => {
  const [w0, w1] = B.wheelWindup, [s0, s1] = B.wheelSpin, back = timeline.wheel.windup;
  if (t < w0) return 0;
  if (t < s0) return -back * standard(clamp((t - w0) / (w1 - w0)));
  return -back + (timeline.wheel.turn + back) * (1 - (1 - clamp((t - s0) / (s1 - s0))) ** 3);
};
const WHEEL_CONFETTI = { seed: 41, count: 34, from: [WHEEL.cx, 190] as [number, number] };
const wheelPieces = () => burstPieces({ ...WHEEL_CONFETTI, colors: WORLDS.wheel.tones.concat(["#D9C67D", "#9ABDA9"]), avoid: [{ x: WHEEL.cx, y: WHEEL.cy, r: WHEEL.r + 30 }], boxes: [{ left: 1480, top: 260, right: 1800, bottom: 860 }, { left: 1180, top: 220, right: 1440, bottom: 320 }] });
const TOOLS = ["Water Eject", "Surface Ruler", "Protractor", "Unit Converter", "Tip & Bill Splitter", "Decision Wheel", "Compass", "Bubble Level", "Flash Dimmer", "Morse & SOS", "Counter", "Coin Flip", "Dice Roll", "Random number", "Reaction Test", "Breathe"];
const WheelScene: React.FC<{ t: number }> = ({ t }) => {
  const w = WORLDS.wheel, rot = wheelRotation(t), breathe = t > B.wheelChoices[5] && t < B.wheelWindup[0] ? 0.008 * Math.sin((t - B.wheelChoices[5]) * 6) : 0;
  const pieces = useMemo(wheelPieces, []);
  const decor = useMemo(() => place([[230, 300, 92], [1840, 160, 64], [1860, 960, 56], [300, 900, 60], [1380, 940, 48]], [w.tones[0], w.tones[1], w.tones[2], w.container], 4), []);
  const pick = clamp((t - B.wheelPick) / 0.3), lift = pop(t, B.wheelBurst, 9, 200), chip = pop(t, B.wheelPick, 10, 210);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Decor t={t} start={SCENE.wheel.start} pieces={decor} />
      <div style={{ position: "absolute", inset: 0, transform: `scale(${1 + breathe})`, transformOrigin: `${WHEEL.cx}px ${WHEEL.cy}px`, zIndex: 3 }}>
        <Wheel cx={WHEEL.cx} cy={WHEEL.cy} r={WHEEL.r} rotation={rot} choices={CHOICES} shown={B.wheelChoices.map((c) => pop(t, c, 11, 200))} colors={w.tones} ground={w.field} ink={w.ink} accent={w.accent} winner={t >= B.wheelBurst ? 4 : -1} lift={lift} />
      </div>
      <Phone screen={SCREENS.wheel} pose={WHEEL_POSE} opacity={1 - pick} />
      <Phone screen={SCREENS.result} pose={WHEEL_POSE} opacity={pick} z={3} />
      <div style={{ position: "absolute", left: 1310, top: 270, transform: `translate(-50%, -50%) scale(${chip})`, opacity: clamp(chip * 2), zIndex: 6, boxShadow: ELEVATION[2], borderRadius: 32 }}>
        <Chip label="Music" selected color="#FFFFFF" container={w.accent} outline={w.accent} size={40} />
      </div>
      {/* The first pieces leave with the montage, which turns them into the app's tools. */}
      <ConfettiBurst t={t} at={B.wheelBurst} from={WHEEL_CONFETTI.from} pieces={pieces} skip={t >= SCENE.montage.start ? TOOLS.length : 0} />
    </AbsoluteFill>
  );
};

// ================================================================================================ 21.3 to 27.1: the toolkit
// The payoff: sixteen pieces of the wheel's confetti fly to a grid and become the app's tools, each a
// MaterialShape badge with its name, while the lilac gives way to the neutral ground. The badges rise into a
// band and the five tools' real screens track in from the right on the beat; the line lands; then everything
// gathers into the icon on the final downbeat.
const TOOL_WORLDS: World[] = [WORLDS.water, WORLDS.ruler, WORLDS.ruler, WORLDS.unit, WORLDS.bill, WORLDS.wheel];
const BADGE_SHAPES = ["cookie_9sided", "cookie_12sided", "soft_burst", "flower", "pill", "sunny", "clover_4leaf", "gem", "very_sunny", "cookie_6sided", "clover_8leaf", "pentagon"];
const toolColor = (i: number) => (i < 6 ? TOOL_WORLDS[i].bright : [WORLDS.water.tones[2], WORLDS.ruler.shape, WORLDS.unit.bright, WORLDS.bill.shape, WORLDS.wheel.bright][i % 5]);
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
  const slot = (i: number) => ({ x: 960 + ((i % 8) - 3.5) * 205, y: i < 8 ? 380 : 650 }), bandSlot = (i: number) => ({ x: 960 + (i - 7.5) * 104, y: 150 });
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} opacity={ground} />
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 5, overflow: "visible" }}>
        {TOOLS.map((_, i) => {
          const q = pieces[i], c = confettoAt(q, i, t, B.wheelBurst, WHEEL_CONFETTI.from), fly = standard(clamp((t - b0 - i * 0.035) / 0.6));
          const s = slot(i), bs = bandSlot(i), g = gone(0.02 * (15 - i));
          const x = mix(mix(c.x, s.x, fly), bs.x, band), y = mix(mix(c.y, s.y, fly), bs.y, band), size = mix(Math.max(q.w, q.h) * c.k, 96, fly) * mix(1, 0.62, band);
          const fx = mix(x, ICON.x, g), fy = mix(y, ICON.y, g);
          return <path key={i} d={morphPath("pill", BADGE_SHAPES[i % BADGE_SHAPES.length], fly, fx, fy, size * (1 - 0.85 * g), c.rot * (1 - fly))} fill={mixColor(q.color, toolColor(i), fly)} opacity={1 - clamp((g - 0.85) / 0.15)} />;
        })}
      </svg>
      {/* The tools' names, under their badges while they are a grid. */}
      <div style={{ position: "absolute", inset: 0, zIndex: 5, opacity: clamp((t - b0 - 0.55) / 0.3) * (1 - clamp(band * 3)) }}>
        {TOOLS.map((name, i) => { const s = slot(i); return <div key={name} style={{ position: "absolute", left: s.x - 100, top: s.y + 62, width: 200, textAlign: "center", fontFamily: FONT, fontSize: 25, fontWeight: 560, color: NEUTRAL.ink }}>{name}</div>; })}
      </div>
      {CARDS.map((card, i) => {
        const land = B.montageCards[i], p = enter(clamp((t - land + 0.35) / 0.55)), track = -46 * ramp(t, B.montageCards[0], g0 - B.montageCards[0], (x) => x);
        const g = gone(0.03 * i), x = 960 + (i - 2) * 300 + (1 - p) * 1300 + track, y = 590 + Math.sin(clamp((t - land) / 0.4) * Math.PI) * -16;
        return p > 0 && <ScreenCard key={i} {...card} x={mix(x, ICON.x, g)} y={mix(y, ICON.y, g)} scale={1 - 0.88 * g} tilt={(i - 2) * 2 * (1 - g)} opacity={1 - clamp((g - 0.85) / 0.15)} />;
      })}
    </AbsoluteFill>
  );
};

// ================================================================================================ 26.95 to 30: the icon
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
 * flattening into the ruler, a match cut
 * on the reading, the cards folding into the receipt (with a soft burst opening the coral world), a hard cut
 * after a rest, the confetti becoming the tools, and the tools converging into the icon.
 */
export const Trailer: React.FC = () => {
  useFonts();
  const t = useCurrentFrame() / FPS;
  const [r0, r1] = B.waveReveal, reveal = 900 * enter(clamp((t - r0) / (r1 - r0)));
  // The blue grows out of the travelling shape over half a second, on Material's standard curve, so it reads as a morph and not a cut.
  const [m0, m1] = B.waterMorph, flood = 2300 * standard(clamp((t - m0 - 0.15) / (m1 - m0 - 0.05))), wm = waterMorph(t);
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
    [t >= SCENE.wheel.start && t < SCENE.wheel.end, <WheelScene t={t} />],
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
