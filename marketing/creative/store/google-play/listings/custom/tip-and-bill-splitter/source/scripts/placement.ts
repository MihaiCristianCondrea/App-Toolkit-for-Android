import {readFileSync,writeFileSync,mkdirSync} from "node:fs";
import {SHOTS} from "../src/composition/shots.ts";
import {composeShot} from "../src/composition/compose.ts";
import {W,H,GOLDEN,CAPTION,bbox,overlapArea,centre} from "../src/composition/geometry.ts";
const read=(name:string)=>JSON.parse(readFileSync(new URL("../src/composition/"+name,import.meta.url),"utf8"));
const ui=read("ui.json"),layout=read("layout.json"),round=(n:number)=>Math.round(n*1000)/1000;
const shots=SHOTS.map((spec,i)=>{
 const g=composeShot(i,SHOTS.length,layout,ui);
 return {id:spec.id,intent:spec.intent,caption:CAPTION[spec.caption].block,hard:g.report.hard,balance:g.report.balance.map((n,k)=>round(n/(k?H:W))),
  phones:g.phones.map((m,j)=>({screen:spec.screens[j],pose:m.pose,bounds:m.full,centroid:m.centroid.map((n,k)=>round(n/(k?H:W))),area:round(m.area/(W*H)),
   critical:Object.fromEntries(Object.entries(m.critical).map(([key,p])=>[key,{box:bbox(p),centre:centre(p).map((n,k)=>round(n/(k?H:W)))}]))})),
  pair:g.phones.length===2?{horizontalGap:round(g.phones[1].full.left-g.phones[0].full.right),overlap:round(overlapArea(g.phones[0].visible,g.phones[1].visible)),stagger:round(g.phones[1].full.top-g.phones[0].full.top)}:null,
  shapes:g.shapes.map(p=>({name:p.name,x:p.x,y:p.y,size:p.size,behind:!!p.behind})),intentional:g.report.intentional};
});
mkdirSync(new URL("../output/",import.meta.url),{recursive:true});
writeFileSync(new URL("../output/placement.json",import.meta.url),JSON.stringify({golden:GOLDEN,shots},null,2)+"\n");
console.log(JSON.stringify(shots.map(s=>({id:s.id,hard:s.hard,pair:s.pair}))));
