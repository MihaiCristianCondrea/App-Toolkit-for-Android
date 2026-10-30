import React,{useCallback,useState} from 'react';
import {Geometry,GeometryProbe,LiveCrop,WIDGETS,DISPLAY,PHONE_WIDTH,SKIN_WIDTH,SKIN_HEIGHT} from './Kit';
import {Headline,Phone,Shape,Toolbox,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';

const launch=[
  {x:480,y:300,scale:1.08,angle:-12},
  {x:920,y:255,scale:1.08,angle:8},
  {x:1395,y:325,scale:1.08,angle:12},
  {x:1450,y:770,scale:1.08,angle:-8},
  {x:470,y:785,scale:.9,angle:-7},
  {x:1010,y:770,scale:1.12,angle:7},
];
const e=timeline.events;

/** One continuous shot carries the same card coordinates from the toolbox into the real screen. */
export const OpeningJourney:React.FC<{seconds:number;onGeometry?:(g:Geometry)=>void}>=({seconds:t,onGeometry})=>{
  const [g,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>{setGeometry(old=>old??next);onGeometry?.(next);},[onGeometry]);
  const open=ease(t,e.lidOpen,e.lidOpen+.85);
  const gather=ease(t,e.gather,e.gatherEnd);
  const pull=ease(t,e.pullBack,e.pullBackEnd);
  const move=ease(t,e.featureMove,14.6);
  const phoneScale=lerp(lerp(1.25,.86,pull),.92,move);
  const phoneX=lerp(960,1320,move);
  const nativeTop=g?.compass.y??0;
  const initialPhoneY=70-nativeTop*1.25-DISPLAY.y*1.25+SKIN_HEIGHT*1.25/2;
  const phoneY=lerp(initialPhoneY,540,pull);
  const screenX=phoneX-SKIN_WIDTH*phoneScale/2+DISPLAY.x*phoneScale;
  const screenY=phoneY-SKIN_HEIGHT*phoneScale/2+DISPLAY.y*phoneScale;
  const sensor=ease(t,e.sensorMove,15.66);
  const heading=Math.round(lerp(312,405,sensor))%360;
  return <>
    <GeometryProbe onGeometry={measured}/>
    {t<e.pullBack?<>
      <Shape name="cookie_9sided" color="#FDE7A2" size={480} x={80-100*gather} y={70} angle={t*4} scale={pop(t,.1)}/>
      <Shape name="flower" color="#E8D3DC" size={320} x={1790+200*gather} y={220} angle={-t*5} scale={pop(t,.2)}/>
      <Shape name="clover_4leaf" color="#D0E9E3" size={450} x={80-200*gather} y={1050} angle={12+t*3} scale={pop(t,.3)}/>
      <Shape name="soft_burst" color="#FFE5C6" size={400} x={1840+200*gather} y={1010} angle={-12-t*3} scale={pop(t,.4)}/>
    </>:null}
    {t<e.gatherEnd?<Toolbox x={960} y={lerp(480,760,ease(t,e.launch-.3,e.launch+.7))+ease(t,e.gather,e.gatherEnd)*500} size={400} open={open} scale={pop(t,.05,.9)*(1-.85*ease(t,e.gather,e.gatherEnd))} angle={-4*Math.sin(t*2)*ease(t,1.7,2.1)*(1-gather)}/>:null}
    {t>=e.pullBack?<Phone id="quick-tools" state={{compassHeading:heading,levelRoll:lerp(4,0,sensor),levelPitch:lerp(2,0,sensor)}} x={phoneX} y={phoneY} scale={phoneScale} angle={-4*move} shell={pull} ui={ease(t,e.pullBackEnd,e.shellBuildEnd)} hideWidgets={t<e.shellBuildEnd}/>:null}
    {g&&t<e.shellBuildEnd?WIDGETS.map((widget,i)=>{
      const rect=g[widget.id],pose=launch[i];
      const p=pop(t,e.launch+i*e.launchStagger,.8);
      const flight=ease(t,e.launch+i*e.launchStagger,e.launch+i*e.launchStagger+.8);
      const x0=lerp(960,pose.x,p);
      const y0=lerp(710,pose.y,flight)-Math.sin(flight*Math.PI)*200;
      const targetX=screenX+(rect.x+rect.width/2)*phoneScale;
      const targetY=screenY+(rect.y+rect.height/2)*phoneScale;
      const x=lerp(x0,targetX,gather),y=lerp(y0,targetY,gather);
      const scale=lerp(pose.scale*Math.max(.01,p),phoneScale,gather);
      const angle=pose.angle*flight*(1-gather);
      const radius=(rect.radii??[28,28,28,28]).map(r=>`${lerp(28,r,gather)}px`).join(' ');
      return t>=e.launch+i*e.launchStagger?<div key={widget.id} data-flying-widget={widget.id} style={{position:'absolute',left:0,top:0,width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) rotate(${angle}deg) scale(${scale})`,zIndex:10+i}}>
        <LiveCrop screen="quick-tools" selector={widget.selector} state={{searchQuery:widget.query,compassHeading:312,levelRoll:0,levelPitch:0}} style={{borderRadius:radius}}/>
      </div>:null;
    }):null}
    {t>=e.launch&&t<e.gather?<Headline text="Many tools." x={610} y={925} width={700} size={100} align="center" progress={ease(t,4.92,5.16)}/>:null}
    {t>=e.gather&&t<e.pullBack?<Headline text="One place." x={80} y={440} width={540} size={132} progress={ease(t,7.46,7.7)}/>:null}
    {t>=e.featureMove?<>
      <Shape name="cookie_9sided" color="#D0E9E3" size={560} x={280} y={650} angle={lerp(-20,0,ease(t,13.72,14.6))}/>
      <Headline text="Quick tools." x={130} y={240} width={720} size={152} progress={ease(t,13.72,14.28)}/>
      <div style={{position:'absolute',left:140,top:670,fontSize:44,lineHeight:1.25,color:'#006A60',fontWeight:500,opacity:ease(t,14.28,14.6)}}>Find your way.<br/>Find your level.</div>
    </>:null}
  </>;
};
