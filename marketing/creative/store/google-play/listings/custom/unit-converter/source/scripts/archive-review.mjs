import {copyFile,mkdir} from "node:fs/promises";
import {fileURLToPath} from "node:url";
import path from "node:path";
const root=fileURLToPath(new URL("../",import.meta.url)),review=path.resolve(root,"../review");
await mkdir(review,{recursive:true});
for(const f of ["contact-clean.png","contact-debug.png","contact-tablet-clean.png","contact-tablet-debug.png","contact-desktop-clean.png","contact-desktop-debug.png"])
 await copyFile(path.join(root,"output/verify",f),path.join(review,f));
await copyFile(path.join(root,"output/verify/report.json"),path.join(review,"verification.json"));
await copyFile(path.join(root,"output/repeatability.json"),path.join(review,"repeatability.json"));
for(const f of ["feature-comparison.png","feature-thumbnails.png","hero-comparison.png","hero-paired.png","hero-flow.png"])
 await copyFile(path.join(root,"output/concepts",f),path.join(review,f));
console.log("Archived review assets");
await copyFile(path.join(root,"output/capture-checks.json"),path.join(review,"capture-checks.json"));
