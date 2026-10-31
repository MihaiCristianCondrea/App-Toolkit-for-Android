import React from 'react';
import {Img,staticFile} from 'remotion';
import {Headline,Phone,Shape,Toolbox,beat,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';
const e=timeline.events;
const PHONES=[
  {id:'quick-tools',label:'Quick Tools',x:1030,y:490,angle:-7,ry:-12,z:-90,at:e.fan[0],state:{compassHeading:45,levelRoll:0,levelPitch:0}},
  {id:'numbers',label:'Measure',x:1360,y:480,angle:0,ry:0,z:60,at:e.fan[1],state:{counterValue:45,protractorAngle:135,converterInput:25,billAmount:120,splitPeople:'4'}},
  {id:'play',label:'Play & Decide',x:1660,y:490,angle:7,ry:12,z:-40,at:e.close,state:{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}},
] as const;
export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=><div className="sc-part" data-theme="light" style={{position:'absolute',inset:0}}>
  {t>=e.closingStage?<Shape name="soft_burst" color="var(--tk-gold-container)" size={290} x={210} y={180} scale={pop(t,e.closingStage,.2)*(1+.1*beat(t,'closing-stage'))}/>:null}
  <div style={{position:'absolute',inset:0,perspective:1800,perspectiveOrigin:'70% 50%'}}>
    {PHONES.map(p=>{
      const land=pop(t,p.at,.64);
      return <div key={p.id} style={{position:'absolute',inset:0,transformOrigin:`${p.x}px ${p.y}px`,transform:`translateZ(${p.z}px) rotateY(${p.ry*land}deg)`}}><Phone seconds={t} id={p.id} state={p.state} x={p.x+320*(1-land)} y={p.y+1450*(1-land)} scale={.86*lerp(.78,1,land)} angle={p.angle+12*(1-land)}/></div>;
    })}
  </div>
  {PHONES.map(p=><div key={p.label} style={{position:'absolute',left:p.x-150,top:950,width:300,textAlign:'center',fontSize:32,fontWeight:600,color:'#3B2D14',transform:`translateY(${300*(1-ease(t,p.at+.4,p.at+.64))}px)`}}>{p.label}</div>)}
  <Toolbox x={210} y={180} size={220} open={0} scale={pop(t,e.fan[1],.6)*lerp(1,.9,ease(t,e.finalAnticipation,e.finalExplosion))}/>
  <Headline seconds={t} text="App Toolkit." x={96} y={355} width={700} size={108}/>
  <Headline seconds={t} text="Your toolkit." x={96} y={510} width={650} size={76}/>
  <Img src={staticFile('google-play.png')} style={{position:'absolute',left:96,top:675,width:350,height:'auto',transform:`translateY(${600*(1-ease(t,e.badge,e.hold))}px)`}}/>
  {t>=e.finalExplosion?<>
    <Shape name="soft_burst" color="var(--tk-gold-container)" size={300} x={lerp(210,960,ease(t,e.finalExplosion,e.finalCover))} y={lerp(180,540,ease(t,e.finalExplosion,e.finalCover))} angle={120*ease(t,e.finalExplosion,e.finalLogo)} scale={lerp(.7,24,ease(t,e.finalExplosion,e.finalCover))}/>
    {t>=e.finalCover?<div data-final-cover style={{position:'absolute',inset:0,background:'var(--tk-gold-container)'}}/>:null}
    {t>=e.finalLogo?<Toolbox x={960} y={540} size={420} open={0} scale={pop(e.finalLogo+6*(t-e.finalLogo),e.finalLogo,.42)}/>:null}
  </>:null}
</div>;
