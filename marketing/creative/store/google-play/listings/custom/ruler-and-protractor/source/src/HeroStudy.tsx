import React from "react";
import {AbsoluteFill} from "remotion";
import {LitScreen,useFonts} from "./LitScreen";
import {FRAME,fitPose,measure} from "./composition/geometry";
import type {ScreenUi} from "./composition/geometry";
import ui from "./composition/ui.json";
import {phoneRules} from "./composition/validate";
import {SHOTS} from "./composition/shots";
import {FONT} from "./theme";

const directions=[
 {field:"#FFF0AB",ink:"#172B51",accent:"#2557C4",paper:"#FFFDF4",turns:[-7,8],xs:[300,785],tops:[530,730],widths:[430,400]},
 {field:"#F6F0E5",ink:"#253B35",accent:"#D07962",paper:"#D8E8D5",turns:[3,-5],xs:[305,775],tops:[750,520],widths:[400,430]},
 {field:"#263E99",ink:"#FFF3CB",accent:"#F0A68A",paper:"#FBDF94",turns:[-10,7],xs:[310,780],tops:[500,800],widths:[430,390]}
];
export const HeroStudy:React.FC<{concept:number}>=({concept})=>{
 useFonts();const d=directions[concept];const ids=["ruler","protractor"] as const;
 const poses=ids.map((id,i)=>fitPose({pitch:0,yaw:i?3:-3,roll:d.turns[i],perspective:3000},ui[id] as ScreenUi,{width:d.widths[i],x:m=>m.centroid[0],xTarget:d.xs[i],y:m=>m.full.top,yTarget:d.tops[i]}));
 const ms=poses.map((p,i)=>measure(p,ui[ids[i]] as ScreenUi));
 const failures=phoneRules({...SHOTS[0],screens:[...ids]},ms);
 if(failures.length)throw Error(failures.join("; "));
 return <AbsoluteFill style={{background:d.field,fontFamily:FONT,overflow:"hidden"}}>
 <svg width="1080" height="1920" style={{position:"absolute",inset:0}}>
 {concept===0?<path d="M 0 1050 Q 150 740 560 1030 Q 960 1280 1080 790 L 1080 1920 H 0Z" fill={d.accent}/>:concept===1?<g transform="translate(110 1050) rotate(-8)"><rect width="900" height="680" rx="32" fill={d.paper}/><path d="M 110 500 V 90 L 520 500Z" fill="none" stroke={d.accent} strokeWidth="12"/>{[0,1,2,3].map(i=><line key={i} x1="600" x2="800" y1={140+i*75} y2={140+i*75} stroke="#A3B6A1" strokeWidth="3"/>)}</g>:<g fill={d.paper}><path d="M 60 1350 A 820 820 0 0 1 920 490 L 920 1350Z"/><rect x="-50" y="1550" width="1250" height="400" rx="110" transform="rotate(-12 540 1700)"/></g>}
 </svg>
 <h1 style={{position:"absolute",top:88,left:90,right:90,margin:0,color:d.ink,fontSize:91,lineHeight:1.12,letterSpacing:-3,fontWeight:650}}>Two tools<br/>one pocket</h1>
 {poses.map((p,i)=><div key={i} style={{position:"absolute",left:p.x-FRAME.width/2,top:p.y-FRAME.height/2,width:FRAME.width,height:FRAME.height,transform:`rotate(${p.roll}deg) scale(${p.scale})`,transformOrigin:"50% 50%",filter:"drop-shadow(0 24px 28px #172B5138)"}}><LitScreen id="numbers" state={{activeSheet:ids[i],protractorAngle:45}} pose={{yaw:p.yaw,pitch:p.pitch,perspective:p.perspective}}/></div>)}
 </AbsoluteFill>
};
