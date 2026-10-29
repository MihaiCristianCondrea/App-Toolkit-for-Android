import React from "react";
import { AbsoluteFill, Img, staticFile } from "remotion";

// 512 x 512, 32-bit PNG. The launcher artwork itself (marketing/assets/logos/icon.svg, from .idea/icon.svg) on a
// transparent ground: Play keeps the alpha and applies its own mask, and the toolbox stands on its own.
export const Icon: React.FC = () => (
  <AbsoluteFill style={{ background: "transparent", alignItems: "center", justifyContent: "center" }}>
    <Img src={staticFile("logos/icon.svg")} style={{ width: 400, height: 400 }} />
  </AbsoluteFill>
);
