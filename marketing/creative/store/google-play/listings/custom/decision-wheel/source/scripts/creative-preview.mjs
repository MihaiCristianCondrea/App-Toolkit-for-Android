import {bundle} from "@remotion/bundler";
import {renderStill,selectComposition} from "@remotion/renderer";
import {mkdir} from "node:fs/promises";
import {fileURLToPath} from "node:url";
import path from "node:path";
import {readPng,writePng,shrink,sheet} from "./png.mjs";
const root=fileURLToPath(new URL("../",import.meta.url));
const out=path.join(root,"output/feature-refinement");
await mkdir(out,{recursive:true});
const serveUrl=await bundle({entryPoint:path.join(root,"src/index.ts"),publicDir:path.join(root,"public"),enableCaching:false});
const imgs=[];
for(const concept of ["ribbon","ripple","editorial"]){
 const inputProps={locale:"en-US",concept};
 const composition=await selectComposition({serveUrl,id:"FeatureGraphic",inputProps});
 const output=path.join(out,concept+".png");
 await renderStill({serveUrl,composition,inputProps,output,timeoutInMilliseconds:120000});
 imgs.push(await readPng(output));
}
await writePng(path.join(out,"comparison.png"),sheet(imgs,24));
await writePng(path.join(out,"thumbnails.png"),sheet(imgs.map(i=>shrink(i,4)),16));
console.log(out);


for(const exploration of [false,true]){
 const inputProps={locale:"en-US",index:0,exploration};
 await renderStill({serveUrl,composition:await selectComposition({serveUrl,id:"PhoneShot",inputProps}),inputProps,output:path.join(out,exploration?"hero-warm.png":"hero-lilac.png"),scale:.5,timeoutInMilliseconds:120000});
}
