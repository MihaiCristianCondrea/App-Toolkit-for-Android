// Composes the trailer's soundtrack (node scripts/compose.mjs) into ../soundtrack.mp3. Everything is synthesised
// here, from nothing but sine waves and seeded noise, so the music is ours and can be changed with the film.
// It reads src/timeline.json: the bars (from barStart) carry the chords, and the beats place each moment's sound
// on the frame it happens: the intro's shapes and icon, the waves, the graduations, the protractor's steps, the
// categories, the receipt's lines, a ratchet that follows the wheel's real rotation, the badges and the cards.
//
// 120 BPM in F major, one chord per bar from 3 s (F C Dm Bb F C Dm Bb C F Dm C F), over an F pad under the intro.
// The arrangement follows the story's energy instead of repeating one block per scene: the intro's rising mallets
// and a bell for the icon; soft half-time pulses for the
// water; deliberate ticks over a four-on-the-floor for the measuring; a syncopated bass for the converter;
// prints for the receipt and then a real rest; a sparse wheel that winds up and builds through the spin to the
// film's only crash on the result; the fullest groove for the montage, where each tool's sound returns with
// its card; and a held F major resolution on the icon.
import { execFileSync } from "node:child_process";
import { readFileSync, rmSync, writeFileSync } from "node:fs";
import { fileURLToPath } from "node:url";

const tl = JSON.parse(readFileSync(new URL("../src/timeline.json", import.meta.url), "utf8"));
const B = tl.beats, RATE = 48000, BEAT = 60 / tl.tempo, BAR = 4 * BEAT, DUR = tl.durationSeconds, N = Math.ceil(DUR * RATE);
const scene = Object.fromEntries(tl.scenes.map((s) => [s.id, s]));
const L = new Float32Array(N), R = new Float32Array(N), SL = new Float32Array(N), SR = new Float32Array(N);

// ------------------------------------------------------------------------------------------------ helpers
let seed = 0x5eed;
const rand = () => ((seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0) / 4294967296);
const noise = () => rand() * 2 - 1;
const hz = (midi) => 440 * 2 ** ((midi - 69) / 12);
const TAU = 2 * Math.PI;

/** Adds a voice f(seconds since start) at `t0` for `len` seconds, panned (-1..1), with a reverb send. */
const voice = (t0, len, f, { gain = 1, pan = 0, send = 0, duck = false } = {}) => {
  const i0 = Math.round(t0 * RATE), n = Math.round(len * RATE);
  const gl = gain * Math.cos(((pan + 1) * Math.PI) / 4), gr = gain * Math.sin(((pan + 1) * Math.PI) / 4);
  for (let i = 0; i < n; i++) {
    const j = i0 + i;
    if (j < 0) continue;
    if (j >= N) break;
    const v = f(i / RATE) * (duck ? pump(j / RATE) : 1);
    L[j] += v * gl; R[j] += v * gr;
    if (send) { SL[j] += v * gl * send; SR[j] += v * gr * send; }
  }
};
/** A state-variable filter; returns a function that filters one sample and gives { lp, bp, hp }. */
const svf = () => {
  let low = 0, band = 0;
  return (x, cutoff, q = 0.7) => {
    const f = 2 * Math.sin((Math.PI * Math.min(cutoff, RATE / 6)) / RATE), high = x - low - band / q;
    band += f * high; low += f * band;
    return { lp: low, bp: band, hp: high };
  };
};
const env = (tau, attack, decay) => (tau < attack ? tau / attack : Math.exp(-(tau - attack) * decay));

