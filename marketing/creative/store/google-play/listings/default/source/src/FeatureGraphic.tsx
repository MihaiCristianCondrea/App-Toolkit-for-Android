import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen, useFonts } from "./LitScreen";
import { SCREENS } from "./screens";
import { Shape } from "./Shape";
import { STRINGS } from "./strings";
import { FONT } from "./theme";

// 1024 x 500. The name and one line, centred (the part Play never crops); real screens at the sides, without a device
// frame; a strong amber ground; no icon, badge or device.
const SIDE: React.CSSProperties = { position: "absolute", width: 430, height: 957, transformOrigin: "0 0", borderRadius: 28, overflow: "hidden", zIndex: 1, boxShadow: "0 30px 50px rgba(58, 38, 0, .32)" };

export const FeatureGraphic: React.FC<{ locale: string }> = ({ locale }) => {
  useFonts();
  const { title, tagline } = STRINGS[locale].featureGraphic;
  return (
    <AbsoluteFill style={{ background: "#F6A800", color: "#2B1B00", fontFamily: FONT, overflow: "hidden" }}>
      <Shape name="cookie_12sided" x={222} y={-70} size={580} color="#FFC94D" rotate={8} />
      <Shape name="clover_4leaf" x={600} y={22} size={120} color="#FFE7A8" rotate={-12} />
      <Shape name="soft_burst" x={300} y={378} size={104} color="#EE9B00" rotate={14} />
      <div style={{ ...SIDE, left: -36, top: 70, transform: "scale(.6) rotate(-6deg)" }}>
        <LitScreen id={SCREENS["quick-tools"][0]} state={SCREENS["quick-tools"][1]} frame={false} style={{ width: 430, height: 957 }} />
      </div>
      <div style={{ ...SIDE, left: 806, top: 44, transform: "scale(.6) rotate(6deg)" }}>
        <LitScreen id={SCREENS.play[0]} state={SCREENS.play[1]} frame={false} style={{ width: 430, height: 957 }} />
      </div>
      <div style={{ position: "absolute", left: "50%", top: "50%", transform: "translate(-50%, -50%)", width: 520, textAlign: "center", zIndex: 2 }}>
        <h1 style={{ margin: 0, fontSize: 72, lineHeight: 1, letterSpacing: -2, fontWeight: 700 }}>{title}</h1>
        <p style={{ margin: "18px auto 0", maxWidth: 400, textWrap: "balance", fontSize: 26, lineHeight: 1.3, fontWeight: 500 }}>{tagline}</p>
      </div>
    </AbsoluteFill>
  );
};
