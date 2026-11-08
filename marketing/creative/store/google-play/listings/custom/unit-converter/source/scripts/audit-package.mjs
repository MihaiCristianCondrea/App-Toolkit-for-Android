import {readFile,writeFile,mkdir,readdir} from "node:fs/promises";
import {fileURLToPath} from "node:url";
import path from "node:path";
import {createHash} from "node:crypto";
import {readPng} from "./png.mjs";
const source=fileURLToPath(new URL("../",import.meta.url)),root=path.resolve(source,".."),locale=path.join(root,"listing/en-US");
const assert=(ok,message)=>{if(!ok)throw Error(message);};
const lengths={};
for(const [name,max] of [["title",30],["short-description",80],["full-description",4000]]){
 const text=(await readFile(path.join(locale,name+".txt"),"utf8")).replace(/\r\n/g,"\n").trim();
 lengths[name]=[...text].length;assert(lengths[name]>0&&lengths[name]<=max,name+" length");
}
const keywords=(await readFile(path.join(root,"keywords.txt"),"utf8")).trim().split(/\r?\n/);
assert(keywords.length<=500,"Keyword count");assert(new Set(keywords.map(k=>k.toLowerCase())).size===keywords.length,"Duplicate keywords");
assert(keywords.every(k=>k.length>0&&k.length<=80&&k===k.trim()),"Keyword length or whitespace");
const graphics=path.join(locale,"graphics"),assets=[];
for(const [kind,count,w,h,type] of [["phone-screenshots",6,2160,3840,2],["tablet-screenshots",4,3840,2160,2],["desktop-screenshots",4,3840,2160,2],["feature-graphic",1,1024,500,2],["icon",1,512,512,6]]){
 const files=(await readdir(path.join(graphics,kind))).filter(f=>f.endsWith(".png"));assert(files.length===count,kind+" count");
 for(const file of files){
  const full=path.join(graphics,kind,file),buf=await readFile(full),img=await readPng(full);
  assert(img.width===w&&img.height===h&&buf[25]===type,"PNG dimensions/mode: "+file);
  assets.push({file:kind+"/"+file,width:w,height:h,mode:type===2?"RGB":"RGBA",sha256:createHash("sha256").update(buf).digest("hex")});
 }
}
const iconPixels=(await readPng(path.join(graphics,"icon/icon.png"))).data;
const officialPixels=(await readPng(path.resolve(root,"../ruler-and-protractor/listing/en-US/graphics/icon/icon.png"))).data;
assert(Buffer.from(iconPixels).equals(Buffer.from(officialPixels)),"Official icon pixels changed");
const alt=(await readFile(path.join(locale,"alt-text.txt"),"utf8")).trim().split(/\r?\n/);
assert(alt.length===14,"Alt count");for(const row of alt){const [file,text]=row.split("\t");assert(assets.some(a=>a.file===file)&&text?.length>0&&text.length<=140,"Alt text: "+file);}
const geometry=JSON.parse(await readFile(path.join(source,"output/verify/report.json"),"utf8"));
assert(geometry.length===14&&geometry.every(g=>g.hard.length===0),"Geometry verification failures");
const repeat=JSON.parse(await readFile(path.join(source,"output/repeatability.json"),"utf8"));assert(repeat.length===6&&repeat.every(r=>r.identical),"Repeatability");
const capture=JSON.parse(await readFile(path.join(source,"output/capture-checks.json"),"utf8"));
assert(capture.browserErrors.length===0&&capture.captures.length>=20,"Capture resource errors or missing runtime checks");
for(const c of capture.captures)for(const family of ["Google Sans Flex","Material Symbols Outlined","Material Symbols Rounded"])
 assert(c.fonts.some(f=>f.replace(/['"]/g,"")===family),"Required font not loaded: "+family);
const report={captureChecks:capture.captures.length,browserResourceErrors:0,requiredFontsLoaded:true,listing:"unit-converter",lengths,keywordCandidates:keywords.length,assetCount:assets.length,officialIconPixelIdentical:true,repeatablePhoneCaptures:6,assets};
await mkdir(path.join(root,"review"),{recursive:true});
await writeFile(path.join(root,"review/package-audit.json"),JSON.stringify(report,null,2)+"\n");
console.log(JSON.stringify({lengths,keywords:keywords.length,assets:assets.length,icon:"pixel-identical",repeatable:6}));
