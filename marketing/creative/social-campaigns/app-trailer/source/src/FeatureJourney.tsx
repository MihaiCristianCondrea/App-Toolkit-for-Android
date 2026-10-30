import React,{useCallback,useState} from 'react';
import {Geometry,Rect,LiveCrop,DISPLAY,SKIN_WIDTH,SKIN_HEIGHT} from './Kit';
import {Headline,Phone,Shape,beat,ease,lerp} from './visuals';
import {QuickCaption} from './OpeningJourney';
import timeline from './timeline.json';

const e=timeline.events;
const HEROES=[
  {tool:'protractor',word:'Angles.',in:e.measureHeroes[0],out:e.heroReturns[0],end:e.measureHeroes[1]},
  {tool:'unit_converter',word:'Units.',in:e.measureHeroes[1],out:e.heroReturns[1],end:e.measureHeroes[2]},
  {tool:'bill_splitter',word:'Bills.',in:e.measureHeroes[2],out:e.heroReturns[2],end:e.measureHeroes[3]},
  {tool:'counter',word:'Counts.',in:e.measureHeroes[3],out:e.heroReturns[3],end:e.play},
];
const CHANCE=[{tool:'random_number',at:e.chance[0],x:1230,scale:.7},{tool:'dice_roll',at:e.chance[1],x:1490,scale:.8},{tool:'coin_flip',at:e.chance[2],x:1690,scale:.8}];
const project=(r:Rect,x:number)=>({x:x+(-SKIN_WIDTH/2+DISPLAY.x+r.x+r.width/2)*.82,y:540+(-SKIN_HEIGHT/2+DISPLAY.y+r.y+r.height/2)*.82});

