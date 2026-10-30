import React from 'react';
import {Easing, Img, spring, staticFile,useCurrentFrame,useVideoConfig} from 'remotion';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,LiveScreen,SKIN_HEIGHT,SKIN_WIDTH} from './Kit';
import timeline from './timeline.json';

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

/** Phrase camera keyframes are shared by the foreground and the slower background. */
const CAMERA=timeline.events.camera;
export const camera=(t:number,depth=1)=>{
  let i=0;while(i<CAMERA.length-2&&t>=CAMERA[i+1][0])i++;
  const [t0,s0,x0,y0,r0]=CAMERA[i],[t1,s1,x1,y1,r1]=CAMERA[i+1];
  const p=cinematic(clamp((t-t0)/(t1-t0)));
  return `translate(${lerp(x0,x1,p)*depth}px,${lerp(y0,y1,p)*depth}px) rotate(${lerp(r0,r1,p)*depth}deg) scale(${1+(lerp(s0,s1,p)-1)*depth})`;
};

/** A short accent weighted by the beat sheet, with no activity between notes. */
export const beat=(t:number)=>{const hit=[...timeline.events.beats].reverse().find(b=>b.at<=t);return hit?hit.strength*Math.exp(-(t-hit.at)*14):0;};

/** A ratchet and a short spring punch per hit, with exact rest between notes. */
export const Backdrop:React.FC=()=>{
  const t=useCurrentFrame()/useVideoConfig().fps;
  const shapes=['cookie_9sided','flower','clover_4leaf','soft_burst'];
  return <div style={{position:'absolute',inset:0,transform:camera(t,.22)}}>{shapes.map((name,i)=>{
    const hits=timeline.events.beats.filter(b=>b.at<=t&&b.target===i);
    const last=hits.at(-1),p=last?pop(t,last.at,.24):1;
    const kick=last?Math.sin(Math.PI*clamp((t-last.at)/.24))*last.strength:0;
    return <div key={name} style={{transform:`scaleY(${1-.055*kick})`}}><Shape name={name} color={['#FDE7A2','#E8D3DC','#D0E9E3','#FFE5C6'][i]} size={i%2?650:780} x={i%2?1820:40} y={i<2?95:1030} angle={(hits.length-1)*32+32*p} scale={1+.15*kick}/></div>;
  })}</div>;
};

/** A tool name that lands on a beat, so a chapter names what the screen holds. */
export const Chip:React.FC<{text:string;color:string;ink:string;progress:number;active?:number}>=({text,color,ink,progress,active=0})=><span style={{display:'inline-block',padding:'12px 26px',borderRadius:999,background:color,color:ink,fontSize:34,fontWeight:600,lineHeight:1.1,whiteSpace:'nowrap',transform:`translateY(${(1-progress)*400}px) scale(${lerp(.6,1,progress)*(1+.08*active)})`}}>{text}</span>;

export const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number}>=({name,color,size,x,y,angle=0,scale=1})=><div aria-hidden="true" style={{position:'absolute',width:size,height:size,left:0,top:0,background:color,maskImage:`url("${shapeUrl(name)}")`,WebkitMaskImage:`url("${shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;

/** Both layers are generated from the existing SVG paths, with no replacement logo artwork. */
/** The lid flips over its hinge like a chest lid and ends behind the body, shaded as its inside; the body drops to make room. */
export const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale?:number;angle?:number;squash?:number}>=({x,y,size,open,scale=1,angle=0,squash=0})=><div style={{position:'absolute',left:0,top:0,width:size,height:size,transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}>
  <div style={{position:'absolute',inset:0,transformOrigin:'50% 92%',transform:`scale(${1+squash*.07},${1-squash*.09})`}}>
    <Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',zIndex:2,transform:`translateY(${open*size*.22}px)`}}/>
    <Img src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:'50% 44.24%',transform:`translateY(${-open*size*.14}px) scaleY(${Math.cos(Math.PI*open)})`,zIndex:open>.5?1:3,filter:open>.5?'brightness(.8)':undefined}}/>
  </div>
</div>;

export const Phone:React.FC<{id:string;state?:Record<string,unknown>;x:number;y:number;scale:number;angle?:number;shell?:number;ui?:number;hideWidgets?:boolean;revealFrom?:{x:number;y:number};reveal?:number;underId?:string;underState?:Record<string,unknown>}>=({id,state={},x,y,scale,angle=0,shell=1,ui=1,hideWidgets=false,reveal=1,revealFrom={x:215,y:900},underId,underState})=><div data-phone={id} style={{position:'absolute',left:0,top:0,width:SKIN_WIDTH,height:SKIN_HEIGHT,transformOrigin:'50% 50%',transform:`translate(${x-SKIN_WIDTH/2}px,${y-SKIN_HEIGHT/2}px) rotate(${angle}deg) scale(${scale})`}}>
  <Img src={staticFile('device/back.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
  <div style={{position:'absolute',left:DISPLAY.x,top:DISPLAY.y,width:DISPLAY.width,height:DISPLAY.height,borderRadius:DISPLAY.radius,overflow:'hidden'}}>
    {underId&&reveal<1?<LiveScreen id={underId} state={underState} style={{position:'absolute',inset:0}}/>:null}
    <div style={{position:'absolute',inset:0,opacity:ui,clipPath:reveal<1?`circle(${reveal*1200}px at ${revealFrom.x}px ${revealFrom.y}px)`:undefined}}><LiveScreen id={id} state={{...state,_filmHideWidgets:hideWidgets}} onReady={root=>{
      if(hideWidgets)for(const node of root.querySelectorAll<HTMLElement>('.qt-live,.qt-breathe,.qt-colors'))node.style.visibility='hidden';
    }}/></div>
    <Img src={staticFile('device/mask.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell,pointerEvents:'none'}}/>
  </div>
</div>;

export const Headline:React.FC<{text:string;x:number;y:number;width?:number;size?:number;progress?:number;seconds?:number;align?:'left'|'center'}>=({text,x,y,width=760,size=126,progress=1,seconds,align='left'})=><div data-headline style={{position:'absolute',left:x,top:y,width,color:INK,fontFamily:'Google Sans Flex',fontSize:size,lineHeight:1.04,fontWeight:650,letterSpacing:-size*.045,textAlign:align,paddingBottom:size*.25}}>{text.split(' ').map((word,i)=>{
  const notes=(timeline.events.type as Record<string,number[]>)[text];
  const p=seconds!==undefined&&notes?ease(seconds,notes[i],notes[i]+.24):clamp(progress*1.5-i*.22);
  return <span key={i} style={{display:'inline-block',overflow:'hidden',verticalAlign:'top',paddingBottom:'.15em',marginBottom:'-.15em',marginRight:'.19em'}}><span style={{display:'inline-block',transform:`translateY(${(1-p)*140}%)`,fontVariationSettings:`'wght' ${lerp(450,650,p)}, 'wdth' ${lerp(75,100,p)}`}}>{word}</span></span>;
})}</div>;
