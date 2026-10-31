import React from 'react';
import {Composition} from 'remotion';
import {TrailerV3} from './Trailer';
import media from './generated/media.json';

const FPS=90;
export const RemotionRoot:React.FC=()=><Composition id="AppTrailerV3" component={TrailerV3} width={1920} height={1080} fps={FPS} durationInFrames={Math.ceil(media.durationSeconds*FPS)}/>;
