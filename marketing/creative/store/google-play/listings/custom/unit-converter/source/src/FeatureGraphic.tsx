import React from "react";
import {AbsoluteFill} from "remotion";
import {Shape} from "./Shape";
import {useFonts} from "./LitScreen";
import {FIELD,FONT,INK,PILL,CYAN,TEAL,BUTTER,PALE} from "./theme";

export type Concept="transformation"|"connected"|"editorial";
const Arrow:React.FC<{x:number;y:number;reverse?:boolean;scale?:number}>=({x,y,reverse=false,scale=1})=><g transform={`translate(${x} ${y}) scale(${reverse?-scale:scale} ${scale})`} fill="none" stroke={PILL} strokeWidth={10} strokeLinecap="round" strokeLinejoin="round">
 <path d="M -96 -24 C -26 -70 40 -70 96 -24 M 67 -53 L 96 -24 L 58 -18"/>
 <path d="M 96 24 C 26 70 -40 70 -96 24 M -67 53 L -96 24 L -58 18"/>
</g>;
export const FeatureGraphic:React.FC<{locale:string;concept?:Concept}>=({concept="connected"})=>{
 useFonts();
 return <AbsoluteFill style={{background:FIELD[0],fontFamily:FONT,color:INK,overflow:"hidden"}}>
 {concept==="transformation"&&<>
  <Shape name="clover_4leaf" x={618} y={-50} size={405} color={CYAN} rotate={-12}/>
  <Shape name="gem" x={735} y={252} size={270} color={BUTTER} rotate={18}/>
  <svg width={1024} height={500} style={{position:"absolute",inset:0}}><Arrow x={795} y={250} scale={.9}/></svg>
  <div style={{position:"absolute",left:80,top:92,fontSize:28,fontWeight:560,color:PILL}}>App Toolkit</div>
  <div style={{position:"absolute",left:76,top:154,fontSize:88,fontWeight:650,lineHeight:1.08,letterSpacing:-3}}>Unit<br/>Converter</div>
 </>}
 {concept==="connected"&&<>
  <Shape name="cookie_6sided" x={54} y={196} size={315} color={CYAN} rotate={-8}/>
  <Shape name="pill" x={680} y={196} size={315} color={BUTTER} rotate={16}/>
  <div style={{position:"absolute",top:42,left:80,fontSize:25,fontWeight:550,color:PILL}}>App Toolkit</div>
  <div style={{position:"absolute",left:77,top:89,fontSize:75,fontWeight:650,letterSpacing:-2.5}}>Unit Converter</div>
  <svg width={1024} height={500} style={{position:"absolute",inset:0}}>
   <text x={211} y={376} textAnchor="middle" fill={INK} fontFamily={FONT} fontSize={108} fontWeight={600}>m</text>
   <text x={834} y={376} textAnchor="middle" fill={INK} fontFamily={FONT} fontSize={93} fontWeight={600}>cm</text>
   <Arrow x={520} y={325} scale={1.1}/>
  </svg>
 </>}
 {concept==="editorial"&&<>
  <Shape name="pill" x={-120} y={75} size={530} color={PALE} rotate={-25}/>
  <Shape name="clover_4leaf" x={74} y={170} size={275} color={CYAN} rotate={18}/>
  <svg width={1024} height={500} style={{position:"absolute",inset:0}}><Arrow x={212} y={270} scale={.75}/></svg>
  <div style={{position:"absolute",left:436,top:111,fontSize:27,fontWeight:550,color:PILL}}>App Toolkit</div>
  <div style={{position:"absolute",left:430,top:173,fontSize:78,lineHeight:1.06,fontWeight:640,letterSpacing:-2}}>Unit<br/>Converter</div>
 </>}
 </AbsoluteFill>;
};
