import React,{useCallback,useState} from 'react';
import {Geometry,Rect,LiveCrop,isolatedRadii,radiusCss} from './Kit';
import {Phone,PhonePose,Shape,CameraRig,View,KineticLine,projectPhone,projectPart,mixView,beat,ease,lerp,pop} from './visuals';
import {quickPhonePose,quickState,quickView} from './OpeningJourney';
import timeline from './timeline.json';
const e=timeline.events;

const stateAt=(t:number)=>({
  counterValue:42+e.measureActions.counter.filter(at=>t>=at).length,
  protractorAngle:Math.round(lerp(20,135,ease(t,...e.measureActions.angle as [number,number]))),
  converterInput:Math.round(lerp(10,25,ease(t,...e.measureActions.converter as [number,number]))),
  billAmount:120,splitPeople:t>=e.measureActions.split[0]?'4':'2',
});
const playAt=(t:number)=>({wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260*ease(t,e.wheelStart,e.wheelEnd),wheelWinner:t>=e.wheelEnd?'Sushi':'',randomResult:t<e.chance[0]?null:73,diceResult:t>=e.chance[1]?5:null,coinSide:t>=e.chance[2]?'Heads':null});

/** A real crop keeps its measured native rectangle while the camera changes the framing. */
const Hero:React.FC<{tool:string;screen:string;rect:Rect;state:Record<string,unknown>;pose:PhonePose;radii:number[];seconds:number;clip?:string}>=({tool,screen,rect,state,pose,radii,seconds:t,clip})=><div data-hero-part={tool} style={{position:'absolute',width:rect.width,height:rect.height,borderRadius:radiusCss(radii),clipPath:clip,transformOrigin:'50% 50%',transform:`translate(${pose.x-rect.width/2}px,${pose.y-rect.height/2}px) rotate(${pose.angle}deg) scale(${pose.scale*(1+.022*beat(t,'hero'))})`,boxShadow:'0 16px 32px rgba(59,45,20,.14)'}}><LiveCrop screen={screen} selector={`[data-tool="${tool}"]`} state={state} radii={radii}/></div>;

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
  const wheelPose={x:lerp(lerp(1440,960,push)-offset.x,960,wide)+1900*(1-enter),y:lerp(540-offset.y,330,wide),scale:lerp(focusScale,2.5,wide),angle:0};
  const bounds=wheelRect&&wheelFace?[wheelFace.y-wheelRect.y,wheelRect.x+wheelRect.width-wheelFace.x-wheelFace.width,wheelRect.y+wheelRect.height-wheelFace.y-wheelFace.height,wheelFace.x-wheelRect.x]:[0,0,0,0];
  const wheelClip=`inset(${bounds.map(n=>Math.max(0,n)*(1-wide)).join('px ')}px round ${lerp((wheelFace?.width??0)/2,28,wide)}px)`;
  const view:View=t<e.measureShots[0].at?mixView(quickView(e.measure,geometry),active.view,ease(t,e.measure,e.measureShots[0].at)):
    isPlay?{x:lerp(960,980,wide),y:lerp(540,520,wide),scale:lerp(1,1.03,wide),angle:lerp(-2,0,push)*(1-wide)}:
    mixView(active.view,active.to,ease(t,active.at+shot.settle,active.end));
  return <>
    {t<e.measureShots[0].at+.2?<KineticLine seconds={t} text="Measure." notes={e.type['Measure.']} x={96} y={260} size={300} exit={ease(t,e.measureShots[0].at-.12,e.measureShots[0].at+.2)}/>:null}
    <CameraRig view={view}>
      <Phone seconds={t} id={isPlay?'play':'numbers'} state={isPlay?playState:measureState} {...phone}
        underId={isPlay?'numbers':'quick-tools'} underState={isPlay?stateAt(e.play):quickState(e.measure)} transition={transition}
        revealFrom={{x:nav.x+nav.width/2,y:nav.y+nav.height/2}} focusSelector={`[data-tool="${isPlay?'decision_wheel':active.tool}"]`}
        hiddenParts={isPlay?['decision_wheel','random_number','dice_roll','coin_flip']:e.measureShots.map(s=>s.tool)} onParts={measured}/>
      {!isPlay?e.measureShots.map((s,i)=>{
        const r=parts[s.tool],next=e.measureShots[i+1]?.at??e.play;
        if(!r||t<(i?s.at-.14:e.measure)||t>=next+.24)return null;
        const enter=ease(t,i?s.at-.14:e.measure,s.at+.28),out=ease(t,next-.14,next+.24);
        const source=projectPhone(r,origin,t),scale=Math.min(s.height/r.height,1720/r.width);
        const p={x:lerp(i?s.x+1900:source.x,s.x,enter)-2400*out,y:lerp(i?s.y:source.y,s.y,enter),scale:lerp(i?scale*.85:source.scale,scale,enter),angle:lerp(i?s.angle+8:origin.angle,s.angle,enter)};
        return <React.Fragment key={s.tool}>
          <Shape name={i%2?'soft_burst':'flower'} color={i%2?'#BBDCD3':'#F6A800'} size={1000} x={p.x} y={p.y} scale={enter*(1+.04*beat(t,'hero'))} angle={s.angle+8*beat(t,'hero')} seconds={t}/>
          <Hero seconds={t} tool={s.tool} screen="numbers" rect={r} state={measureState} pose={p} radii={isolatedRadii(r,enter)}/>
        </React.Fragment>;
      }):null}
      {isPlay&&wheelRect?(()=>{
        const enter=ease(t,e.wheelEntry-.14,e.wheelEntry+.28),p=wheelPose;
        return <><Shape name="soft_burst" color="#D6AFBF" size={1100} x={960} y={570} scale={enter*(1+.05*beat(t,'hero'))} angle={40*ease(t,e.wheelStart,e.wheelEnd)} seconds={t}/><Hero seconds={t} tool="decision_wheel" screen="play" rect={wheelRect} state={playState} pose={p} radii={[28,28,28,28]} clip={wheelClip}/></>;
      })():null}
      {isPlay?['random_number','dice_roll','coin_flip'].map((tool,i)=>{
        const r=parts[tool],at=e.chance[i];if(!r||t<at)return null;
        const p=pop(t,at,.4),scale=Math.min(480/r.width,390/r.height);
        return <Hero key={tool} seconds={t} tool={tool} screen="play" rect={r} state={playState} radii={[28,28,28,28]} pose={{x:400+560*i,y:950-r.height*scale/2+1000*(1-p),scale:scale*lerp(.7,1,p),angle:lerp(i%2?-12:12,0,p)}}/>;
      }):null}
    </CameraRig>

    {!isPlay&&t>=active.at?<KineticLine seconds={t} text={active.word} notes={[active.at]} x={active.textX} y={active.textY} size={active.textSize} width={800} exit={ease(t,active.end-.14,active.end+.1)}/>:null}
    {isPlay&&t<e.wheelStart+.28?<KineticLine seconds={t} text="Let chance decide." notes={e.type['Let chance decide.']} x={96} y={120} width={1050} size={220} exit={ease(t,e.wheelStart,e.wheelStart+.28)}/>:null}
  </>;
};
