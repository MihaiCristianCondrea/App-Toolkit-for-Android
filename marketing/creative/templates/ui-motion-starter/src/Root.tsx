import React from "react";
import { Composition } from "remotion";
import { UiScreensDemo } from "./UiScreensDemo";

/** Frames per second of every render. The demo times everything in seconds, so this is the only place it is set. */
const FPS = 90;

export const RemotionRoot: React.FC = () => (
  <Composition id="UiScreensDemo" component={UiScreensDemo} width={1920} height={1080} fps={FPS} durationInFrames={8 * FPS} />
);
