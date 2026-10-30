import React from 'react';
import timeline from './timeline.json';
const e=timeline.events;
import {Img,staticFile} from 'remotion';
import {Headline,Phone,Shape,Toolbox,beat,ease,easeIn,lerp,pop} from './visuals';

/** The three destinations fan out one onset apart, each named under its phone. */
const PHONES=[
  {id:'quick-tools',label:'Quick Tools',x:1190,scale:.48,angle:-3,at:e.fan[0],state:{compassHeading:45,levelRoll:0,levelPitch:0}},
  {id:'numbers',label:'Measure',x:1430,scale:.48,angle:0,at:e.fan[1],state:{counterValue:45,protractorAngle:135,converterInput:25,splitTotal:'120',splitPeople:'4'}},
  // Keep the outgoing Play phone foremost while the other destinations fan from behind it.
  {id:'play',label:'Play & Decide',x:1660,scale:.48,angle:3,at:e.close,state:{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}},
] as const;

export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=>{
  return <div className="sc-part" data-theme="light" style={{position:'absolute',inset:0}}>
    {t>=e.closingStage?<Shape name="soft_burst" color="var(--tk-gold-container)" size={320} x={250} y={300} scale={1+.1*beat(t,'closing-stage')}/>:null}
    {PHONES.map(phone=>{
      const p=pop(phone.at+.9*ease(t,phone.at,phone.at+.9),phone.at,.9);
      return <Phone seconds={t} key={phone.id} id={phone.id} state={phone.state} x={lerp(540,phone.x,p)} y={540} scale={lerp(.82,phone.scale,p)} angle={lerp(0,phone.angle,p)}/>;
    })}
    {PHONES.map(phone=><div key={phone.label} style={{position:'absolute',left:phone.x-115,top:840,width:230,textAlign:'center',fontSize:32,fontWeight:600,color:'#3B2D14',transform:`translateY(${lerp(300,0,ease(t,phone.at+.5,phone.at+.8))}px)`}}>{phone.label}</div>)}
    <Toolbox x={250} y={300} size={250} open={0} scale={pop(t,e.cues.at26_82,.6)*lerp(1,.9,ease(t,e.finalAnticipation,e.finalExplosion))}/>
    <Headline seconds={t} text="App Toolkit." x={120} y={480} width={740} size={124} progress={ease(t,e.cues.at27_04,e.cues.at27_68)}/>
    <Headline seconds={t} text="Your toolkit." x={120} y={645} width={700} size={72} progress={ease(t,e.cues.at27_26,e.cues.at27_96)}/>
    <Img src={staticFile('google-play.png')} style={{position:'absolute',left:120,top:810,width:330,height:'auto',transform:`translateY(${lerp(600,0,ease(t,e.badge,e.hold))}px)`}}/>
    {t>=e.finalExplosion?<>
      <Shape name="soft_burst" color="var(--tk-gold-container)" size={300} x={lerp(250,960,ease(t,e.finalExplosion,e.finalCover))} y={lerp(300,540,ease(t,e.finalExplosion,e.finalCover))} angle={120*ease(t,e.finalExplosion,e.finalLogo)} scale={lerp(.7,24,ease(t,e.finalExplosion,e.finalCover))}/>
      {t>=e.finalCover?<div data-final-cover style={{position:'absolute',inset:0,background:'var(--tk-gold-container)'}}/>:null}
      {t>=e.finalLogo?<Toolbox x={960} y={540} size={420} open={0} scale={pop(e.finalLogo+6*(t-e.finalLogo),e.finalLogo,.42)}/>:null}
    </>:null}
  </div>;
};
