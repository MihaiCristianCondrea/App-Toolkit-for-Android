import React,{createContext,useContext} from 'react';
import {AbsoluteFill,Easing,Img,spring,staticFile,useCurrentFrame,useVideoConfig} from 'remotion';
import {CameraMotionBlur} from '@remotion/motion-blur';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,LiveScreen,SKIN_HEIGHT,SKIN_WIDTH,localRect,measuredRect,Geometry,Rect,LiveCrop,radiusCss} from './Kit';
import timeline from './timeline.json';

export const INK='#3B2D14';
export const GROUND='#FFF4D9';
export const lerp=(a:number,b:number,p:number)=>a+(b-a)*p;
export const clamp=(n:number)=>Math.max(0,Math.min(1,n));
const emphasized=Easing.bezier(.2,0,0,1),accelerate=Easing.bezier(.3,0,.8,.15);
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

export type View={x:number;y:number;scale:number;angle:number};
const ViewContext=createContext<View>({x:960,y:540,scale:1,angle:0});
/** Adapted from Android Insider's focus camera and logarithmic shape-to-ground sizing. */
export const logSize=(a:number,b:number,p:number)=>a*Math.pow(b/a,p);
export const mixView=(a:View,b:View,p:number):View=>({x:lerp(a.x,b.x,p),y:lerp(a.y,b.y,p),scale:logSize(a.scale,b.scale,p),angle:lerp(a.angle,b.angle,p)});
const viewAffine=(v:View)=>{
  const a=v.angle*Math.PI/180,dx=(v.x-960)*v.scale,dy=(v.y-540)*v.scale;
  return {x:-dx*Math.cos(a)+dy*Math.sin(a),y:-dx*Math.sin(a)-dy*Math.cos(a),scale:v.scale,angle:v.angle};
};
const affineCss=(c:View,depth=1)=>`translate(${c.x*depth}px,${c.y*depth}px) rotate(${c.angle*depth}deg) scale(${1+(c.scale-1)*depth})`;
export const CameraRig:React.FC<{view:View;children:React.ReactNode}>=({view,children})=><ViewContext.Provider value={view}><div style={{position:'absolute',inset:0,transformOrigin:'50% 50%',transform:affineCss(viewAffine(view))}}>{children}</div></ViewContext.Provider>;
export const projectPart=(rect:Rect,box:Rect,pose:PhonePose)=>{
  const a=pose.angle*Math.PI/180,dx=(rect.x-box.x+rect.width/2-box.width/2)*pose.scale,dy=(rect.y-box.y+rect.height/2-box.height/2)*pose.scale;
  return {x:pose.x+dx*Math.cos(a)-dy*Math.sin(a),y:pose.y+dx*Math.sin(a)+dy*Math.cos(a)};
};
/** The reference film grows an originating shape far enough to cover every corner. */
export const coverSize=(x:number,y:number)=>Math.max(Math.hypot(x,y),Math.hypot(1920-x,y),Math.hypot(x,1080-y),Math.hypot(1920-x,1080-y))/.34*1.06;
export const TYPE={chapter:{size:240,weight:750},toolLabel:{size:56,weight:650},closingTitle:{size:128,weight:750},closingSubtitle:{size:72,weight:650},phoneLabel:{size:36,weight:650}} as const;

