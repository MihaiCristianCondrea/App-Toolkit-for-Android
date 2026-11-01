import React,{useCallback,useState} from 'react';
import {Geometry,isolatedRadii} from './Kit';
import {Phone,Shape,Hero,TYPE,CameraRig,View,KineticLine,projectPhone,projectPart,mixView,beat,ease,lerp,pop,closeupView,projectedCard,projectPoint,GroundWipe} from './visuals';
import {quickPhonePose,quickState,quickView} from './OpeningJourney';
import timeline from './timeline.json';
const e=timeline.events;

const stateAt=(t:number)=>({
  counterValue:42+e.measureActions.counter.filter(at=>t>=at).length,
  protractorAngle:Math.round(lerp(20,135,ease(t,...e.measureActions.angle as [number,number]))),
  converterInput:Math.round(lerp(10,25,ease(t,...e.measureActions.converter as [number,number]))),
  billAmount:120,splitPeople:t>=e.measureActions.split[0]?'4':'2',
});
const playAt=(t:number)=>({wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260*ease(t,e.wheelStart,e.wheelEnd),wheelWinner:t>=e.wheelEnd?'Sushi':'',randomResult:t<e.chance[0]?null:t<e.chanceLayout.values[1]?72:73,diceResult:t>=e.chance[1]?5:null,coinSide:t<e.chance[2]?null:t<e.chanceLayout.values[0]?'Tails':'Heads'});

