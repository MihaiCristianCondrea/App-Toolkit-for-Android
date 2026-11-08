import React from "react";
import {FIELD,PALE} from "./theme";
export type TextBox={left:number;top:number;right:number;bottom:number};
export const Glass:React.FC<{width:number;height:number;panels:number;id?:string;text?:TextBox[];variant?:"paired"|"flow"}>=({width,height,panels,id="ground",text=[],variant="paired"})=>{
 const step=width/panels,y=height*.57;
 return <svg aria-hidden width={width} height={height} style={{position:"absolute",inset:0}}>
 <defs><mask id={id+"-quiet"}><rect width={width} height={height} fill="white"/>{text.map((b,i)=><rect key={i} x={b.left-55} y={b.top-55} width={b.right-b.left+110} height={b.bottom-b.top+110} fill="black"/>)}</mask></defs>
 <rect width={width} height={height} fill={FIELD[0]}/>
 <g mask={`url(#${id}-quiet)`} fill="none" stroke={PALE} strokeWidth={variant==="paired"?5:32} strokeLinecap="round">
 <path d={Array.from({length:panels},(_,i)=>`${i?"":"M 0 "+y} C ${step*i+step*.3} ${y-height*.25} ${step*i+step*.7} ${y+height*.25} ${step*(i+1)} ${y}`).join(" ")}/>
 <path d={Array.from({length:panels},(_,i)=>`${i?"":"M 0 "+(y+34)} C ${step*i+step*.3} ${y-height*.25+34} ${step*i+step*.7} ${y+height*.25+34} ${step*(i+1)} ${y+34}`).join(" ")}/>
 </g>
 </svg>;
};
