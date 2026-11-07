import React from "react";
import { Img, staticFile } from "remotion";

/** A precomputed optical surface, always behind UI and captions. No live clock or GPU water simulation. */
export const WaterArt: React.FC<{width:number;height:number;panels?:number}> = ({width,height}) => {
 const texture=height===1920?"phone":height===1080?"wide":"feature";
 return <Img src={staticFile(`water/${texture}.png`)} aria-hidden style={{position:"absolute",inset:0,width,height,pointerEvents:"none"}}/>;
};
