import React from "react";
import {FIELD} from "./theme";
export type TextBox={left:number;top:number;right:number;bottom:number};
export const Glass:React.FC<{width:number;height:number;panels:number;id?:string;text?:TextBox[];ribbons?:boolean;field?:[string,string]}>=({width,height,panels,field=FIELD})=><svg aria-hidden width={width} height={height} style={{position:"absolute",inset:0}}>
<rect width={width} height={height} fill={field[0]}/>
{height!==1920&&Array.from({length:panels},(_,i)=>{const pw=width/panels,n=i===1?16:i===0?6:4;return <g key={i} transform={`translate(${i*pw} 0)`}>
{Array.from({length:n},(_,k)=>{const x=k%2?pw-65:45,y=height*(.27+.055*k),a=k*31+i*19;return <rect key={k} x={x} y={y} width={k%3?13:22} height={k%3?28:12} rx="5" fill={["#BFA0D0","#DDA795","#D9C67D","#9ABDA9"][k%4]} transform={`rotate(${a} ${x+10} ${y+12})`}/>})}
</g>})}
</svg>;
export const SoundBubbles:React.FC<{arcs:{pts:number[][];ring:number}[];bubbles:number[][];width:number;height:number}>=()=>null;
