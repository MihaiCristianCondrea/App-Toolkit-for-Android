import React from "react";
import { AbsoluteFill, Easing, interpolate, spring, useCurrentFrame, useVideoConfig } from "remotion";
import { LitScreen } from "./LitScreen";

const clamp = { extrapolateLeft: "clamp", extrapolateRight: "clamp" } as const;

/**
 * Three live app screens driven by the frame number: the Compass sheet turning, the
 * Counter sheet counting up, and the Bill Splitter adding people to the split.
 */
export const UiScreensDemo: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  // Every time below is in seconds, so the demo keeps its pacing at any frame rate.
  const t = frame / fps;

  const compass = {
    activeToolId: "compass",
    compassHeading: Math.round(interpolate(t, [0.6, 6], [312, 45 + 360], { ...clamp, easing: Easing.bezier(0.65, 0, 0.35, 1) }) % 360),
  };

  // One tap every 0.4 s from 1 s on.
  const counter = { activeSheet: "counter", counterValue: 24 + Math.max(0, Math.floor((t - 1) / 0.4) + 1) };

  // A person joins the split at each of these times.
  const joins = [2, 3.5, 5];
  const bill = { activeSheet: "bill_splitter", billAmount: 120, splitPeople: 2 + joins.filter((s) => t >= s).length };

  const enter = (i: number) => spring({ frame: frame - i * 0.2 * fps, fps, config: { damping: 18, stiffness: 90 } });
  const phone = (i: number): React.CSSProperties => ({
    zoom: 0.84,
    transform: `translateY(${(1 - enter(i)) * 260}px)`,
    opacity: enter(i),
  });

  return (
    <AbsoluteFill style={{ background: "#F4F3FA", flexDirection: "row", justifyContent: "center", alignItems: "center", gap: 60 }}>
      <LitScreen id="quick-tools" state={compass} style={phone(0)} />
      <LitScreen id="numbers" state={counter} style={phone(1)} />
      <LitScreen id="numbers" state={bill} style={phone(2)} />
    </AbsoluteFill>
  );
};
