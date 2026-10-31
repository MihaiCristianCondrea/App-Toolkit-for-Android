import React,{useCallback,useState} from 'react';
import {staticFile} from 'remotion';
import {Geometry,GeometryProbe,LiveCrop,WIDGETS,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT,isolatedRadii,radiusCss} from './Kit';
import {Phone,Shape,Toolbox,PhonePose,projectPhone,projectPart,CameraRig,View,mixView,KineticLine,beat,ease,easeIn,lerp,pop} from './visuals';
import timeline from './timeline.json';

const e=timeline.events;
const launch=e.launchPoses;
const names=['Compass','Bubble level','Flash Dimmer','Morse & SOS','Water Eject','Breathe'];
const gridPose=(g:Geometry,i:number,scale:number,cx:number,cy:number)=>{
  const a=g.compass,b=g.level,c=g.flash,d=g.sos;
  const left=Math.max(a.width,c.width),right=Math.max(b.width,d.width),top=Math.max(a.height,b.height),bottom=Math.max(c.height,d.height);
  return {x:cx+(i%2?(left+4)/2:-(right+4)/2)*scale,y:cy+(i<2?-(bottom+4)/2:(top+4)/2)*scale};
};
const connectedCorners=[[28,8,8,8],[8,28,8,8],[8,8,8,28],[8,8,28,8]];
const flightRadii=(t:number,g:Geometry,i:number)=>{
  const joined=ease(t,e.bentoJoin[0],e.bentoJoin[1]),pull=ease(t,e.pullBack,e.pullBackEnd);
  return isolatedRadii(g[WIDGETS[i].id],0).map((native,k)=>lerp(lerp(28,connectedCorners[i][k],joined),native,pull));
};

const IntroBackdrop:React.FC<{seconds:number}>=({seconds:t})=>{
  const turn=ease(t,e.lidOpen,e.chestLanding),exit=easeIn(t,e.gather,e.gatherEnd);
  return <div className="sc-part" data-theme="light" style={{position:'absolute',inset:0}}>{e.backgroundShapes.map((shape,i)=>{
    const x=lerp(shape.from[0],shape.to[0],turn),y=lerp(shape.from[1],shape.to[1],turn);
    return t>=e.backdropNotes[i]?<Shape key={shape.file} name="" mask={staticFile('shapes/'+shape.file)} color={shape.color} size={shape.size} x={x+(x<960?-1100:1100)*exit} y={y} scale={pop(t,e.backdropNotes[i],.42)*(1+.09*beat(t,'intro-stage'))} angle={lerp(shape.from[2],shape.to[2],turn)+8*beat(t,'intro-stage')} seconds={t}/>:null;
  })}</div>;
};

