import React from "react";
import {AbsoluteFill} from "remotion";
import {useFonts} from "./LitScreen";
import {Shape} from "./Shape";
import {FONT,INK} from "./theme";

export type Concept="ripple"|"ribbon"|"editorial";
const Ruler:React.FC<{x:number;y:number;width:number;angle:number}>=({x,y,width,angle})=><g transform={`translate(${x} ${y}) rotate(${angle})`}>
 <rect x="0" y="8" width={width} height="104" rx="28" fill="#B56D00" opacity=".14"/>
 <rect width={width} height="104" rx="28" fill="#F6B91D"/>
 <path d={`M 26 80 H ${width-26}`} stroke="#8F5700" opacity=".25"/>
 {Array.from({length:Math.floor((width-48)/12)},(_,i)=><line key={i} x1={24+i*12} x2={24+i*12} y1="0" y2={i%10===0?53:i%5===0?37:20} stroke="#654000" strokeWidth={i%5?1.5:2.5}/>)}
</g>;
const Protractor:React.FC<{x:number;y:number;r:number;angle?:number}>=({x,y,r,angle=0})=><g transform={`translate(${x} ${y}) rotate(${angle})`}>
 <path d={`M ${-r} 0 A ${r} ${r} 0 0 1 ${r} 0 Z`} transform="translate(0 8)" fill="#8F5700" opacity=".12"/>
 <path d={`M ${-r} 0 A ${r} ${r} 0 0 1 ${r} 0 Z`} fill="#FDE7A2"/>
 <path d={`M ${-r*.65} 0 A ${r*.65} ${r*.65} 0 0 1 ${r*.65} 0`} stroke="#C48B24" strokeWidth="2" fill="none"/>
 {Array.from({length:37},(_,i)=>{const a=Math.PI+i*Math.PI/36,outer=r-12,inner=outer-(i%3?10:24);return <line key={i} x1={Math.cos(a)*outer} y1={Math.sin(a)*outer} x2={Math.cos(a)*inner} y2={Math.sin(a)*inner} stroke="#8F5700" strokeWidth={i%3?1.5:2.5}/>})}
 <path d={`M ${r*.78} 0 H 0 L ${r*.55} ${-r*.55}`} fill="none" stroke="#8F5700" strokeWidth="5" strokeLinecap="round"/><circle r="9" fill="#8F5700"/>
</g>;
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({concept="ribbon"})=>{
 useFonts();
 const editorial=concept==="editorial", geometry=concept==="ripple";
 return <AbsoluteFill style={{background:"#FFF4D9",fontFamily:FONT,color:INK,overflow:"hidden"}}>
 {geometry?<><Shape name="cookie_12sided" x={-100} y={80} size={520} color="#FFD03B" rotate={15}/><Shape name="clover_4leaf" x={700} y={-180} size={580} color="#FDE7A2" rotate={12}/></>:editorial?<Shape name="flower" x={535} y={40} size={510} color="#FDE7A2" rotate={18}/>:<><Shape name="soft_burst" x={530} y={-60} size={650} color="#FFD03B" rotate={-12}/><Shape name="clover_4leaf" x={-120} y={295} size={340} color="#FDE7A2" rotate={8}/></>}
 <svg width="1024" height="500" style={{position:"absolute",inset:0}}>
 {geometry?<><Ruler x={-50} y={445} width={530} angle={-14}/><Protractor x={875} y={220} r={240} angle={-25}/></>:editorial?<><Protractor x={790} y={365} r={195} angle={-8}/><Ruler x={590} y={373} width={540} angle={-22}/></>:<><Protractor x={825} y={298} r={230} angle={-14}/><Ruler x={310} y={425} width={790} angle={-13}/></>}
 </svg>
 <div style={{position:"absolute",left:editorial?108:geometry?250:108,top:editorial?132:geometry?145:110,width:editorial?450:geometry?530:480}}>
 <div style={{fontSize:22,fontWeight:600,color:"#8F5700",marginBottom:18}}>App Toolkit</div>
 <h1 style={{fontSize:editorial?67:74,lineHeight:1.04,letterSpacing:-2.4,fontWeight:650,margin:0}}>Ruler &amp;<br/>Protractor</h1>
 </div>
 </AbsoluteFill>
};