/** Two Material shadow layers stay outside the clip and share the actor's pose. */
export const elevation=(level:number,pose:PhonePose)=>{
  const p=clamp(level-2),r=timeline.events.polish.shadow.rest,l=timeline.events.polish.shadow.lift;
  const n=r.map((v,i)=>lerp(v,l[i],p)),a=pose.angle*Math.PI/180,s=Math.max(.1,pose.scale);
  return [0,3].map(i=>`drop-shadow(${Math.sin(a)*n[i]/s}px ${Math.cos(a)*n[i]/s}px ${n[i+1]/s}px rgba(35,29,22,${n[i+2]}))`).join(' ');
};
export const Hero:React.FC<{tool:string;screen:string;selector?:string;rect:Rect;state:Record<string,unknown>;pose:PhonePose;radii:number[];seconds:number;clip?:string;level?:number;zIndex?:number;nudge?:boolean}>=({tool,screen,selector,rect,state,pose,radii,clip,level=3,zIndex,seconds,nudge=true})=><div data-hero-part={tool} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${pose.x-rect.width/2}px,${pose.y+(nudge?timeline.events.polish.hitOffset*beat(seconds,'hero'):0)-rect.height/2}px) rotate(${pose.angle}deg) scale(${pose.scale})`,filter:elevation(level,pose),zIndex}}><div style={{width:'100%',height:'100%',borderRadius:radiusCss(radii),clipPath:clip}}><LiveCrop screen={screen} selector={selector??`[data-tool="${tool}"]`} state={state} radii={radii}/></div></div>;

/** The shape remains the ground after it covers every corner. It never wipes the subject away. */
export const GroundWipe:React.FC<{from:{x:number;y:number};color:string;at:number;shape:string}>=({from,color,at,shape})=>{
  const t=useCurrentFrame()/useVideoConfig().fps,d=timeline.events.polish.groundDuration;
  if(t<at)return null;
  if(t>=at+d)return <div style={{position:'absolute',inset:0,background:color}}/>;
  return <Shape name={shape} color={color} size={logSize(timeline.events.polish.groundStart,coverSize(from.x,from.y),ease(t,at,at+d))} x={from.x} y={from.y}/>;
};
export const Ground:React.FC=()=><>{timeline.events.wipes.map(w=><GroundWipe key={w.at} from={{x:w.x,y:w.y}} color={w.color} at={w.at} shape={w.shape}/>)}</>;

/** A sharp current frame keeps the six-sample trail below the opacity cap. */
export const BoundedMotionBlur:React.FC<{children:React.ReactNode}>=({children})=>{
  const b=timeline.events.polish.blur;
  return <AbsoluteFill><CameraMotionBlur samples={b.samples} shutterAngle={b.shutter}>{children}</CameraMotionBlur><AbsoluteFill style={{opacity:1-b.tail}}>{children}</AbsoluteFill></AbsoluteFill>;
};

/** Fit the projected whole card, including roll, inside the safe crop rectangle. */
export const closeupView=(rect:Rect,pose:PhonePose,view:View):View=>{
  const c=timeline.events.polish.crop,a=(pose.angle+view.angle)*Math.PI/180;
  const w=Math.abs(Math.cos(a))*rect.width+Math.abs(Math.sin(a))*rect.height,h=Math.abs(Math.cos(a))*rect.height+Math.abs(Math.sin(a))*rect.width;
  const scale=Math.min(view.scale,(c.right-c.left)/(w*pose.scale),(c.bottom-c.top)/(h*pose.scale));
  const dx=(c.screenX-960)/scale,dy=(c.screenY-540)/scale,r=-view.angle*Math.PI/180;
  return {x:pose.x-dx*Math.cos(r)+dy*Math.sin(r),y:pose.y-dx*Math.sin(r)-dy*Math.cos(r),scale,angle:view.angle};
};
/** Whole-word masks from the reference headline system, with padded descenders. */
export const KineticLine:React.FC<{seconds:number;text:string;notes:number[];x:number;y:number;size?:number;width?:number;exit?:number;color?:string;weight?:number}>=({seconds:t,text,notes,x,y,size=TYPE.chapter.size,width=1780,exit=0,color=INK,weight=TYPE.chapter.weight})=><div style={{position:'absolute',left:x,top:y,width,fontFamily:'Google Sans Flex',fontSize:size,fontWeight:weight,lineHeight:1.02,letterSpacing:-size*.045,color,transform:`translateX(${-2200*exit}px)`}}>{text.split(' ').map((word,i)=>{
  const p=pop(t,notes[i]??notes[0],.18);
  return <span key={i} style={{display:'inline-block',overflow:'hidden',padding:'.08em .05em .26em',margin:'-.08em -.05em -.26em',marginRight:'.18em',whiteSpace:'nowrap'}}><span style={{display:'inline-block',transform:`translateY(${(1-p)*145}%)`,fontVariationSettings:`'wght' ${lerp(520,weight,p)}, 'wdth' ${lerp(80,100,p)}`}}>{word}</span></span>;
})}</div>;

export const ToolLabel:React.FC<{seconds:number;text:string;at:number;exit?:number}>=({seconds,text,at,exit=0})=>{
  const l=timeline.events.polish.label;
  return <div data-tool-label style={{position:'absolute',inset:0,transform:`translate(${l.x}px,${l.y}px)`}}><div style={{height:TYPE.toolLabel.size,transform:'translateY(-100%)'}}><KineticLine seconds={seconds} text={text} notes={text.split(' ').map(()=>at)} x={0} y={0} width={l.width} size={TYPE.toolLabel.size} weight={TYPE.toolLabel.weight} exit={exit}/></div></div>;
};

/** A short accent weighted by the beat sheet, with no activity between notes. */
export const beat=(t:number,target?:string)=>{
  const cue=target?[...timeline.events.accents].reverse().find(a=>a.at<=t&&a.target===target):undefined;
  const hit=[...timeline.events.beats].reverse().find(b=>target?b.at===cue?.at:b.at<=t);
  return hit?hit.strength*Math.exp(-(t-hit.at)*18)*(1-ease(t-hit.at,.1,.2)):0;
};
export const phoneScaleAt=(t:number,scale:number)=>scale*(1+.008*beat(t,'phone'));

export type PhonePose={x:number;y:number;scale:number;angle:number};
/** Project a measured UI rectangle through exactly the same pose as its device. */
export const projectPhone=(rect:Rect,pose:PhonePose,t:number)=>{
  const s=phoneScaleAt(t,pose.scale),a=pose.angle*Math.PI/180;
  const x=(-SKIN_WIDTH/2+DISPLAY.x+rect.x+rect.width/2)*s,y=(-SKIN_HEIGHT/2+DISPLAY.y+rect.y+rect.height/2)*s;
  return {x:pose.x+x*Math.cos(a)-y*Math.sin(a),y:pose.y+x*Math.sin(a)+y*Math.cos(a),scale:s};
};

/** Background stages share the camera direction at a lower depth than the subject. */
export const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number;seconds?:number;mask?:string}>=({name,color,size,x,y,angle=0,scale=1,seconds,mask:maskUrl})=>{
  const view=useContext(ViewContext);
  const mask=<div aria-hidden="true" style={{position:'absolute',width:size,height:size,left:0,top:0,background:color,maskImage:`url("${maskUrl??shapeUrl(name)}")`,WebkitMaskImage:`url("${maskUrl??shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;
  if(seconds===undefined)return mask;
  const c=viewAffine(view);
  return <div style={{position:'absolute',inset:0,transformOrigin:'50% 50%',transform:`scale(${1/c.scale}) rotate(${-c.angle}deg) translate(${-c.x}px,${-c.y}px) ${affineCss(c,.4)}`}}>{mask}</div>;
};