export const FeatureJourney:React.FC<{seconds:number;geometry:Geometry}>=({seconds:t,geometry})=>{
  const [parts,setParts]=useState<Geometry>({});
  const measured=useCallback((g:Geometry)=>setParts(old=>JSON.stringify(old)===JSON.stringify(g)?old:g),[]);
  const isPlay=t>=e.play,active=[...e.measureShots].reverse().find(s=>t>=s.at)??e.measureShots[0];
  const transition=ease(t,isPlay?e.play:e.measure,(isPlay?e.play:e.measure)+.3);
  const nav=geometry[isPlay?'Play & Decide':'Measure'];
  if(!nav)throw new Error('Missing measured tab geometry');
  const origin=quickPhonePose(e.measure),depart=ease(t,e.measure+.22,e.measure+.78);
  const phone={...origin,x:origin.x-3000*depart};
  const shot=e.shots.find(s=>s.at===active.at)!;
  const measureState=stateAt(t),playState=playAt(t);
  const wide=ease(t,e.wheelWide,e.wheelWideEnd),wheelRect=parts.decision_wheel;
  const wheelFace=parts['decision_wheel-face'];
  const push=ease(t,e.wheelStart,e.wheelStart+.3),diameter=lerp(850,960,push);
  const focusScale=wheelFace?diameter/wheelFace.height:1;
  const offset=wheelRect&&wheelFace?projectPart(wheelFace,wheelRect,{x:0,y:0,scale:focusScale,angle:0}):{x:0,y:0};
  const enter=ease(t,e.wheelEntry-.14,e.wheelEntry+.28);
  const wheelPose={x:lerp(lerp(1440,960,push)-offset.x,960,wide)+1900*(1-enter),y:lerp(540-offset.y,270,wide),scale:lerp(focusScale,2.5,wide),angle:0};
  const bounds=wheelRect&&wheelFace?[wheelFace.y-wheelRect.y,wheelRect.x+wheelRect.width-wheelFace.x-wheelFace.width,wheelRect.y+wheelRect.height-wheelFace.y-wheelFace.height,wheelFace.x-wheelRect.x]:[0,0,0,0];
  const wheelClip=`inset(${bounds.map(n=>Math.max(0,n)*(1-wide)).join('px ')}px round ${lerp((wheelFace?.width??0)/2,28,wide)}px)`;
  const baseView:View=t<e.measureShots[0].at?mixView(quickView(e.measure,geometry),active.view,ease(t,e.measure,e.measureShots[0].at)):
    isPlay?{x:lerp(960,980,wide),y:lerp(540,520,wide),scale:lerp(1,1.03,wide),angle:lerp(-2,0,push)*(1-wide)}:
    mixView(active.view,active.to,ease(t,active.at+shot.settle,active.end));
  const activeRect=parts[active.tool];
  const view=!isPlay&&t>=e.measureShots[0].at&&activeRect?closeupView(activeRect,{x:active.x,y:active.y,scale:Math.min(active.height/activeRect.height,1720/activeRect.width),angle:active.angle},baseView,{x:active.x,y:active.tool==='counter'?active.y:e.polish.crop.screenY}):baseView;
  const actors=e.measureShots.flatMap((s,i)=>{
    const r=parts[s.tool],next=e.measureShots[i+1]?.at??e.play;
    if(!r||t<(i?s.at-e.polish.pushLead:e.measure)||t>=next+e.polish.pushOut)return [];
    const enter=ease(t,i?s.at-e.polish.pushLead:e.measure,s.at+e.polish.pushEnter),out=ease(t,next,next+e.polish.pushOut),source=projectPhone(r,origin,t),scale=Math.min(s.height/r.height,1720/r.width);
    const pose={x:lerp(i?s.x+e.polish.pushTravel:source.x,s.x,enter)-e.polish.pushTravel*out,y:lerp(i?s.y:source.y,s.y,enter),scale:lerp(i?scale*.85:source.scale,scale,enter),angle:lerp(i?s.angle+8:origin.angle,s.angle,enter)};
    const bounds=projectedCard(r,{...pose,y:pose.y+e.polish.hitOffset*beat(t,'hero')},view);
    if(out>0&&(bounds.left<0||bounds.right>1920||bounds.top<0||bounds.bottom>1080))return [];
    return [{s,r,enter,out,pose,next,visible:bounds.left>=0&&bounds.right<=1920&&bounds.top>=0&&bounds.bottom<=1080}];
  });
  const visibleActors=actors.filter(a=>a.visible);
  const chanceTools=['random_number','dice_roll','coin_flip'];
  const row=chanceTools.map(tool=>{
    const r=parts[tool];return r?{tool,r,scale:Math.min(e.chanceLayout.width/r.width,e.chanceLayout.height/r.height)}:null;
  });
  const rowWidth=row.reduce((sum,c)=>sum+(c?c.r.width*c.scale:0),0)+e.chanceLayout.gap*(chanceTools.length-1);
  let rowX=(1920-rowWidth)/2;
  return <>
    {e.wipes.filter(w=>e.measureShots.some(s=>s.at===w.at)||w.at===e.play).map(w=>{
      const actor=actors.find(a=>a.s.at===w.at),from=actor?projectPoint(actor.pose,view):{x:w.x,y:w.y};
      return <GroundWipe key={w.at} at={w.at-e.measure} from={from} color={w.color} shape={w.shape}/>;
    })}
    <CameraRig view={view}>
      {!isPlay?actors.map(({s,enter,pose})=><Shape key={s.tool} name={s.shape} color={s.ground} size={1000} x={pose.x} y={pose.y} scale={enter*(1+.04*beat(t,'hero'))} angle={s.angle+8*beat(t,'hero')} seconds={t}/>):null}
      {isPlay?<Shape name="soft_burst" color="#D6AFBF" size={1100} x={960} y={570} scale={enter*(1+.05*beat(t,'hero'))} angle={40*ease(t,e.wheelStart,e.wheelEnd)} seconds={t}/>:null}
    </CameraRig>
    {t<e.measureShots[0].at+.2?<KineticLine seconds={t} text="Measure." notes={e.type['Measure.']} x={96} y={260} size={TYPE.chapter.size} exit={ease(t,e.measureShots[0].at-.12,e.measureShots[0].at+.2)}/>:null}
    {!isPlay?visibleActors.map(({s,enter,out})=>t>=s.at?<div key={s.word} style={{position:'absolute',inset:0,transform:`translateX(${e.polish.wordTravel*(1-enter-out)}px)`}}><KineticLine seconds={t} text={s.word} notes={[s.at]} x={s.textX} y={s.textY} size={TYPE.chapter.size} width={800} exit={out}/></div>:null):null}
    {isPlay&&t<e.wheelStart+.28?<KineticLine seconds={t} text="Let chance decide." notes={e.type['Let chance decide.']} x={96} y={120} width={1050} size={TYPE.chapter.size} exit={ease(t,e.wheelStart,e.wheelStart+.28)}/>:null}
    {isPlay?row.map((c,i)=>{
      if(!c)return null;
      const {tool,r,scale}=c,width=r.width*scale,x=rowX+width/2;rowX+=width+e.chanceLayout.gap;
      const at=e.chance[i];if(t<at)return null;
      const p=pop(t,at,e.chanceLayout.land),q=e.chanceLayout;
      return <Hero key={tool} seconds={t} tool={tool} screen="play" rect={r} state={playState} radii={[28,28,28,28]} nudge={false} level={3-ease(t,at,at+q.land)} pose={{x:lerp(q.sourceX,x,p),y:lerp(q.sourceY,q.baseline-r.height*scale/2,p),scale:lerp(q.sourceScale,scale,p),angle:lerp(i%2?-q.startAngle:q.startAngle,0,p)}}/>;
    }):null}
    <CameraRig view={view}>
      <Phone seconds={t} id={isPlay?'play':'numbers'} state={isPlay?playState:measureState} {...phone}
        underId={isPlay?'numbers':'quick-tools'} underState={isPlay?stateAt(e.play):quickState(e.measure)} transition={transition}
        revealFrom={{x:nav.x+nav.width/2,y:nav.y+nav.height/2}} focusSelector={`[data-tool="${isPlay?'decision_wheel':active.tool}"]`}
        hiddenParts={isPlay?['decision_wheel','random_number','dice_roll','coin_flip']:actors.map(a=>a.s.tool)} onParts={measured}/>
      {!isPlay?visibleActors.map(({s,r,enter,pose})=><Hero key={s.tool} seconds={t} tool={s.tool} screen="numbers" rect={r} state={measureState} pose={pose} radii={isolatedRadii(r,enter)}/>):null}
      {isPlay&&wheelRect?<Hero seconds={t} tool="decision_wheel" screen="play" rect={wheelRect} state={playState} pose={wheelPose} radii={[28,28,28,28]} clip={wheelClip} nudge={t<e.wheelWideEnd}/>:null}
    </CameraRig>
  </>;
};
