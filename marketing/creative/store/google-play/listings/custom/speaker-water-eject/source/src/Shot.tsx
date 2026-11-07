import React from "react";
import { LitScreen } from "./LitScreen";
import { SCREENS } from "./screens";
import { SoundBubbles } from "./Glass";
import { Shape } from "./Shape";
import layout from "./composition/layout.json";
import ui from "./composition/ui.json";
import { CAPTION, FRAME, GOLDEN, H, SAFE_X, SPACE, TEXT_CLEARANCE, W, clipConvex, overlapArea } from "./composition/geometry";
import type { Pt, ScreenUi } from "./composition/geometry";
import { composeShot } from "./composition/compose";
import type { Layout } from "./composition/compose";
import { textZone } from "./composition/validate";

export const shotGeometry = (index: number, panels: number) => composeShot(index, panels, layout as Layout, ui as Record<string, ScreenUi>);

/**
 * One screenshot's foreground, in its own coordinates: shapes, then the phones back to front (each the
 * frame's centre at (x, y), turned in 3D by the device frame itself and only scaled and rolled in the
 * picture plane), then the effects. `debug` draws the measurements over it instead of the effects.
 */
export const Shot: React.FC<{ index: number; panels: number; debug?: boolean }> = ({ index, panels, debug }) => {
  const g = shotGeometry(index, panels);
  if (g.report.hard.length && !debug) throw new Error(`Screenshot ${index + 1} (${g.spec.id}) breaks its layout rules: ${g.report.hard.join("; ")}`);
  return (
    <>
      {g.shapes.map((p, i) => <Shape key={i} name={p.name} x={p.x} y={p.y} size={p.size} color={p.color} rotate={p.rotate} />)}
      {g.poses.map((pose, i) => {
        const [id, state] = SCREENS[g.spec.screens[i]];
        return (
          <div key={i} style={{ position: "absolute", left: pose.x - FRAME.width / 2, top: pose.y - FRAME.height / 2, width: FRAME.width, height: FRAME.height, zIndex: 1 + i, transform: `rotate(${pose.roll}deg) scale(${pose.scale})`, transformOrigin: "50% 50%", filter: "drop-shadow(0 34px 44px rgba(0,40,48,.26))" }}>
            <LitScreen id={id} state={state} pose={{ yaw: pose.yaw, pitch: pose.pitch, perspective: pose.perspective }} style={{ width: FRAME.width }} />
          </div>
        );
      })}
      {!debug && (g.arcs.length > 0 || g.spheres.length > 0) && (
        <div style={{ position: "absolute", left: 0, top: 0, width: W, height: H, zIndex: 10 }}>
          <SoundBubbles arcs={g.arcs} bubbles={g.spheres} width={W} height={H} />
        </div>
      )}
      {debug && <Overlay g={g} />}
    </>
  );
};

const poly = (pts: Pt[]) => pts.map((p) => p.map((n) => n.toFixed(1)).join(",")).join(" ");
const COLOURS = ["#C2185B", "#6A1B9A"];

/**
 * The debug overlay: golden guides, safe margins, the caption's band, text block and exclusion zone, each
 * phone's projected silhouette, visible box, centroid and UI boxes (critical solid, secondary dashed),
 * shapes and effects as outlines, overlaps between phones, the composition's visual centroid, and the
 * validation report: hard failures in red, preferences in amber, intentional exceptions in green.
 */
