import React from 'react';
import {Geometry,LiveCrop,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT} from './Kit';
import {Headline,Phone,Shape,beat,clamp,ease,lerp,pop} from './visuals';
import timeline from './timeline.json';

import {QuickCaption} from './OpeningJourney';
const e=timeline.events;
const origin=(geometry:Geometry|null,label:string)=>{
  const box=geometry?.[label];
  if(!box)throw new Error(`Missing measured navigation target: ${label}`);
  return {x:box.x+box.width/2,y:box.y+box.height/2};
};

/** Measure tools take the stage one onset at a time; each travels out as the next lands. */
const MEASURE_HEROES=[
  {tool:'protractor',word:'Angles.',in:e.measureHeroes[0],out:e.measureHeroes[1]-.12},
  {tool:'unit_converter',word:'Units.',in:e.measureHeroes[1],out:e.measureHeroes[2]-.12},
  {tool:'bill_splitter',word:'Bills.',in:e.measureHeroes[2],out:e.measureHeroes[3]-.12},
  {tool:'counter',word:'Counts.',in:e.measureHeroes[3],out:e.play},
] as const;

/** The chance tools join the settled wheel on the following onsets. */
const CHANCE=[
  // The random number card spans the kit's full row (about 398 px); dice and coin are half width.
  {tool:'random_number',at:e.cues.at24_54,x:1230,scale:.7},
  {tool:'dice_roll',at:e.cues.at25_12,x:1490,scale:.8},
  {tool:'coin_flip',at:e.cues.at25_4,x:1690,scale:.8},
] as const;

/** A hero enters from below with an overshoot, holds, then travels beyond the frame. */
const heroTransform=(t:number,start:number,end:number,x:number,y:number,scale:number)=>{
  const enter=pop(t,start,.32),leave=ease(t,end,end+.22);
  const px=x+1000*(1-enter)+1500*leave,py=y;
  return `translate(${px}px,${py}px) translate(-50%,-50%) rotate(0deg) scale(${scale})`;
};

