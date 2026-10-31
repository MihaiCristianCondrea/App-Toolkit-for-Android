import React,{useCallback,useState} from 'react';
import {Geometry,GeometryProbe,LiveCrop,WIDGETS,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT} from './Kit';
import {Chip,Headline,Phone,Shape,Toolbox,PhonePose,projectPhone,beat,ease,easeIn,lerp,pop} from './visuals';
import timeline from './timeline.json';

const e=timeline.events;
const launch=[{x:350,y:290,angle:0},{x:1530,y:290,angle:0},{x:350,y:740,angle:0},{x:1530,y:740,angle:0}];
const iconPoses=[{x:500,y:300},{x:960,y:300},{x:1420,y:300},{x:500,y:750},{x:960,y:750},{x:1420,y:750}];
const symbols=[['explore','sunny'],['straighten','pill'],['flashlight_on','burst'],['emergency','cookie_7sided'],['water_drop','cookie_9sided'],['favorite','soft_burst']];
const names=['Compass','Bubble level','Flash Dimmer','Morse & SOS','Water Eject','Breathe'];
// Four tools leave the box: the frame holds four cards at a readable size, not the whole screen.
const bento=[{x:1220,y:330},{x:1590,y:330},{x:1220,y:750},{x:1590,y:750}];

/** Four cards use separate flight lanes and restore the measured screen geometry. */
const cardPose=(t:number,g:Geometry,i:number)=>{
  const rect=g[WIDGETS[i].id],at=e.launches[i],u=ease(t,at,at+.46);
  const gather=ease(t,e.gather,e.gatherEnd),pull=ease(t,e.pullBack,e.pullBackEnd),push=ease(t,e.bentoPush,e.bentoPush+.5);
  const cameraScale=e.cameraMoves.find(move=>move.at===e.bentoPush)!.to[0];
  const height=Math.max(...WIDGETS.slice(0,4).map(w=>g[w.id].height));
  const closeScale=Math.min(1.55,((1080-2*96)/cameraScale-40)/(2*height));
  const bx=lerp(bento[i].x,i%2?1270:650,push),by=lerp(bento[i].y,540+(i<2?-1:1)*(height*closeScale+40)/2,push);
  const x0=lerp(960,launch[i].x,u),y0=lerp(698+rect.height*.15,launch[i].y,u)-Math.sin(u*Math.PI)*60-8*beat(t,`opening-${i}`);
  const px=960+(-SKIN_WIDTH/2+DISPLAY.x+rect.x+rect.width/2)*.82;
  const py=540+(-SKIN_HEIGHT/2+DISPLAY.y+rect.y+rect.height/2)*.82;
  return {x:lerp(lerp(x0,bx,gather),px,pull),y:lerp(lerp(y0,by,gather),py,pull),scale:lerp(lerp(lerp(.3,1.25,u),lerp(1.3,closeScale,push),gather),.82,pull),flight:u};
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
      if(Math.abs(a.x-b.x)<(a.width+b.width)/2-.5&&Math.abs(a.y-b.y)<(a.height+b.height)/2-.5)throw new Error(`Cards overlap at ${t}: ${a.id}, ${b.id}`);
    }
  }
};
export const quickPhonePose=(t:number):PhonePose=>{
  const p=ease(t,e.featureMove,e.shellBuildEnd);
  return {x:lerp(960,1360,p),y:lerp(540,560,p),scale:lerp(.82,.78,p),angle:-4*p};
};
export const quickState=(t:number)=>{
  const d=e.demo;
  const heading=lerp(312,405,ease(t,d.headingStart,d.headingEnd))+90*ease(t,d.heroTurn,d.heroTurnEnd)*(1-ease(t,d.heroReturn,d.heroReturnEnd));
  return {compassHeading:Math.round(heading)%360,levelRoll:4*(1-ease(t,e.launches[3],d.headingEnd))+4*ease(t,d.levelTilt,d.levelTiltEnd)*(1-ease(t,d.levelSettle,d.levelEnd)),levelPitch:2*(1-ease(t,e.launches[3],d.headingEnd)),flashOn:true,flashMaxLevel:5,flashLevel:t<d.torchHits[0]?3:t<d.torchHits[1]?5:t<d.torchHits[2]?1:3,sosActive:t<e.gather&&d.sosHits.filter(at=>t>=at).length%2===1};
};
export const OpeningJourney:React.FC<{seconds:number;onGeometry?:(g:Geometry)=>void}>=({seconds:t,onGeometry})=>{
  const [g,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>{checkFlights(next);setGeometry(old=>old??next);onGeometry?.(next);},[onGeometry]);
  const gather=ease(t,e.gather,e.gatherEnd),pull=ease(t,e.pullBack,e.pullBackEnd);
  const pose=quickPhonePose(t),phoneX=pose.x;
  const state=quickState(t);
  const lifted=[0,1].find(i=>t>=e.quickHeroIn[i]&&t<e.quickHeroEnd[i]);
  return <>
    <GeometryProbe onGeometry={measured}/>
    {/* One stage per subject: it grows behind the gathered cards and carries them into the phone. */}
    {t>=e.gather?<Shape name="cookie_9sided" color="#D0E9E3" size={1000} x={lerp(lerp(1405,960,ease(t,e.bentoPush,e.bentoPush+.5)),phoneX,pull)} y={540} scale={pop(t,e.gather,.6)*lerp(1,.95,pull)*(1+.065*beat(t,'stage'))*(1-ease(t,e.quickHeroOut[1],e.measure))} angle={lerp(-30,0,ease(t,e.gather,e.gatherEnd))+10*beat(t,'stage')} seconds={t}/>:null}
    {t<e.launch?symbols.map(([icon,shape],i)=>{
      const at=e.iconNotes[i],p=pop(t,at,.35),exit=easeIn(t,e.logo,e.iconsExitEnd);
      const spread=ease(t,e.iconNotes[1],e.iconNotes[2]);
      const x=lerp(960,iconPoses[i].x,spread),y=lerp(540,iconPoses[i].y,spread);
      const size=lerp(680,300,spread),glyph=size*.46;
      return t>=at?<div key={icon} style={{position:'absolute',inset:0,transform:`translate(${(960-x)*exit}px,${(698-y)*exit+(1-p)*900}px) scale(${1-exit})`,transformOrigin:`${x}px ${y}px`}}>
        <Shape name={shape} color="#FDE7A2" size={size} x={x} y={y} angle={lerp(-25,0,p)}/>
        <span className="material-symbols-outlined" style={{position:'absolute',left:x-glyph/2,top:y-glyph/2,fontSize:glyph,lineHeight:1,color:'#3B2D14'}}>{icon}</span>
      </div>:null;
    }):null}
    {t>=e.logo&&t<e.pullBackEnd?(()=>{
      const arrive=pop(t,e.logo,.48),split=Math.min(1,pop(t,e.logo,.35)),land=pop(t,e.logo,.58);
      const recoil=beat(t,'toolbox')*.5,away=easeIn(t,e.gather,e.gatherEnd);
      const anticipate=ease(t,e.logo,e.logo+.08),overshoot=pop(t,e.logo+.08,.36);
      return <div style={{position:'absolute',inset:0,zIndex:30}}><Toolbox x={960} y={lerp(540,620,land)+600*away} size={410} open={split} scale={lerp(1.7,.65,anticipate)+.52*overshoot} squash={recoil} angle={lerp(-8,0,arrive)}/></div>;
    })():null}
    {t>=e.pullBack?<Phone seconds={t} id="quick-tools" state={state} x={pose.x} y={pose.y} scale={pose.scale*lerp(1.12,1,pull)} angle={pose.angle} shell={pull} reveal={pull} hideWidgets={t<e.pullBackEnd} hiddenSelectors={lifted===undefined?[]:[WIDGETS[lifted].selector]}/>:null}
    {g&&lifted!==undefined?(()=>{
      const rect=g[WIDGETS[lifted].id],source=projectPhone(rect,pose,t);
      const p=ease(t,e.quickHeroIn[lifted],e.quickHeroIn[lifted]+.36)*(1-ease(t,e.quickHeroOut[lifted],e.quickHeroEnd[lifted]));
      return <div data-quick-hero={WIDGETS[lifted].id} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${lerp(source.x,500,p)-rect.width/2}px,${lerp(source.y,700,p)+120*Math.sin(p*Math.PI)-rect.height/2}px) rotate(${lerp(pose.angle,lifted?4:-5,p)}deg) scale(${lerp(source.scale,1.8,p)})`,boxShadow:`0 ${18*p}px ${36*p}px rgba(59,45,20,${.14*p})`}}><LiveCrop screen="quick-tools" selector={WIDGETS[lifted].selector} state={state} style={{borderRadius:28}}/></div>;
    })():null}
    {g&&t<e.pullBackEnd?WIDGETS.slice(0,4).map((widget,i)=>{
      const at=e.launches[i],rect=g[widget.id];
      const {x,y,scale,flight}=cardPose(t,g,i);
      const radius=(rect.radii??[28,28,28,28]).map(r=>`${lerp(28,r,gather)}px`).join(' ');
      return t>=at?<div key={widget.id} data-flying-widget={widget.id} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) scale(${scale})`,zIndex:10+i,boxShadow:`0 ${lerp(2,18,flight)}px ${lerp(5,32,flight)}px rgba(59,45,20,.14)`}}>
        <LiveCrop screen="quick-tools" selector={widget.selector} state={{...state,searchQuery:widget.query}} style={{borderRadius:radius}}/>
      </div>:null;
    }):null}
    {t>=e.gather&&t<e.pullBackEnd?<div style={{transform:`translateX(${-1100*ease(t,e.bentoPush,e.bentoPush+.5)}px)`}}><Headline seconds={t} text="One place." x={230} y={330} width={630} size={170} progress={ease(t,e.gather,e.gather+.4)}/></div>:null}
    {t>=e.featureMove?<QuickCaption seconds={t}/>:null}
  </>;
};

