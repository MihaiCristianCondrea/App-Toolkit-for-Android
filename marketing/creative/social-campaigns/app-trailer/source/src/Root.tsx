import React from 'react';
import {Composition} from 'remotion';
import {Trailer} from './Trailer';
import {TrailerV2} from './v2/TrailerV2';
import media from './generated/media.json';

const FPS=90;
export const RemotionRoot:React.FC=()=><><Composition id="AppTrailer" component={Trailer} width={1920} height={1080} fps={FPS} durationInFrames={Math.ceil(media.durationSeconds*FPS)}/><Composition id="AppTrailerV2" component={TrailerV2} width={1920} height={1080} fps={FPS} durationInFrames={Math.ceil(media.durationSeconds*FPS)}/></>;
