import React from 'react';
import {Img,staticFile} from 'remotion';
import {Headline,Phone,Shape,Toolbox,ease,lerp,pop} from './visuals';

/** The three destinations fan out one onset apart, each named under its phone. */
const PHONES=[
  {id:'quick-tools',label:'Quick Tools',x:1070,scale:.62,angle:-6,at:26.5,state:{compassHeading:45,levelRoll:0,levelPitch:0}},
  {id:'numbers',label:'Measure',x:1340,scale:.64,angle:0,at:26.82,state:{counterValue:45,protractorAngle:135,converterInput:25,splitTotal:'120',splitPeople:'4'}},
  // Keep the outgoing Play phone foremost while the other destinations fan from behind it.
  {id:'play',label:'Play & Decide',x:1610,scale:.62,angle:6,at:26.26,state:{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}},
] as const;

export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=>{
  const fan=ease(t,26.26,27.26);
  return <>
    <Shape name="cookie_9sided" color="#FDE7A2" size={580} x={1840} y={120} angle={lerp(-20,8,fan)+t*3}/>
    <Shape name="clover_4leaf" color="#D0E9E3" size={450} x={10} y={1070} angle={lerp(20,0,fan)-t*3}/>
    {PHONES.map(phone=>{
      const p=pop(phone.at+.9*ease(t,phone.at,phone.at+.9),phone.at,.9);
      return <Phone key={phone.id} id={phone.id} state={phone.state} x={lerp(540,phone.x,p)} y={540} scale={lerp(.92,phone.scale,p)} angle={lerp(4,phone.angle,p)}/>;
    })}
    {PHONES.map(phone=><div key={phone.label} style={{position:'absolute',left:phone.x-150,top:880,width:300,textAlign:'center',fontSize:32,fontWeight:600,color:'#3B2D14',opacity:ease(t,phone.at+.5,phone.at+.8),transform:`translateY(${lerp(24,0,ease(t,phone.at+.5,phone.at+.8))}px)`}}>{phone.label}</div>)}
    <Toolbox x={230} y={300} size={250} open={ease(t,28.88,29.3)*.35} scale={pop(t,26.82,.6)}/>
    <Headline text="App Toolkit." x={110} y={480} width={780} size={124} progress={ease(t,27.04,27.68)}/>
    <Headline text="Your toolkit." x={116} y={645} width={700} size={72} progress={ease(t,27.26,27.96)}/>
    <Img src={staticFile('google-play.png')} style={{position:'absolute',left:95,top:810,width:330,height:'auto',transform:`translateY(${lerp(80,0,ease(t,27.68,28.26))}px)`,opacity:ease(t,27.68,28.26)}}/>
  </>;
};