// The sections, and where the kick plays (it ducks the pad and bass, a light sidechain pump).
const section = (t) => t < scene.water.start ? "intro" : t < scene.ruler.start + 0.5 ? "water" : t < scene.unit.start ? "ruler" : t < B.unitFold[1] ? "unit" : t < B.billRest[0] ? "bill" : t < scene.wheel.start ? "rest" : t < B.wheelBurst ? "wheel" : t < scene.end.start + 0.05 ? "montage" : "end";
const fourOnFloor = (t) => ["ruler", "unit", "bill", "montage"].includes(section(t)) && !(t >= B.rulerLift[0] && t < scene.unit.start);
const pump = (t) => (fourOnFloor(t) ? 1 - 0.45 * Math.exp(-(((t - tl.barStart) % BEAT + BEAT) % BEAT) / 0.09) : 1);

// ------------------------------------------------------------------------------------------------ instruments
const kick = (t, gain = 0.9) => voice(t, 0.5, (u) => {
  const phase = TAU * (48 * u + (120 * (1 - Math.exp(-u * 32))) / 32);
  return Math.sin(phase) * Math.exp(-u * 6.5) + noise() * Math.exp(-u * 400) * 0.25;
}, { gain });

const clap = (t, gain = 0.3) => { const f = svf(); voice(t, 0.3, (u) => {
  const bursts = [0, 0.011, 0.022].reduce((a, o) => a + (u >= o ? Math.exp(-(u - o) * (o < 0.02 ? 180 : 22)) : 0), 0);
  return f(noise(), 1500, 1.4).bp * bursts * 1.6;
}, { gain, send: 0.25 }); };

const hat = (t, gain = 0.08, pan = 0.25, open = false) => { const f = svf(); voice(t, open ? 0.3 : 0.08, (u) => f(noise(), 9000, 0.8).hp * Math.exp(-u * (open ? 14 : 60)), { gain, pan }); };

/** Additive saw (band-limited), `harmonics` partials rolled off by `bright`. */
const saw = (freq, u, harmonics = 10, bright = 0.25) => {
  let v = 0;
  for (let k = 1; k <= harmonics && k * freq < 16000; k++) v += (Math.sin(TAU * k * freq * u) / k) * Math.exp(-(k - 1) * bright);
  return v;
};

const bass = (t, midi, len, gain = 0.32) => voice(t, len, (u) => {
  const e = Math.min(1, u / 0.004) * Math.exp(-u * 3) * Math.min(1, (len - u) / 0.02);
  return (0.7 * Math.sin(TAU * hz(midi) * u) + 0.35 * saw(hz(midi), u, 6, 0.6)) * e;
}, { gain, duck: true });

const pad = (t, notes, len, gain = 0.05) => notes.forEach((m, i) => [-1, 1].forEach((side) => voice(t, len + 0.6, (u) => {
  const e = Math.min(1, u / 0.35) * Math.min(1, Math.max(0, (len + 0.6 - u) / 0.6));
  return saw(hz(m) * 2 ** ((side * 7) / 1200), u, 8, 0.45) * e;
}, { gain, pan: side * (0.35 + i * 0.1), send: 0.5, duck: true })));

/** A soft FM mallet, the film's lead voice. */
const mallet = (t, midi, gain = 0.16, pan = 0, decay = 5) => voice(t, 1.4, (u) => {
  const f = hz(midi);
  return Math.sin(TAU * f * u + 1.6 * Math.exp(-u * 14) * Math.sin(TAU * 2 * f * u)) * env(u, 0.003, decay);
}, { gain, pan, send: 0.35 });

/** A bell (FM, inharmonic ratio), for the icon and the end. */
const bell = (t, midi, gain = 0.12, pan = 0, len = 3.5) => voice(t, len, (u) => {
  const f = hz(midi);
  return Math.sin(TAU * f * u + 2.2 * Math.exp(-u * 3) * Math.sin(TAU * 3.5 * f * u)) * env(u, 0.002, 1.6);
}, { gain, pan, send: 0.55 });

/** A water drop: a sine that sweeps up as it rings. */
const drop = (t, from, to, gain = 0.22, pan = 0) => voice(t, 0.25, (u) => {
  const k = 1 - Math.exp(-u * 40), phase = TAU * (from * u + ((to - from) * (u - k / 40)));
  return Math.sin(phase) * env(u, 0.002, 22);
}, { gain, pan, send: 0.4 });

