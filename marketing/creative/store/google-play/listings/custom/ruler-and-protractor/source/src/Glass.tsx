import React from "react";
import {FIELD} from "./theme";
export type TextBox={left:number;top:number;right:number;bottom:number};
export const Glass:React.FC<{width:number;height:number;panels:number;id?:string;text?:TextBox[];ribbons?:boolean;field?:[string,string]}>=({width,height,panels,field=FIELD})=>{
 const pw=width/panels;
 return <svg aria-hidden width={width} height={height} style={{position:"absolute",inset:0}}>
 <rect width={width} height={height} fill={field[0]}/>
 {Array.from({length:panels},(_,i)=><g key={i} transform={`translate(${i*pw} 0)`} fill="none" stroke="#B87512" strokeWidth="2" opacity=".6">
 {i%2===0?<g><path d={`M 0 ${height*.82} H ${pw}`}/>{Array.from({length:25},(_,k)=><path key={k} d={`M ${k*pw/24} ${height*.82} v ${k%4===0?26:12}`}/>)}</g>:<g transform={`translate(${pw*.55} ${height*.69})`}><path d={`M ${-pw*.46} 0 A ${pw*.46} ${pw*.46} 0 0 1 0 ${-pw*.46}`}/>{Array.from({length:19},(_,k)=>{const a=Math.PI+k*Math.PI/36,r=pw*.46;return <line key={k} x1={Math.cos(a)*r} y1={Math.sin(a)*r} x2={Math.cos(a)*(r-(k%3?10:24))} y2={Math.sin(a)*(r-(k%3?10:24))}/>})}</g>}
 </g>)}
 </svg>
};
export const SoundBubbles:React.FC<{arcs:{pts:number[][];ring:number}[];bubbles:number[][];width:number;height:number}>=()=>null;
