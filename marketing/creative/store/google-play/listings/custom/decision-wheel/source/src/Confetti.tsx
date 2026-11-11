import React from "react";
import type { Confetto } from "./composition/confetti";

/** Confetti pieces: rounded strips and squares, and dots. */
export const Confetti: React.FC<{ pieces: Confetto[]; width: number; height: number }> = ({ pieces, width, height }) => (
  <svg aria-hidden width={width} height={height} style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
    {pieces.map((p, i) => p.kind === "dot"
      ? <circle key={i} cx={p.x} cy={p.y} r={p.w / 2} fill={p.color} />
      : <rect key={i} x={p.x - p.w / 2} y={p.y - p.h / 2} width={p.w} height={p.h} rx={Math.min(p.w, p.h) * 0.4} fill={p.color} transform={`rotate(${p.rotate.toFixed(1)} ${p.x.toFixed(1)} ${p.y.toFixed(1)})`} />)}
  </svg>
);
