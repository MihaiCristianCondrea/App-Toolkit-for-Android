import React,{useCallback,useState} from 'react';
import {AbsoluteFill,Audio,Sequence,staticFile,useCurrentFrame,useVideoConfig} from 'remotion';
import type {Geometry} from './Kit';
import {OpeningJourney} from './OpeningJourney';
import {FeatureJourney} from './FeatureJourney';
import {ClosingScene} from './ClosingScene';
import {GROUND,camera} from './visuals';
import timeline from './timeline.json';
import {CameraMotionBlur} from '@remotion/motion-blur';
import {Backdrop} from './visuals';

const Picture:React.FC=()=>{
  const frame=useCurrentFrame(),{fps}=useVideoConfig();
  const seconds=frame/fps;
  const [geometry,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((g:Geometry)=>setGeometry(old=>old??g),[]);
  return <AbsoluteFill style={{background:GROUND,fontFamily:'Google Sans Flex',color:'#3B2D14',overflow:'hidden'}}>
    <style>{`*,*::before,*::after{animation:none!important;transition:none!important;scroll-behavior:auto!important}html,body{margin:0} [data-phone] mnw-android-screen{overflow:hidden}`}</style>
    <Backdrop/>
    <AbsoluteFill style={{transformOrigin:'50% 50%',transform:camera(seconds)}}>
    <Sequence name="Toolbox to Quick Tools" durationInFrames={Math.round(timeline.events.measure*fps)}>
      <OpeningJourney seconds={seconds} onGeometry={measured}/>
    </Sequence>
    <Sequence name="Measure and Play" from={Math.round(timeline.events.measure*fps)} durationInFrames={Math.round((timeline.events.close+.4)*fps)-Math.round(timeline.events.measure*fps)}>
      {geometry?<FeatureJourney seconds={seconds} geometry={geometry}/>:<OpeningJourney seconds={seconds} onGeometry={measured}/>}
    </Sequence>
    <Sequence name="Your toolkit" from={Math.round(timeline.events.close*fps)}>
      <ClosingScene seconds={seconds}/>
    </Sequence>
    </AbsoluteFill>
  </AbsoluteFill>;
};

export const Trailer:React.FC=()=>{
  const t=useCurrentFrame()/useVideoConfig().fps;
  const fast=timeline.events.blurWindows.some(([start,end])=>t>=start&&t<end);
  return <AbsoluteFill>
    <Audio src={staticFile('audio/soundtrack.mp3')} volume={1}/>
    {fast?<CameraMotionBlur samples={3} shutterAngle={180}><Picture/></CameraMotionBlur>:<Picture/>}
  </AbsoluteFill>;
};