/** Tab changes reveal from measured controls. The same device continues between chapters. */
export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const measure=timeline.events.measure,play=timeline.events.play;
  const isPlay=t>=play;
  const move=ease(t,e.cues.at17_7,e.cues.at18_1);
  const count=42+[e.cues.at20_84,e.cues.at21_12,e.cues.at21_46].filter(hit=>t>=hit).length;
  const angle=Math.round(lerp(20,135,ease(t,e.cues.at18_38,e.cues.at18_92)));
  const converter=Math.round(lerp(10,25,ease(t,e.cues.at19_42,e.cues.at19_88)));
  const people=t>=e.cues.at20_56?'4':'2';
  const rotation=1260*ease(t,e.wheelStart,e.wheelEnd);
  const result=t>=e.wheelEnd?'Sushi':'';
  // The random number shuffles until the dice lands, the die tumbles until the coin lands.
  const shuffle=Math.floor(t*30);
  const randomResult=t<e.cues.at24_54?null:t<e.cues.at25_12?1+(shuffle*37)%100:73;
  const diceResult=t<e.cues.at25_12||t>=e.cues.at25_4?5:1+(shuffle*5)%6;
  const coinSide=t>=e.cues.at25_4&&t<e.cues.at25_68?'Tails':'Heads';
  const measureState={counterValue:count,protractorAngle:angle,converterInput:converter,splitTotal:'120',splitPeople:people};
  const playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:rotation,wheelWinner:result,randomResult,diceResult,coinSide};
  const chapterReveal=ease(t,isPlay?play:measure,isPlay?e.cues.at22_56:e.cues.at17_7);
  const active=[...MEASURE_HEROES].reverse().find(hero=>t>=hero.in)?.word;
  const settle=ease(t,e.wheelEnd,e.chance[0]);
  const tab=origin(geometry,isPlay?'Play & Decide':'Measure');
  const a=0;
  const dx=(DISPLAY.x+tab.x-SKIN_WIDTH/2)*.82,dy=(DISPLAY.y+tab.y-SKIN_HEIGHT/2)*.82;
  const tx=(isPlay?540:1320)+dx*Math.cos(a)-dy*Math.sin(a),ty=540+dx*Math.sin(a)+dy*Math.cos(a);
  const ground=ease(t,isPlay?e.play:e.measure,isPlay?e.cues.at22_56:e.cues.at18_1);
  const leave=ease(t,e.close,e.close+.4);
  return <>
    {isPlay?<Shape name="flower" color="#FDE7A2" size={680} x={1450+2000*leave} y={690}/>:null}
    <Shape name={isPlay?"soft_burst":"flower"} color={isPlay?"#E8D3DC":"#FDE7A2"} size={680} x={lerp(tx,1450,ground)+2000*leave} y={lerp(ty,690,ground)} angle={32*beat(t)} scale={ground}/>
    {t<e.close?<Phone id={isPlay?'play':'numbers'} underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{...measureState,counterValue:45}:{compassHeading:45,levelRoll:0,levelPitch:0}}
      state={isPlay?playState:measureState} x={lerp(1320,540,move)} y={540} scale={.82} angle={0}
      reveal={chapterReveal} revealFrom={tab}/>:null}
    {t<e.cues.at17_7?<QuickCaption seconds={t} exit={ease(t,e.measure,e.cues.at17_7)}/>:null}
    {!isPlay?<>
      <div style={{transform:`translateX(${2200*ease(t,e.cues.at21_72,e.play)}px)`}}><Headline seconds={t} text="Measure." x={1060} y={160} width={740} size={142} progress={ease(t,e.cues.at18_1,e.cues.at18_38)}/></div>
      <div style={{position:'absolute',left:1060,top:900,display:'flex',gap:22,fontSize:40,fontWeight:600,transform:`translate(${2200*ease(t,e.cues.at21_46,e.cues.at21_98)}px,${1000*(1-ease(t,e.measureHeroes[0],e.measureHeroes[0]+.3))}px)`}}>
        {MEASURE_HEROES.map(hero=><span key={hero.word} style={{color:hero.word===active?'#3B2D14':'rgba(59,45,20,.45)',fontWeight:hero.word===active?700:450}}>{hero.word}</span>)}
      </div>
    </>:<>
      <div style={{transform:`translateY(${-1000*leave}px)`}}><Headline seconds={t} text="Let chance decide." x={1060} y={140} width={740} size={112} progress={ease(t,e.cues.at22_56,e.cues.at22_86)}/>
      <div data-hero-part="wheel" style={{position:'absolute',left:0,top:0,transform:`translate(1430px,${lerp(lerp(1180,660,pop(t,e.cues.at22_56,.8)),530,settle)}px) translate(-50%,-50%) rotate(${lerp(-5,0,ease(t,e.cues.at22_56,e.cues.at23_4))}deg) scale(${lerp(1.7,1.3,settle)*(1+.04*(t>=e.wheelEnd&&t<e.cues.at24_54?beat(t):0))})`}}>
        <LiveCrop screen="play" selector=".play-wheel-card" state={{...playState,searchQuery:'Decision Wheel'}} style={{borderRadius:28}}/>
      </div>
      {CHANCE.map(card=>t>=card.at?<div key={card.tool} data-hero-part={card.tool} style={{position:'absolute',left:0,top:0,transform:`translate(${card.x}px,${lerp(1260,840,pop(t,card.at,.5))}px) translate(-50%,-50%) rotate(${lerp(8,0,ease(t,card.at,card.at+.5))}deg) scale(${card.scale*(1+.06*beat(t))})`}}>
        <LiveCrop screen="play" selector={`[data-tool="${card.tool}"]`} state={{...playState,searchQuery:''}} style={{borderRadius:28}}/>
      </div>:null)}</div>
    </>}
    {MEASURE_HEROES.map(hero=>t>=hero.in&&t<hero.out+.22?<div key={hero.tool} data-hero-part={hero.tool} style={{position:'absolute',left:0,top:0,transform:heroTransform(t,hero.in,hero.out,1430,600,1.75)}}>
      <LiveCrop screen="numbers" selector={`[data-tool="${hero.tool}"]`} state={{...measureState,searchQuery:''}} style={{borderRadius:28}}/>
    </div>:null)}
  </>;
};