const tick = (t, gain = 0.14, freq = 2200, pan = 0) => { const f = svf(); voice(t, 0.06, (u) => (Math.sin(TAU * freq * u) * 0.6 + f(noise(), freq * 1.5, 3).bp) * Math.exp(-u * 120), { gain, pan, send: 0.15 }); };

/** Filtered noise rising into a downbeat; the shape wipes open on it. */
const riser = (end, len = 0.75, gain = 0.12) => { const f = svf(); voice(end - len, len + 0.12, (u) => {
  const p = Math.min(1, u / len), tail = u > len ? Math.exp(-(u - len) * 40) : 1;
  return f(noise(), 300 + 7000 * p * p, 1.2).bp * p * p * tail;
}, { gain, send: 0.3 }); };

/** A low tom: the cut accents, so they are not all crashes. */
const tom = (t, gain = 0.5, from = 150, to = 70) => voice(t, 0.6, (u) => {
  const phase = TAU * (to * u + ((from - to) * (1 - Math.exp(-u * 18))) / 18);
  return Math.sin(phase) * Math.exp(-u * 7) + noise() * Math.exp(-u * 80) * 0.15;
}, { gain, send: 0.3 });
/** A short paper sweep, for the fold. */
const fwip = (t, len = 0.3, gain = 0.08) => { const f = svf(); voice(t, len, (u) => f(noise(), 5000 - 4000 * (u / len), 1.5).bp * Math.sin(Math.PI * u / len), { gain, send: 0.2 }); };
/** A sine glide, for the wave straightening. */
const glide = (t, len, from, to, gain = 0.06) => voice(t, len, (u) => Math.sin(TAU * (from * u + ((to - from) * u * u) / (2 * len))) * Math.sin(Math.PI * u / len), { gain, send: 0.4 });

const crash = (t, gain = 0.1) => { const f = svf(); voice(t, 2.2, (u) => f(noise(), 6000, 0.6).hp * Math.exp(-u * 2.2), { gain, send: 0.4 }); };

// ------------------------------------------------------------------------------------------------ harmony
const F = { root: 41, pad: [53, 57, 60, 64], lead: [77, 72, 74, 72, 69], arp: [65, 69, 72, 76] };
const C = { root: 36, pad: [52, 55, 60, 62], lead: [79, 76, 74, 72, 74], arp: [64, 67, 72, 74] };
const Dm = { root: 38, pad: [50, 53, 57, 60], lead: [74, 77, 81, 77, 74], arp: [62, 65, 69, 72] };
const Bb = { root: 34, pad: [53, 57, 58, 62], lead: [74, 72, 70, 72, 77], arp: [58, 62, 65, 69] };
const PROG = [F, C, Dm, Bb, F, C, Dm, Bb, C, F, Dm, C, F, F];
const barOf = (t) => Math.floor((t - tl.barStart) / BAR);
const chordAt = (t) => PROG[Math.max(0, Math.min(PROG.length - 1, barOf(t)))];
const LEAD_RHYTHM = [0, 3, 4, 6, 7]; // in eighths

// Pads, one chord per bar, at the level each section wants; the intro holds F beneath the name.
const PAD = { intro: 0.05, water: 0.045, ruler: 0.04, unit: 0.035, bill: 0.035, rest: 0.06, wheel: 0.05, montage: 0.04 };
pad(0, F.pad, scene.water.start, PAD.intro);
for (let bar = 0; tl.barStart + bar * BAR < scene.end.start; bar++) {
  const t0 = tl.barStart + bar * BAR, gain = PAD[section(t0 + 0.01)] ?? 0.04;
  pad(t0, chordAt(t0 + 0.01).pad, BAR, gain);
}

