import React from "react";
import { AbsoluteFill } from "remotion";
import { LitScreen } from "./LitScreen";
import { SCREENS } from "./screens";
import { STRINGS } from "./strings";
import { WaterArt } from "./WaterArt";

export const CANVAS = { w:1920,h:1080 };
export const SETS = {
 tablet:{key:"tabletScreenshots",window:"fold",device:[904.53,933.27]},
 desktop:{key:"desktopScreenshots",window:"desktop",device:[1459.47,973.2]},
} as const;
export type Kind=keyof typeof SETS;
export const WideStrip: React.FC<{locale:string;kind:Kind;only?:number}> = ({locale,kind,only})=>{
 const set=SETS[kind],list=STRINGS[locale][set.key];
 const [w,h]=set.device;
 return <AbsoluteFill style={{width:list.length*CANVAS.w,overflow:"hidden"}}>
  <WaterArt width={list.length*CANVAS.w} height={CANVAS.h} ripples={list.map((_,i)=>({x:i*CANVAS.w+CANVAS.w/2,y:CANVAS.h+40,reach:1150,strength:.7}))}/>
  {list.map((s,i)=>(only===undefined||only===i)&&<div key={s.id} style={{position:"absolute",left:i*CANVAS.w+(CANVAS.w-w)/2,top:(CANVAS.h-h)/2}}>
   <LitScreen id={SCREENS[s.id][0]} state={SCREENS[s.id][1]} window={set.window}/>
  </div>)}
 </AbsoluteFill>;
};
export const WideShot: React.FC<{locale:string;kind:Kind;index:number}> = ({locale,kind,index})=>(
 <AbsoluteFill style={{overflow:"hidden"}}>
  <div style={{position:"absolute",left:-index*CANVAS.w,top:0,height:CANVAS.h}}>
   <WideStrip locale={locale} kind={kind} only={index}/>
  </div>
 </AbsoluteFill>
);
