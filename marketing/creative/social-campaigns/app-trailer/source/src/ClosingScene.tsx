import React from 'react';
import {Img,staticFile} from 'remotion';
import {Headline,TYPE,Phone,Shape,Toolbox,beat,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';
const e=timeline.events,l=e.closingLayout;
const PHONES=[
  {id:'quick-tools',label:'Quick Tools',x:l.phoneX[0],y:l.phoneY[0],angle:-7,ry:-12,z:-90,at:e.fan[0],state:{compassHeading:45,levelRoll:0,levelPitch:0}},
  {id:'numbers',label:'Measure',x:l.phoneX[1],y:l.phoneY[1],angle:0,ry:0,z:60,at:e.fan[1],state:{counterValue:45,protractorAngle:135,converterInput:25,billAmount:120,splitPeople:'4'}},
  {id:'play',label:'Play & Decide',x:l.phoneX[2],y:l.phoneY[2],angle:7,ry:12,z:-40,at:e.close,state:{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}},
] as const;
export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=><div className="sc-part" data-theme="light" style={{position:'absolute',inset:0}}>
  {t>=e.closingStage?<Shape name="soft_burst" color="var(--tk-gold-container)" size={290} x={l.markX} y={l.markY} scale={pop(t,e.closingStage,.2)*(1+.1*beat(t,'closing-stage'))}/>:null}
  <div style={{position:'absolute',inset:0,perspective:1800,perspectiveOrigin:'70% 50%'}}>
    {PHONES.map(p=>{
      const first=p.id==='play';
      if(!first&&t<p.at)return null;
      const land=pop(t,first?e.fan[0]:p.at,l.land),entry=first?l.entry:{x:p.x+l.travelX,y:p.y+l.travelY,scale:l.scale*l.startScale,angle:p.angle+l.startAngle};
      return <div key={p.id} style={{position:'absolute',inset:0,transformOrigin:`${p.x}px ${p.y}px`,transform:`translateZ(${p.z*land}px) rotateY(${p.ry*land}deg)`}}><Phone seconds={t} id={p.id} state={p.state} reveal={first?lerp(l.entryReveal,1,ease(land,l.revealTail,1)):1} level={3-land} x={lerp(entry.x,p.x,land)} y={lerp(entry.y,p.y,land)} scale={lerp(entry.scale,l.scale,land)} angle={lerp(entry.angle,p.angle,land)}/></div>;
    })}
  </div>
  {PHONES.map(p=>{const at=p.id==='play'?e.fan[0]:p.at;return <div key={p.label} style={{position:'absolute',left:p.x-l.labelWidth/2,top:l.labelY,width:l.labelWidth,textAlign:'center',fontSize:TYPE.phoneLabel.size,fontWeight:TYPE.phoneLabel.weight,color:'#3B2D14',transform:`translateY(${l.labelTravel*(1-ease(t,at+l.land/2,at+l.land))}px)`}}>{p.label}</div>;})}
  <Toolbox x={l.markX} y={l.markY} size={l.markSize} open={0} scale={pop(t,l.markAt,.6)*lerp(1,.9,ease(t,e.finalAnticipation,e.finalExplosion))}/>
  <Headline seconds={t} text="App Toolkit." x={l.textX} y={l.titleY} width={l.textWidth} size={TYPE.closingTitle.size} wdth={l.titleWidthAxis}/>
  <Headline seconds={t} text="Your toolkit." x={l.textX} y={l.subtitleY} width={l.textWidth} size={TYPE.closingSubtitle.size}/>
  <Img src={staticFile('google-play.png')} style={{position:'absolute',left:l.textX,top:l.badgeY,width:l.badgeWidth,height:'auto',transform:`translateY(${600*(1-ease(t,e.badge,e.hold))}px)`}}/>
  {t>=e.finalExplosion?<>
    <Shape name="soft_burst" color="var(--tk-gold-container)" size={300} x={lerp(l.markX,960,ease(t,e.finalExplosion,e.finalCover))} y={lerp(l.markY,540,ease(t,e.finalExplosion,e.finalCover))} angle={120*ease(t,e.finalExplosion,e.finalLogo)} scale={lerp(.7,24,ease(t,e.finalExplosion,e.finalCover))}/>
    {t>=e.finalCover?<div data-final-cover style={{position:'absolute',inset:0,background:'var(--tk-gold-container)'}}/>:null}
    {t>=e.finalLogo?<Toolbox x={960} y={540} size={420} open={0} scale={pop(e.finalLogo+6*(t-e.finalLogo),e.finalLogo,.42)}/>:null}
  </>:null}
</div>;