// The groove, sixteenth by sixteenth, by section.
for (let t = tl.barStart; t < scene.end.start - 0.01; t += BEAT / 4) {
  const s16 = Math.round((t - tl.barStart) / (BEAT / 4)), inBar = s16 % 16, onBeat = inBar % 4 === 0, eighth = inBar % 2 === 0, c = chordAt(t + 0.001), sec = section(t + 0.001);
  if (sec === "water") {
    // Half time and flowing: a soft kick on 1 and 3, a shaker, a rippling arpeggio.
    if (inBar === 0 || inBar === 8) kick(t, 0.55);
    if (eighth) hat(t, inBar % 4 ? 0.04 : 0.025, inBar % 8 < 4 ? 0.35 : -0.35);
    if (eighth) mallet(t, c.arp[(inBar / 2) % 4] + 12, 0.045, (inBar % 8) / 4 - 0.9, 6);
  } else if (sec === "ruler") {
    if (fourOnFloor(t) && onBeat && t >= B.rulerPhone - 0.01) kick(t);
    if (fourOnFloor(t) && inBar % 4 === 2) hat(t, 0.07, 0.3);
    // Deliberate ticks: every sixteenth, accented on the beat, like a ruler's graduations.
    if (t >= B.rulerPhone - 0.01) tick(t, onBeat ? 0.07 : 0.03, onBeat ? 2600 : 3400, (inBar / 8) - 1);
    if (fourOnFloor(t) && eighth && t >= B.rulerPhone - 0.01) bass(t, c.root + (inBar % 8 === 6 ? 12 : 0), BEAT / 2 - 0.01, 0.28);
  } else if (sec === "unit") {
    if (onBeat) kick(t);
    if (inBar === 4 || inBar === 12) clap(t, 0.28);
    hat(t, eighth ? 0.05 : 0.025, inBar % 4 < 2 ? 0.3 : -0.3);
    // A syncopated bass: the dotted figure that makes the converter feel quicker.
    if ([0, 3, 6, 10, 12, 14].includes(inBar)) bass(t, c.root + (inBar === 6 || inBar === 14 ? 12 : 0), BEAT / 4 * (inBar === 0 ? 3 : 2) - 0.01, 0.3);
  } else if (sec === "bill") {
    if (onBeat) kick(t, 0.85);
    if (inBar === 4 || inBar === 12) clap(t, 0.24);
    if (eighth) hat(t, inBar % 4 ? 0.07 : 0.035, inBar % 8 < 4 ? 0.3 : -0.3);
    if (eighth) bass(t, c.root + (inBar % 8 === 6 ? 12 : 0), BEAT / 2 - 0.01, 0.28);
  } else if (sec === "montage" && t >= B.wheelBurst + BAR - 0.01) {
    // The fullest groove: everything at once, with the lead motif over it.
    if (onBeat) kick(t);
    if (inBar === 4 || inBar === 12) clap(t, 0.32);
    hat(t, eighth ? (inBar % 4 ? 0.09 : 0.05) : 0.035, inBar % 4 < 2 ? 0.3 : -0.3, inBar === 14);
    if (eighth) bass(t, c.root + (inBar % 8 === 6 ? 12 : 0), BEAT / 2 - 0.01, 0.32);
    const n = LEAD_RHYTHM.indexOf(inBar / 2);
    if (eighth && n >= 0) mallet(t, c.lead[n], 0.075, n % 2 ? 0.4 : -0.4);
  }
}

// ------------------------------------------------------------------------------------------------ the story's moments
// Intro: one mallet note per arriving shape, rising through F major; a bell for the icon; the name and the line
// each land on a mallet; hats thicken into the water world, which opens on a drop and a low tom.
[65, 69, 72, 76, 77].forEach((m, i) => mallet(B.introShapes[i], m, 0.2, (i - 2) * 0.3, 3.5));
bell(B.icon, 77, 0.16); bell(B.icon, 84, 0.07, 0.3);
mallet(B.title, 72, 0.12); mallet(B.tagline, 74, 0.1);
for (let t = B.tagline + BEAT; t < scene.water.start - 0.01; t += BEAT / 4) hat(t, 0.02 + 0.05 * ((t - B.tagline) / (scene.water.start - B.tagline)), Math.round(t * 8) % 2 ? 0.3 : -0.3);
glide(B.waterMorph[0], B.waterMorph[1] - B.waterMorph[0], 260, 700, 0.05);
drop(scene.water.start, 360, 1400, 0.24); tom(scene.water.start, 0.4);

