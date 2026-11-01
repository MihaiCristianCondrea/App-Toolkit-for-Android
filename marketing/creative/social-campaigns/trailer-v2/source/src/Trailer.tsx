import React,{useCallback,useState} from 'react';
import {AbsoluteFill,Audio,Img,staticFile,useCurrentFrame,useVideoConfig} from 'remotion';
import {shapeUrl} from '../../../../../ui/src/kit/assets.js';
import {DISPLAY,Geometry,GeometryProbe,LiveCrop,LiveScreen,Rect,SKIN_HEIGHT,SKIN_WIDTH,WIDGETS} from './Kit';
import {clamp,ease,easeIn,lerp,pop} from './motion';
import music from './music-analysis.json';

/**
 * Trailer v2: Standalone production cut.
 * Story: motif shapes, the mark pops and opens like a chest, four live tools rise out of it and
 * lock into a bento, the bento becomes the phone, then Measure and Play open from their tabs,
 * the three destinations fan out and a gold burst ends the film.
 */
const INK='#3B2D14',CREAM='#FFF4D9',TEAL='#D0E9E3',GOLD='#FDE7A2',PINK='#F3D9E4',BOOM='#F2C94C';
const ONSETS=music.onsetsSeconds as number[];
/** A short, exact accent on every onset; nothing moves between notes. */
const hit=(t:number)=>{let last=-1;for(const o of ONSETS){if(o>t)break;last=o;}return last<0?0:Math.exp(-(t-last)*12);};
const steps=(t:number,times:number[])=>times.filter(at=>t>=at).length;

// Shots (seconds, all on detected onsets).
const T={motif:[.02,.32,.62,1.04],mark:1.76,lid:2.32,toBottom:2.64,launches:[3.32,4.04,4.58,5.16],lock:5.74,
  place:6.76,placeWords:[7.06,7.46],phone:10.08,phoneEnd:10.9,quick:[10.9,11.44],lifts:[12,12.92,13.72,14.6,15.44,16.44],
  measure:17.14,measureWords:[17.36],measureHeroes:[17.92,18.84,19.7,20.56],play:21.98,playWords:[22.26,22.56],spin:[22.86,24.54],
  chance:[24.54,25.12,25.4,25.68],close:26.26,fan:[26.26,26.5,26.82],closeWords:[27.04,27.26],badge:27.68,boom:29.4,boomCover:29.74,end:29.88};

const Shape:React.FC<{name:string;color:string;size:number;x:number;y:number;angle?:number;scale?:number}>=({name,color,size,x,y,angle=0,scale=1})=>
  <div aria-hidden="true" style={{position:'absolute',left:0,top:0,width:size,height:size,background:color,maskImage:`url("${shapeUrl(name)}")`,WebkitMaskImage:`url("${shapeUrl(name)}")`,maskSize:'contain',WebkitMaskSize:'contain',maskRepeat:'no-repeat',transform:`translate(${x-size/2}px,${y-size/2}px) rotate(${angle}deg) scale(${scale})`}}/>;

/** A ground grows from the thing that caused it, then stays as the new background. */
const Wipe:React.FC<{t:number;at:number;color:string;x:number;y:number;shape?:string;spin?:number}>=({t,at,color,x,y,shape='cookie_9sided',spin=60})=>{
  if(t<at)return null;
  const reach=Math.max(Math.hypot(x,y),Math.hypot(1920-x,y),Math.hypot(x,1080-y),Math.hypot(1920-x,1080-y))*2.3/200;
  const p=ease(t,at,at+.5);
  return <Shape name={shape} color={color} size={200} x={x} y={y} scale={p*reach} angle={spin*(1-p)}/>;
};

