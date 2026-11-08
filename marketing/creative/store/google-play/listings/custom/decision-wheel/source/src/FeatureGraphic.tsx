import React from "react";
import {AbsoluteFill} from "remotion";
import {useFonts} from "./LitScreen";
import {Shape} from "./Shape";
import {FONT,INK} from "./theme";
export type Concept="ribbon"|"ripple"|"editorial";
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({concept="ribbon"})=>{
useFonts();const editorial=concept==="editorial",moment=concept==="ripple";const cx=editorial?650:moment?260:820,cy=editorial?-25:moment?390:280,r=editorial?280:moment?290:235;
return <AbsoluteFill style={{background:"#F7F1FA",fontFamily:FONT,color:INK,overflow:"hidden"}}>
<Shape name={moment?"flower":"soft_burst"} x={cx-r-70} y={cy-r-70} size={r*2+140} color="#E7D9EF" rotate={12}/>
<svg width="1024" height="500" style={{position:"absolute",inset:0}}>
<g transform={`translate(${cx} ${cy}) rotate(-18)`}>{Array.from({length:6},(_,i)=>{const a=(i*60-90)*Math.PI/180,b=((i+1)*60-90)*Math.PI/180;return <path key={i} d={`M 0 0 L ${r*Math.cos(a)} ${r*Math.sin(a)} A ${r} ${r} 0 0 1 ${r*Math.cos(b)} ${r*Math.sin(b)} Z`} fill={["#BFA0D0","#E3C4DF","#DDA795"][i%3]} stroke="#F7F1FA" strokeWidth="5"/>})}<circle r="32" fill="#F7F1FA"/><circle r="10" fill="#684379"/></g>
<path d={`M ${cx-20} ${cy-r-15} h 40 l -20 38 Z`} fill="#684379"/>
{[0,1,2,3,4].map(i=><rect key={i} x={560+i*83} y={45+(i%3)*47} width="12" height="27" rx="5" fill={["#BFA0D0","#DDA795","#E9D38A"][i%3]} transform={`rotate(${i*23} ${566+i*83} ${58+(i%3)*47})`}/>)}
</svg>
<div style={{position:"absolute",left:moment?510:108,top:editorial?280:145,width:editorial?820:moment?410:500}}>
<div style={{fontSize:22,fontWeight:600,marginBottom:18,color:"#684379"}}>App Toolkit</div><h1 style={{fontSize:editorial?90:76,lineHeight:1.02,letterSpacing:-3,fontWeight:650,margin:0}}>{editorial?"Spin the Wheel":<>Spin the<br/>Wheel</>}</h1></div>
</AbsoluteFill>
};
