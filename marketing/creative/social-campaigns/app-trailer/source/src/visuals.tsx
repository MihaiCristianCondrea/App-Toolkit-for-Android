import React from 'react';
import {Easing, Img, spring, staticFile,useCurrentFrame,useVideoConfig} from 'remotion';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,LiveScreen,SKIN_HEIGHT,SKIN_WIDTH,localRect,Geometry} from './Kit';
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
const CAMERA=[[0,1,0,0,0],...timeline.events.cameraMoves.flatMap((move,i)=>{
  const previous=i?timeline.events.cameraMoves[i-1].to:[1,0,0,0];
  return [[move.at,...previous],[move.at+move.duration,...move.to]];
})];
export const camera=(t:number,depth=1)=>{
  let i=0;while(i<CAMERA.length-2&&t>=CAMERA[i+1][0])i++;
  const [t0,s0,x0,y0,r0]=CAMERA[i],[t1,s1,x1,y1,r1]=CAMERA[i+1];
  const p=cinematic(clamp((t-t0)/(t1-t0)));
  return `translate(${lerp(x0,x1,p)*depth}px,${lerp(y0,y1,p)*depth}px) rotate(${lerp(r0,r1,p)*depth}deg) scale(${1+(lerp(s0,s1,p)-1)*depth})`;
};

/** A short accent weighted by the beat sheet, with no activity between notes. */
export const beat=(t:number,target?:string)=>{
  const hit=[...timeline.events.beats].reverse().find(b=>b.at<=t&&(!target||b.responses.some(r=>'target' in r&&r.target===target)));
  return hit?hit.strength*Math.exp(-(t-hit.at)*18):0;
};
export const phoneScaleAt=(t:number,scale:number)=>scale*(1+.008*beat(t,'phone'));

/** A tool name that lands on a beat, so a chapter names what the screen holds. */
export const Chip:React.FC<{text:string;color:string;ink:string;progress:number;active?:number}>=({text,color,ink,progress,active=0})=><span style={{display:'inline-block',padding:'12px 26px',borderRadius:999,background:color,color:ink,fontSize:34,fontWeight:600,lineHeight:1.1,whiteSpace:'nowrap',transform:`translateY(${(1-progress)*400}px) scale(${lerp(.6,1,progress)*(1+.08*active)})`}}>{text}</span>;

