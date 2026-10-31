import React,{useCallback,useState} from 'react';
import {staticFile} from 'remotion';
import {Geometry,GeometryProbe,LiveCrop,WIDGETS,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT,isolatedRadii,radiusCss} from './Kit';
import {Chip,Headline,Phone,Shape,Toolbox,PhonePose,projectPhone,beat,ease,easeIn,lerp,pop} from './visuals';
import timeline from './timeline.json';

const e=timeline.events;
const launch=[{x:350,y:290,angle:0},{x:1530,y:290,angle:0},{x:350,y:740,angle:0},{x:1530,y:740,angle:0}];
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
    return t>=e.backdropNotes[i]?<Shape key={shape.file} name="" mask={staticFile('shapes/'+shape.file)} color={shape.color} size={shape.size} x={x+(x<960?-1100:1100)*exit} y={y} scale={pop(t,e.backdropNotes[i],.42)} angle={lerp(shape.from[2],shape.to[2],turn)} seconds={t}/>:null;
  })}</div>;
};

/** Four cards use separate flight lanes and restore the measured screen geometry. */
const cardPose=(t:number,g:Geometry,i:number)=>{
  const rect=g[WIDGETS[i].id],at=e.launches[i],u=ease(t,at,at+.46);
  const gather=ease(t,e.gather,e.gatherEnd),pull=ease(t,e.pullBack,e.pullBackEnd),push=ease(t,e.bentoPush,e.bentoPush+.5);
  const cameraScale=e.cameraMoves.find(move=>move.at===e.bentoPush)!.to[0];
  const height=Math.max(g.compass.height,g.level.height)+Math.max(g.flash.height,g.sos.height)+4;
  const closeScale=Math.min(1.55,(1080-2*96)/(cameraScale*height));
  const scale=lerp(1.3,closeScale,push),grid=gridPose(g,i,scale,lerp(1405,960,push),540);
  const x0=lerp(960,launch[i].x,u),y0=lerp(698+rect.height*.15,launch[i].y,u)-Math.sin(u*Math.PI)*60-8*beat(t,`opening-${i}`);
  const px=960+(-SKIN_WIDTH/2+DISPLAY.x+rect.x+rect.width/2)*.82;
  const py=540+(-SKIN_HEIGHT/2+DISPLAY.y+rect.y+rect.height/2)*.82;
  return {x:lerp(lerp(x0,grid.x,gather),px,pull),y:lerp(lerp(y0,grid.y,gather),py,pull),scale:lerp(lerp(lerp(.3,1.25,u),scale,gather),.82,pull),flight:u};
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
  for(let i=0;i<4;i++){
    const alone=flightRadii(e.launches[i],g,i),joined=flightRadii(e.gatherEnd,g,i),landed=flightRadii(e.pullBackEnd,g,i);
    if(alone.some(r=>r!==28)||joined.some((r,k)=>r!==connectedCorners[i][k])||landed.some((r,k)=>r!==g[WIDGETS[i].id].radii![k]))throw new Error('Bento corner endpoints changed');
  }

};
export const quickPhonePose=(t:number):PhonePose=>{
  const p=ease(t,e.featureMove,e.shellBuildEnd);
  return {x:lerp(960,1360,p),y:lerp(540,560,p),scale:lerp(.82,.78,p),angle:-4*p};
};
export const quickState=(t:number)=>{
  const d=e.demo;
  const heading=lerp(312,405,ease(t,d.headingStart,d.headingEnd))+90*ease(t,d.heroTurn,d.heroTurnEnd)*(1-ease(t,d.heroReturn,d.heroReturnEnd));
  let fill=d.torchLevels[0];
  for(let i=0;i<d.torchHits.length;i++)fill=lerp(fill,d.torchLevels[i+1],ease(t,d.torchHits[i],d.torchHits[i]+.24));
  return {compassHeading:Math.round(heading)%360,levelRoll:4*(1-ease(t,e.launches[3],d.headingEnd))+4*ease(t,d.levelTilt,d.levelTiltEnd)*(1-ease(t,d.levelSettle,d.levelEnd)),levelPitch:2*(1-ease(t,e.launches[3],d.headingEnd)),flashOn:fill>.01,flashMaxLevel:5,flashLevel:Math.round(fill),sosActive:t>=d.sosStart&&t<e.gather,_filmMotionSeconds:Math.max(0,t-d.sosStart),_filmTorchFill:fill/5};
};
export const OpeningJourney:React.FC<{seconds:number;onGeometry?:(g:Geometry)=>void}>=({seconds:t,onGeometry})=>{
  const [g,setGeometry]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>{checkFlights(next);setGeometry(old=>old??next);onGeometry?.(next);},[onGeometry]);
  const pull=ease(t,e.pullBack,e.pullBackEnd);
  const pose=quickPhonePose(t),phoneX=pose.x;
  const state=quickState(t);
  const lifted=[0,1].find(i=>t>=e.quickHeroIn[i]&&t<e.quickHeroEnd[i]);
  return <>
    <GeometryProbe onGeometry={measured}/>
    {/* One stage per subject: it grows behind the gathered cards and carries them into the phone. */}
    {t>=e.gather?<Shape name="cookie_9sided" color="#D0E9E3" size={1000} x={lerp(lerp(1405,960,ease(t,e.bentoPush,e.bentoPush+.5)),phoneX,pull)} y={540} scale={pop(t,e.gather,.6)*lerp(1,.95,pull)*(1+.065*beat(t,'stage'))*(1-ease(t,e.quickHeroOut[1],e.measure))} angle={lerp(-30,0,ease(t,e.gather,e.gatherEnd))+10*beat(t,'stage')} seconds={t}/>:null}
    {t<e.gatherEnd?<IntroBackdrop seconds={t}/>:null}
    {t>=e.logo&&t<e.pullBackEnd?(()=>{
      const arrive=pop(t,e.logo,.42),open=pop(t,e.lidOpen,e.lidSettled-e.lidOpen),land=ease(t,e.lidOpen,e.chestLanding),away=easeIn(t,e.gather,e.gatherEnd);
      const anticipate=1-.08*ease(t,e.logoAnticipation,e.lidOpen)*(1-ease(t,e.lidOpen,e.lidSettled));
      const props={x:960,y:lerp(520,620,land)+1000*away,size:lerp(620,410,land),open,scale:arrive*anticipate*lerp(1,1.17,land),squash:beat(t,'toolbox')*.5,angle:lerp(-6,0,arrive)};
      return <><div style={{position:'absolute',inset:0,zIndex:5}}><Toolbox {...props} layer="lid"/></div><div style={{position:'absolute',inset:0,zIndex:30}}><Toolbox {...props} layer="body"/></div></>;
    })():null}
    {t>=e.pullBack?<Phone seconds={t} id="quick-tools" state={state} x={pose.x} y={pose.y} scale={pose.scale*lerp(1.12,1,pull)} angle={pose.angle} shell={pull} reveal={pull} hideWidgets={t<e.pullBackEnd} hiddenSelectors={lifted===undefined?[]:[WIDGETS[lifted].selector]}/>:null}
    {g&&lifted!==undefined?(()=>{
      const rect=g[WIDGETS[lifted].id],source=projectPhone(rect,pose,t);
      const p=ease(t,e.quickHeroIn[lifted],e.quickHeroIn[lifted]+.36)*(1-ease(t,e.quickHeroOut[lifted],e.quickHeroEnd[lifted]));
      const radii=isolatedRadii(rect,p);
      return <div data-quick-hero={WIDGETS[lifted].id} style={{position:'absolute',width:rect.width,height:rect.height,borderRadius:radiusCss(radii),transformOrigin:'50% 50%',transform:`translate(${lerp(source.x,500,p)-rect.width/2}px,${lerp(source.y,700,p)+120*Math.sin(p*Math.PI)-rect.height/2}px) rotate(${lerp(pose.angle,lifted?4:-5,p)}deg) scale(${lerp(source.scale,1.8,p)})`,boxShadow:`0 ${18*p}px ${36*p}px rgba(59,45,20,${.14*p})`}}><LiveCrop screen="quick-tools" selector={WIDGETS[lifted].selector} state={state} radii={radii}/></div>;
    })():null}
    {g&&t<e.pullBackEnd?WIDGETS.slice(0,4).map((widget,i)=>{
      const at=e.launches[i],rect=g[widget.id];
      const {x,y,scale,flight}=cardPose(t,g,i);
      const radii=flightRadii(t,g,i);
      return t>=at?<div key={widget.id} data-flying-widget={widget.id} style={{position:'absolute',width:rect.width,height:rect.height,borderRadius:radiusCss(radii),transformOrigin:'50% 50%',transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) scale(${scale})`,zIndex:10+i,boxShadow:`0 ${lerp(2,18,flight)}px ${lerp(5,32,flight)}px rgba(59,45,20,.14)`}}>
        <LiveCrop screen="quick-tools" selector={widget.selector} state={{...state,searchQuery:widget.query}} radii={radii}/>
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
