import { html, svg, nothing } from "lit";

// CoinFlipTool.kt drawables, DiceRollTool.kt face numbers and RandomNumberTool.kt arrangement.
const COINS = {"heads":{"view":"0 0 960 960","path":"M480,880Q397,880 324,848.5Q251,817 197,763Q143,709 111.5,636Q80,563 80,480Q80,397 111.5,324Q143,251 197,197Q251,143 324,111.5Q397,80 480,80Q563,80 636,111.5Q709,143 763,197Q817,251 848.5,324Q880,397 880,480Q880,563 848.5,636Q817,709 763,763Q709,817 636,848.5Q563,880 480,880ZM479,760Q493,760 503.5,749.5Q514,739 514,725L514,710Q564,701 600,671Q636,641 636,582Q636,540 612,505Q588,470 516,444Q456,424 433,409Q410,394 410,368Q410,342 428.5,327Q447,312 482,312Q502,312 517,319Q532,326 542,338Q552,350 564.5,354.5Q577,359 588,354Q603,348 608.5,333.5Q614,319 606,307Q590,284 566.5,268Q543,252 516,250L516,235Q516,221 505.5,210.5Q495,200 481,200Q467,200 456.5,210.5Q446,221 446,235L446,250Q396,261 368,294Q340,327 340,368Q340,415 367.5,444Q395,473 454,494Q517,517 541.5,535Q566,553 566,582Q566,615 542.5,630.5Q519,646 486,646Q460,646 439,633.5Q418,621 404,596Q396,582 383,577Q370,572 357,577Q343,582 336.5,596Q330,610 336,623Q352,657 379,678.5Q406,700 444,708L444,725Q444,739 454.5,749.5Q465,760 479,760Z"},"tails":{"view":"0 0 24 24","path":"M12 2c1.38 0 2.68 0.26 3.9 0.79 1.22 0.52 2.28 1.23 3.18 2.13 0.9 0.9 1.6 1.96 2.13 3.18C21.74 9.32 22 10.62 22 12s-0.26 2.68-0.79 3.9c-0.52 1.22-1.23 2.28-2.13 3.18-0.9 0.9-1.96 1.6-3.18 2.13C14.68 21.74 13.38 22 12 22s-2.68-0.26-3.9-0.79c-1.22-0.52-2.28-1.23-3.18-2.13-0.9-0.9-1.6-1.96-2.13-3.18C2.26 14.68 2 13.38 2 12s0.26-2.68 0.79-3.9C3.3 6.88 4.02 5.82 4.92 4.92c0.9-0.9 1.96-1.6 3.18-2.13C9.32 2.26 10.62 2 12 2ZM7.8 16.2c-0.2 0-0.37 0.07-0.5 0.2-0.13 0.14-0.2 0.3-0.2 0.5s0.07 0.36 0.2 0.5c0.13 0.13 0.3 0.2 0.5 0.2h8.4c0.2 0 0.37-0.07 0.5-0.2 0.13-0.14 0.2-0.3 0.2-0.5s-0.07-0.36-0.2-0.5c-0.13-0.13-0.3-0.2-0.5-0.2H7.8ZM12 5c-0.3 0-0.54 0.1-0.74 0.3-0.2 0.21-0.31 0.46-0.31 0.75 0 0.17 0.04 0.34 0.12 0.49 0.08 0.15 0.19 0.27 0.32 0.37L9.2 9.9 7.01 8.92c0.03-0.06 0.05-0.12 0.06-0.2C7.1 8.67 7.1 8.59 7.1 8.5c0-0.3-0.1-0.54-0.3-0.74-0.21-0.2-0.46-0.31-0.75-0.31-0.3 0-0.54 0.1-0.74 0.3C5.1 7.97 5 8.22 5 8.5c0 0.3 0.1 0.54 0.3 0.74 0.21 0.2 0.46 0.31 0.75 0.31h0.08L6.2 9.52l0.7 4.45c0.06 0.34 0.22 0.62 0.48 0.84 0.26 0.22 0.56 0.33 0.9 0.33h7.42c0.34 0 0.64-0.11 0.9-0.33 0.26-0.22 0.42-0.5 0.48-0.84l0.7-4.45 0.08 0.01 0.08 0.01c0.3 0 0.54-0.1 0.74-0.3C18.9 9.03 19 8.78 19 8.5c0-0.3-0.1-0.54-0.3-0.74-0.21-0.2-0.46-0.31-0.75-0.31-0.3 0-0.54 0.1-0.74 0.3C17 7.97 16.9 8.22 16.9 8.5c0 0.08 0 0.16 0.03 0.23 0.01 0.07 0.03 0.13 0.06 0.19L14.8 9.9l-2.19-3c0.13-0.09 0.24-0.2 0.32-0.36 0.08-0.15 0.12-0.32 0.12-0.49 0-0.3-0.1-0.54-0.3-0.74C12.53 5.1 12.28 5 12 5Z"}};
export function coinVisual(side = "Heads", small = false) {
  return html`<div class="chance-coin-stage ${small ? 'chance-coin-stage--small' : ''}" role="img" aria-label=${side}><div class="chance-coin" style="transform:rotateY(${side==='Tails'?180:0}deg)">${['heads','tails'].map((name,i)=>html`<div class="chance-coin__face ${i?'chance-coin__face--back':''}"><svg viewBox=${COINS[name].view} aria-hidden="true">${svg`<path d=${COINS[name].path} fill="currentColor"/>`}</svg></div>`)}</div></div>`;
}
const PIPS = {1:[4],2:[0,8],3:[0,4,8],4:[0,2,6,8],5:[0,2,4,6,8],6:[0,2,3,5,6,8]};
const ANGLES = {1:[-90,0],2:[0,0],3:[0,-90],4:[0,90],5:[0,180],6:[90,0]};
export function diceVisual(value = 1, small = false) {
  const [x,y] = ANGLES[value] || ANGLES[1];
  return html`<div class="chance-dice-stage ${small ? 'chance-dice-stage--small' : ''}" role="img" aria-label="Die showing ${value}"><div class="chance-dice" style="transform:rotateX(${x}deg) rotateY(${y}deg)">${[2,5,3,4,1,6].map((v,i)=>html`<div class="chance-dice__face chance-dice__face--${i}">${Array.from({length:9},(_,p)=>html`<i class=${PIPS[v].includes(p)?'pip':'blank'}></i>`)}</div>`)}</div></div>`;
}
// One motion path for widgets and sheets. Geometry stays reserved while only transforms animate.
const activeTosses = new WeakSet();
export async function playChance(event, id, state, update) {
  event.stopPropagation();
  if (event.type === 'keydown') {
    if (!['Enter',' '].includes(event.key)) return;
    event.preventDefault();
  }
  const root = event.currentTarget.closest('.qt-chance, .chance-body');
  if (!root || activeTosses.has(root)) return;
  activeTosses.add(root);
  const coin = id === 'coin_flip';
  const result = coin ? {coinSide:Math.random()<.5?'Heads':'Tails'} : {diceResult:1+Math.floor(Math.random()*6)};
  const rotor = root.querySelector(coin?'.chance-coin':'.chance-dice');
  const stage = root.querySelector(coin?'.chance-coin-stage':'.chance-dice-stage');
  const [x,y] = coin ? [0,result.coinSide==='Tails'?180:0] : ANGLES[result.diceResult];
  const reduced = matchMedia('(prefers-reduced-motion: reduce)').matches;
  const end = coin ? `rotateY(${1800+y}deg)` : `rotateX(${1080+x}deg) rotateY(${1440+y}deg) rotateZ(1800deg)`;
  root.dataset.tossing = 'true';
  const button = root.querySelector('md-filled-button');
  if (button) button.disabled = true;
  const spin = rotor.animate([{transform:rotor.style.transform},{transform:end}],{duration:reduced?0:coin?1000:960,easing:'cubic-bezier(.4,0,.2,1)',fill:'forwards'});
  const lift = stage.animate([{translate:'0 0',scale:1},{translate:coin?'0 -24px':'0 -40px',scale:coin?1.12:.9,offset:.35},{translate:'0 0',scale:1}],{duration:reduced?0:coin?1000:960,easing:'cubic-bezier(.4,0,.2,1)'});
  try {
    await Promise.all([spin.finished,lift.finished]);
    if(root.isConnected) {
      update(result);
      // Keep the final pose until Lit has painted the same result underneath it.
      await new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve)));
    }
  } catch { /* Closing the host can interrupt motion. */ }
  finally {
    spin.cancel(); lift.cancel(); delete root.dataset.tossing;
    if(button) button.disabled=false;
    activeTosses.delete(root);
  }
}
const parseLong = value => {
  const text = String(value).trim();
  if (!/^[+-]?\d+$/.test(text)) return null;
  const n = BigInt(text);
  return n >= -(1n<<63n) && n < (1n<<63n) ? n : null;
};
function randomLong(lo,hi) {
  const span=hi-lo+1n, limit=(1n<<64n)-((1n<<64n)%span), words=new Uint32Array(2);
  let n; do { crypto.getRandomValues(words); n=(BigInt(words[0])<<32n)|BigInt(words[1]); } while(n>=limit);
  return String(lo+n%span);
}
export function chanceBody(tool,state,onState) {
  if(tool.id === 'random_number') {
    const min=String(state.randomMin??1),max=String(state.randomMax??100),lo=parseLong(min),hi=parseLong(max);
    const invalid=lo===null||hi===null||lo>hi;
    const change=patch=>onState({...patch,randomResult:null});
    const presets=[6,10,100,2];
    return html`<div class="tk-tool-interactive chance-body random-picker">
      <div class="random-picker__result">
        <span class="random-picker__eyebrow">${state.randomResult==null?'Ready to pick':'Your number'}</span>
        <div class="chance-number" style=${String(state.randomResult??'?').length>12?'font-size:32px':''} aria-live="polite" aria-label="Random result" dir="ltr">${state.randomResult==null?'?':BigInt(state.randomResult).toLocaleString()}</div>
        <span class="random-picker__range">${invalid?'Choose a valid range':`From ${lo.toLocaleString()} to ${hi.toLocaleString()}`}</span>
      </div>
      <div class="random-picker__settings">
        <h4 class="random-picker__label">Number range</h4>
        <md-outlined-segmented-button-set aria-label="Number range presets" @segmented-button-set-selection=${e=>{
          if(!e.detail.selected) { e.detail.button.selected=true; return; }
          change({randomMin:'1',randomMax:String(presets[e.detail.index])});
        }}>${presets.map(n=>html`<md-outlined-segmented-button label="1–${n}" .selected=${lo===1n&&hi===BigInt(n)}></md-outlined-segmented-button>`)}</md-outlined-segmented-button-set>
        <div class="chance-range">${[['Min',min,'randomMin'],['Max',max,'randomMax']].map(([label,value,key])=>html`<md-filled-text-field label=${label} .value=${value} inputmode="numeric" ?error=${invalid} @input=${e=>e.target.value.length<=20 && change({[key]:e.target.value})}></md-filled-text-field>`)}</div>
        <p class="random-picker__hint ${invalid?'chance-error':''}" role=${invalid?'alert':'note'}>${invalid?'Enter whole numbers with Min no greater than Max.':'Both ends of the range are included.'}</p>
      </div>
      <md-filled-button class="random-picker__generate" ?disabled=${invalid} @click=${()=>onState({randomResult:randomLong(lo,hi)})}><md-icon slot="icon">casino</md-icon>Generate</md-filled-button>
    </div>`;

  }
  if(tool.id==='dice_roll') return html`<div class="tk-tool-interactive tk-tool-interactive--center chance-body">${diceVisual(state.diceResult)}<md-filled-button @click=${e=>playChance(e,'dice_roll',state,onState)}><md-icon slot="icon">play_arrow</md-icon>Roll die</md-filled-button></div>`;
  if(tool.id==='coin_flip') return html`<div class="tk-tool-interactive tk-tool-interactive--center chance-body">${coinVisual(state.coinSide)}<span class="chance-side" aria-live="polite">${state.coinSide??'Heads'}</span><md-filled-button @click=${e=>playChance(e,'coin_flip',state,onState)}><md-icon slot="icon">play_arrow</md-icon>Flip coin</md-filled-button></div>`;
  return null;
}
