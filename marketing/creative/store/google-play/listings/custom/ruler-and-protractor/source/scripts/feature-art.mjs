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

if(process.argv.includes("--publish")){
 const {execFileSync}=await import("node:child_process");
 const {copyFile,rename}=await import("node:fs/promises");
 const target=path.resolve(root,"../listing/en-US/graphics/feature-graphic/feature-graphic.png");
 const staged=path.join(out,"final.png");
 await copyFile(path.join(out,"ribbon.png"),staged);
 execFileSync(process.platform==="win32"?"python":"python3",["-c",'from PIL import Image; import sys; Image.open(sys.argv[1]).convert("RGB").save(sys.argv[1])',path.toNamespacedPath(staged)]);
 const image=await readPng(staged);
 if(image.width!==1024||image.height!==500)throw Error("Invalid feature dimensions");
 await copyFile(staged,target+".tmp");
 await rename(target+".tmp",target);
 console.log("Published feature graphic only");
}