// Water Eject: a drop on every wave (the ones that throw droplets are brighter).
B.waterWaves.forEach((t, i) => drop(t, 380 + (i % 3) * 90, 1300 + (i % 4) * 160, B.waterDrops.includes(t) ? 0.24 : 0.15, i % 2 ? 0.45 : -0.45));

// The wave becomes the ruler: a rising glide as it straightens, a run of ticks along it for the graduations,
// and a soft knock as it thickens into the ruler on the downbeat.
glide(B.waveLine[0], B.waveLine[1] - B.waveLine[0] + 0.1, 320, 1250, 0.07);
// The phone's turn: a soft sweep through edge-on.
fwip(B.phoneTurn[0] + 0.15, 0.4, 0.07);
for (let k = 0; k < 16; k++) tick(B.waveTicks[0] + (k * (B.waveTicks[1] - B.waveTicks[0])) / 16, 0.045, 3200 + k * 40, -0.8 + k / 10);
tom(B.rulerBody[1], 0.3, 120, 90);

// Ruler and Protractor: a click and a rising mallet on each step of the arm, the marker's run of ticks, then the reading lifts on a short swell and the match cut lands on a clap.
B.protractorSweep.forEach((t, i) => { tick(t, 0.15, 1800 + i * 200); mallet(t, [72, 74, 77, 81][i], 0.1, 0.2); });
for (let k = 0; k < 12; k++) tick(B.rulerMeasure[0] + (k * (B.rulerMeasure[1] - B.rulerMeasure[0])) / 12, 0.05, 2600, -0.4);
bell(B.rulerMeasure[1], 81, 0.09, 0.2, 2);
// The reading's shape expands into the converter's ground: a swell into the downbeat its first card grows on.
const unitIn = B.unitCategories[0];
riser(unitIn, unitIn - B.rulerLift[0], 0.08);
clap(unitIn, 0.4); bass(unitIn, 38, 0.4, 0.34); mallet(unitIn, 74, 0.12); mallet(unitIn + 0.08, 81, 0.08, 0.3);

// Unit Converter: a mallet pair per category, a step higher each time; then the fold, a paper sweep and a
// falling pair as the two cards become one surface.
B.unitCategories.forEach((t, i) => { if (i) { mallet(t, [72, 74, 77, 79, 81][i], 0.11, (i - 2) * 0.3); mallet(t + 0.125, [79, 81, 84, 86, 89][i], 0.07, -(i - 2) * 0.3, 8); } });
fwip(B.unitFold[0], 0.32, 0.09); mallet(B.unitFold[0] + 0.2, 77, 0.08); mallet(B.unitFold[0] + 0.32, 72, 0.08, -0.2);

// Tip and Bill Splitter: a print per line, a bell for the share, a chime for each person's card; then the rest,
// only the pad, and a quiet swell into the cut.
B.billLines.forEach((t, i) => { for (let k = 0; k < 4; k++) tick(t + k * 0.035, 0.05, 1400, 0.4); if (i === 3) bell(t, 81, 0.1, 0.2, 2); });
B.billShares.forEach((t, i) => { bell(t, [84, 88][i], 0.07, i ? 0.4 : -0.4, 1.6); });
riser(scene.wheel.start, 0.7, 0.05);

