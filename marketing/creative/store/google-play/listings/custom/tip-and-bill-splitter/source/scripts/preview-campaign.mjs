import {bundle} from "./bundle.mjs";
import {renderStill,selectComposition} from "@remotion/renderer";
import {mkdir,readFile} from "node:fs/promises";
import {fileURLToPath} from "node:url";
import path from "node:path";
import {readPng,writePng,sheet} from "./png.mjs";
const root=fileURLToPath(new URL("../",import.meta.url)),out=path.join(root,"output/regrouped");
await mkdir(out,{recursive:true});
const strings=JSON.parse(await readFile(path.join(root,"strings/en-US.json"),"utf8"));
const serveUrl=await bundle({entryPoint:path.join(root,"src/index.ts"),publicDir:path.join(root,"public"),enableCaching:false});
const wanted=process.argv.slice(2).map(Number);
for(let index=0;index<strings.screenshots.length;index++){
 if(wanted.length&&!wanted.includes(index))continue;
 const inputProps={locale:"en-US",index},output=path.join(out,(index+1)+".png");
 await renderStill({serveUrl,composition:await selectComposition({serveUrl,id:"PhoneShot",inputProps}),inputProps,output,scale:.5,timeoutInMilliseconds:120000});

}
await writePng(path.join(out,"contact.png"),sheet(await Promise.all(strings.screenshots.map((s,i)=>readPng(path.join(out,(i+1)+".png")))),24));
console.log(out);
