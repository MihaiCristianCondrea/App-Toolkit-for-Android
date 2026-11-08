import {bbox,rectPoly} from "./geometry.ts";
import type {Box,Pt,ScreenUi} from "./geometry.ts";
export type PanelPlan={screen:string;label:string;x:number;y:number;width:number};
export type Panel={screen:string;label:string;width:number;height:number;scale:number;box:Box;crop:{width:number;height:number};critical:Record<string,Pt[]>};
export const measurePanels=(plans:PanelPlan[],ui:Record<string,ScreenUi>):Panel[]=>plans.map(p=>{
 const source=ui[p.screen],names=["from","to","swap",...(p.screen==="data"?["hint","decimal"]:[])];
 const boxes=names.map(k=>source.critical[k]),left=Math.min(...boxes.map(b=>b[0])),top=Math.min(...boxes.map(b=>b[1])),right=Math.max(...boxes.map(b=>b[2])),bottom=Math.max(...boxes.map(b=>b[3]));
 const scale=p.width/(right-left),height=(bottom-top)*scale,box={left:p.x,top:p.y,right:p.x+p.width,bottom:p.y+height};
 const critical=Object.fromEntries(names.map(k=>{const [l,t,r,b]=source.critical[k];return [k,rectPoly({left:p.x+(l-left)*scale,top:p.y+(t-top)*scale,right:p.x+(r-left)*scale,bottom:p.y+(b-top)*scale})]}));
 return {...p,width:p.width,height,scale,box,crop:{width:right-left,height:bottom-top},critical};
});