// Decision Wheel: a tom and a bell on the cut; a mallet per choice; a wind-up swell; a ratchet click each time
// a segment passes the pointer, following the wheel's own rotation (Trailer.tsx wheelRotation); a bass pedal,
// a snare roll and hats that build through the spin; the result is the film's only crash, with a sparkle.
tom(scene.wheel.start, 0.55, 170, 80); bell(scene.wheel.start, 70, 0.1);
B.wheelChoices.forEach((t, i) => mallet(t, [65, 69, 72, 74, 77, 81][i], 0.11, (i - 2.5) * 0.3));
riser(B.wheelWindup[1], B.wheelWindup[1] - B.wheelWindup[0] + 0.1, 0.06);
{
  const [w0, w1] = B.wheelWindup, [s0, s1] = B.wheelSpin, back = tl.wheel.windup, clamp01 = (x) => Math.min(1, Math.max(0, x));
  const rot = (t) => t < w0 ? 0 : t < s0 ? -back * clamp01((t - w0) / (w1 - w0)) : -back + (tl.wheel.turn + back) * (1 - (1 - clamp01((t - s0) / (s1 - s0))) ** 3);
  let last = Math.floor((rot(s0) + 1000 * 60) / 60);
  for (let t = s0; t < s1; t += 48 / RATE) { const seg = Math.floor((rot(t) + 1000 * 60) / 60); if (seg !== last) { tick(t, 0.13, 1600, -0.2); last = seg; } }
  bass(s0, 36, s1 - s0, 0.2);
  for (let k = 0; k < 20; k++) { const t = s0 + 1.0 + ((s1 - s0 - 1.0) * (1 - (1 - k / 20) ** 1.6)); clap(t, 0.03 + k * 0.012); }
  for (let t = s0 + 0.5; t < s1; t += BEAT / 4) hat(t, 0.02 + 0.06 * ((t - s0) / (s1 - s0)), Math.round(t * 8) % 2 ? 0.35 : -0.35);
  riser(s1, 1.5, 0.12);
  kick(s1, 1); crash(s1, 0.15); tom(s1, 0.35, 120, 60);
  [96, 93, 91, 89, 86, 84, 81, 79, 77].forEach((m, i) => mallet(s1 + 0.03 + i * 0.05 + rand() * 0.02, m, 0.065, rand() * 1.4 - 0.7, 9));
  bell(B.wheelPick, 77, 0.12, -0.2, 2.5);
}

// Montage: a pluck as each confetto becomes a tool, climbing the scale; each card lands with its own tool's
// sound (a drop, a run of ticks, a mallet pair, a print, a ratchet); a bell for the line; one riser into the icon.
// A breath after the result, leaning into the montage's drop on the next bar: hats that thicken.
for (let t = B.wheelBurst + BAR / 2; t < B.wheelBurst + BAR - 0.01; t += BEAT / 4) hat(t, 0.02 + 0.07 * ((t - B.wheelBurst - BAR / 2) / (BAR / 2)), Math.round(t * 8) % 2 ? 0.35 : -0.35);
const PENTA = [65, 67, 69, 72, 74, 77, 79, 81, 84, 86, 89, 91, 93, 96, 98, 101];
PENTA.forEach((m, i) => mallet(B.montageBadges[0] + 0.25 + i * 0.035, m, 0.05, (i % 8) / 4 - 0.9, 10));
const [c0, c1, c2, c3, c4] = B.montageCards;
drop(c0, 420, 1500, 0.2, -0.4);
for (let k = 0; k < 5; k++) tick(c1 + k * 0.04, 0.07, 2600 + k * 120, -0.2);
mallet(c2, 77, 0.12); mallet(c2 + 0.125, 84, 0.08, 0.3);
for (let k = 0; k < 4; k++) tick(c3 + k * 0.035, 0.07, 1400, 0.2);
for (let k = 0; k < 6; k++) tick(c4 + k * (0.05 + k * 0.01), 0.1, 1600, 0.4);
bell(B.montageHeadline, 81, 0.11, 0, 2);
riser(B.converge[1], B.converge[1] - B.converge[0] + 0.2, 0.12);

