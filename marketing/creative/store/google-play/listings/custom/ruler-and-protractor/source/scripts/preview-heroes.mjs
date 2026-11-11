import {bundle} from "@remotion/bundler";
import {renderStill,selectComposition} from "@remotion/renderer";
import {mkdir} from "node:fs/promises";
import path from "node:path";
import {fileURLToPath} from "node:url";
const root=fileURLToPath(new URL("../",import.meta.url));
await mkdir(path.join(root,"output/heroes"),{recursive:true});
const serveUrl=await bundle({entryPoint:path.join(root,"src/index.ts"),publicDir:path.join(root,"public"),enableCaching:false});
for(let concept=0;concept<3;concept++){
 const inputProps={concept};
 const composition=await selectComposition({serveUrl,id:"HeroStudy",inputProps});
 await renderStill({serveUrl,composition,inputProps,output:path.join(root,"output/heroes",`hero-${concept+1}.png`),scale:0.5,timeoutInMilliseconds:120000});
 console.log(`Hero ${concept+1} rendered`);
}
