import {readFileSync,writeFileSync,mkdirSync} from "node:fs";
import {DIRECTIONS} from "../src/composition/shots.ts";
import {centre,measure,W,H,GOLDEN,CAPTION,SPACE} from "../src/composition/geometry.ts";
const read=(name:string)=>JSON.parse(readFileSync(new URL("../src/composition/"+name,import.meta.url),"utf8"));
const ui=read("ui.json"),layout=read("layout.json");
const round=(n:number)=>Math.round(n*1000)/1000;
const shots=DIRECTIONS.map((d,i)=>{
 const m=measure(layout.shots[i].poses[0],ui[d.id]),point=centre(m.critical[d.focus]);
 const top=d.caption==="top"?CAPTION.top.block.bottom+SPACE.xl:SPACE.xl;
 const bottom=d.caption==="bottom"?CAPTION.bottom.block.top-SPACE.xxl:H-SPACE.xl;
 return {id:d.id,primaryAnchor:"Projected phone silhouette",pose:m.pose,framingTarget:[W/2,round((top+bottom)/2)],framingActual:[round(m.centroid[0]),round((m.full.top+m.full.bottom)/2)],framingErrorPx:[round(m.centroid[0]-W/2),round((m.full.top+m.full.bottom-top-bottom)/2)],focus:d.focus,guideY:round(d.guideY),focusActual:point.map(round),focusNormalized:point.map((n,k)=>round(n/(k?H:W))),guideOffsetY:round(point[1]-d.guideY),centroid:m.centroid.map((n,k)=>round(n/(k?H:W))),bounds:m.full};
});
mkdirSync(new URL("../output/",import.meta.url),{recursive:true});writeFileSync(new URL("../output/placement.json",import.meta.url),JSON.stringify({golden:GOLDEN,shots},null,2)+"\n");
console.log(JSON.stringify(shots.map(({id,pose,centroid,framingErrorPx})=>({id,pitch:pose.pitch,yaw:pose.yaw,centroid,framingErrorPx})),null,2));
