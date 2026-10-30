import React from 'react';
import timeline from './timeline.json';
const e=timeline.events;
import {Img,staticFile} from 'remotion';
import {Headline,Phone,Shape,Toolbox,ease,easeIn,lerp,pop} from './visuals';

/** The three destinations fan out one onset apart, each named under its phone. */
const PHONES=[
  {id:'quick-tools',label:'Quick Tools',x:1000,scale:.74,angle:-6,at:e.cues.at26_5,state:{compassHeading:45,levelRoll:0,levelPitch:0}},
  {id:'numbers',label:'Measure',x:1330,scale:.76,angle:0,at:e.cues.at26_82,state:{counterValue:45,protractorAngle:135,converterInput:25,splitTotal:'120',splitPeople:'4'}},
  // Keep the outgoing Play phone foremost while the other destinations fan from behind it.
  {id:'play',label:'Play & Decide',x:1660,scale:.74,angle:6,at:e.cues.at26_26,state:{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}},
] as const;

export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=>{
  const fan=ease(t,e.cues.at26_26,e.cues.at27_26);
  return <>
    {PHONES.map(phone=>{
      const p=pop(phone.at+.9*ease(t,phone.at,phone.at+.9),phone.at,.9);
      return <Phone key={phone.id} id={phone.id} state={phone.state} x={lerp(540,phone.x,p)} y={540} scale={lerp(.92,phone.scale,p)} angle={lerp(4,phone.angle,p)}/>;
    })}
    {PHONES.map(phone=><div key={phone.label} style={{position:'absolute',left:phone.x-150,top:955,width:300,textAlign:'center',fontSize:32,fontWeight:600,color:'#3B2D14',transform:`translateY(${lerp(300,0,ease(t,phone.at+.5,phone.at+.8))}px)`}}>{phone.label}</div>)}
    <Toolbox x={230} y={300} size={250} open={0} scale={pop(t,e.cues.at26_82,.6)}/>
    <Headline seconds={t} text="App Toolkit." x={110} y={480} width={780} size={124} progress={ease(t,e.cues.at27_04,e.cues.at27_68)}/>
    <Headline seconds={t} text="Your toolkit." x={116} y={645} width={700} size={72} progress={ease(t,e.cues.at27_26,e.cues.at27_96)}/>
    <Img src={staticFile('google-play.png')} style={{position:'absolute',left:95,top:810,width:330,height:'auto',transform:`translateY(${lerp(600,0,ease(t,e.cues.at27_68,e.cues.at28_26))}px)`}}/>
    {/* The boom: a burst explodes from the mark on the last hits and takes the frame, the mark riding it to the centre. */}
    {t>=29.4?<>
      <Shape name="soft_burst" color="#F2C94C" size={300} x={lerp(230,960,easeIn(t,29.4,29.8))} y={lerp(300,540,easeIn(t,29.4,29.8))} angle={90*easeIn(t,29.4,29.88)} scale={lerp(.2,9,easeIn(t,29.4,29.8))}/>
      <Toolbox x={960} y={540} size={420} open={0} scale={pop(t,29.54,.3)}/>
    </>:null}
  </>;
};
