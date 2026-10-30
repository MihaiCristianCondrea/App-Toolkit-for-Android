import React from 'react';
import {Geometry,LiveCrop} from './Kit';
import {Headline,Phone,Shape,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';

const origin=(geometry:Geometry|null,label:string)=>{
  const box=geometry?.[label];
  if(!box)throw new Error(`Missing measured navigation target: ${label}`);
  return {x:box.x+box.width/2,y:box.y+box.height/2};
};

/** Tab changes reveal from measured controls. The same device continues between chapters. */
export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const measure=timeline.events.measure,play=timeline.events.play;
  const isPlay=t>=play;
  const move=ease(t,17.92,18.62);
  const count=42+[19.7,19.98,20.56].filter(beat=>t>=beat).length;
  const rotation=1260*ease(t,22.86,24.54);
  const result=t>=24.54?'Sushi':'';
  const measureState={counterValue:count,protractorAngle:45,converterInput:10,splitTotal:'120',splitPeople:'4'};
  const playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:rotation,wheelWinner:result,randomResult:t>=25.12?73:null,diceResult:5,coinSide:'Heads'};
  const chapterReveal=ease(t,isPlay?play:measure,isPlay?22.56:17.7);
  return <>
    <Phone id={isPlay?'play':'numbers'} underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{...measureState,counterValue:45}:{compassHeading:45,levelRoll:0,levelPitch:0}}
      state={isPlay?playState:measureState} x={lerp(1320,540,move)} y={540} scale={.92} angle={lerp(-4,4,move)}
      reveal={chapterReveal} revealFrom={origin(geometry,isPlay?'Play & Decide':'Measure')}/>
    {!isPlay?<>
      <Shape name="flower" color="#FDE7A2" size={580} x={1450} y={690} angle={-12*ease(t,18.62,19.42)}/>
      <Headline text="Measure." x={990} y={180} width={800} size={142} progress={ease(t,18.62,19.14)*(1-ease(t,21.46,21.98))}/>
      <div data-hero-part="counter" style={{position:'absolute',left:1360,top:lerp(1180,610,pop(t,18.62,.8)),transform:`translate(-50%,-50%) rotate(${lerp(5,0,ease(t,18.62,19.42))}deg) scale(${1.65*(1-ease(t,21.46,21.98))})`}}>
        <LiveCrop screen="numbers" selector='[data-tool="counter"]' state={{counterValue:count,searchQuery:'Counter'}} style={{borderRadius:28}}/>
      </div>
      <div style={{position:'absolute',left:1000,top:890,fontSize:40,color:'#6B5E00',opacity:ease(t,19.14,19.42)*(1-ease(t,21.46,21.98))}}>Convert. Split. Count.</div>
    </>:<>
      <Shape name="soft_burst" color="#E8D3DC" size={600} x={1460} y={690} angle={-18+18*ease(t,22.56,23.4)}/>
      <Headline text="Let chance decide." x={1000} y={140} width={780} size={112} progress={ease(t,22.56,22.86)*(1-ease(t,25.98,26.26))}/>
      <div data-hero-part="wheel" style={{position:'absolute',left:1360,top:lerp(1180,660,pop(t,22.56,.8)),transform:`translate(-50%,-50%) rotate(${lerp(-5,0,ease(t,22.56,23.4))}deg) scale(1.7)`}}>
        <LiveCrop screen="play" selector=".play-wheel-card" state={{...playState,searchQuery:'Decision Wheel'}} style={{borderRadius:28}}/>
      </div>
    </>}
  </>;
};
