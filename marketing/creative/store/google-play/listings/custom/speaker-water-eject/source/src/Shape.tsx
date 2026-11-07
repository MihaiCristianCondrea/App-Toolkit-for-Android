import React from "react";
import { shapeUrl } from "../../../../../../../../ui/src/kit/assets.js";

/** A MaterialShape from the UI kit (the same curves its tool badges use), filled with one colour. */
export const Shape: React.FC<{ name: string; x: number; y: number; size: number; color: string; rotate?: number }> = ({ name, x, y, size, color, rotate = 0 }) => {
  const mask = `url("${shapeUrl(name)}") center / contain no-repeat`;
  return <i style={{ position: "absolute", left: x, top: y, width: size, height: size, background: color, WebkitMask: mask, mask, transform: `rotate(${rotate}deg)` }} />;
};

export const Wave: React.FC<{ x: number; y: number; width: number; rotate: number; color: string }> = ({ x, y, width, rotate, color }) => (
  <svg style={{ position: "absolute", left: x, top: y, width, transform: `rotate(${rotate}deg)` }} viewBox="0 0 400 60" fill="none">
    <rect x="80" y="8" width="160" height="44" rx="22" stroke={color} strokeWidth="5"/>
    {[110,140,170,200].map(x=><path key={x} d={`M${x} 22v16`} stroke={color} strokeWidth="5" strokeLinecap="round"/>)}
    <path d="M55 15q-18 15 0 30M30 5q-30 25 0 50M267 15q18 15 0 30M292 5q30 25 0 50" stroke={color} strokeWidth="5" strokeLinecap="round"/>
  </svg>
);

export const Dots: React.FC<{ x: number; y: number; color: string }> = ({ x, y, color }) => (
  <svg style={{ position: "absolute", left: x, top: y }} width="96" height="96" viewBox="0 0 96 96">
    <path d="M48 8C40 24 20 43 20 60a28 28 0 0 0 56 0C76 43 56 24 48 8Z" fill={color}/><path d="M32 60q0 16 16 16" stroke="#E5F7FC" strokeWidth="5" fill="none" strokeLinecap="round"/>
  </svg>
);

/** A seeded generator, so every render places the same shapes. */
export const seeded = (seed: number) => () => (seed = (seed * 16807) % 2147483647) / 2147483647;

// Round shapes only: the pixel shapes clash with the rounded set.
export const SCATTER = ["gem", "pill", "sunny", "flower", "clover_4leaf", "cookie_6sided", "soft_burst", "arch", "cookie_4sided", "bun", "pentagon"];
