import React from "react";
import { AbsoluteFill } from "remotion";
import { useFonts } from "./LitScreen";
import { Shape } from "./Shape";
import { Confetti } from "./Confetti";
import { scatterConfetti } from "./composition/confetti";
import { FEATURE_FIELD, FONT, INK, PLUM, SHAPE_BEHIND, WHEEL_SEGMENTS } from "./theme";

export type Concept = "ribbon" | "ripple" | "editorial";

/**
 * The feature graphic: "Spin the Wheel" with App Toolkit small above it, beside a large wheel drawn in the
 * campaign's lilac, pink and coral, on the campaign's lilac field. Confetti is scattered over the open
 * space, clear of the type, the wheel and the shape behind it. No device frame, no icon, no claims.
 */
export const FeatureGraphic: React.FC<{ locale: string; concept?: Concept }> = ({ concept = "ribbon" }) => {
  useFonts();
  const editorial = concept === "editorial", moment = concept === "ripple";
  const cx = editorial ? 650 : moment ? 260 : 820, cy = editorial ? -25 : moment ? 390 : 280, r = editorial ? 280 : moment ? 290 : 235;
  const text = { left: moment ? 510 : 108, top: editorial ? 280 : 145, width: editorial ? 820 : moment ? 410 : 500 };
  // The type's box: the brand line and two lines of 76 px title (one line of 90 px for the editorial concept).
  const textBox = { left: text.left, top: text.top, right: text.left + (editorial ? 640 : 300), bottom: text.top + 40 + (editorial ? 92 : 156) };
  const backR = r + 70;
  const confetti = scatterConfetti({
    width: 1024, height: 500, seed: 31337, count: 26, margin: 18,
    focus: { left: cx - r, top: cy - r, right: cx + r, bottom: cy + r },
    free: (x, y, cr) =>
      !(x + cr > textBox.left - 34 && x - cr < textBox.right + 34 && y + cr > textBox.top - 34 && y - cr < textBox.bottom + 34) &&
      Math.hypot(x - cx, y - cy) > backR + cr + 13,
  });
  return (
    <AbsoluteFill style={{ background: FEATURE_FIELD, fontFamily: FONT, color: INK, overflow: "hidden" }}>
      <Shape name={moment ? "flower" : "soft_burst"} x={cx - backR} y={cy - backR} size={backR * 2} color={SHAPE_BEHIND} rotate={12} />
      <Confetti pieces={confetti} width={1024} height={500} />
      <svg width="1024" height="500" style={{ position: "absolute", inset: 0 }}>
        <g transform={`translate(${cx} ${cy}) rotate(-18)`}>
          {Array.from({ length: 6 }, (_, i) => {
            const a = ((i * 60 - 90) * Math.PI) / 180, b = (((i + 1) * 60 - 90) * Math.PI) / 180;
            return <path key={i} d={`M 0 0 L ${r * Math.cos(a)} ${r * Math.sin(a)} A ${r} ${r} 0 0 1 ${r * Math.cos(b)} ${r * Math.sin(b)} Z`} fill={WHEEL_SEGMENTS[i % 3]} stroke={FEATURE_FIELD} strokeWidth="5" />;
          })}
          <circle r="32" fill={FEATURE_FIELD} />
          <circle r="10" fill={PLUM} />
        </g>
        <path d={`M ${cx - 20} ${cy - r - 15} h 40 l -20 38 Z`} fill={PLUM} />
      </svg>
      <div style={{ position: "absolute", left: text.left, top: text.top, width: text.width }}>
        <div style={{ fontSize: 22, fontWeight: 600, marginBottom: 18, color: PLUM }}>App Toolkit</div>
        <h1 style={{ fontSize: editorial ? 90 : 76, lineHeight: 1.02, letterSpacing: -3, fontWeight: 650, margin: 0 }}>{editorial ? "Spin the Wheel" : <>Spin the<br />Wheel</>}</h1>
      </div>
    </AbsoluteFill>
  );
};