export const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number}>=({name,color,size,x,y,angle=0,scale=1})=><div aria-hidden="true" style={{position:'absolute',width:size,height:size,left:0,top:0,background:color,maskImage:`url("${shapeUrl(name)}")`,WebkitMaskImage:`url("${shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;

/** The visible bottom edge of the folding lid stays at the body's 44.24% seam. */
export const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale?:number;angle?:number;squash?:number}>=({x,y,size,open,scale=1,angle=0,squash=0})=>{
  const fold=Math.cos(Math.PI*clamp(open)),drop=clamp(open)*size*.22;
  // Compensate around the lid face's centre so its mirrored inside remains above the seam.
  const compensation=-size*(.4424-.23494)/2*(1-Math.abs(fold));
  return <div data-toolbox style={{position:'absolute',width:size,height:size,transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}>
    <div style={{position:'absolute',inset:0,transformOrigin:'50% 92%',transform:`scale(${1+squash*.07},${1-squash*.09})`}}>
      <Img src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:'50% 33.867%',transform:`translateY(${drop+compensation}px) scaleY(${fold})`,zIndex:fold<0?1:3,clipPath:fold<0?'inset(23.494% 0 55.76% 0)':undefined,filter:`brightness(${lerp(1,.8,clamp(open))})`}}/>
      <Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',zIndex:2,transform:`translateY(${drop}px)`}}/>
    </div>
  </div>;
};

export const Phone:React.FC<{id:string;state?:Record<string,unknown>;x:number;y:number;scale:number;angle?:number;shell?:number;ui?:number;hideWidgets?:boolean;revealFrom?:{x:number;y:number};reveal?:number;underId?:string;underState?:Record<string,unknown>;transition?:number;focusSelector?:string;underFocusSelector?:string;hiddenParts?:string[];onParts?:(g:Geometry)=>void;seconds?:number}>=({id,state={},x,y,scale,angle=0,shell=1,ui=1,hideWidgets=false,reveal=1,revealFrom={x:215,y:900},underId,underState,transition=1,focusSelector,underFocusSelector,hiddenParts=[],onParts,seconds=0})=>{
  const direction=revealFrom.x<215?-1:1;
  const focusPart=(root:HTMLElement,selector?:string)=>{
    const focus=selector?root.querySelector<HTMLElement>(selector):null;
    const viewport=root.querySelector('mnw-android-screen')?.shadowRoot?.querySelector<HTMLElement>('.content');
    if(focus&&viewport){const r=localRect(focus,root),v=localRect(viewport,root);if(r.y<v.y||r.y+r.height>v.y+v.height)viewport.scrollTop+=r.y-v.y-(v.height-r.height)/2;}
  };
  return <div data-phone={id} style={{position:'absolute',width:SKIN_WIDTH,height:SKIN_HEIGHT,transformOrigin:'50% 50%',transform:`translate(${x-SKIN_WIDTH/2}px,${y-SKIN_HEIGHT/2}px) rotate(${angle}deg) scale(${phoneScaleAt(seconds,scale)})`}}>
    <Img src={staticFile('device/back.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
    <div style={{position:'absolute',left:DISPLAY.x,top:DISPLAY.y,width:DISPLAY.width,height:DISPLAY.height,borderRadius:DISPLAY.radius,overflow:'hidden'}}>
      {underId&&transition<1?<LiveScreen id={underId} state={underState} onReady={root=>focusPart(root,underFocusSelector)} style={{position:'absolute',inset:0,opacity:1-ease(transition,0,.35),transform:`translateX(${-30*direction*transition}px)`}}/>:null}
      <div style={{position:'absolute',inset:0,opacity:ui*(underId?ease(transition,.35,1):1),transform:`translateX(${underId?30*direction*(1-transition):0}px)`,clipPath:reveal<1?`inset(${(1-reveal)*50}%)`:undefined}}>
        <LiveScreen id={id} state={{...state,_filmHideWidgets:hideWidgets}} onReady={root=>{
          focusPart(root,focusSelector);
          if(onParts){
            const parts:Geometry={};
            for(const node of root.querySelectorAll<HTMLElement>('[data-tool]'))if(node.dataset.tool)parts[node.dataset.tool]=localRect(node,root);
            onParts(parts);
          }
          if(hideWidgets)for(const node of root.querySelectorAll<HTMLElement>('.qt-live,.qt-breathe,.qt-colors'))node.style.visibility='hidden';
          for(const key of hiddenParts){const node=root.querySelector<HTMLElement>(`[data-tool="${key}"]`);if(node)node.style.visibility='hidden';}
        }}/>
      </div>
      <Img src={staticFile('device/mask.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell,pointerEvents:'none'}}/>
    </div>
  </div>;
};

export const Headline:React.FC<{text:string;x:number;y:number;width?:number;size?:number;progress?:number;seconds?:number;align?:'left'|'center'}>=({text,x,y,width=760,size=126,progress=1,seconds,align='left'})=><div data-headline style={{position:'absolute',left:x,top:y,width,color:INK,fontFamily:'Google Sans Flex',fontSize:size,lineHeight:1.04,fontWeight:650,letterSpacing:-size*.045,textAlign:align,paddingBottom:size*.25}}>{text.split(' ').map((word,i)=>{
  const notes=(timeline.events.type as Record<string,number[]>)[text];
  const p=seconds!==undefined&&notes?ease(seconds,notes[i],notes[i]+.24):clamp(progress*1.5-i*.22);
  return <span key={i} style={{display:'inline-block',overflow:'hidden',verticalAlign:'top',paddingBottom:'.15em',marginBottom:'-.15em',marginRight:'.19em'}}><span style={{display:'inline-block',transform:`translateY(${(1-p)*140}%)`,fontVariationSettings:`'wght' ${lerp(450,650,p)}, 'wdth' ${lerp(75,100,p)}`}}>{word}</span></span>;
})}</div>;