/** Four cards use separate flight lanes and restore the measured screen geometry. */
export const cardPose=(t:number,g:Geometry,i:number)=>{
  const rect=g[WIDGETS[i].id],at=e.launches[i],u=ease(t,at,at+.3);
  const gather=ease(t,e.gather,e.gatherEnd),pull=ease(t,e.pullBack,e.pullBackEnd);
  const grid=gridPose(g,i,1.3,1300,540);
  const x0=lerp(e.launchOrigin.x,launch[i].x,ease(t,at,at+.24));
  const y0=lerp(e.launchOrigin.y+rect.height*.15,launch[i].y,ease(t,at+.02,at+.3))-Math.sin(u*Math.PI)*65;
  const px=1300+(-SKIN_WIDTH/2+DISPLAY.x+rect.x+rect.width/2),py=540+(-SKIN_HEIGHT/2+DISPLAY.y+rect.y+rect.height/2);
  return {x:lerp(lerp(x0,grid.x,gather),px,pull),y:lerp(lerp(y0,grid.y,gather),py,pull),scale:lerp(lerp(lerp(.3,1.35,ease(t,at,at+.24)),1.3,gather),1,pull)*(t<e.gather&&i===e.launches.filter(at=>t>=at).length-1?1+.025*beat(t,'hero'):1),flight:u,angle:0};
};
const launchView=(t:number,g:Geometry,i:number):View=>{
  const rect=g[WIDGETS[i].id],face=e.launchFraming[i].kind==='face'?g[WIDGETS[i].id+'-face']:rect;
  const pose=cardPose(t,g,i),point=projectPart(face,rect,pose),s=e.launchFraming[i].height/(face.height*1.35);
  return {x:point.x-(e.launchFraming[i].screenX-960)/s,y:point.y,scale:s,angle:e.launchFraming[i].angle};
};
export const openingView=(t:number,g:Geometry|null):View=>{
  const icon={x:960,y:500,scale:.84,angle:0},box={x:1280,y:680,scale:1.45,angle:-3},wide={x:960,y:540,scale:1.05,angle:0};
  if(t<e.lidOpen)return {...icon,scale:t<e.logo?lerp(.98,1.08,ease(t,0,e.logo)):lerp(1.08,.84,ease(t,e.logo,e.lidOpen))};
  if(t<e.launches[0])return mixView(icon,box,ease(t,e.lidSettled,e.chestLanding));
  if(t<e.launchWide&&g){
    const i=Math.max(0,e.launches.filter(at=>t>=at).length-1),at=e.launches[i];
    const view=mixView(box,launchView(t,g,i),ease(t,at,at+e.shots.find(s=>s.at===at)!.settle));
    const next=e.launches[i+1];
    return next?mixView(view,box,ease(t,next-.12,next)):view;
  }
  if(t<e.gather)return mixView(g?launchView(e.launchWide,g,3):box,wide,ease(t,e.launchWide,e.launchWideEnd));
  const gathered={x:1080,y:540,scale:1,angle:-1};
  if(t<e.bentoPush)return mixView(wide,gathered,ease(t,e.gather,e.gatherEnd));
  const close={x:1300,y:540,scale:1.85,angle:0};
  return t<e.pullBack?mixView(gathered,close,ease(t,e.bentoPush,e.pullBack)):mixView(close,{x:1300,y:560,scale:1.65,angle:0},ease(t,e.pullBack,e.quickStart));
};
/** Check every frame using the same measured boxes and paths used for drawing. */
const checkFlights=(g:Geometry)=>{
  for(let frame=Math.ceil(e.launch*90);frame<=Math.floor(e.pullBackEnd*90);frame++){
    const t=frame/90,boxes=WIDGETS.slice(0,4).flatMap((w,i)=>{
      if(t<e.launches[i])return [];
      const p=cardPose(t,g,i),r=g[w.id];return [{...p,width:r.width*p.scale,height:r.height*p.scale,id:w.id}];
    });
    for(let i=0;i<boxes.length;i++)for(let j=i+1;j<boxes.length;j++){
      const a=boxes[i],b=boxes[j];
      if(Math.abs(a.x-b.x)<(a.width+b.width)/2-.5&&Math.abs(a.y-b.y)<(a.height+b.height)/2-.5)throw new Error(`Cards overlap at ${t}: ${JSON.stringify(a)}, ${JSON.stringify(b)}`);
    }
  }
  for(let i=0;i<4;i++){
    const alone=flightRadii(e.launches[i],g,i),joined=flightRadii(e.gatherEnd,g,i),landed=flightRadii(e.pullBackEnd,g,i);
    if(alone.some(r=>r!==28)||joined.some((r,k)=>r!==connectedCorners[i][k])||landed.some((r,k)=>r!==g[WIDGETS[i].id].radii![k]))throw new Error('Bento corner endpoints changed');
  }

};

