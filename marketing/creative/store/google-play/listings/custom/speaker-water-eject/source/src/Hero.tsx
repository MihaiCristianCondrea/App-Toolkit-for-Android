import React from "react";
import { LitScreen } from "./LitScreen";
import { SCREENS } from "./screens";
import { Wave } from "./Glass";
import solved from "./hero-pose.json";
import { CAPTION_BAND, CAPTION_TOP, FRAME, GOLDEN, H, SAFE_X, SPACE, W, WATER_BAND, measure, violations, waterDrops, waterHalfWidth, waterSpine } from "./heroGeometry";
import type { Pose, Pt, Ui } from "./heroGeometry";

const pose = solved.pose as Pose;
const geometry = measure(pose, solved.ui as Ui);
const problems = violations(geometry);
if (problems.length) throw new Error(`The hero pose breaks its constraints: ${problems.join("; ")}`);
const spine = waterSpine(geometry);
const drops = waterDrops(spine);

/**
 * The first screenshot: the phone in the pose scripts/solve-hero.ts solved for (src/hero-pose.json), the
 * frame's centre at (x, y), turned in 3D by the device frame itself and only scaled and rolled in the
 * picture plane. Water leaves its speaker holes along the spine the geometry defines. `debug` draws the
 * guides and measurements instead of the water.
 */
export const Hero: React.FC<{ debug?: boolean }> = ({ debug }) => {
  const [id, state] = SCREENS["water-eject"];
  return (
    <>
      <div style={{ position: "absolute", left: pose.x - FRAME.width / 2, top: pose.y - FRAME.height / 2, width: FRAME.width, height: FRAME.height, zIndex: 1, transform: `rotate(${pose.roll}deg) scale(${pose.scale})`, transformOrigin: "50% 50%", filter: "drop-shadow(0 34px 44px rgba(0,40,48,.26))" }}>
        <LitScreen id={id} state={state} pose={{ yaw: pose.yaw, pitch: pose.pitch, perspective: pose.perspective }} style={{ width: FRAME.width }} />
      </div>
      {!debug && (
        <div style={{ position: "absolute", left: 0, top: 0, width: W, height: H, zIndex: 10 }}>
          <Wave spine={spine} halfWidth={waterHalfWidth} drops={drops} width={W} height={H} />
        </div>
      )}
      {debug && <Overlay />}
    </>
  );
};

const poly = (pts: Pt[]) => pts.map((p) => p.map((n) => n.toFixed(1)).join(",")).join(" ");

/** The debug overlay: golden guides, safe areas, the projected silhouette and its measurements. */
const Overlay: React.FC = () => {
  const g = geometry, b = g.box, c = g.centroid;
  const label = (x: number, y: number, text: string, fill = "#C2185B") => <text x={x} y={y} fill={fill} fontSize="22" fontFamily="monospace" stroke="#fff" strokeWidth="5" paintOrder="stroke">{text}</text>;
  const n = (v: number, d: number) => (v / d).toFixed(3);
  return (
    <svg width={W} height={H} style={{ position: "absolute", left: 0, top: 0, zIndex: 40, overflow: "visible" }}>
      {GOLDEN.x.map((x) => <line key={x} x1={x} y1={0} x2={x} y2={H} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      {[...GOLDEN.y, GOLDEN.yLow].map((y) => <line key={y} x1={0} y1={y} x2={W} y2={y} stroke="#E6A100" strokeWidth="2" strokeDasharray="12 8" />)}
      <line x1={W / 2} y1={0} x2={W / 2} y2={H} stroke="#555" strokeWidth="1.5" />
      {[SAFE_X, W - SAFE_X].map((x) => <line key={x} x1={x} y1={0} x2={x} y2={H} stroke="#2E7D32" strokeWidth="2" />)}
      <rect x={0} y={WATER_BAND.top} width={W} height={WATER_BAND.bottom - WATER_BAND.top} fill="#00ACC1" opacity=".08" stroke="#00ACC1" strokeDasharray="6 6" />
      <rect x={CAPTION_BAND.left} y={CAPTION_BAND.top} width={CAPTION_BAND.right - CAPTION_BAND.left} height={CAPTION_BAND.bottom - CAPTION_BAND.top} fill="none" stroke="#6A1B9A" strokeWidth="3" />
      <line x1={0} y1={CAPTION_TOP} x2={W} y2={CAPTION_TOP} stroke="#6A1B9A" strokeDasharray="4 6" />
      <polygon points={poly(g.silhouette)} fill="none" stroke="#C2185B" strokeWidth="3" />
      <rect x={b.left} y={b.top} width={b.right - b.left} height={b.bottom - b.top} fill="none" stroke="#1565C0" strokeWidth="2" strokeDasharray="10 6" />
      {Object.entries(g.quads).map(([k, q]) => <polygon key={k} points={poly(q)} fill="#1565C0" fillOpacity=".08" stroke="#1565C0" strokeWidth="2" />)}
      <polyline points={poly(g.edgeFront)} fill="none" stroke="#FF6D00" strokeWidth="4" />
      <polyline points={poly(g.edgeBack)} fill="none" stroke="#FF6D00" strokeWidth="2" strokeDasharray="6 4" />
      <circle cx={c[0]} cy={c[1]} r="12" fill="none" stroke="#C2185B" strokeWidth="4" />
      <line x1={c[0] - 24} y1={c[1]} x2={c[0] + 24} y2={c[1]} stroke="#C2185B" strokeWidth="3" />
      <line x1={c[0]} y1={c[1] - 24} x2={c[0]} y2={c[1] + 24} stroke="#C2185B" strokeWidth="3" />
      <circle cx={g.focal[0]} cy={g.focal[1]} r="9" fill="#FF6D00" />
      <circle cx={g.speaker[0]} cy={g.speaker[1]} r="9" fill="#00838F" />
      <line x1={g.speaker[0]} y1={g.speaker[1]} x2={g.speaker[0] + g.direction[0] * SPACE.xl} y2={g.speaker[1] + g.direction[1] * SPACE.xl} stroke="#00838F" strokeWidth="4" />
      <polyline points={poly(spine)} fill="none" stroke="#00838F" strokeWidth="3" strokeDasharray="8 6" />
      {label(c[0] + 30, c[1] - 10, `silhouette centroid ${n(c[0], W)} W, ${n(c[1], H)} H`)}
      {label(g.focal[0] + 16, g.focal[1] + 64, `speaker edge ${n(g.focal[0], W)} W, ${n(g.focal[1], H)} H`, "#E65100")}
      {label(g.speaker[0] - 330, g.speaker[1] - 18, `speaker ${n(g.speaker[0], W)} W, ${n(g.speaker[1], H)} H`, "#00838F")}
      {label(b.left + 8, b.bottom + 30, `box ${n(b.left, W)}–${n(b.right, W)} W, to ${n(b.bottom, H)} H`, "#1565C0")}
      {label(SAFE_X + 8, WATER_BAND.top + 26, `water band 0.65–0.80 H · edge ${n(g.thickness, W)} W thick, ${g.angle.toFixed(1)}°`, "#00838F")}
      {label(CAPTION_BAND.left + 8, CAPTION_BAND.top - 10, "caption band 0.82–0.954 H", "#6A1B9A")}
    </svg>
  );
};