/** A crop replaces its hidden screen counterpart, then returns to that measured rectangle. */
const Container:React.FC<{tool:string;screen:string;rect:Rect;state:Record<string,unknown>;phoneX:number;progress:number;x:number;y:number;scale:number}>=({tool,screen,rect,state,phoneX,progress,x,y,scale})=>{
  const source=project(rect,phoneX),s=lerp(.82,scale,progress);
  return <div data-hero-part={tool} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${lerp(source.x,x,progress)-rect.width/2}px,${lerp(source.y,y,progress)-rect.height/2}px) scale(${s})`,boxShadow:`0 ${12*progress}px ${28*progress}px rgba(59,45,20,${.12*progress})`}}>
    <LiveCrop screen={screen} selector={`[data-tool="${tool}"]`} state={{...state,searchQuery:''}} style={{borderRadius:lerp(8,28,progress)}}/>
  </div>;
};

export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const [parts,setParts]=useState<Geometry>({});
  const measured=useCallback((g:Geometry)=>setParts(old=>JSON.stringify(old)===JSON.stringify(g)?old:g),[]);
  const isPlay=t>=e.play,move=ease(t,e.cues.at17_7,e.cues.at18_1),phoneX=lerp(1320,540,move);
  const active=[...HEROES].reverse().find(h=>t>=h.in&&t<h.end);
  const count=42+[e.cues.at20_84,e.cues.at21_12,e.cues.at21_46].filter(at=>t>=at).length;
  const measureState={counterValue:count,protractorAngle:Math.round(lerp(20,135,ease(t,e.cues.at18_38,e.cues.at18_92))),converterInput:Math.round(lerp(10,25,ease(t,e.cues.at19_42,e.cues.at19_88))),splitTotal:'120',splitPeople:t>=e.cues.at20_56?'4':'2'};
  const shuffle=Math.floor(t*30),playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260*ease(t,e.wheelStart,e.wheelEnd),wheelWinner:t>=e.wheelEnd?'Sushi':'',randomResult:t<e.chance[0]?null:t<e.chance[1]?1+(shuffle*37)%100:73,diceResult:t<e.chance[1]||t>=e.chance[2]?5:1+(shuffle*5)%6,coinSide:t>=e.chance[2]&&t<e.cues.at25_68?'Tails':'Heads'};
  const transition=ease(t,isPlay?e.play:e.measure,(isPlay?e.play:e.measure)+.3);
  const nav=geometry?.[isPlay?'Play & Decide':'Measure'];
  if(!nav)throw new Error('Missing measured tab geometry');
  const tab={x:nav.x+nav.width/2,y:nav.y+nav.height/2};
  const returnAll=1-ease(t,e.chanceReturn,e.close),wheelVisible=isPlay&&t>=e.wheelEntry&&t<e.close;
  const hidden=isPlay?[...(wheelVisible?['decision_wheel']:[]),...CHANCE.filter(c=>t>=c.at&&t<e.close).map(c=>c.tool)]:active?[active.tool]:[];
  const leave=ease(t,e.close,e.close+.4),ground=ease(t,isPlay?e.wheelEntry:e.measureHeroes[0],(isPlay?e.wheelEntry:e.measureHeroes[0])+.3);
  const heroRect=parts[isPlay?'decision_wheel':'protractor'];
  const source=heroRect?project(heroRect,phoneX):{x:1430,y:600};
  return <>
    <Shape name={isPlay?'soft_burst':'flower'} color={isPlay?'#E8D3DC':'#FDE7A2'} size={680} x={lerp(source.x,1430,ground)+2000*leave} y={lerp(source.y,690,ground)} scale={ground}/>
    {t<e.close?<Phone id={isPlay?'play':'numbers'} state={isPlay?playState:measureState} x={phoneX} y={540} scale={.82}
      underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{counterValue:45,protractorAngle:135,converterInput:25,splitTotal:'120',splitPeople:'4'}:{compassHeading:45,levelRoll:0,levelPitch:0}}
      transition={transition} revealFrom={tab} underFocusSelector={isPlay?'[data-tool="counter"]':undefined} focusSelector={`[data-tool="${isPlay?'decision_wheel':active?.tool??'protractor'}"]`} hiddenParts={hidden} onParts={measured}/>:null}
    {t<e.cues.at17_7?<QuickCaption seconds={t} exit={ease(t,e.measure,e.cues.at17_7)}/>:null}
    {!isPlay?<>
      <div style={{transform:`translateX(${2200*ease(t,e.cues.at21_72,e.play)}px)`}}><Headline seconds={t} text="Measure." x={1060} y={160} width={740} size={142}/></div>
      <div style={{position:'absolute',left:1060,top:900,display:'flex',gap:22,fontSize:40,transform:`translate(${2200*ease(t,e.cues.at21_46,e.play)}px,${1000*(1-ease(t,e.measureHeroes[0],e.measureHeroes[0]+.3))}px)`}}>{HEROES.map(h=><span key={h.tool} style={{fontWeight:h===active?700:450,color:h===active?'#3B2D14':'rgba(59,45,20,.45)'}}>{h.word}</span>)}</div>
      {HEROES.map(h=>t>=h.in&&t<h.end&&parts[h.tool]?<Container key={h.tool} tool={h.tool} screen="numbers" rect={parts[h.tool]} state={measureState} phoneX={phoneX} progress={ease(t,h.in,h.in+.3)*(1-ease(t,h.out,h.end))} x={1430} y={600} scale={1.75}/>:null)}
    </>:<>
      <div style={{transform:`translateY(${-1000*leave}px)`}}><Headline seconds={t} text="Let chance decide." x={1060} y={140} width={740} size={112}/></div>
      {wheelVisible&&parts.decision_wheel?<Container tool="decision_wheel" screen="play" rect={parts.decision_wheel} state={playState} phoneX={phoneX} progress={ease(t,e.wheelEntry,e.cues.at22_86)*returnAll} x={1430} y={lerp(660,530,ease(t,e.wheelEnd,e.chance[0]))} scale={lerp(1.7,1.3,ease(t,e.wheelEnd,e.chance[0]))}/>:null}
      {CHANCE.map(c=>t>=c.at&&t<e.close&&parts[c.tool]?<Container key={c.tool} tool={c.tool} screen="play" rect={parts[c.tool]} state={playState} phoneX={phoneX} progress={ease(t,c.at,c.at+.3)*returnAll} x={c.x} y={840} scale={c.scale}/>:null)}
    </>}
  </>;
};
