import {CAPTION,H,W,bbox,gapToBox,overlapArea,rectPoly,grow} from "./geometry.ts";
import type {Pose,ScreenUi,Box,Pt} from "./geometry.ts";
import type {Arc,Bubble,Piece} from "./effects.ts";
import {SHOTS,measureAll} from "./shots.ts";
import {measurePanels} from "./panels.ts";
import {validate} from "./validate.ts";
import {CYAN,BUTTER,PALE} from "../theme.ts";
export type Layout={shots:{id:string;poses:Pose[]}[]};
const decorative:Piece[][]=[
 [{name:"pill",x:55,y:340,size:630,color:CYAN,rotate:-24,behind:true},{name:"gem",x:630,y:850,size:390,color:BUTTER,rotate:12,behind:true}],
 [{name:"clover_4leaf",x:200,y:590,size:780,color:PALE,rotate:18,behind:true}],
 [{name:"cookie_6sided",x:20,y:470,size:510,color:BUTTER,rotate:-18,behind:true},{name:"pill",x:500,y:720,size:480,color:CYAN,rotate:24,behind:true}],
 [{name:"clover_4leaf",x:35,y:1050,size:590,color:CYAN,rotate:12,behind:true},{name:"gem",x:600,y:530,size:420,color:PALE,rotate:-18,behind:true}]
];
export const composeShot=(index:number,panels:number,layout:Layout,ui:Record<string,ScreenUi>)=>{
 const spec=SHOTS[index],poses=layout.shots[index].poses,phones=measureAll(poses,spec.screens,ui),details=measurePanels(spec.panels,ui);
 const shapes=decorative[index],arcs:Arc[]=[],spheres:Bubble[]=[];
 const report=validate(spec,phones,shapes,[],[],{left:false,right:false});
 const block=CAPTION[spec.caption].block,zone=grow(block,34),all:Box[]=[];
 for(const p of details){
  const label={...p.box,top:p.box.top-70,bottom:p.box.top-14},regions=[p.box,label];
  for(const b of regions){
   if(b.left<55||b.right>W-55||b.top<89||b.bottom>H-89)report.hard.push(p.screen+": detail or label leaves safe bounds");
   if(gapToBox(rectPoly(b),zone)<1)report.hard.push(p.screen+": detail collides with caption");
   if(phones.some(m=>overlapArea(rectPoly(b),m.visible)>0))report.hard.push(p.screen+": detail covers a phone");
   if(all.some(other=>overlapArea(rectPoly(b),rectPoly(other))>0))report.hard.push(p.screen+": detail regions overlap");
   all.push(b);
  }
 }
 // These are genuine interface crops; their full measured fields and controls must remain visible.
 for(const p of details)for(const [key,pts] of Object.entries(p.critical)){
  const b=bbox(pts);
  if(b.left<p.box.left-.5||b.right>p.box.right+.5||b.top<p.box.top-.5||b.bottom>p.box.bottom+.5)
   report.hard.push(p.screen+": "+key+" is cropped");
 }
 if(details.length){
  const masses:[number,Pt][]=[
   ...phones.map(m=>[m.area,m.centroid] as [number,Pt]),
   ...details.map(p=>[p.width*p.height,[(p.box.left+p.box.right)/2,(p.box.top+p.box.bottom)/2]] as [number,Pt]),
   [840*207*.3,[(block.left+block.right)/2,(block.top+block.bottom)/2]]
  ];
  const sum=masses.reduce((n,[w])=>n+w,0);
  report.balance=[0,1].map(k=>masses.reduce((n,[w,p])=>n+w*p[k],0)/sum) as Pt;
  report.preferences=report.preferences.filter(t=>!t.startsWith("visual centroid"));
  if(Math.abs(report.balance[0]/W-.5)>.08)report.preferences.push("Grouped UI balance is outside the central 16 percent");
 }
 report.intentional.push(...details.map(p=>p.screen+": real From/To UI detail without a device frame"));
 return {spec,poses,phones,shapes,arcs,spheres,panels:details,report};
};
