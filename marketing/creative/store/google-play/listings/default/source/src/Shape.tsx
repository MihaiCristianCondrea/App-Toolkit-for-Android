import React from "react";
import { shapeUrl } from "../../../../../../../ui/src/kit/assets.js";

/** A MaterialShape from the UI kit (the same curves its tool badges use), filled with one colour. */
export const Shape: React.FC<{ name: string; x: number; y: number; size: number; color: string; rotate?: number }> = ({ name, x, y, size, color, rotate = 0 }) => {
  const mask = `url("${shapeUrl(name)}") center / contain no-repeat`;
  return <i style={{ position: "absolute", left: x, top: y, width: size, height: size, background: color, WebkitMask: mask, mask, transform: `rotate(${rotate}deg)` }} />;
};

export const Wave: React.FC<{ x: number; y: number; width: number; rotate: number; color: string }> = ({ x, y, width, rotate, color }) => (
  <svg style={{ position: "absolute", left: x, top: y, width, transform: `rotate(${rotate}deg)` }} viewBox="0 0 400 60" fill="none">
    <path d="M6 30 q25 -30 50 0 t50 0 t50 0 t50 0 t50 0 t50 0 t50 0" stroke={color} strokeWidth="9" strokeLinecap="round" />
  </svg>
);

export const Dots: React.FC<{ x: number; y: number; color: string }> = ({ x, y, color }) => (
  <svg style={{ position: "absolute", left: x, top: y }} width="96" height="96" viewBox="0 0 96 96">
    {[0, 1, 2].flatMap((r) => [0, 1, 2].map((c) => <circle key={`${r}${c}`} cx={16 + c * 32} cy={16 + r * 32} r="9" fill={color} />))}
  </svg>
);

/** A seeded generator, so every render places the same shapes. */
export const seeded = (seed: number) => () => (seed = (seed * 16807) % 2147483647) / 2147483647;

// Round shapes only: the pixel shapes clash with the rounded set.
export const SCATTER = ["gem", "pill", "sunny", "flower", "clover_4leaf", "cookie_6sided", "soft_burst", "arch", "cookie_4sided", "bun", "pentagon"];
