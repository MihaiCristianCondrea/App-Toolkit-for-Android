import React from 'react';
import {Geometry,LiveCrop} from './Kit';
import {Headline,Phone,Shape,beat,clamp,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';

const origin=(geometry:Geometry|null,label:string)=>{
  const box=geometry?.[label];
  if(!box)throw new Error(`Missing measured navigation target: ${label}`);
  return {x:box.x+box.width/2,y:box.y+box.height/2};
};

/** Measure tools take the stage one onset at a time; each flings out as the next lands. */
const MEASURE_HEROES=[
  {tool:'protractor',word:'Angles.',in:18.62,out:19.7},
  {tool:'unit_converter',word:'Units.',in:19.7,out:20.56},
  {tool:'bill_splitter',word:'Bills.',in:20.56,out:21.12},
  {tool:'counter',word:'Counts.',in:21.12,out:21.64},
] as const;

/** The chance tools join the settled wheel on the following onsets. */
const CHANCE=[
  // The random number card spans the kit's full row (about 398 px); dice and coin are half width.
  {tool:'random_number',at:25.12,x:1180},
  {tool:'dice_roll',at:25.4,x:1480},
  {tool:'coin_flip',at:25.68,x:1680},
] as const;

/** A hero enters from below with an overshoot, holds, then is thrown towards the phone. */
const heroTransform=(t:number,start:number,end:number,x:number,y:number,scale:number)=>{
  const enter=pop(t,start,.7),leave=ease(t,end,end+.34);
  const px=x-760*leave,py=lerp(y+560,y,enter)-120*leave;
  return `translate(${px}px,${py}px) translate(-50%,-50%) rotate(${lerp(7,0,clamp(enter))-14*leave}deg) scale(${scale*lerp(1,.5,leave)*(1+.03*beat(t))})`;
};

/** Tab changes reveal from measured controls. The same device continues between chapters. */
export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const measure=timeline.events.measure,play=timeline.events.play;
  const isPlay=t>=play;
  const move=ease(t,17.92,18.62);
  const count=43+[21.28,21.46].filter(hit=>t>=hit).length;
  const angle=Math.round(lerp(20,135,ease(t,18.84,19.6)));
  const converter=Math.round(lerp(10,25,ease(t,19.98,20.42)));
  const people=t>=20.84?'4':'2';
  const rotation=1260*ease(t,22.86,24.54);
  const result=t>=24.54?'Sushi':'';
  // The random number shuffles until the dice lands, the die tumbles until the coin lands.
  const shuffle=Math.floor(t*30);
  const randomResult=t<25.12?null:t<25.4?1+(shuffle*37)%100:73;
  const diceResult=t<25.4||t>=25.98?5:1+(shuffle*5)%6;
  const coinSide=t>=25.68&&t<26?'Tails':'Heads';
  const measureState={counterValue:count,protractorAngle:angle,converterInput:converter,splitTotal:'120',splitPeople:people};
  const playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:rotation,wheelWinner:result,randomResult,diceResult,coinSide};
  const chapterReveal=ease(t,isPlay?play:measure,isPlay?22.56:17.7);
  const active=[...MEASURE_HEROES].reverse().find(hero=>t>=hero.in)?.word;
  const settle=ease(t,24.84,25.3);
  return <>
    <Phone id={isPlay?'play':'numbers'} underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{...measureState,counterValue:45}:{compassHeading:45,levelRoll:0,levelPitch:0}}
      state={isPlay?playState:measureState} x={lerp(1320,540,move)} y={540} scale={.92} angle={lerp(-4,4,move)}
      reveal={chapterReveal} revealFrom={origin(geometry,isPlay?'Play & Decide':'Measure')}/>
    {!isPlay?<>
      <Shape name="flower" color="#FDE7A2" size={580} x={1450} y={690} angle={-12*ease(t,18.62,19.42)+t*4} scale={1+.06*beat(t)}/>
      <Headline text="Measure." x={990} y={180} width={800} size={142} progress={ease(t,18.62,19.14)*(1-ease(t,21.46,21.98))}/>
      {MEASURE_HEROES.map(hero=>t>=hero.in&&t<hero.out+.34?<div key={hero.tool} data-hero-part={hero.tool} style={{position:'absolute',left:0,top:0,transform:heroTransform(t,hero.in,hero.out,1360,600,1.5)}}>
        <LiveCrop screen="numbers" selector={`[data-tool="${hero.tool}"]`} state={{...measureState,searchQuery:''}} style={{borderRadius:28}}/>
      </div>:null)}
      <div style={{position:'absolute',left:1000,top:900,display:'flex',gap:22,fontSize:40,fontWeight:600,opacity:ease(t,18.84,19.14)*(1-ease(t,21.46,21.98))}}>
        {MEASURE_HEROES.map(hero=><span key={hero.word} style={{color:hero.word===active?'#6B5E00':'rgba(59,45,20,.32)',transform:`translateY(${hero.word===active?-6*beat(t):0}px)`}}>{hero.word}</span>)}
      </div>
    </>:<>
      <Shape name="soft_burst" color="#E8D3DC" size={600} x={1460} y={690} angle={-18+18*ease(t,22.56,23.4)+t*4} scale={1+.06*beat(t)}/>
      <Headline text="Let chance decide." x={1000} y={140} width={780} size={112} progress={ease(t,22.56,22.86)*(1-ease(t,25.98,26.26))}/>
      <div data-hero-part="wheel" style={{position:'absolute',left:1360,top:lerp(lerp(1180,660,pop(t,22.56,.8)),470,settle),transform:`translate(-50%,-50%) rotate(${lerp(-5,0,ease(t,22.56,23.4))}deg) scale(${lerp(1.7,1.3,settle)*(1+.04*(t>=24.54&&t<24.84?beat(t):0))})`}}>
        <LiveCrop screen="play" selector=".play-wheel-card" state={{...playState,searchQuery:'Decision Wheel'}} style={{borderRadius:28}}/>
      </div>
      {CHANCE.map(card=>t>=card.at?<div key={card.tool} data-hero-part={card.tool} style={{position:'absolute',left:card.x,top:lerp(1260,850,pop(t,card.at,.7)),transform:`translate(-50%,-50%) rotate(${lerp(8,0,ease(t,card.at,card.at+.5))}deg) scale(${.9*(1+.06*beat(t))})`}}>
        <LiveCrop screen="play" selector={`[data-tool="${card.tool}"]`} state={{...playState,searchQuery:''}} style={{borderRadius:28}}/>
      </div>:null)}
    </>}
  </>;
};