export const QuickCaption:React.FC<{seconds:number;exit?:number}>=({seconds:t,exit=0})=>{
  const latest=e.chips.filter(at=>t>=at).length-1;
  return <div style={{position:'absolute',inset:0,transform:`translateX(${-1800*exit}px)`}}>
      <Headline seconds={t} text="Quick tools." x={180} y={120} width={800} size={132} progress={ease(t,e.featureMove,e.shellBuildEnd)}/>
      <div style={{position:'absolute',left:185,top:295,width:800,fontSize:40,lineHeight:1.25,color:'#006A60',fontWeight:500,overflow:'hidden'}}><div style={{transform:`translateY(${(1-ease(t,e.quickHeroIn[0],e.quickHeroIn[0]+.3))*150}px)`}}>Find your way. Find your level.</div></div>
      <div data-tool-chips style={{position:'absolute',left:180,top:350,width:800,height:70,overflow:'hidden'}}>
        <div style={{display:'flex',gap:20,width:1800,transform:`translateX(${-e.chips.slice(1).reduce((sum,at)=>sum+300*ease(t,at,at+.28),0)}px) translateY(${100*(1-ease(t,e.quickHeroIn[0],e.quickHeroIn[0]+.3))}px)`}}>
          {names.map((text,i)=><div key={text} style={{flex:'0 0 280px'}}><Chip text={text} color={i===latest?'#D0E9E3':'rgba(59,45,20,.07)'} ink={i===latest?'#00504A':'#3B2D14'} progress={1} active={i===latest?beat(t):0}/></div>)}
        </div>
      </div>
  </div>;
};
