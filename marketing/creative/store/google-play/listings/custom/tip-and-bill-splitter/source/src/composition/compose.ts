import {W,H,SPACE,CAPTION} from "./geometry.ts";
import type {Pose,ScreenUi} from "./geometry.ts";
import type {Arc,Bubble,Piece} from "./effects.ts";
import {placeShapes} from "./effects.ts";
import {SHOTS,measureAll} from "./shots.ts";
import {validate} from "./validate.ts";
import {CYAN,BUTTER,PALE,TEAL} from "../theme.ts";
export type Layout={shots:{id:string;poses:Pose[]}[]};
const decorative:Piece[][]=[
 [{name:"pill",x:80,y:570,size:720,color:CYAN,rotate:-18,behind:true}],
 [{name:"soft_burst",x:470,y:600,size:590,color:PALE,rotate:14,behind:true}],
 [{name:"cookie_9sided",x:40,y:930,size:650,color:BUTTER,rotate:-12,behind:true}],
 [{name:"clover_4leaf",x:250,y:400,size:680,color:PALE,rotate:16,behind:true}],
 [{name:"pill",x:300,y:800,size:720,color:CYAN,rotate:22,behind:true}]
];
export const composeShot=(index:number,panels:number,layout:Layout,ui:Record<string,ScreenUi>)=>{
 const spec=SHOTS[index],poses=layout.shots[index].poses,phones=measureAll(poses,spec.screens,ui);
 const fixed=decorative[index],arcs:Arc[]=[],spheres:Bubble[]=[];
 const shapes=placeShapes({...spec.shapes,colors:[CYAN,BUTTER,TEAL,PALE]},phones,
  // Captions reserve their whole text block, including the padding checked by validate.
  CAPTION[spec.caption].block,[],[],{left:false,right:false},{w:W,h:H},fixed);
 const report=validate(spec,phones,shapes,[],[],{left:false,right:false});
 if(shapes.length<fixed.length+spec.shapes.small)report.hard.push("The requested MaterialShape accents do not fit");
 for(let i=0;i<shapes.length;i++)for(const b of shapes.slice(i+1)){
  const a=shapes[i],gap=Math.hypot(a.x+a.size/2-b.x-b.size/2,a.y+a.size/2-b.y-b.size/2)-(a.size+b.size)/2;
  if(gap<SPACE.m-.5)report.hard.push(a.name+" overlaps the clearance of "+b.name);
 }
 return {spec,poses,phones,shapes,arcs,spheres,report};
};
