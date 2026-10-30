import React,{useCallback,useState} from 'react';
import {Geometry,GeometryProbe,LiveCrop,WIDGETS,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT} from './Kit';
import {Chip,Headline,Phone,Shape,Toolbox,beat,ease,easeIn,lerp,pop} from './visuals';
import timeline from './timeline.json';

const e=timeline.events;
const launch=[{x:400,y:260,angle:-9},{x:930,y:235,angle:5},{x:1460,y:270,angle:9},{x:1500,y:710,angle:-6},{x:420,y:720,angle:-5},{x:950,y:720,angle:4},{x:960,y:1000,angle:0}];
const symbols=[['explore','sunny'],['straighten','pill'],['flashlight_on','burst'],['emergency','cookie_7sided'],['water_drop','cookie_9sided'],['favorite','soft_burst']];
const names=['Compass','Bubble level','Flash Dimmer','Morse & SOS','Water Eject','Breathe'];
// Four tools leave the box: the frame holds four cards at a readable size, not the whole screen.
const bento=[{x:1100,y:330},{x:1420,y:330},{x:1100,y:750},{x:1420,y:750}];

/** The same seven measured cards travel from launcher to full bento to phone. */
export const OpeningJourney:React.FC<{seconds:number;onGeometry?:(g:Geometry)=>void}>=({seconds:t,onGeometry})=>{
  const [g,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>{setGeometry(old=>old??next);onGeometry?.(next);},[onGeometry]);
  const gather=ease(t,e.gather,e.gatherEnd),pull=ease(t,e.pullBack,e.pullBackEnd);
  const move=ease(t,e.featureMove,e.shellBuildEnd);
  const phoneScale=lerp(1,.92,move),phoneX=lerp(960,1320,move);
  const sensor=ease(t,e.sensorMove,e.chips[3]);
  const heading=Math.round(lerp(312,405,sensor))%360;
  return <>
    <GeometryProbe onGeometry={measured}/>
    {/* One stage per subject: it grows behind the gathered cards and carries them into the phone. */}
    {t>=e.gather?<Shape name="cookie_9sided" color="#D0E9E3" size={1000} x={lerp(1260,phoneX,pull)} y={540} scale={pop(t,e.gather,.6)*lerp(1,.95,pull)} angle={lerp(-30,0,ease(t,e.gather,e.gatherEnd))}/>:null}
    {t<e.launch?symbols.map(([icon,shape],i)=>{
      const at=e.iconNotes[i],p=pop(t,at,.35),exit=easeIn(t,e.logo,e.lidOpen);
      const spread=ease(t,e.iconNotes[1],e.iconNotes[4]);
      const x=lerp(960,launch[i].x,spread),y=lerp(540,launch[i].y,spread);
      const size=lerp(680,300,spread),glyph=size*.46;
      return t>=at?<div key={icon} style={{position:'absolute',inset:0,transform:`translate(${(960-x)*exit}px,${(1000-y)*exit+(1-p)*900}px) scale(${1-.94*exit})`,transformOrigin:`${x}px ${y}px`}}>
        <Shape name={shape} color="#FDE7A2" size={size} x={x} y={y} angle={lerp(-25,0,p)}/>
        <span className="material-symbols-outlined" style={{position:'absolute',left:x-glyph/2,top:y-glyph/2,fontSize:glyph,lineHeight:1,color:'#3B2D14'}}>{icon}</span>
      </div>:null;
    }):null}
    {t>=e.logo&&t<e.pullBackEnd?(()=>{
      const arrive=pop(t,e.logo,.48),split=Math.min(1,pop(t,e.logo,.35)),land=pop(t,e.logo,.58);
      const recoil=beat(t)*(t>=e.launch?.5:0),away=easeIn(t,e.gather,e.gatherEnd);
      const anticipate=ease(t,e.logo,e.logo+.08),overshoot=pop(t,e.logo+.08,.36);
      return <Toolbox x={960} y={lerp(540,620,land)+600*away} size={410} open={split} scale={lerp(1.7,.65,anticipate)+.52*overshoot} squash={recoil} angle={lerp(-8,0,arrive)}/>;
    })():null}
    {t>=e.pullBack?<Phone id="quick-tools" state={{compassHeading:heading,levelRoll:lerp(4,0,sensor),levelPitch:lerp(2,0,sensor)}} x={phoneX} y={540} scale={phoneScale*lerp(1.12,1,pull)} angle={-4*move} shell={pull} ui={1} reveal={pull} revealFrom={{x:215,y:480}} hideWidgets={t<e.pullBackEnd}/>:null}
    {g&&t<e.pullBackEnd?WIDGETS.slice(0,4).map((widget,i)=>{
      const at=i===6?e.colors:e.launches[i],rect=g[widget.id],pose=launch[i];
      const flight=ease(t,at,at+.42),p=pop(t,at,.52);
      const spread=ease(t,e.launches[Math.min(i+1,5)],e.launches[Math.min(i+1,5)]+.4);
      const x0=lerp(960,lerp(960,pose.x,spread),p),y0=lerp(960,lerp(480,pose.y,spread),flight)-Math.sin(flight*Math.PI)*210;
      const gx=bento[i].x,gy=bento[i].y;
      const px=phoneX-SKIN_WIDTH*phoneScale/2+DISPLAY.x*phoneScale+(rect.x+rect.width/2)*phoneScale;
      const py=540-SKIN_HEIGHT*phoneScale/2+DISPLAY.y*phoneScale+(rect.y+rect.height/2)*phoneScale;
      const x=lerp(lerp(x0,gx,gather),px,pull),y=lerp(lerp(y0,gy,gather),py,pull);
      const scale=lerp(lerp(lerp(1.6,i===4?1.05:1.38,spread),1.5,gather),phoneScale,pull);
      const radius=(rect.radii??[28,28,28,28]).map(r=>`${lerp(28,r,gather)}px`).join(' ');
      return t>=at?<div key={widget.id} data-flying-widget={widget.id} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) rotate(${pose.angle*(1-gather)}deg) scale(${scale})`,zIndex:10+i}}>
        <LiveCrop screen="quick-tools" selector={widget.selector} state={{searchQuery:widget.query,compassHeading:312,levelRoll:4,levelPitch:2}} style={{borderRadius:radius}}/>
      </div>:null;
    }):null}
    {t>=e.gather&&t<e.pullBackEnd?<div style={{transform:`translateX(${-1100*pull}px)`}}><Headline seconds={t} text="One place." x={130} y={330} width={700} size={170} progress={ease(t,e.gather,e.gather+.4)}/></div>:null}
    {t>=e.featureMove?<QuickCaption seconds={t}/>:null}
  </>;
};

export const QuickCaption:React.FC<{seconds:number;exit?:number}>=({seconds:t,exit=0})=>{
  const latest=e.chips.filter(at=>t>=at).length-1;
  return <div style={{position:'absolute',inset:0,transform:`translateX(${-1800*exit}px)`}}>
      <Headline seconds={t} text="Quick tools." x={130} y={240} width={720} size={152} progress={ease(t,e.featureMove,e.shellBuildEnd)}/>
      <div style={{position:'absolute',left:140,top:620,width:700,fontSize:52,lineHeight:1.25,color:'#006A60',fontWeight:500,overflow:'hidden'}}><div style={{transform:`translateY(${(1-ease(t,e.sensorMove,e.chips[0]))*300}px)`}}>Find your way.<br/>Find your level.</div></div>
      <div data-tool-chips style={{position:'absolute',left:130,top:820,width:780,display:'flex',flexWrap:'wrap',gap:14,overflow:'hidden'}}>
        {names.map((text,i)=>t>=e.chips[i]?<Chip key={text} text={text} color={i===latest?'#D0E9E3':'rgba(59,45,20,.07)'} ink={i===latest?'#00504A':'#3B2D14'} progress={pop(t,e.chips[i],.35)} active={i===latest?beat(t):0}/>:null)}
      </div>
  </div>;
};