/** Words rise through their own masks, each on its note, widening as they land. */
const Words:React.FC<{t:number;text:string;at:number[];x:number;y:number;size:number;width:number;exitAt?:number;exitX?:number}>=({t,text,at,x,y,size,width,exitAt,exitX=-1600})=>{
  const out=exitAt===undefined?0:easeIn(t,exitAt,exitAt+.3);
  return <div style={{position:'absolute',left:x,top:y,width,fontFamily:'Google Sans Flex',fontSize:size,lineHeight:1,letterSpacing:-size*.04,color:INK,transform:`translateX(${exitX*out}px)`}}>
    {text.split(' ').map((word,i)=>{
      const p=pop(t,at[Math.min(i,at.length-1)],.5);
      return <span key={i} style={{display:'inline-block',overflow:'hidden',verticalAlign:'top',paddingBottom:size*.18,marginRight:size*.22}}>
        <span style={{display:'inline-block',transform:`translateY(${(1-p)*115}%)`,fontVariationSettings:`'wght' ${lerp(500,760,clamp(p))}, 'wdth' ${lerp(80,100,clamp(p))}`}}>{word}</span>
      </span>;
    })}
  </div>;
};

/** Flat front-view mark: the lid swings about its right hinge like a chest lid, handle attached. */
const Toolbox:React.FC<{x:number;y:number;size:number;open:number;scale:number;squash?:number}>=({x,y,size,open,scale,squash=0})=>
  <div style={{position:'absolute',left:0,top:0,width:size,height:size,transform:`translate(${x-size/2}px,${y-size/2}px) scale(${scale})`,filter:'drop-shadow(0 18px 24px rgba(59,45,20,.18))'}}>
    <div style={{position:'absolute',inset:0,transformOrigin:'50% 90%',transform:`scale(${1+squash*.06},${1-squash*.08})`}}>
      <Img src={staticFile('toolbox-lid.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%',transformOrigin:'89% 44.24%',transform:`rotate(${open*128}deg)`}}/>
      <Img src={staticFile('toolbox-base.svg')} style={{position:'absolute',inset:0,width:'100%',height:'100%'}}/>
    </div>
  </div>;

type Pose={x:number;y:number;scale:number};
const PHONE:Pose={x:1340,y:540,scale:.9};
/** Play mirrors the layout: the phone springs to the left half and the story moves to the right. */
const phoneAt=(t:number):Pose=>{const m=pop(t,T.play+.05,.7);return {x:lerp(PHONE.x,580,m),y:PHONE.y,scale:PHONE.scale};};
/** Where a measured part of the screen lands on the canvas for a phone pose. */
const onPhone=(r:Rect,p:Pose)=>({x:p.x-SKIN_WIDTH*p.scale/2+(DISPLAY.x+r.x+r.width/2)*p.scale,y:p.y-SKIN_HEIGHT*p.scale/2+(DISPLAY.y+r.y+r.height/2)*p.scale});

