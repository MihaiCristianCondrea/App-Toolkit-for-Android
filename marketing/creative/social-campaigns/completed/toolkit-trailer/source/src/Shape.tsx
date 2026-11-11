import React from "react";
import { morphPath } from "./motifs/morph";

/**
 * A MaterialShape (the UI kit's curves) filled with one colour, at (x, y) with its box `size` across. Drawn as an
 * SVG path with the turn built into the path, so it moves and turns at sub-pixel precision; a CSS mask snapped
 * to whole pixels and made slow motion look like shaking.
 */
export const Shape: React.FC<{ name: string; x: number; y: number; size: number; color: string; rotate?: number; opacity?: number }> = ({ name, x, y, size, color, rotate = 0, opacity = 1 }) =>
  size <= 0 ? null : (
    <svg aria-hidden width="1" height="1" style={{ position: "absolute", left: 0, top: 0, overflow: "visible", pointerEvents: "none" }}>
      <path d={morphPath(name, name, 1, x + size / 2, y + size / 2, size * 0.98, rotate)} fill={color} opacity={opacity} />
    </svg>
  );