// End: the resolution on the downbeat the icon pops: F major in bells over a low F, a ripple of drops, the name
// and the badge each on a mallet, and a held chord that fades out.
{
  const t = B.endBurst;
  kick(t, 0.9); tom(t, 0.3, 100, 55); bass(t, 29, DUR - t - 0.2, 0.3); pad(t, [53, 57, 60, 65, 69], DUR - t - 0.6, 0.06);
  [65, 69, 72, 77].forEach((m, i) => bell(t + i * 0.02, m, 0.1, (i - 1.5) * 0.4));
  B.endRings.forEach((r, i) => drop(r, 420 + i * 70, 1400 + i * 140, 0.12, (i - 2) * 0.35));
  [96, 91, 89, 84, 81, 77].forEach((m, i) => mallet(t + 0.04 + i * 0.06, m, 0.05, rand() * 1.4 - 0.7, 9));
  mallet(B.endTitle, 77, 0.14); mallet(B.endPlay, 81, 0.12); mallet(B.endPlay + BEAT, 84, 0.1);
}

// ------------------------------------------------------------------------------------------------ mix
// A small Schroeder reverb on the sends.
const reverb = (input, offset) => {
  const out = new Float32Array(N);
  for (const [d, g] of [[1557, 0.82], [1617, 0.81], [1491, 0.83], [1422, 0.8]]) {
    const n = Math.round((d + offset) * 1.088), buf = new Float32Array(n);
    let k = 0, lp = 0;
    for (let i = 0; i < N; i++) { const y = buf[k]; lp = y * 0.7 + lp * 0.3; buf[k] = input[i] + lp * g; out[i] += y * 0.25; k = (k + 1) % n; }
  }
  for (const d of [556, 441]) {
    const n = Math.round((d + offset) * 1.088), buf = new Float32Array(n);
    let k = 0;
    for (let i = 0; i < N; i++) { const b = buf[k], y = -out[i] + b; buf[k] = out[i] + b * 0.5; out[i] = y; k = (k + 1) % n; }
  }
  return out;
};
const [wl, wr] = [reverb(SL, 0), reverb(SR, 23)];
let peak = 0;
for (let i = 0; i < N; i++) {
  const t = i / RATE, fade = Math.min(1, t / 0.01, (DUR - t) / 1.6);
  L[i] = Math.tanh((L[i] + wl[i] * 0.32) * 1.1) * fade; R[i] = Math.tanh((R[i] + wr[i] * 0.32) * 1.1) * fade;
  peak = Math.max(peak, Math.abs(L[i]), Math.abs(R[i]));
}
const k = 10 ** (-1 / 20) / peak, pcm = Buffer.alloc(44 + N * 4);
pcm.write("RIFF", 0); pcm.writeUInt32LE(36 + N * 4, 4); pcm.write("WAVEfmt ", 8); pcm.writeUInt32LE(16, 16);
pcm.writeUInt16LE(1, 20); pcm.writeUInt16LE(2, 22); pcm.writeUInt32LE(RATE, 24); pcm.writeUInt32LE(RATE * 4, 28);
pcm.writeUInt16LE(4, 32); pcm.writeUInt16LE(16, 34); pcm.write("data", 36); pcm.writeUInt32LE(N * 4, 40);
for (let i = 0; i < N; i++) { pcm.writeInt16LE(Math.round(L[i] * k * 32767), 44 + i * 4); pcm.writeInt16LE(Math.round(R[i] * k * 32767), 46 + i * 4); }

const wav = fileURLToPath(new URL("../../soundtrack.wav", import.meta.url)), mp3 = fileURLToPath(new URL("../../soundtrack.mp3", import.meta.url));
writeFileSync(wav, pcm);
execFileSync("ffmpeg", ["-hide_banner", "-loglevel", "error", "-y", "-i", wav, "-codec:a", "libmp3lame", "-b:a", "320k", "-metadata", "title=App Toolkit trailer", mp3]);
rmSync(wav);
console.log(`${mp3}: ${DUR} s, ${tl.tempo} BPM, peak -1 dBFS`);
