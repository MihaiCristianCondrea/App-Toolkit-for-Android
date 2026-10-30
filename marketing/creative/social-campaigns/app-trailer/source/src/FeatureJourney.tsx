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
  {tool:'protractor',word:'Angles.',in:18.14,out:19.08},
  {tool:'unit_converter',word:'Units.',in:19.14,out:19.92},
  {tool:'bill_splitter',word:'Bills.',in:19.98,out:20.78},
  {tool:'counter',word:'Counts.',in:20.84,out:21.72},
] as const;

/** The chance tools join the settled wheel on the following onsets. */
const CHANCE=[
  // The random number card spans the kit's full row (about 398 px); dice and coin are half width.
  {tool:'random_number',at:24.54,x:1170},
  {tool:'dice_roll',at:25.12,x:1505},
  {tool:'coin_flip',at:25.4,x:1740},
] as const;

/** A hero enters from below with an overshoot, holds, then is thrown towards the phone. */
const heroTransform=(t:number,start:number,end:number,x:number,y:number,scale:number)=>{
  const enter=pop(t,start,.32),leave=ease(t,end,end+.22);
  const px=x-760*leave,py=lerp(y+560,y,enter)-120*leave;
  return `translate(${px}px,${py}px) translate(-50%,-50%) rotate(${lerp(7,0,clamp(enter))-14*leave}deg) scale(${scale*lerp(1,.5,leave)})`;
};

/** Tab changes reveal from measured controls. The same device continues between chapters. */
export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const measure=timeline.events.measure,play=timeline.events.play;
  const isPlay=t>=play;
  const move=ease(t,17.7,18.1);
  const count=42+[20.84,21.12,21.46].filter(hit=>t>=hit).length;
  const angle=Math.round(lerp(20,135,ease(t,18.38,18.92)));
  const converter=Math.round(lerp(10,25,ease(t,19.42,19.88)));
  const people=t>=20.56?'4':'2';
  const rotation=1260*ease(t,22.86,24);
  const result=t>=24?'Sushi':'';
  // The random number shuffles until the dice lands, the die tumbles until the coin lands.
  const shuffle=Math.floor(t*30);
  const randomResult=t<24.54?null:t<25.12?1+(shuffle*37)%100:73;
  const diceResult=t<25.12||t>=25.4?5:1+(shuffle*5)%6;
  const coinSide=t>=25.4&&t<25.68?'Tails':'Heads';
  const measureState={counterValue:count,protractorAngle:angle,converterInput:converter,splitTotal:'120',splitPeople:people};
  const playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:rotation,wheelWinner:result,randomResult,diceResult,coinSide};
  const chapterReveal=ease(t,isPlay?play:measure,isPlay?22.56:17.7);
  const active=[...MEASURE_HEROES].reverse().find(hero=>t>=hero.in)?.word;
  const settle=ease(t,24,24.54);
  return <>
    {!isPlay?<Shape name="flower" color="#FDE7A2" size={580} x={1450} y={690} angle={t*4}/>:<Shape name="soft_burst" color="#E8D3DC" size={600} x={1460} y={690} angle={-18+t*4}/>}
    <Phone id={isPlay?'play':'numbers'} underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{...measureState,counterValue:45}:{compassHeading:45,levelRoll:0,levelPitch:0}}
      state={isPlay?playState:measureState} x={lerp(1320,540,move)} y={540} scale={.92} angle={lerp(-4,4,move)}
      reveal={chapterReveal} revealFrom={origin(geometry,isPlay?'Play & Decide':'Measure')}/>
    {!isPlay?<>
      <Headline text="Measure." x={990} y={180} width={800} size={142} progress={ease(t,18.1,18.38)*(1-ease(t,21.46,21.98))}/>
      {MEASURE_HEROES.map(hero=>t>=hero.in&&t<hero.out+.22?<div key={hero.tool} data-hero-part={hero.tool} style={{position:'absolute',left:0,top:0,opacity:1-ease(t,hero.out,hero.out+.22),transform:heroTransform(t,hero.in,hero.out,1360,600,2.1)}}>
        <LiveCrop screen="numbers" selector={`[data-tool="${hero.tool}"]`} state={{...measureState,searchQuery:''}} style={{borderRadius:28}}/>
      </div>:null)}
      <div style={{position:'absolute',left:1000,top:890,display:'flex',gap:22,fontSize:40,fontWeight:600,opacity:ease(t,18.1,18.38)*(1-ease(t,21.46,21.98))}}>
        {MEASURE_HEROES.map(hero=><span key={hero.word} style={{color:hero.word===active?'#3B2D14':'rgba(59,45,20,.45)',fontWeight:hero.word===active?700:450}}>{hero.word}</span>)}
      </div>
    </>:<>
      <Headline text="Let chance decide." x={1000} y={140} width={780} size={112} progress={ease(t,22.56,22.86)*(1-ease(t,25.98,26.26))}/>
      <div data-hero-part="wheel" style={{position:'absolute',left:0,top:0,transform:`translate(1360px,${lerp(lerp(1180,660,pop(t,22.56,.8)),530,settle)}px) translate(-50%,-50%) rotate(${lerp(-5,0,ease(t,22.56,23.4))}deg) scale(${lerp(1.7,1.3,settle)*(1+.04*(t>=24&&t<24.54?beat(t):0))})`}}>
        <LiveCrop screen="play" selector=".play-wheel-card" state={{...playState,searchQuery:'Decision Wheel'}} style={{borderRadius:28}}/>
      </div>
      {CHANCE.map(card=>t>=card.at?<div key={card.tool} data-hero-part={card.tool} style={{position:'absolute',left:0,top:0,transform:`translate(${card.x}px,${lerp(1260,870,pop(t,card.at,.5))}px) translate(-50%,-50%) rotate(${lerp(8,0,ease(t,card.at,card.at+.5))}deg) scale(${1.1*(1+.06*beat(t))})`}}>
        <LiveCrop screen="play" selector={`[data-tool="${card.tool}"]`} state={{...playState,searchQuery:''}} style={{borderRadius:28}}/>
      </div>:null)}
    </>}
  </>;
};
