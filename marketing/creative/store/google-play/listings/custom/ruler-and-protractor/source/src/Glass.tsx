import React from "react";
import { FIELD } from "./theme";
export type TextBox = {left:number;top:number;right:number;bottom:number};
export const Glass:React.FC<{width:number;height:number;panels:number;id?:string;text?:TextBox[];ribbons?:boolean;field?:[string,string]}> = ({width,height,panels,field=FIELD}) => <svg aria-hidden width={width} height={height} style={{position:"absolute",inset:0}}>
<rect width={width} height={height} fill={field[0]}/>
<path d={`M 0 ${height*.83} H ${width}`} stroke="#9ABCE2" strokeWidth="3" fill="none"/>
{Array.from({length:panels*12+1},(_,i)=><line key={i} x1={i*width/(panels*12)} x2={i*width/(panels*12)} y1={height*.83} y2={height*.83+(i%3===0?28:12)} stroke="#9ABCE2" strokeWidth="2"/>)}
</svg>;
export const SoundBubbles:React.FC<{arcs:{pts:number[][];ring:number}[];bubbles:number[][];width:number;height:number}>=()=>null;
