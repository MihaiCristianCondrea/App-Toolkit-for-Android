import React from 'react';
import {Composition} from 'remotion';
import {TrailerV2} from './Trailer';

const FPS = 90;
const DURATION_FRAMES = 2690; // 29.88s * 90 fps

export const RemotionRoot: React.FC = () => (
  <>
    <Composition
      id="AppTrailerV2"
      component={TrailerV2}
      width={1920}
      height={1080}
      fps={FPS}
      durationInFrames={DURATION_FRAMES}
    />
    <Composition
      id="AppTrailer"
      component={TrailerV2}
      width={1920}
      height={1080}
      fps={FPS}
      durationInFrames={DURATION_FRAMES}
    />
  </>
);
