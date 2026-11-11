import React from "react";
import { Composition } from "remotion";
import { Trailer } from "./Trailer";
import timeline from "./timeline.json";

// Laid out at 1920 x 1080, 90 fps; the preview renders at 0.5 and the master at 4/3 (2560 x 1440).
export const RemotionRoot: React.FC = () => (
  <Composition id="ToolkitTrailer" component={Trailer} width={timeline.design.width} height={timeline.design.height} fps={timeline.fps} durationInFrames={Math.round(timeline.durationSeconds * timeline.fps)} />
);
