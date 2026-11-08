import React, { useMemo } from "react";
import { AbsoluteFill, Audio, Img, staticFile, useCurrentFrame } from "remotion";
import { useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import timeline from "./timeline.json";
import icon from "./assets/icon.png";
import { B, FPS, clamp, drift, enter, leave, mix, pop, ramp } from "./lib/anim";
import { CONFETTI, FONT, NEUTRAL, WORLDS } from "./theme";
import type { World } from "./theme";
import { ConfettiBurst, Headline, Phone, Protractor, Receipt, Ruler, ShapeWipe, SoundWaves, UnitPair, Wheel, burstPieces, speakers } from "./motifs";
import type { PhonePose, Screen } from "./motifs";

// The real screens, in the states the store listings show.
const SCREENS: Record<string, Screen> = {
  water: ["quick-tools", { activeToolId: "water_eject" }],
  protractor: ["numbers", { activeSheet: "protractor", protractorAngle: 45 }],
  length: ["numbers", { converterCategory: "Length", converterFrom: "Metre", converterTo: "Centimetre", converterInput: "1", activeSheet: "unit_converter" }],
  bill: ["numbers", { activeSheet: "bill_splitter", billAmount: "52.00", billCurrency: "EUR", billTipEnabled: true, tipPercent: 10, splitPeople: 2 }],
  wheel: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 0, wheelWinner: "" }],
  result: ["play", { activeToolId: "decision_wheel", wheelText: "Walk\nRead\nCook\nDraw\nMusic\nRest", wheelRotation: 1530, wheelWinner: "Music" }],
};
/** Water Eject opens from its card in Quick Tools: the list is scrolled to it, as on the listing. */
const WATER_SCROLL = 672;

const Ground: React.FC<{ color: string }> = ({ color }) => <AbsoluteFill style={{ background: color }} />;

/** A large tonal shape that breathes slowly behind the phone. */
const Behind: React.FC<{ t: number; name: string; x: number; y: number; size: number; color: string; seed: number; start: number }> = ({ t, name, x, y, size, color, seed, start }) => {
  const p = pop(t, start, 14, 120), s = size * (0.7 + 0.3 * p);
  return <div style={{ position: "absolute", inset: 0, zIndex: 1 }}><Shape name={name} x={x - s / 2 + drift(t, seed) * 10} y={y - s / 2 + drift(t, seed + 1) * 10} size={s} color={color} rotate={(t - start) * 6 + seed * 20} /></div>;
};

/** The phone arrives from below on the scene's first beat and drifts gently while it holds. */
const enterPhone = (t: number, start: number, pose: PhonePose, seed: number) => {
  const p = ramp(t, start + 0.04, 0.75);
  return { dx: drift(t, seed) * 6, dy: (1 - p) * 820 + drift(t, seed + 3) * 6 };
};

