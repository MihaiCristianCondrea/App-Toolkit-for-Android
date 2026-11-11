import {bundle} from "./bundle.mjs";
import {renderStill,selectComposition} from "@remotion/renderer";
import {mkdir} from "node:fs/promises";
import {fileURLToPath} from "node:url";
import path from "node:path";
import {readPng,writePng,shrink,sheet} from "./png.mjs";
const root=fileURLToPath(new URL("../",import.meta.url)),out=path.join(root,"output/concepts");
await mkdir(out,{recursive:true});
const serveUrl=await bundle({entryPoint:path.join(root,"src/index.ts"),publicDir:path.join(root,"public"),enableCaching:false});
const features=[];
for(const concept of ["transformation","connected","editorial"]){
 const inputProps={locale:"en-US",concept},output=path.join(out,concept+".png");
 await renderStill({serveUrl,composition:await selectComposition({serveUrl,id:"FeatureGraphic",inputProps}),inputProps,output,timeoutInMilliseconds:120000});
 features.push(await readPng(output));
}
await writePng(path.join(out,"feature-comparison.png"),sheet(features,24));
await writePng(path.join(out,"feature-thumbnails.png"),sheet(features.map(i=>shrink(i,4)),16));
const heroes=[];
for(const exploration of [false,true]){
 const inputProps={locale:"en-US",index:0,exploration},output=path.join(out,exploration?"hero-flow.png":"hero-paired.png");
 await renderStill({serveUrl,composition:await selectComposition({serveUrl,id:"PhoneShot",inputProps}),inputProps,output,scale:.5,timeoutInMilliseconds:120000});
 heroes.push(await readPng(output));
}
await writePng(path.join(out,"hero-comparison.png"),sheet(heroes,24));
console.log(out);