export const quickPhonePose=(t:number):PhonePose=>{
  if(t<e.quickPhoneIn)return {x:lerp(1300,-1600,ease(t,e.quickStart,e.macroEntryEnd)),y:540,scale:1,angle:0};
  const p=ease(t,e.quickPhoneIn,e.quickPhoneLanded);
  return {x:lerp(2400,1310,p),y:540,scale:.94,angle:-4};
};
export const quickState=(t:number)=>{
  const d=e.demo,q=e.quickActions;
  let heading=lerp(312,405,ease(t,d.headingStart,d.headingEnd));
  if(t>=q.heading[0])heading=45+165*ease(t,q.heading[0],q.heading[1])-165*ease(t,q.heading[2],q.heading[3]);
  let fill=d.torchLevels[0];
  for(let i=0;i<d.torchHits.length;i++)fill=lerp(fill,d.torchLevels[i+1],ease(t,d.torchHits[i],d.torchHits[i]+.24));
  if(t>=q.torch[0]){
    fill=q.torchValues[0];
    q.torch.forEach((at,i)=>{fill=lerp(fill,q.torchValues[i+1],ease(t,at,at+.2));});
  }
  const sos=t>=d.sosStart&&t<e.gather||t>=q.sos[0]&&t<q.sos[1];
  return {compassHeading:Math.round(heading)%360,levelRoll:t>=q.level[0]?12*(1-ease(t,q.level[0],q.level[1])):4,levelPitch:t>=q.level[0]?0:2,flashOn:fill>.01,flashMaxLevel:5,flashLevel:Math.round(fill),sosActive:sos,_filmMotionSeconds:Math.max(0,t-(t>=q.sos[0]?q.sos[0]:d.sosStart)),_filmTorchFill:fill/5};
};
const macroPose=(t:number,g:Geometry,i:number):PhonePose=>{
  const rect=g[WIDGETS[i].id],from=projectPhone(rect,{x:1300,y:540,scale:1,angle:0},t),to=projectPhone(rect,quickPhonePose(t),t);
  const lift=ease(t,e.quickStart,e.macroEntryEnd),back=ease(t,e.quickReturn,e.quickReturnEnd);
  return {x:lerp(lerp(from.x,e.quickShots[i].railX,lift),to.x,back),y:lerp(lerp(from.y,540,lift),to.y,back),scale:lerp(lerp(from.scale,e.macroScale,lift),to.scale,back),angle:lerp(0,quickPhonePose(t).angle,back)};
};
const macroView=(t:number,g:Geometry,i:number):View=>{
  const shot=e.quickShots[i],rect=g[WIDGETS[i].id],face=shot.kind==='face'?g[WIDGETS[i].id+'-face']:rect;
  const pose=macroPose(t,g,i),point=projectPart(face,rect,pose),s=shot.height/(face.height*e.macroScale);
  return {x:point.x-(shot.screenX-960)/s,y:point.y,scale:s,angle:shot.angle};
};
export const quickView=(t:number,g:Geometry):View=>{
  if(t<e.quickReturn){
    const i=Math.max(0,e.quickShots.filter(s=>t>=s.at).length-1),shot=e.quickShots[i];
    const prior=i?macroView(shot.at,g,i-1):openingView(e.quickStart,g);
    const settle=e.shots.find(s=>s.at===shot.at)!.settle;
    const view=mixView(prior,macroView(t,g,i),ease(t,shot.at,shot.at+settle));
    return {...view,scale:view.scale*lerp(1,1.035,ease(t,shot.at+settle,shot.end))};
  }
  const wide={x:960,y:540,scale:1,angle:0};
  if(t<e.tabPush)return mixView(macroView(e.quickReturn,g,3),wide,ease(t,e.quickReturn,e.quickReturn+.65));
  const tab=projectPhone(g.Measure,quickPhonePose(t),t);
  return mixView(wide,{x:tab.x,y:tab.y,scale:3,angle:4},ease(t,e.tabPush,e.measure));
};
export const OpeningJourney:React.FC<{seconds:number;onGeometry?:(g:Geometry)=>void}>=({seconds:t,onGeometry})=>{
  const [g,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>{checkFlights(next);setGeometry(old=>old??next);onGeometry?.(next);},[onGeometry]);
  const pull=ease(t,e.pullBack,e.pullBackEnd),pose=quickPhonePose(t),state=quickState(t);
  const macros=t>=e.quickStart&&t<e.quickReturnEnd;
  const view=t<e.quickStart?openingView(t,g):g?quickView(t,g):{x:960,y:540,scale:1,angle:0};
  return <>
    <GeometryProbe onGeometry={measured}/>
    <CameraRig view={view}>
    {t>=e.gather&&t<e.quickStart?<Shape name="cookie_9sided" color="#D0E9E3" size={1150} x={1300} y={540} scale={pop(t,e.gather,.6)*(1+.065*beat(t,'stage'))} angle={-30+10*beat(t,'stage')} seconds={t}/>:null}
    {t<e.gatherEnd?<IntroBackdrop seconds={t}/>:null}
    {t>=e.logo&&t<e.pullBackEnd?(()=>{
      const arrive=pop(t,e.logo,.42)*(1-.13*ease(t,e.logo+.18,e.logo+.25)*(1-ease(t,e.logo+.25,e.logo+.42))),open=pop(t,e.lidOpen,e.lidSettled-e.lidOpen),land=ease(t,e.lidOpen,e.chestLanding),away=easeIn(t,e.gather,e.gatherEnd);
      const props={x:lerp(960,e.launchOrigin.x,land),y:lerp(500,620,land)+1000*away,size:lerp(840,410,land),open,scale:arrive*lerp(1,1.17,land),squash:beat(t,'toolbox')*.5+e.launches.reduce((sum,at)=>sum+.55*pop(t,at,.1)*(1-ease(t,at+.08,at+.2)),0),angle:lerp(-6,0,arrive)};
      return <><div style={{position:'absolute',inset:0,zIndex:5}}><Toolbox {...props} layer="lid"/></div><div style={{position:'absolute',inset:0,zIndex:30}}><Toolbox {...props} layer="body"/></div></>;
    })():null}
    {t>=e.pullBack?<Phone seconds={t} id="quick-tools" state={state} {...pose} shell={1} reveal={pull} hideWidgets={t<e.pullBackEnd} hiddenSelectors={macros?WIDGETS.slice(0,4).map(w=>w.selector):[]}/>:null}
    {g&&t<e.pullBackEnd?WIDGETS.slice(0,4).map((widget,i)=>{
      const at=e.launches[i],rect=g[widget.id],{x,y,scale,flight}=cardPose(t,g,i),radii=flightRadii(t,g,i);
      return t>=at?<div key={widget.id} data-flying-widget={widget.id} style={{position:'absolute',width:rect.width,height:rect.height,borderRadius:radiusCss(radii),transformOrigin:'50% 50%',transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) scale(${scale})`,zIndex:10+i,boxShadow:`0 ${lerp(2,18,flight)}px ${lerp(5,32,flight)}px rgba(59,45,20,.14)`}}><LiveCrop screen="quick-tools" selector={widget.selector} state={state} radii={radii}/></div>:null;
    }):null}
    {g&&macros?WIDGETS.slice(0,4).map((w,i)=>{
      const r=g[w.id],p=macroPose(t,g,i),join=ease(t,e.quickStart,e.macroEntryEnd)*(1-ease(t,e.quickReturn,e.quickReturnEnd)),radii=isolatedRadii(r,join);
      const punch=1+.025*beat(t,'hero');
      return <div key={w.id} data-macro-widget={w.id} style={{position:'absolute',width:r.width,height:r.height,borderRadius:radiusCss(radii),transformOrigin:'50% 50%',transform:`translate(${p.x-r.width/2}px,${p.y-r.height/2}px) rotate(${p.angle}deg) scale(${p.scale*punch})`,boxShadow:'0 16px 32px rgba(59,45,20,.14)'}}><LiveCrop screen="quick-tools" selector={w.selector} state={state} radii={radii}/></div>;
    }):null}
    </CameraRig>
    {t>=e.gather&&t<e.bentoPush+.3?<KineticLine seconds={t} text="One place." notes={e.type['One place.']} x={110} y={230} width={950} size={280} exit={ease(t,e.bentoPush,e.bentoPush+.3)}/>:null}
    {t>=e.launches[0]&&t<e.launchWide?(()=>{const i=e.launches.filter(at=>t>=at).length-1;return <KineticLine seconds={t} text={names[i]} notes={names[i].split(' ').map(()=>e.launches[i])} x={100} y={925} size={78} width={1700}/>;})():null}
    {t>=e.quickStart&&t<e.quickReturn?(()=>{
      const shot=[...e.quickShots].reverse().find(s=>t>=s.at)!;
      return <KineticLine seconds={t} text={names[shot.widget]} notes={names[shot.widget].split(' ').map(()=>shot.at)} x={100} y={925} size={78} width={1720}/>;
    })():null}
    {t>=e.quickReturn?<QuickCaption seconds={t}/>:null}
  </>;
};
export const QuickCaption:React.FC<{seconds:number;exit?:number}>=({seconds:t,exit=0})=><KineticLine seconds={t} text="Quick tools." notes={e.type['Quick tools.']} x={100} y={180} width={1000} size={260} exit={Math.max(exit,ease(t,e.tabPush,e.measure))}/>;
