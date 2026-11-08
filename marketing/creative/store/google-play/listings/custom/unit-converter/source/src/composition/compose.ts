import {CAPTION,H,SPACE,W,distanceTo,grow,inside} from "./geometry.ts";
import type {Pose,ScreenUi,Pt} from "./geometry.ts";
import type {Arc,Bubble,Piece} from "./effects.ts";
import {SHOTS,measureAll} from "./shots.ts";
import {validate} from "./validate.ts";
import {CYAN,TEAL,BUTTER,PALE} from "../theme.ts";
export type Layout={shots:{id:string;poses:Pose[]}[]};

const names=[["clover_4leaf","gem"],["pill","cookie_6sided"],["cookie_9sided","pill"],["clover_4leaf","cookie_6sided"],["gem","soft_burst"],["pill","clover_4leaf"]];
const colors=[[CYAN,BUTTER],[BUTTER,PALE],[PALE,CYAN],[CYAN,PALE],[PALE,BUTTER],[BUTTER,CYAN]];
export const composeShot=(index:number,panels:number,layout:Layout,ui:Record<string,ScreenUi>)=>{
 const spec=SHOTS[index],poses=layout.shots[index].poses,phones=measureAll(poses,spec.screens,ui);
 const block=CAPTION[spec.caption].block,m=phones[0],cy=(m.full.top+m.full.bottom)/2;
 // Two different MaterialShapes represent the two sides of a conversion.
 const sizes=index===4?[520,420]:[570,470];
 const shapes:Piece[]=sizes.map((size,k)=>{
  const r=size/2,x=k?W-r-34:r+34;
  const y=cy+(k?250:-250);
  return {name:names[index][k],x:x-r,y:y-r,size,color:colors[index][k],rotate:[-12,18,-8,12,0,-16][index]*(k?-1:1),behind:true};
 });
 const zone=grow(block,34),arcs:Arc[]=[],spheres:Bubble[]=[];
 // The arrows stay in the real free space outside the projected silhouette.
 const left=Math.max(70,m.box.left-68),right=Math.min(W-70,m.box.right+68);
 for(const [k,x] of [left,right].entries()){
  const y0=cy-195,y1=cy+195,sign=k?1:-1;
  const pts:Pt[]=Array.from({length:41},(_,i)=>{
   const t=i/40;return [x+sign*26*Math.sin(Math.PI*t),k?y1-(y1-y0)*t:y0+(y1-y0)*t];
  });
  if(pts.every(([x,y])=>!(x>zone.left-12&&x<zone.right+12&&y>zone.top-12&&y<zone.bottom+12)&&!inside(m.visible,[x,y])&&distanceTo(m.visible,[x,y])>21))
   arcs.push({pts,ring:k});
 }
 const effects:Bubble[]=arcs.flatMap(a=>a.pts.map(([x,y])=>[x,y,12] as Bubble));
 const report=validate(spec,phones,shapes,effects,[],{left:false,right:false});
 return {spec,poses,phones,shapes,arcs,spheres,report};
};
