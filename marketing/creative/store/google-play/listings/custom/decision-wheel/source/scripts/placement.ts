import {readFileSync,writeFileSync} from "node:fs";
import {DIRECTIONS} from "../src/composition/shots.ts";
import {centre,measure,W,H,GOLDEN} from "../src/composition/geometry.ts";
const read=(name:string)=>JSON.parse(readFileSync(new URL("../src/composition/"+name,import.meta.url),"utf8"));
const ui=read("ui.json"),layout=read("layout.json");
const round=(n:number)=>Math.round(n*1000)/1000;
const shots=DIRECTIONS.map((d,i)=>{
 const m=measure(layout.shots[i].poses[0],ui[d.id]),point=centre(m.critical[d.focus]);
 return {id:d.id,focus:d.focus,pose:m.pose,target:d.target.map(round),actual:point.map(round),normalized:point.map((n,k)=>round(n/(k?H:W))),errorPx:point.map((n,k)=>round(n-d.target[k])),centroid:m.centroid.map((n,k)=>round(n/(k?H:W))),bounds:m.full};
});
writeFileSync(new URL("../../review/placement.json",import.meta.url),JSON.stringify({golden:GOLDEN,shots},null,2)+"\n");
console.log(JSON.stringify(shots.map(({id,pose,normalized,errorPx})=>({id,pitch:pose.pitch,yaw:pose.yaw,normalized,errorPx})),null,2));
