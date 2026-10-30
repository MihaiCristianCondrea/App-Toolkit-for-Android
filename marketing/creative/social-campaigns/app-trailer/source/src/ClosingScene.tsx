import React from 'react';
import {Img,staticFile} from 'remotion';
import {Headline,Phone,Shape,Toolbox,ease,lerp,pop} from './visuals';

export const ClosingScene:React.FC<{seconds:number}>=({seconds:t})=>{
  const fan=ease(t,26.26,27.26);
  return <>
    <Shape name="cookie_9sided" color="#FDE7A2" size={580} x={1840} y={120} angle={lerp(-20,8,fan)}/>
    <Shape name="clover_4leaf" color="#D0E9E3" size={450} x={10} y={1070} angle={lerp(20,0,fan)}/>
    <Phone id="quick-tools" state={{compassHeading:45,levelRoll:0,levelPitch:0}} x={lerp(540,1070,fan)} y={lerp(540,540,fan)} scale={lerp(.92,.62,fan)} angle={lerp(4,-6,fan)}/>
    <Phone id="numbers" state={{counterValue:45}} x={lerp(540,1340,fan)} y={540} scale={lerp(.92,.64,fan)} angle={lerp(4,0,fan)}/>
    <Phone id="play" state={{wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260,wheelWinner:'Sushi',randomResult:73,diceResult:5,coinSide:'Heads'}} x={lerp(540,1610,fan)} y={540} scale={lerp(.92,.62,fan)} angle={lerp(4,6,fan)}/>
    <Toolbox x={230} y={300} size={250} open={0} scale={pop(t,26.82,.6)}/>
    <Headline text="App Toolkit." x={110} y={480} width={780} size={124} progress={ease(t,27.04,27.68)}/>
    <Headline text="Your toolkit." x={116} y={645} width={700} size={72} progress={ease(t,27.26,27.96)}/>
    <Img src={staticFile('google-play.png')} style={{position:'absolute',left:95,top:810,width:330,height:'auto',transform:`translateY(${lerp(80,0,ease(t,27.68,28.26))}px)`,opacity:ease(t,27.68,28.26)}}/>
  </>;
};
