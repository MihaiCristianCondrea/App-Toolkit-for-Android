import {copyFile,mkdir} from "node:fs/promises";
import path from "node:path";
import {fileURLToPath} from "node:url";
const source=fileURLToPath(new URL("../",import.meta.url)),review=path.resolve(source,"../review");
await mkdir(review,{recursive:true});
for(const name of ["contact-clean.png","contact-debug.png","contact-tablet-clean.png","contact-tablet-debug.png","contact-desktop-clean.png","contact-desktop-debug.png"]){
 await copyFile(path.join(source,"output/verify",name),path.join(review,name));
}
await copyFile(path.join(source,"output/verify/report.json"),path.join(review,"verification.json"));
await copyFile(path.join(source,"output/repeatability.json"),path.join(review,"repeatability.json"));
for(const [from,to] of [["comparison.png","feature-concepts.png"],["thumbnails.png","feature-thumbnails.png"],["hero-warm.png","hero-warm.png"],["hero-lilac.png","hero-lilac.png"]]){
 await copyFile(path.join(source,"output/feature-refinement",from),path.join(review,to));
}
console.log("Archived contact sheets, concepts, verification and repeatability.");
