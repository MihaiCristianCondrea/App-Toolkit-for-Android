import React from "react";
import { shapeUrl } from "../../../../../ui/src/kit/assets.js";

/** A MaterialShape from the UI kit (the same curves its tool badges use), filled with one colour. */
export const Shape: React.FC<{ name: string; x: number; y: number; size: number; color: string; rotate?: number }> = ({ name, x, y, size, color, rotate = 0 }) => {
  const mask = `url("${shapeUrl(name)}") center / contain no-repeat`;
  return <i style={{ position: "absolute", left: x, top: y, width: size, height: size, background: color, WebkitMask: mask, mask, transform: `rotate(${rotate}deg)` }} />;
};
