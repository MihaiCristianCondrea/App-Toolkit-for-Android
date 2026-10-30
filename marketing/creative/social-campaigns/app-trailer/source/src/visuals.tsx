import React from 'react';
import {Img, staticFile} from 'remotion';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,LiveScreen,SKIN_HEIGHT,SKIN_WIDTH} from './Kit';

export const INK='#3B2D14';
export const GROUND='#FFF4D9';
export const lerp=(a:number,b:number,p:number)=>a+(b-a)*p;
export const clamp=(n:number)=>Math.max(0,Math.min(1,n));
export const ease=(t:number,start:number,end:number)=>{const p=clamp((t-start)/(end-start));return p*p*p*(p*(p*6-15)+10);};
export const pop=(t:number,start:number,duration=.7)=>{const p=clamp((t-start)/duration);return p===1?1:1-Math.exp(-7*p)*Math.cos(8*p);};

export const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number}>=({name,color,size,x,y,angle=0,scale=1})=><div aria-hidden="true" style={{position:'absolute',width:size,height:size,left:0,top:0,background:color,maskImage:`url("${shapeUrl(name)}")`,WebkitMaskImage:`url("${shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;

/** Both layers are generated from the existing SVG paths, with no replacement logo artwork. */
export const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale?:number;angle?:number}>=({x,y,size,open,scale=1,angle=0})=><div style={{position:'absolute',left:0,top:0,width:size,height:size,perspective:800,transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}>
  <Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',zIndex:2}}/>
  <Img src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:'50% 44.24%',transform:`translateY(${-open*size*.16}px) rotateX(${-open*68}deg) rotate(${-open*5}deg)`,zIndex:3}}/>
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