const Phone:React.FC<{id:string;state:Record<string,unknown>;pose:Pose;angle?:number;shell?:number;reveal?:number;from?:{x:number;y:number};under?:{id:string;state:Record<string,unknown>};hide?:string[]}>=({id,state,pose,angle=0,shell=1,reveal=1,from={x:215,y:480},under,hide=[]})=>
  <div style={{position:'absolute',left:0,top:0,width:SKIN_WIDTH,height:SKIN_HEIGHT,transform:`translate(${pose.x-SKIN_WIDTH/2}px,${pose.y-SKIN_HEIGHT/2}px) rotate(${angle}deg) scale(${pose.scale})`,filter:`drop-shadow(0 ${28*shell}px ${40*shell}px rgba(59,45,20,${.22*shell}))`}}>
    <Img src={staticFile('device/back.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
    <div style={{position:'absolute',left:DISPLAY.x,top:DISPLAY.y,width:DISPLAY.width,height:DISPLAY.height,borderRadius:DISPLAY.radius,overflow:'hidden'}}>
      {under&&reveal<1?<LiveScreen id={under.id} state={under.state} style={{position:'absolute',inset:0}}/>:null}
      <div style={{position:'absolute',inset:0,clipPath:reveal<1?`circle(${reveal*1150}px at ${from.x}px ${from.y}px)`:undefined}}>
        <LiveScreen id={id} state={{...state,_hide:hide.join()}} onReady={root=>{for(const sel of hide)for(const n of root.querySelectorAll<HTMLElement>(sel))n.style.visibility='hidden';}}/>
      </div>
      <Img src={staticFile('device/mask.webp')} style={{position:'absolute',inset:0,width:'100%',height:'100%',opacity:shell}}/>
    </div>
  </div>;

/** A live crop placed by its centre, with Material elevation that grows while it is lifted. */
const Card:React.FC<{screen:string;selector:string;state:Record<string,unknown>;rect:Rect;x:number;y:number;scale:number;angle?:number;radii?:number[];lift?:number}>=({screen,selector,state,rect,x,y,scale,angle=0,radii=[28,28,28,28],lift=1})=>
  <div style={{position:'absolute',left:0,top:0,zIndex:5,width:rect.width,height:rect.height,transform:`translate(${x-rect.width/2}px,${y-rect.height/2}px) rotate(${angle}deg) scale(${scale})`,filter:`drop-shadow(0 ${6+10*lift}px ${10+18*lift}px rgba(59,45,20,${.12+.1*lift}))`}}>
    <LiveCrop screen={screen} selector={selector} state={state} radii={radii}/>
  </div>;

const BENTO_RADII=[[28,8,8,8],[8,28,8,8],[8,8,8,28],[8,8,28,8]];
const LIFTS=[['compass','Compass'],['level','Bubble level'],['flash','Flash Dimmer'],['sos','Morse & SOS'],['water','Water Eject'],['breathe','Breathe']] as const;
// Scales fit each measured card into the left column (half-width cards are about 194 x 242, the counter 403 x 145).
const MEASURE=[['protractor','Angles.',2.3],['unit_converter','Units.',2.3],['bill_splitter','Split.',2.3],['counter','Counts.',1.9]] as const;

export const TrailerV2:React.FC=()=>{
  const t=useCurrentFrame()/useVideoConfig().fps;
  const [g,setG]=useState<Geometry|null>(null);
  const measured=useCallback((next:Geometry)=>setG(old=>old??next),[]);
  const beat=hit(t);

  // Live tool state, driven by the music rather than a clock.
  const quick={compassHeading:Math.round(312+steps(t,ONSETS)*17)%360,levelRoll:t<T.lock?4:lerp(4,0,ease(t,T.lock,T.place)),levelPitch:2,flashOn:true,flashLevel:1+steps(t,ONSETS)%5,flashMaxLevel:5,sosActive:Math.floor(t*4)%2===0};
  const measureState={protractorAngle:Math.round(lerp(20,135,ease(t,T.measureHeroes[0],T.measureHeroes[1]))),converterInput:Math.round(lerp(10,25,ease(t,T.measureHeroes[1],T.measureHeroes[2]))),splitTotal:'120',splitPeople:t>=20.12?'4':'2',counterValue:40+steps(t,ONSETS.filter(o=>o>=T.measureHeroes[3]))};
  const shuffle=Math.floor(t*30);
  const playState={wheelText:'Pizza\nSushi\nPasta',wheelRotation:1260*ease(t,T.spin[0],T.spin[1]),wheelWinner:t>=T.spin[1]?'Sushi':'',randomResult:t<T.chance[1]?1+(shuffle*37)%100:73,diceResult:t<T.chance[2]?1+(shuffle*5)%6:5,coinSide:t>=T.chance[3]&&t<T.chance[3]+.2?'Tails':'Heads'};

  // Camera: a slow push per chapter plus a tiny exact punch on each note once the film is moving.
  const chapter=[0,T.mark,T.place,T.phone,T.measure,T.play,T.close,T.boom].reduce((a,b)=>t>=b?b:a,0);
  const push=1+.035*ease(t,chapter,chapter+3.2)+(t>T.launches[0]&&t<T.boom?.012*beat:0);

  const nav=(label:string)=>g?.[label]?onPhone(g[label],phoneAt(t)):{x:PHONE.x,y:960};
  const navLocal=(label:string)=>g?.[label]?{x:g[label].x+g[label].width/2,y:g[label].y+g[label].height/2}:{x:215,y:900};

  // Bento geometry from the measured cards.
  const cards=WIDGETS.slice(0,4).map(w=>g?.[w.id]);
  const cw=cards[0]?.width??190,ch=cards[0]?.height??240;
  const lock=ease(t,T.lock,T.lock+.34);
  const bentoScale=lerp(1.05,1.6,ease(t,T.place,T.place+.7)),gap=lerp(18,4,lock)*bentoScale;
  const bentoCenter={x:lerp(960,1300,ease(t,T.place,T.place+.7)),y:lerp(400,540,ease(t,T.place,T.place+.7))};
  const slot=(i:number)=>({x:bentoCenter.x+(i%2?1:-1)*(cw*bentoScale+gap)/2,y:bentoCenter.y+(i<2?-1:1)*(ch*bentoScale+gap)/2});
  const toPhone=ease(t,T.phone,T.phoneEnd);

  // The mark: pop, swing the lid, settle low as the launcher, leave on "One place".
  const markIn=pop(t,T.mark,.55),lid=pop(t,T.lid,.6),low=ease(t,T.toBottom,T.launches[0]),gone=easeIn(t,T.place,T.place+.4);
  const recoil=T.launches.reduce((s,at)=>s+(t>=at?Math.exp(-(t-at)*9)*Math.cos((t-at)*24):0),0);

  return <AbsoluteFill style={{background:CREAM,overflow:'hidden',fontFamily:'Google Sans Flex',color:INK}}>
    <style>{`*,*::before,*::after{animation:none!important;transition:none!important}`}</style>
    <Audio src={staticFile('audio/soundtrack.mp3')}/>
    <GeometryProbe onGeometry={measured}/>
    <AbsoluteFill style={{transform:`scale(${push})`}}>
      {/* Grounds, each grown from its cause. */}
      <Wipe t={t} at={T.place} color={TEAL} x={960} y={400}/>
      <Wipe t={t} at={T.measure} color={GOLD} x={nav('Measure').x} y={nav('Measure').y} shape="flower"/>
      <Wipe t={t} at={T.play} color={PINK} x={nav('Play & Decide').x} y={nav('Play & Decide').y} shape="soft_burst"/>
      <Wipe t={t} at={T.close} color={CREAM} x={960} y={540} shape="sunny"/>

      {/* Motif: corner shapes from the sketch arrive one per note and part for the mark. */}
      {t<T.place+.5?[
        {name:'cookie_9sided',color:'#E2C700',x:120,y:930,size:620,from:[-260,240]},
        {name:'clover_4leaf',color:'#FAE3A0',x:1780,y:110,size:460,from:[260,-220]},
        {name:'gem',color:'#E6A823',x:1500,y:860,size:300,from:[300,260]},
        {name:'flower',color:'#F7D774',x:260,y:130,size:300,from:[-280,-220]},
      ].map((s,i)=>{
        const p=pop(t,T.motif[i],.6),part=ease(t,T.mark,T.mark+.5)*50+easeIn(t,T.place,T.place+.5)*700;
        const dx=Math.sign(s.x-960),dy=Math.sign(s.y-540);
        return <Shape key={s.name} name={s.name} color={s.color} size={s.size} x={s.x+s.from[0]*(1-p)+dx*part} y={s.y+s.from[1]*(1-p)+dy*part} angle={lerp(40,0,p)+6*hit(t)} scale={lerp(.4,1,p)*(1-.06*ease(t,1.46,T.mark)+.06*ease(t,T.mark,T.mark+.2))}/>;
      }):null}

      {t>=T.mark&&t<T.place+.45?<Toolbox x={960} y={lerp(540,870,low)+700*gone} size={460} open={lid} scale={lerp(.2,1,markIn)*lerp(1,.78,low)} squash={.5*recoil}/>:null}

      {/* Four live tools rise out of the box and lock into a bento; corners tighten where they meet. */}
      {g&&t>=T.launches[0]&&t<T.phoneEnd+.05?WIDGETS.slice(0,4).map((w,i)=>{
        const rect=g[w.id],at=T.launches[i];
        if(!rect||t<at)return null;
        const f=ease(t,at,at+.55),s=slot(i);
        const mouth={x:960,y:800};
        const target=onPhone(rect,PHONE);
        const x=lerp(lerp(mouth.x,s.x,f),target.x,toPhone),y=lerp(lerp(mouth.y,s.y,f)-Math.sin(Math.PI*f)*220,target.y,toPhone);
        const scale=lerp(lerp(.25,bentoScale,f),PHONE.scale,toPhone);
        const radii=BENTO_RADII[i].map(r=>lerp(28,r,lock));
        return <Card key={w.id} screen="quick-tools" selector={w.selector} state={quick} rect={rect} x={x} y={y} scale={scale} angle={lerp(i%2?18:-18,0,pop(t,at,.6))} radii={radii} lift={1-lock}/>;
      }):null}
      <Words t={t} text="One place." at={T.placeWords} x={110} y={320} size={210} width={760} exitAt={T.phone}/>

      {/* The bento becomes the phone: the screen grows around the cards and the frame forms. */}
      {t>=T.phone&&t<T.close?(()=>{
        const isMeasure=t>=T.measure&&t<T.play,isPlay=t>=T.play;
        const id=isPlay?'play':isMeasure?'numbers':'quick-tools';
        const tab=isPlay?ease(t,T.play,T.play+.5):isMeasure?ease(t,T.measure,T.measure+.5):1;
        const lifted=LIFTS.find(([,],k)=>t>=T.lifts[k]&&t<T.lifts[k]+.62)?.[0];
        const hide=t<T.phoneEnd?WIDGETS.slice(0,4).map(w=>w.selector):!isMeasure&&!isPlay&&lifted?[WIDGETS.find(w=>w.id===lifted)!.selector]:[];
        const swing=t>=T.play?Math.sin(Math.PI*ease(t,T.play+.05,T.play+.6))*-6:0;
        return <Phone id={id} pose={{...phoneAt(t),scale:PHONE.scale*lerp(1.08,1,toPhone)}} angle={-2*hit(t)+swing} shell={toPhone}
          state={isPlay?playState:isMeasure?measureState:quick} hide={hide}
          reveal={t<T.phoneEnd?toPhone:tab} from={t<T.phoneEnd?{x:215,y:480}:navLocal(isPlay?'Play & Decide':'Measure')}
          under={isPlay?{id:'numbers',state:measureState}:isMeasure?{id:'quick-tools',state:quick}:undefined}/>;
      })():null}

      {/* Quick Tools: each tool lifts out of the screen on its note while its name lands. */}
      <Words t={t} text="Quick tools." at={T.quick} x={110} y={170} size={200} width={760} exitAt={T.measure-.3}/>
      {g&&t>=T.lifts[0]&&t<T.measure?LIFTS.map(([id,name],k)=>{
        const at=T.lifts[k],end=T.lifts[k+1]??T.measure-.3;
        if(t<at-.05||t>=end+.25)return null;
        const w=WIDGETS.find(x=>x.id===id)!,rect=g[id];if(!rect)return null;
        const up=pop(t,at,.45),down=ease(t,end-.2,end+.2),p=up*(1-down),home=onPhone(rect,PHONE);
        const nameIn=pop(t,at,.4),nameOut=easeIn(t,end-.12,end+.12);
        return <React.Fragment key={id}>
          <Card screen="quick-tools" selector={w.selector} state={{...quick,searchQuery:w.query}} rect={rect} x={lerp(home.x,home.x-300,p)} y={lerp(home.y,620,p)} scale={lerp(PHONE.scale,1.7,p)} angle={lerp(0,-4,p)} lift={p}/>
          <div style={{position:'absolute',left:110,top:860,fontSize:64,fontWeight:650,overflow:'hidden',paddingBottom:12}}><div style={{transform:`translateY(${(1-nameIn+nameOut)*110}%)`}}>{name}</div></div>
        </React.Fragment>;
      }):null}

      {/* Measure: four tools push through the left column, values changing on the beat. */}
      <Words t={t} text="Measure." at={T.measureWords} x={110} y={90} size={200} width={760} exitAt={T.play-.3}/>
      {t>=T.measureHeroes[0]&&t<T.play?<div style={{position:'absolute',left:0,top:300,width:960,height:740,overflow:'hidden'}}>
        {MEASURE.map(([tool,word,zoom],k)=>{
          const at=T.measureHeroes[k],next=T.measureHeroes[k+1]??T.play;
          if(t<at-.2||t>=next+.3)return null;
          const enter=ease(t,at-.2,at+.3),leave=ease(t,next-.2,next+.3),x=480+960*(1-enter)-960*leave;
          return <div key={tool} style={{position:'absolute',left:0,top:0,width:960,height:740,transform:`translateX(${x-480}px)`}}>
            <div style={{position:'absolute',left:110,top:0,fontSize:64,fontWeight:650,color:'#6B5E00'}}>{word}</div>
            <div style={{position:'absolute',left:110,top:100,transformOrigin:'0 0',transform:`scale(${zoom*(1+.02*hit(t))})`}}>
              <LiveCrop screen="numbers" selector={`[data-tool="${tool}"]`} state={{...measureState,searchQuery:''}} radii={[28,28,28,28]}/>
            </div>
          </div>;
        })}
      </div>:null}

      {/* Play: the wheel spins large, then the three chance tools land on consecutive notes. */}
      <Words t={t} text="Let chance decide." at={T.playWords} x={1010} y={70} size={150} width={860} exitAt={T.close-.3} exitX={1600}/>
      {t>=T.playWords[1]&&t<T.close?<>
        <div style={{position:'absolute',left:1010,top:420,transformOrigin:'0 0',transform:`translateY(${(1-pop(t,T.playWords[1],.5))*700-easeIn(t,T.chance[0],T.chance[0]+.3)*900}px) scale(1.75)`}}>
          <LiveCrop screen="play" selector=".play-wheel-card" state={{...playState,searchQuery:'Decision Wheel'}} radii={[28,28,28,28]}/>
        </div>
        {['random_number','dice_roll','coin_flip'].map((tool,k)=>t>=T.chance[k+1]?<div key={tool} style={{position:'absolute',left:1010+[0,0,316][k],top:lerp(1250,[430,690,690][k],pop(t,T.chance[k+1],.45)),transformOrigin:'0 0',transform:`scale(1.5) rotate(${lerp(10,0,pop(t,T.chance[k+1],.45))}deg)`}}>
          <LiveCrop screen="play" selector={`[data-tool="${tool}"]`} state={{...playState,searchQuery:''}} radii={[28,28,28,28]}/>
        </div>:null)}
      </>:null}

      {/* Closing: three destinations fan in, one per note, beside the mark and title. */}
      {t>=T.close?<>
        {[{id:'quick-tools',x:1060,angle:-7,state:{...quick,sosActive:false}},{id:'play',x:1660,angle:7,state:{...playState,wheelWinner:'Sushi'}},{id:'numbers',x:1360,angle:0,state:measureState}].map((p,k)=>{
          const land=pop(t,T.fan[k],.6);
          return <Phone key={p.id} id={p.id} state={p.state} pose={{x:lerp(p.x+120,p.x,land),y:lerp(1400,560,land),scale:.66}} angle={lerp(p.angle*2.5,p.angle,land)}/>;
        })}
        <Toolbox x={250} y={250} size={230} open={0} scale={pop(t,T.fan[2],.5)*(1-.1*ease(t,T.boom,T.boom+.12))}/>
        <Words t={t} text="App Toolkit." at={T.closeWords} x={110} y={400} size={128} width={800}/>
        <Words t={t} text="Your toolkit." at={[T.closeWords[1]+.2]} x={114} y={560} size={72} width={800}/>
        <Img src={staticFile('google-play.png')} style={{position:'absolute',left:110,top:700,width:340,transform:`translateY(${(1-pop(t,T.badge,.5))*500}px)`}}/>
      </>:null}

      {/* Boom: a gold burst from the mark takes the frame and the mark lands at its centre. */}
      {t>=T.boom?<>
        <Shape name="soft_burst" color={BOOM} size={300} x={lerp(250,960,ease(t,T.boom,T.boomCover))} y={lerp(250,540,ease(t,T.boom,T.boomCover))} angle={140*ease(t,T.boom,T.end)} scale={lerp(.6,16,easeIn(t,T.boom,T.boomCover))}/>
        <Toolbox x={960} y={540} size={560} open={0} scale={pop(t,T.boom+.2,.26)}/>
      </>:null}
    </AbsoluteFill>
  </AbsoluteFill>;
};

export const Trailer = TrailerV2;
export default TrailerV2;