const Overlay: React.FC<{ g: ReturnType<typeof shotGeometry> }> = ({ g }) => {
  const caption = CAPTION[g.spec.caption], zone = textZone(g.spec);
  const label = (x: number, y: number, text: string, fill: string, size = 20) => <text x={x} y={y} fill={fill} fontSize={size} fontFamily="monospace" stroke="#fff" strokeWidth="5" paintOrder="stroke">{text}</text>;
  const n = (v: number, d: number) => (v / d).toFixed(3);
  const lines = [...g.report.hard.map((t) => ["#D50000", `HARD ${t}`]), ...g.report.preferences.map((t) => ["#E65100", `PREF ${t}`]), ...g.report.intentional.map((t) => ["#2E7D32", `OK ${t}`])];
  const b = g.report.balance;
  // The report sits on the side away from the caption.
  const reportTop = g.spec.caption === "top" ? H - SPACE.s - 12 - lines.length * 26 : SPACE.s;
  return (
    <svg width={W} height={H} style={{ position: "absolute", left: 0, top: 0, zIndex: 40, overflow: "visible" }}>
      {GOLDEN.x.map((x) => <line key={x} x1={x} y1={0} x2={x} y2={H} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      {[...GOLDEN.y, GOLDEN.yLow].map((y) => <line key={y} x1={0} y1={y} x2={W} y2={y} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      <line x1={W / 2} y1={0} x2={W / 2} y2={H} stroke="#555" strokeWidth="1.5" />
      {[SAFE_X, W - SAFE_X].map((x) => <line key={x} x1={x} y1={0} x2={x} y2={H} stroke="#2E7D32" strokeWidth="2" />)}
      <rect x={zone.left} y={zone.top} width={zone.right - zone.left} height={zone.bottom - zone.top} fill="#6A1B9A" fillOpacity=".06" stroke="#6A1B9A" strokeDasharray="6 6" />
      <rect x={caption.band.left} y={caption.band.top} width={caption.band.right - caption.band.left} height={caption.band.bottom - caption.band.top} fill="none" stroke="#6A1B9A" strokeWidth="1.5" />
      <rect x={caption.block.left} y={caption.block.top} width={caption.block.right - caption.block.left} height={caption.block.bottom - caption.block.top} fill="none" stroke="#6A1B9A" strokeWidth="3" />
      {label(zone.left + 6, g.spec.caption === "top" ? zone.bottom + 22 : zone.top - 8, `text zone: block + ${TEXT_CLEARANCE} px`, "#6A1B9A")}
      {g.shapes.map((p, i) => <circle key={i} cx={p.x + p.size / 2} cy={p.y + p.size / 2} r={p.size / 2} fill="none" stroke="#00897B" strokeWidth="2" strokeDasharray={p.behind ? "4 6" : undefined} />)}
      {g.arcs.map((a, i) => <polyline key={i} points={poly(a.pts)} fill="none" stroke="#00838F" strokeWidth="2" strokeDasharray="8 6" />)}
      {g.spheres.map(([x, y, r], i) => <circle key={i} cx={x} cy={y} r={r} fill="none" stroke="#00838F" strokeWidth="2" />)}
      {g.phones.map((m, i) => {
        const c = m.centroid, col = COLOURS[i % COLOURS.length];
        return (
          <g key={i}>
            <polygon points={poly(m.silhouette)} fill="none" stroke={col} strokeWidth="3" />
            <rect x={m.box.left} y={m.box.top} width={m.box.right - m.box.left} height={m.box.bottom - m.box.top} fill="none" stroke="#1565C0" strokeWidth="1.5" strokeDasharray="10 6" />
            {Object.entries(m.critical).map(([k, q]) => <polygon key={k} points={poly(q)} fill="#1565C0" fillOpacity=".1" stroke="#1565C0" strokeWidth="2" />)}
            {Object.entries(m.secondary).map(([k, q]) => <polygon key={k} points={poly(q)} fill="none" stroke="#78909C" strokeWidth="2" strokeDasharray="6 5" />)}
            <circle cx={c[0]} cy={c[1]} r="11" fill="none" stroke={col} strokeWidth="4" />
            <line x1={c[0] - 22} y1={c[1]} x2={c[0] + 22} y2={c[1]} stroke={col} strokeWidth="3" />
            <line x1={c[0]} y1={c[1] - 22} x2={c[0]} y2={c[1] + 22} stroke={col} strokeWidth="3" />
            {label(c[0] + 26, c[1] - 8 + i * 26, `${g.spec.screens[i]} ${n(c[0], W)} W, ${n(c[1], H)} H · ${(100 * m.area / (W * H)).toFixed(0)}% · p${m.pose.pitch} y${m.pose.yaw} r${m.pose.roll} ×${m.pose.scale}`, col)}
            {g.phones.slice(i + 1).map((front, j) => {
              const shared = clipConvex(m.visible, front.visible);
              return shared.length > 2 && overlapArea(m.visible, front.visible) > 0 ? <polygon key={j} points={poly(shared)} fill="#43A047" fillOpacity=".25" stroke="#2E7D32" strokeWidth="2" /> : null;
            })}
          </g>
        );
      })}
      <circle cx={b[0]} cy={b[1]} r="16" fill="none" stroke="#000" strokeWidth="3" />
      <circle cx={b[0]} cy={b[1]} r="4" fill="#000" />
      {label(b[0] + 22, b[1] + 30, `visual centroid ${n(b[0], W)} W, ${n(b[1], H)} H`, "#000")}
      <rect x={SPACE.s} y={reportTop} width={W - 2 * SPACE.s} height={lines.length * 26 + 12} fill="#fff" fillOpacity=".86" />
      {lines.map(([fill, t], i) => label(SPACE.s + 8, reportTop + 24 + i * 26, t, fill, 19))}
    </svg>
  );
};
