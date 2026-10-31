import React,{useCallback,useState} from 'react';
import {Geometry,Rect,LiveCrop} from './Kit';
import {Headline,Phone,PhonePose,Shape,projectPhone,beat,ease,lerp,pop} from './visuals';
import {QuickCaption,quickPhonePose,quickState} from './OpeningJourney';
import timeline from './timeline.json';

const e=timeline.events;
const HEROES=[
  {tool:'protractor',word:'Angles.',in:e.measureHeroes[0],out:e.heroReturns[0],end:e.measureHeroes[1]},
  {tool:'unit_converter',word:'Units.',in:e.measureHeroes[1],out:e.heroReturns[1],end:e.measureHeroes[2]},
  {tool:'bill_splitter',word:'Bills.',in:e.measureHeroes[2],out:e.heroReturns[2],end:e.measureHeroes[3]},
  {tool:'counter',word:'Counts.',in:e.measureHeroes[3],out:e.heroReturns[3],end:e.play},
];
const CHANCE=[{tool:'random_number',at:e.chance[0],x:1140,scale:.85,angle:-4},{tool:'dice_roll',at:e.chance[1],x:1430,scale:1.05,angle:5},{tool:'coin_flip',at:e.chance[2],x:1700,scale:.95,angle:-5}];
const heroProgress=(t:number,h:typeof HEROES[number])=>ease(t,h.in,h.in+.3)*(1-ease(t,h.out,h.end));

/** A crop replaces its hidden screen counterpart, then returns to that measured rectangle. */
const Container:React.FC<{tool:string;screen:string;rect:Rect;state:Record<string,unknown>;pose:PhonePose;seconds:number;progress:number;x:number;y:number;scale:number;angle?:number}>=({tool,screen,rect,state,pose,seconds,progress,x,y,scale,angle=0})=>{
  const source=projectPhone(rect,pose,seconds),s=lerp(source.scale,scale,progress);
  return <div data-hero-part={tool} style={{position:'absolute',width:rect.width,height:rect.height,transformOrigin:'50% 50%',transform:`translate(${lerp(source.x,x,progress)-rect.width/2}px,${lerp(source.y,y,progress)-rect.height/2}px) rotate(${lerp(pose.angle,angle,progress)}deg) scale(${s})`,boxShadow:`0 ${18*progress}px ${36*progress}px rgba(59,45,20,${.14*progress})`}}>
    <LiveCrop screen={screen} selector={`[data-tool="${tool}"]`} state={{...state,searchQuery:''}} style={{borderRadius:lerp(8,28,progress)}}/>
  </div>;
};

export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry|null}>=({seconds:t,geometry})=>{
  const [parts,setParts]=useState<Geometry>({});
  const measured=useCallback((g:Geometry)=>setParts(old=>JSON.stringify(old)===JSON.stringify(g)?old:g),[]);
  const isPlay=t>=e.play,move=ease(t,e.phonePan,e.phonePanEnd);
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
  const leave=ease(t,e.close,e.close+.4),ground=isPlay?ease(t,e.wheelEntry,e.cues.at22_86)*returnAll:active?heroProgress(t,active):0;
  const from=quickPhonePose(e.measure),to=e.closingOrigin,resolve=ease(t,e.wheelEnd,e.chance[0]);
  const offstage=isPlay?resolve*(1-ease(t,e.chanceReturn,e.close)):0;
  const pose={x:lerp(from.x,to.x,move)-55*ground-900*offstage,y:lerp(from.y,to.y,move),scale:lerp(from.scale,to.scale,move)*(1-.2*offstage),angle:lerp(from.angle,to.angle,move)+2*ground+12*offstage};
  const shot=isPlay?{x:lerp(1370,550,resolve),y:650,scale:2,angle:lerp(-3,-7,resolve)}:e.featureShots[active?HEROES.indexOf(active):0];
  const heroRect=parts[isPlay?'decision_wheel':active?.tool??'protractor'];
  const source=heroRect?projectPhone(heroRect,pose,t):{x:1430,y:600};
  return <>
    <Shape name={isPlay?'soft_burst':'flower'} color={isPlay?'#E8D3DC':'#FDE7A2'} size={820} x={lerp(source.x,shot.x,ground)+2000*leave} y={lerp(source.y,shot.y,ground)} scale={ground*(1+.055*beat(t,'phone'))} angle={lerp(-35,shot.angle,ground)+9*beat(t,'phone')} seconds={t}/>
    {t<e.close?<Phone seconds={t} id={isPlay?'play':'numbers'} state={isPlay?playState:measureState} x={pose.x} y={pose.y} scale={pose.scale} angle={pose.angle}
      underId={isPlay?'numbers':'quick-tools'} underState={isPlay?{counterValue:45,protractorAngle:135,converterInput:25,splitTotal:'120',splitPeople:'4'}:quickState(e.measure)}
      transition={transition} revealFrom={tab} underFocusSelector={isPlay?'[data-tool="counter"]':undefined} focusSelector={`[data-tool="${isPlay?'decision_wheel':active?.tool??'protractor'}"]`} hiddenParts={hidden} onParts={measured}/>:null}
    {t<e.cues.at17_7?<QuickCaption seconds={t} exit={ease(t,e.measure,e.cues.at17_7)}/>:null}
    {!isPlay?<>
      <div style={{transform:`translateX(${2200*ease(t,e.measureHeroes[3],e.cues.at21_12)}px)`}}><Headline seconds={t} text="Measure." x={1060} y={110} width={740} size={132}/></div>
      {HEROES.map((h,i)=>t>=h.in&&t<h.end?<React.Fragment key={h.tool}>
        <div style={{position:'absolute',left:1060,top:280,width:740,overflow:'hidden',fontSize:54,fontWeight:650,color:'#006A60'}}><div style={{transform:`translateY(${140*(1-pop(t,h.in,.32))-140*ease(t,h.out,h.end)}%)`}}>{h.word}</div></div>
        {parts[h.tool]?<Container seconds={t} tool={h.tool} screen="numbers" rect={parts[h.tool]} state={measureState} pose={pose} progress={heroProgress(t,h)} {...e.featureShots[i]}/>:null}
      </React.Fragment>:null)}
    </>:<>
      <div style={{transform:`translateY(${-1000*leave}px)`}}><Headline seconds={t} text="Let chance decide." x={1060} y={140} width={740} size={112}/></div>
      {wheelVisible&&parts.decision_wheel?<Container seconds={t} tool="decision_wheel" screen="play" rect={parts.decision_wheel} state={playState} pose={pose} progress={ease(t,e.wheelEntry,e.cues.at22_86)*returnAll} x={shot.x} y={shot.y} scale={shot.scale} angle={shot.angle}/>:null}
      {CHANCE.map(c=>t>=c.at&&t<e.close&&parts[c.tool]?<Container seconds={t} key={c.tool} tool={c.tool} screen="play" rect={parts[c.tool]} state={playState} pose={pose} progress={ease(t,c.at,c.at+.3)*returnAll} x={c.x} y={710} scale={c.scale} angle={c.angle}/>:null)}
    </>}
  </>;
};
