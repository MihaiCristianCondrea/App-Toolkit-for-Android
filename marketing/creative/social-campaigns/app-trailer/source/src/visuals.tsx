import React from 'react';
import {Easing, Img, spring, staticFile} from 'remotion';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,LiveScreen,SKIN_HEIGHT,SKIN_WIDTH} from './Kit';
import music from './music-analysis.json';

export const INK='#3B2D14';
export const GROUND='#FFF4D9';
export const lerp=(a:number,b:number,p:number)=>a+(b-a)*p;
export const clamp=(n:number)=>Math.max(0,Math.min(1,n));
const emphasized=Easing.bezier(.2,0,0,1),accelerate=Easing.bezier(.3,0,.8,.15),cinematic=Easing.bezier(.45,0,.25,1);
/** Material emphasized: a quick start and a long, exact settle. */
export const ease=(t:number,start:number,end:number)=>emphasized(clamp((t-start)/(end-start)));
/** Exits accelerate away instead of drifting. */
export const easeIn=(t:number,start:number,end:number)=>accelerate(clamp((t-start)/(end-start)));
/** A lightly damped spring (one small overshoot) whose tail blends to exact rest, so nothing shimmers once landed. */
export const pop=(t:number,start:number,duration=.7)=>{
  if(t<=start)return 0;
  const frame=(t-start)*90,length=duration*90;
  if(frame>=length)return 1;
  const value=spring({frame,fps:90,config:{damping:15,stiffness:170,mass:.85}});
  return lerp(value,1,clamp((frame-length*.7)/(length*.3)));
};

/** One deterministic camera: chapter pushes and a recoil on the burst, held still under each headline's settle. */
const CAMERA:[number,number,number,number,number][]=[
  [0,1.14,0,30,-1.5],[1.76,1.08,0,10,0],[3.32,1.08,0,10,0],[3.62,.97,0,0,.6],[6.76,1,0,0,0],
  [8.02,1.05,30,0,0],[10.08,1.05,30,0,0],[11.44,1,0,0,0],[13.72,1,0,0,0],[14.6,1.04,40,0,0],
  [17.14,1.06,40,0,0],[17.7,1,0,0,0],[21.46,1.05,-40,0,0],[21.98,1.05,-40,0,0],[22.56,1,0,0,0],
  [25.98,1.05,-40,0,0],[26.26,1.05,-40,0,0],[27.26,.98,0,0,0],[29.88,1.02,0,0,0],
];
export const camera=(t:number)=>{
  let i=0;while(i<CAMERA.length-2&&t>=CAMERA[i+1][0])i++;
  const [t0,s0,x0,y0,r0]=CAMERA[i],[t1,s1,x1,y1,r1]=CAMERA[i+1];
  const p=cinematic(clamp((t-t0)/(t1-t0)));
  return `translate(${lerp(x0,x1,p)}px,${lerp(y0,y1,p)}px) rotate(${lerp(r0,r1,p)}deg) scale(${lerp(s0,s1,p)})`;
};

/** A decaying accent on the soundtrack's detected onsets: 1 on the hit, near 0 a few frames later. */
export const beat=(t:number)=>{let last=-1;for(const onset of music.onsetsSeconds){if(onset>t)break;last=onset;}return last<0?0:Math.exp(-(t-last)*9);};

/** A tool name that lands on a beat, so a chapter names what the screen holds. */
export const Chip:React.FC<{text:string;color:string;ink:string;progress:number;active?:number}>=({text,color,ink,progress,active=0})=><span style={{display:'inline-block',padding:'12px 26px',borderRadius:999,background:color,color:ink,fontSize:34,fontWeight:600,lineHeight:1.1,whiteSpace:'nowrap',opacity:clamp(progress*3),transform:`translateY(${(1-progress)*60}px) scale(${lerp(.6,1,progress)*(1+.08*active)})`}}>{text}</span>;

export const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number}>=({name,color,size,x,y,angle=0,scale=1})=><div aria-hidden="true" style={{position:'absolute',width:size,height:size,left:0,top:0,background:color,maskImage:`url("${shapeUrl(name)}")`,WebkitMaskImage:`url("${shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;

/** Both layers are generated from the existing SVG paths, with no replacement logo artwork. */
/** The lid hinges in the picture plane (a flat mark has no depth to tilt), and the body squashes and stretches from its base. */
export const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale?:number;angle?:number;squash?:number}>=({x,y,size,open,scale=1,angle=0,squash=0})=><div style={{position:'absolute',left:0,top:0,width:size,height:size,transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}>
  <div style={{position:'absolute',inset:0,transformOrigin:'50% 92%',transform:`scale(${1+squash*.07},${1-squash*.09})`}}>
    <Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',zIndex:2}}/>
    <Img src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:'16% 44.24%',transform:`translateY(${-open*size*.08}px) rotate(${-open*24}deg)`,zIndex:3}}/>
  </div>
</div>;

export const Phone:React.FC<{id:string;state?:Record<string,unknown>;x:number;y:number;scale:number;angle?:number;shell?:number;ui?:number;hideWidgets?:boolean;revealFrom?:{x:number;y:number};reveal?:number;underId?:string;underState?:Record<string,unknown>}>=({id,state={},x,y,scale,angle=0,shell=1,ui=1,hideWidgets=false,reveal=1,revealFrom={x:215,y:900},underId,underState})=><div data-phone={id} style={{position:'absolute',left:0,top:0,width:SKIN_WIDTH,height:SKIN_HEIGHT,transformOrigin:'50% 50%',transform:`translate(${x-SKIN_WIDTH/2}px,${y-SKIN_HEIGHT/2}px) rotate(${angle}deg) scale(${scale})`}}>
  <Img src={staticFile('device/back.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
  <div style={{position:'absolute',left:DISPLAY.x,top:DISPLAY.y,width:DISPLAY.width,height:DISPLAY.height,borderRadius:DISPLAY.radius,overflow:'hidden'}}>
    {underId&&reveal<1?<LiveScreen id={underId} state={underState} style={{position:'absolute',inset:0}}/>:null}
    <div style={{position:'absolute',inset:0,opacity:ui,clipPath:reveal<1?`circle(${reveal*1200}px at ${revealFrom.x}px ${revealFrom.y}px)`:undefined}}><LiveScreen id={id} state={{...state,_filmHideWidgets:hideWidgets}} onReady={root=>{
      if(hideWidgets)for(const node of root.querySelectorAll<HTMLElement>('.qt-live,.qt-breathe'))node.style.visibility='hidden';
    }}/></div>
    <Img src={staticFile('device/mask.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell,pointerEvents:'none'}}/>
  </div>
</div>;

export const Headline:React.FC<{text:string;x:number;y:number;width?:number;size?:number;progress?:number;align?:'left'|'center'}>=({text,x,y,width=760,size=126,progress=1,align='left'})=><div data-headline style={{position:'absolute',left:x,top:y,width,color:INK,fontFamily:'Google Sans Flex',fontSize:size,lineHeight:1.04,fontWeight:650,letterSpacing:-size*.045,textAlign:align,overflow:'hidden',paddingBottom:size*.25}}><div style={{transform:`translateY(${(1-progress)*130}%)`,fontVariationSettings:`'wght' ${lerp(500,650,progress)}, 'wdth' ${lerp(92,100,progress)}`}}>{text}</div></div>;