// ------------------------------------------------------------------------------------------------- intro
const INTRO_SHAPES: { world: World; name: string; x: number; y: number; size: number }[] = [
  { world: WORLDS.water, name: "cookie_9sided", x: 1540, y: 300, size: 250 },
  { world: WORLDS.ruler, name: "soft_burst", x: 380, y: 790, size: 270 },
  { world: WORLDS.unit, name: "cookie_6sided", x: 1500, y: 810, size: 230 },
  { world: WORLDS.bill, name: "pill", x: 400, y: 270, size: 240 },
  { world: WORLDS.wheel, name: "flower", x: 700, y: 950, size: 220 },
];
const Intro: React.FC<{ t: number }> = ({ t }) => {
  const swing = ramp(t, B.swing, 0.8, enter), iconP = pop(t, B.icon, 10, 190);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {INTRO_SHAPES.map((s, i) => {
        const p = pop(t, B.introShapes[i], 12, 200), a = (swing * 22 * Math.PI) / 180, dx = s.x - 960, dy = s.y - 540, k = 1 - 0.08 * swing;
        const x = 960 + k * (dx * Math.cos(a) - dy * Math.sin(a)), y = 540 + k * (dx * Math.sin(a) + dy * Math.cos(a)), size = s.size * p;
        return <Shape key={i} name={s.name} x={x - size / 2} y={y - size / 2} size={size} color={s.world.shape} rotate={i * 23 + t * 10} />;
      })}
      <Img src={icon} style={{ position: "absolute", left: 960 - 150, top: 410 - 150 - (1 - iconP) * 30, width: 300, height: 300, transform: `scale(${iconP})`, zIndex: 4 }} />
      <Headline t={t} at={[B.title, B.tagline]} title="App Toolkit" sub="Everyday tools in one app" x={960} y={590} width={1200} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={118} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- water
const WATER_POSE: PhonePose = { x: 1330, y: 320, scale: 0.84, pitch: 26, yaw: -12 };
const Water: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.water, m = enterPhone(t, start, WATER_POSE, 1);
  const from = speakers(WATER_POSE).map((s) => ({ at: [s.at[0] + m.dx, s.at[1] + m.dy] as [number, number], direction: s.direction }));
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="cookie_12sided" x={1330} y={360} size={880} color={w.shape} seed={1} start={start} />
      <Phone screen={SCREENS.water} scroll={WATER_SCROLL} pose={WATER_POSE} dx={m.dx} dy={m.dy} />
      <SoundWaves t={t} onsets={B.waterWaves} from={from} color={w.accent} tint={w.tones[0]} reach={330} />
      <Headline t={t} at={B.waterHeadline} title="Water Eject" sub={"A short tone\nfor wet speakers"} x={140} y={330} color={w.ink} subColor={w.accent} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- ruler
const RULER_POSE: PhonePose = { x: 1420, y: 560, scale: 0.8, yaw: -10, roll: 4 };
const RulerScene: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.ruler, m = enterPhone(t, start, RULER_POSE, 2);
  const s = B.protractorSweep, arm = 45 * [ramp(t, s[0], 0.3), ramp(t, s[1], 0.35), ramp(t, s[2], 0.35), ramp(t, s[3], 0.4)].reduce((a, p, i) => a + p * [0.3, 0.25, 0.25, 0.2][i], 0);
  const slide = ramp(t, B.rulerSlide, 0.7);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="soft_burst" x={1420} y={470} size={860} color={w.shape} seed={2} start={start} />
      <Phone screen={SCREENS.protractor} pose={RULER_POSE} dx={m.dx} dy={m.dy} />
      <svg aria-hidden width={1920} height={1080} style={{ position: "absolute", inset: 0, zIndex: 4 }}>
        <Protractor x={560} y={1030} r={380} arm={arm} />
        <Ruler x={mix(-1250, 60, slide)} y={850} width={1120} angle={-6} ticks={ramp(t, B.rulerSlide + 0.2, 0.9)} />
      </svg>
      <Headline t={t} at={B.rulerHeadline} title={"Ruler &\nProtractor"} sub="Measure on your screen" x={140} y={110} color={w.ink} subColor={w.accent} size={96} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- unit
const UNIT_POSE: PhonePose = { x: 1440, y: 560, scale: 0.8, yaw: -8, roll: -3 };
const Unit: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.unit, m = enterPhone(t, start, UNIT_POSE, 3);
  const count = Math.round(100 * ramp(t, B.unitCount[0], B.unitCount[1] - B.unitCount[0], (x) => x));
  const swap = ramp(t, B.unitSwap, 0.6, enter), temp = t >= B.unitTemperature, tp = pop(t, B.unitTemperature, 10, 220);
  const L = { x: 400, y: 660 }, R = { x: 880, y: 660 }, arc = Math.sin(swap * Math.PI) * 170;
  const a = { x: mix(L.x, R.x, swap), y: L.y - arc, label: temp ? "°C" : "m", value: temp ? "20" : "1", color: w.tones[0], shape: "cookie_9sided" };
  const b = { x: mix(R.x, L.x, swap), y: R.y + arc, label: temp ? "°F" : "cm", value: temp ? "68" : String(count), color: w.tones[1], shape: "cookie_6sided" };
  const enterP = pop(t, start + 0.2, 13, 160);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="clover_4leaf" x={1440} y={480} size={820} color={w.tones[2]} seed={3} start={start} />
      <Phone screen={SCREENS.length} pose={UNIT_POSE} dx={m.dx} dy={m.dy} />
      <div style={{ position: "absolute", inset: 0, transform: `scale(${(0.6 + 0.4 * enterP) * (temp ? 0.94 + 0.06 * tp : 1)})`, transformOrigin: "640px 660px", opacity: clamp(enterP * 2), zIndex: 4 }}>
        <UnitPair a={a} b={b} ink={w.ink} arrows={w.accent} turn={swap * 180} size={320} />
      </div>
      <Headline t={t} at={B.unitHeadline} title="Unit Converter" sub="Length, mass, temperature and more" x={140} y={110} color={w.ink} subColor={w.accent} size={96} width={1100} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- bill
const BILL_POSE: PhonePose = { x: 1470, y: 560, scale: 0.8, yaw: -10, roll: 3 };
const Bill: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.bill, m = enterPhone(t, start, BILL_POSE, 4), out = ramp(t, B.billReceipt, 0.7);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="soft_burst" x={1470} y={470} size={840} color={w.shape} seed={4} start={start} />
      <Behind t={t} name="pill" x={240} y={1000} size={420} color={w.tones[1]} seed={5} start={start + 0.2} />
      {t >= B.billReceipt && <Receipt x={mix(1330, 760, out)} y={480} angle={mix(10, -5, out)} t={t} lines={B.billLines} ink={w.ink} accent={w.accent} />}
      <Phone screen={SCREENS.bill} pose={BILL_POSE} dx={m.dx} dy={m.dy} z={5} />
      <Headline t={t} at={B.billHeadline} title={"Tip & Bill\nSplitter"} sub="The tip, the total and each share" x={140} y={110} color={w.ink} subColor={w.accent} size={96} width={1000} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- wheel
const WHEEL_POSE: PhonePose = { x: 1480, y: 560, scale: 0.8, yaw: -12, roll: 2 };
const WheelScene: React.FC<{ t: number; start: number }> = ({ t, start }) => {
  const w = WORLDS.wheel, m = enterPhone(t, start, WHEEL_POSE, 6);
  const [s0, s1] = B.wheelSpin, spin = 1 - (1 - clamp((t - s0) / (s1 - s0))) ** 3, rot = spin * 1530 + 15;
  const pieces = useMemo(() => burstPieces({ seed: 41, count: 70, from: [600, 360], colors: w.tones.concat(["#D9C67D", "#9ABDA9"]), avoid: [{ x: 600, y: 700, r: 330 }, { x: 1480, y: 560, r: 300 }, { x: 420, y: 170, r: 260 }] }), []);
  const pick = clamp((t - B.wheelPick) / 0.3), bump = pop(t, B.wheelPick, 9, 220);
  return (
    <AbsoluteFill>
      <Ground color={w.field} />
      <Behind t={t} name="flower" x={600} y={700} size={820} color={w.container} seed={6} start={start} />
      <Wheel cx={600} cy={700} r={300} rotation={rot} colors={w.tones} ground={w.field} accent={w.accent} />
      <Phone screen={SCREENS.wheel} pose={WHEEL_POSE} dx={m.dx} dy={m.dy} opacity={1 - pick} />
      <Phone screen={SCREENS.result} pose={WHEEL_POSE} dx={m.dx} dy={m.dy} opacity={pick} grow={1 + 0.04 * Math.sin(bump * Math.PI)} z={3} />
      <ConfettiBurst t={t} at={B.wheelBurst} from={[600, 380]} pieces={pieces} />
      <Headline t={t} at={B.wheelHeadline} title="Decision Wheel" sub="Let chance decide" x={140} y={90} color={w.ink} subColor={w.accent} size={96} width={1100} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- montage
const TILES: { key: keyof typeof WORLDS; draw: (c: World) => React.ReactNode }[] = [
  { key: "water", draw: (c) => <g fill="none" stroke={c.accent} strokeLinecap="round">{[40, 70, 100].map((r, i) => <path key={i} d={`M ${150 - r * 0.7} ${110 + r * 0.7} A ${r} ${r} 0 0 0 ${150 + r * 0.7} ${110 + r * 0.7}`} strokeWidth={8 - i * 2} opacity={1 - i * 0.25} />)}<circle cx="215" cy="215" r="13" fill="#FFFFFF" stroke={c.accent} strokeWidth="3" /><circle cx="92" cy="232" r="8" fill="#FFFFFF" stroke={c.accent} strokeWidth="3" /></g> },
  { key: "ruler", draw: (c) => <g transform="translate(36 140) rotate(-10)"><rect width="230" height="70" rx="20" fill="#F6B91D" />{Array.from({ length: 17 }, (_, i) => <line key={i} x1={20 + i * 12} x2={20 + i * 12} y1="0" y2={i % 5 ? 18 : 34} stroke="#654000" strokeWidth="2.2" />)}</g> },
  { key: "unit", draw: (c) => <g fontFamily={FONT} fontWeight={650} fill={c.ink} textAnchor="middle"><circle cx="90" cy="160" r="62" fill={c.tones[0]} /><circle cx="210" cy="160" r="62" fill={c.tones[1]} /><text x="90" y="178" fontSize="52">m</text><text x="210" y="176" fontSize="46">cm</text></g> },
  { key: "bill", draw: (c) => <g fontFamily={FONT}><rect x="70" y="60" width="160" height="200" rx="22" fill="#FFFBF5" /><rect x="94" y="96" width="112" height="10" rx="5" fill={c.tones[2]} /><rect x="94" y="122" width="80" height="10" rx="5" fill={c.tones[2]} /><text x="150" y="214" fontSize="40" fontWeight={650} fill={c.ink} textAnchor="middle">€28.60</text></g> },
  { key: "wheel", draw: (c) => <g transform="translate(150 165)">{Array.from({ length: 6 }, (_, i) => { const a = ((i * 60 - 90) * Math.PI) / 180, b = (((i + 1) * 60 - 90) * Math.PI) / 180, r = 100; return <path key={i} d={`M 0 0 L ${r * Math.cos(a)} ${r * Math.sin(a)} A ${r} ${r} 0 0 1 ${r * Math.cos(b)} ${r * Math.sin(b)} Z`} fill={c.tones[i % 3]} stroke={c.field} strokeWidth="4" />; })}<path d="M -13 -118 h 26 l -13 26 Z" fill={c.accent} /></g> },
];
const MORE = ["Compass", "Bubble Level", "Flash Dimmer", "Morse & SOS", "Counter", "Coin Flip", "Dice Roll", "Random number", "Reaction Test", "Breathe"];
const Montage: React.FC<{ t: number }> = ({ t }) => {
  const rise = ramp(t, B.montageHeadline, 0.6);
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      {TILES.map((tile, i) => {
        const c = WORLDS[tile.key], p = pop(t, B.montageTiles[i], 12, 190), x = 960 + (i - 2) * 350 - 160, y = 170 - rise * 70;
        return (
          <div key={tile.key} style={{ position: "absolute", left: x, top: y, width: 320, height: 380, borderRadius: 48, background: c.field, boxShadow: "0 20px 36px rgba(40,30,20,.12)", transform: `scale(${p}) rotate(${(i - 2) * 2.5 * (1 - rise)}deg)`, transformOrigin: "50% 60%", overflow: "hidden", zIndex: 3 }}>
            <svg width="320" height="300" viewBox="0 0 300 300" style={{ position: "absolute", left: 10, top: 6 }}>{tile.draw(c)}</svg>
            <div style={{ position: "absolute", left: 0, right: 0, bottom: 34, textAlign: "center", fontFamily: FONT, fontSize: 30, fontWeight: 600, color: c.ink }}>{c.name}</div>
          </div>
        );
      })}
      <Headline t={t} at={[B.montageHeadline]} title="Everyday tools, one app" x={960} y={560} width={1500} align="center" color={NEUTRAL.ink} size={88} />
      <div style={{ position: "absolute", left: 160, right: 160, top: 740, display: "flex", flexWrap: "wrap", justifyContent: "center", gap: 18, zIndex: 4 }}>
        {MORE.map((name, i) => {
          const p = pop(t, B.montageChips[Math.min(Math.floor(i / 2), B.montageChips.length - 1)] + (i % 2) * 0.12, 12, 220);
          const c = Object.values(WORLDS)[i % 5];
          return <div key={name} style={{ transform: `scale(${p})`, opacity: clamp(p * 2), padding: "16px 30px", borderRadius: 999, background: c.container, color: c.ink, fontFamily: FONT, fontSize: 32, fontWeight: 560 }}>{name}</div>;
        })}
      </div>
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- end
const End: React.FC<{ t: number }> = ({ t }) => {
  const iconP = pop(t, B.endBurst, 10, 190);
  const pieces = useMemo(() => burstPieces({ seed: 7, count: 90, from: [960, 380], colors: CONFETTI, avoid: [{ x: 960, y: 380, r: 190 }, { x: 960, y: 700, r: 330 }] }), []);
  const ring = [B.endBurst, B.endBurst + 0.3, B.endBurst + 0.56];
  return (
    <AbsoluteFill>
      <Ground color={NEUTRAL.field} />
      <SoundWaves t={t} onsets={ring} from={[{ at: [960, 470], direction: [0, 1] }, { at: [960, 290], direction: [0, -1] }]} color={WORLDS.water.accent} tint={WORLDS.water.tones[0]} reach={520} />
      <ConfettiBurst t={t} at={B.endBurst} from={[960, 380]} pieces={pieces} z={3} />
      <Img src={icon} style={{ position: "absolute", left: 960 - 160, top: 380 - 160, width: 320, height: 320, transform: `scale(${iconP})`, zIndex: 5 }} />
      <Headline t={t} at={[B.endTitle, B.endPlay]} title="App Toolkit" sub="Available on Google Play" x={960} y={590} width={1300} align="center" color={NEUTRAL.ink} subColor={NEUTRAL.accent} size={124} />
    </AbsoluteFill>
  );
};

// ------------------------------------------------------------------------------------------------- film
const SCENES: Record<string, React.FC<{ t: number; start: number }>> = {
  intro: ({ t }) => <Intro t={t} />, water: Water, ruler: RulerScene, unit: Unit, bill: Bill, wheel: WheelScene, montage: ({ t }) => <Montage t={t} />, end: ({ t }) => <End t={t} />,
};

/**
 * The film: one scene per world, each opening out of a MaterialShape on its first beat (the previous world
 * stays underneath until it is covered), over the supplied soundtrack. Every time comes from timeline.json.
 */
export const Trailer: React.FC = () => {
  useFonts();
  const t = useCurrentFrame() / FPS, wipe = timeline.wipeSeconds;
  return (
    <AbsoluteFill style={{ background: NEUTRAL.field, fontFamily: FONT, overflow: "hidden" }}>
      {timeline.scenes.map((s, i) => {
        if (t < s.start || t >= s.end + wipe) return null;
        const Scene = SCENES[s.id], body = <Scene t={t} start={s.start} />;
        return <div key={s.id} style={{ position: "absolute", inset: 0, zIndex: i }}>{i === 0 ? body : <ShapeWipe t={t} start={s.start} dur={wipe} at={s.from as [number, number]} shape={s.shape!}>{body}</ShapeWipe>}</div>;
      })}
      <Audio src={staticFile("soundtrack.mp3")} />
    </AbsoluteFill>
  );
};