/** The original lid swings around the right lip, carrying its handle into the open pose. */
export const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale?:number;angle?:number;squash?:number;layer?:'body'|'lid'|'both';level?:number}>=({x,y,size,open,scale=1,angle=0,squash=0,layer='both',level=2})=>{
  const p=clamp(open),drop=p*size*.22;
  const seam=(366.1-154.55)/478.2,lip=(375.7-154.55)/478.2;
  const hingeShift=p*(lip-seam)*size;
  return <div data-toolbox style={{position:'absolute',width:size,height:size,filter:elevation(level,{x,y,scale,angle}),transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}>
    <div style={{position:'absolute',inset:0,transformOrigin:'50% 92%',transform:`scale(${1+squash*.07},${1-squash*.09})`}}>
      {layer!=='body'?<Img data-toolbox-lid src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:`97% ${seam*100}%`,transform:`translateY(${drop+hingeShift}px) rotate(${135*p}deg)`,zIndex:p>0?1:3}}/>:null}
      {layer!=='lid'?<Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',zIndex:2,transform:`translateY(${drop}px)`}}/>:null}
    </div>
  </div>;
};

export const Phone:React.FC<{id:string;state?:Record<string,unknown>;x:number;y:number;scale:number;angle?:number;shell?:number;ui?:number;hideWidgets?:boolean;revealFrom?:{x:number;y:number};reveal?:number;underId?:string;underState?:Record<string,unknown>;transition?:number;focusSelector?:string;underFocusSelector?:string;hiddenParts?:string[];hiddenSelectors?:string[];onParts?:(g:Geometry)=>void;seconds?:number;level?:number}>=({id,state={},x,y,scale,angle=0,shell=1,ui=1,hideWidgets=false,reveal=1,revealFrom={x:215,y:900},underId,underState,transition=1,focusSelector,underFocusSelector,hiddenParts=[],hiddenSelectors=[],onParts,seconds=0,level=2})=>{
  const direction=revealFrom.x<215?-1:1;
  const focusPart=(root:HTMLElement,selector?:string)=>{
    const focus=selector?root.querySelector<HTMLElement>(selector):null;
    const viewport=root.querySelector('mnw-android-screen')?.shadowRoot?.querySelector<HTMLElement>('.content');
    if(focus&&viewport){const r=localRect(focus,root),v=localRect(viewport,root);if(r.y<v.y||r.y+r.height>v.y+v.height)viewport.scrollTop+=r.y-v.y-(v.height-r.height)/2;}
  };
  return <div data-phone={id} style={{position:'absolute',width:SKIN_WIDTH,height:SKIN_HEIGHT,filter:elevation(level,{x,y,scale,angle}),transformOrigin:'50% 50%',transform:`translate(${x-SKIN_WIDTH/2}px,${y-SKIN_HEIGHT/2}px) rotate(${angle}deg) scale(${phoneScaleAt(seconds,scale)})`}}>
    <Img src={staticFile('device/back.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
    <div style={{position:'absolute',left:DISPLAY.x,top:DISPLAY.y,width:DISPLAY.width,height:DISPLAY.height,borderRadius:DISPLAY.radius,overflow:'hidden'}}>
      {underId&&transition<1?<LiveScreen id={underId} state={underState} onReady={root=>focusPart(root,underFocusSelector)} style={{position:'absolute',inset:0,opacity:1-ease(transition,0,.35),transform:`translateX(${-30*direction*transition}px)`}}/>:null}
      <div style={{position:'absolute',inset:0,opacity:ui*(underId?ease(transition,.35,1):1),transform:`translateX(${underId?30*direction*(1-transition):0}px)`,clipPath:reveal<1?`inset(${(1-reveal)*50}%)`:undefined}}>
        {/* Visibility cues must remount the capture even while its displayed values hold. */}
        <LiveScreen id={id} state={{...state,_filmHideWidgets:hideWidgets,_filmHiddenParts:hiddenParts,_filmHiddenSelectors:hiddenSelectors}} onReady={root=>{
          focusPart(root,focusSelector);
          if(onParts){
            const parts:Geometry={};
            for(const node of root.querySelectorAll<HTMLElement>('[data-tool]'))if(node.dataset.tool){
              parts[node.dataset.tool]=measuredRect(node,root);
              const wheel=node.querySelector<HTMLElement>('.decision-wheel');
              if(wheel)parts[node.dataset.tool+'-face']=measuredRect(wheel,root);
            }
            onParts(parts);
          }
          if(hideWidgets)for(const node of root.querySelectorAll<HTMLElement>('.qt-live,.qt-breathe,.qt-colors'))node.style.visibility='hidden';
          for(const selector of hiddenSelectors){const node=root.querySelector<HTMLElement>(selector);if(!node)throw new Error(`Missing lifted widget: ${selector}`);node.style.visibility='hidden';}
          for(const key of hiddenParts){const node=root.querySelector<HTMLElement>(`[data-tool="${key}"]`);if(node)node.style.visibility='hidden';}
        }}/>
      </div>
      <Img src={staticFile('device/mask.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell,pointerEvents:'none'}}/>
    </div>
  </div>;
};

export const Headline:React.FC<{text:string;x:number;y:number;width?:number;size?:number;progress?:number;seconds?:number;align?:'left'|'center'}>=({text,x,y,width=760,size=TYPE.closingTitle.size,progress=1,seconds,align='left'})=><div data-headline style={{position:'absolute',left:x,top:y,width,color:INK,fontFamily:'Google Sans Flex',fontSize:size,lineHeight:1.04,fontWeight:size===TYPE.closingTitle.size?TYPE.closingTitle.weight:TYPE.closingSubtitle.weight,letterSpacing:-size*.045,textAlign:align,paddingBottom:size*.25}}>{text.split(' ').map((word,i)=>{
  const notes=(timeline.events.type as Record<string,number[]>)[text];
  const p=seconds!==undefined&&notes?(seconds>=timeline.events.close?ease(seconds,notes[i],notes[i]+.2):pop(seconds,notes[i],.34)):clamp(progress*1.5-i*.22);
  return <span key={i} style={{display:'inline-block',overflow:'hidden',verticalAlign:'top',paddingBottom:'.15em',marginBottom:'-.15em',marginRight:'.19em'}}><span style={{display:'inline-block',transform:`translate(${(1-clamp(p))*-24}px,${(1-p)*140}%) skewX(${9*(1-clamp(p))}deg)`,fontVariationSettings:`'wght' ${lerp(450,650,p)}, 'wdth' ${lerp(75,100,p)}`}}>{word}</span></span>;
})}</div>;
