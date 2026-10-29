import { ref } from "lit/directives/ref.js";
import { html, svg, nothing } from 'lit';
import { slider, rangeSlider } from "./slider.js";
import { windowOf } from "./layout.js";
// Without set() (captures) nothing measures the page, so sizes start from the window: the full-screen
// ruler is as tall as it, and the protractor as wide as a sheet in it.

// Translated from the six Compose tools in sample/feature/tiles/ui/views/tools.
const choice = (label, values, selected, change) => html`<md-outlined-segmented-button-set aria-label=${label} @segmented-button-set-selection=${e=>e.detail.selected&&change(values[e.detail.index][0])}>${values.map(([id,text])=>html`<md-outlined-segmented-button label=${text} .selected=${id===selected}></md-outlined-segmented-button>`)}</md-outlined-segmented-button-set>`;
const field=(label,value,change,error=false)=>html`<md-outlined-text-field label=${label} .value=${String(value)} inputmode="decimal" ?error=${error} @input=${e=>e.target.value.length<=64&&change(e.target.value)}></md-outlined-text-field>`;
const decimal = value => {
  const text=String(value).trim().replace(/[٠-٩]/g,d=>String(d.charCodeAt(0)-1632)).replace(/[۰-۹]/g,d=>String(d.charCodeAt(0)-1776)).replace(/[０-９]/g,d=>String(d.charCodeAt(0)-65296)).replace(/[,٫]/g,'.');
  return /^[+-]?(?:\d+(?:\.\d*)?|\.\d+)$/.test(text)?text:null;
};
const CURRENCIES = [
  {id:'USD',symbol:'$',digits:2}, {id:'EUR',symbol:'€',digits:2}, {id:'GBP',symbol:'£',digits:2},
  {id:'RON',symbol:'lei',digits:2}, {id:'JPY',symbol:'¥',digits:0}, {id:'INR',symbol:'₹',digits:2},
];
const currencyOf=s=>CURRENCIES.find(c=>c.id===s.billCurrency)??CURRENCIES[0];
function billMoney(value,currency) {
  const factor=10n**BigInt(currency.digits);
  const amount=(value/factor).toLocaleString('en-US')+(currency.digits?'.'+String(value%factor).padStart(currency.digits,'0'):'');
  return currency.id==='RON'?amount+' lei':currency.symbol+amount;
}
export function billSummary(s) {
  const bill=String(s.billAmount??''),people=decimal(s.splitPeople??1)??String(s.splitPeople??1),currency=currencyOf(s),tipEnabled=s.billTipEnabled!==false,pct=tipEnabled?Number(s.tipPercent??15):0,parsed=decimal(bill);
  const validAmount=parsed!==null&&/^\+?(?:\d+(?:\.\d*)?|\.\d+)$/.test(parsed)&&((parsed.split('.')[1]??'').length<=currency.digits);
  const valid=validAmount&&/^\+?\d+$/.test(people)&&BigInt(people)>0n&&BigInt(people)<(1n<<63n)&&Number.isInteger(pct)&&pct>=0&&pct<=100;
  let total,tip,share;
  if(valid){const parts=parsed.replace('+','').split('.'),factor=10n**BigInt(currency.digits);const minor=BigInt(parts[0]||0)*factor+BigInt((parts[1]??'').padEnd(currency.digits,'0')||0);tip=(minor*BigInt(pct)+50n)/100n;total=minor+tip;share=(total+BigInt(people)-1n)/BigInt(people);}
  return {bill,people,pct,valid,total,tip,share,currency,tipEnabled,perPerson:valid?billMoney(share,currency):null};
}

export function measureBody(tool,s,set) {
  if(tool.id==='counter') {
    const count=Math.max(0,Number(s.counterValue??0));
    return html`<div class="measure-body measure-counter"><div class="measure-counter__value" aria-live="polite">${count}</div><div class="measure-actions"><md-outlined-icon-button aria-label="Decrease" ?disabled=${count===0} @click=${()=>set?.({counterValue:count-1})}><md-icon>remove</md-icon></md-outlined-icon-button><md-filled-button @click=${()=>set?.({counterValue:Math.min(2147483647,count+1)})}><md-icon slot="icon">add</md-icon>Add</md-filled-button>${count?html`<md-outlined-button @click=${()=>set?.({counterValue:0})}><md-icon slot="icon">refresh</md-icon>Reset</md-outlined-button>`:nothing}</div></div>`;
  }
  if(tool.id==='bill_splitter') {
    const { bill, people, pct, valid, total, tip, share, currency, tipEnabled } = billSummary(s);
    return html`<div class="measure-body"><div class="bill-input-row">${field('Bill amount',bill,v=>set?.({billAmount:v}),!valid&&bill!=='')}<md-outlined-select class="bill-currency" label="Currency" .value=${currency.id} @change=${e=>set?.({billCurrency:e.target.value})}>${CURRENCIES.map(c=>html`<md-select-option .value=${c.id} ?selected=${c.id===currency.id}><div slot="headline">${c.symbol} · ${c.id}</div></md-select-option>`)}</md-outlined-select></div><label class="bill-tip-toggle"><md-checkbox aria-label="Include tip" ?checked=${tipEnabled} @change=${e=>set?.({billTipEnabled:e.target.checked})}></md-checkbox>Include tip</label><div class="bill-tip-expansion" ?data-expanded=${tipEnabled} ?inert=${!tipEnabled} aria-hidden=${!tipEnabled}><div><div class="bill-tip-options">${choice('Tip percentage',[10,15,20].map(v=>[v,v+'%']),Number(s.tipPercent??15),v=>set?.({tipPercent:v}))}</div></div></div><div class="measure-people"><md-icon-button aria-label="Remove person" ?disabled=${!/^\d+$/.test(people)||BigInt(people)<=1n} @click=${()=>set?.({splitPeople:String(BigInt(people)-1n)})}><md-icon>remove</md-icon></md-icon-button>${field('People',people,v=>set?.({splitPeople:v}),!valid)}<md-icon-button aria-label="Add person" ?disabled=${!/^\d+$/.test(people)||BigInt(people)<1n||BigInt(people)>=(1n<<63n)-1n} @click=${()=>set?.({splitPeople:String(BigInt(people)+1n)})}><md-icon>add</md-icon></md-icon-button></div>${!valid&&bill?html`<p class="chance-error" role="alert">Enter a non-negative bill with up to ${currency.digits} decimal places and at least one person.</p>`:nothing}${valid?html`<div class="measure-bill" aria-live="polite"><span>Per person</span><strong>${billMoney(share,currency)}</strong><div class="measure-summary"><span>Total<b>${billMoney(total,currency)}</b></span><span>Tip<b>${billMoney(tip,currency)}</b></span></div><p><md-icon>info</md-icon>Shares round up to ${currency.digits ? "2 decimal places" : "whole yen"}.</p></div>`:nothing}</div>`;
  }
  if(tool.id==='unit_converter')return converter(s,set);
  if(tool.id==='reaction_test')return reaction(s,set);
  return null;
}

export function rulerDialog(s,set,close) {
  const height=s.rulerHeight??windowOf(s.window).height,scale=Number(s.rulerScale??1),start=Number(s.rulerStart??0),end=Number(s.rulerEnd??.5),ppm=160/25.4*scale,inch=s.rulerUnit==='in',mm=(end-start)*height/ppm;
  const step=inch?ppm*25.4/16:ppm,major=inch?16:10,count=Math.min(4000,Math.floor(height/step));
  const pointer=e=>{if(e.type==='pointermove'&&!e.buttons)return;const rect=e.currentTarget.getBoundingClientRect(),fraction=Math.max(0,Math.min(1,(e.clientY-rect.top)/rect.height));const first=e.type==='pointerdown'?Math.abs(fraction-start)<Math.abs(fraction-end):e.currentTarget.dataset.handle==='start';if(e.type==='pointerdown'){e.currentTarget.dataset.handle=first?'start':'end';e.currentTarget.setPointerCapture(e.pointerId);}set?.(first?{rulerStart:Math.min(fraction,end)}:{rulerEnd:Math.max(fraction,start)});};
  return html`<div class="measure-ruler"><svg class="measure-ruler__scale" ${ref(el=>{if(el)requestAnimationFrame(()=>{if(el.isConnected&&Math.abs(el.clientHeight-height)>1)set?.({rulerHeight:el.clientHeight});});})} viewBox="0 0 96 ${height}" preserveAspectRatio="none" aria-label="Ruler measurement handles" @pointerdown=${pointer} @pointermove=${pointer}><rect x="0" y=${start*height} width="96" height=${(end-start)*height} fill="var(--md-sys-color-primary)" opacity=".14"/>${Array.from({length:count+1},(_,i)=>svg`<line x1="0" y1=${i*step} x2=${i%major===0?32:i%(major/2)===0?22:12} y2=${i*step} stroke="currentColor"/>${i%major?nothing:svg`<text x="36" y=${Math.max(12,i*step+4)} fill="currentColor">${i/major}</text>`}`)}${[start,end].map(f=>svg`<line x1="0" x2="96" y1=${f*height} y2=${f*height} stroke="var(--md-sys-color-primary)" stroke-width="3"/>`)}</svg><div class="measure-ruler__controls"><header><h3>Surface Ruler</h3><md-icon-button aria-label="Close" @click=${close}><md-icon>close</md-icon></md-icon-button></header><div class="measure-display" aria-live="polite">${(inch?mm/25.4:mm).toFixed(inch?2:1)} ${inch?'in':'mm'}</div>${choice('Ruler units',[['mm','mm'],['in','in']],s.rulerUnit??'mm',v=>set?.({rulerUnit:v}))}<h4>Measurement range</h4>${rangeSlider({start,end,min:0,max:1,step:0.001,labelStart:'Range start',labelEnd:'Range end',onInput:set&&(([a,b])=>set?.({rulerStart:a,rulerEnd:b}))})}<section class="measure-calibration"><h4>Calibration</h4><p>Match the line below to 3 cm on a real ruler before measuring.</p><div class="measure-ruler__reference-box" style="height:${30*160/25.4*2+8}px"><div class="measure-ruler__reference" style="height:${30*ppm}px" role="img" aria-label="30 millimetre reference"></div></div>${slider({value:scale,min:0.5,max:2,step:0.01,label:'Calibration',onInput:set&&(v=>set?.({rulerScale:v}))})}${s.rulerSaveFailed?html`<p class="chance-error">Could not save calibration.</p>`:nothing}</section></div></div>`;
}

const UNITS=[{"id":"Millimetre","category":"Length","symbol":"mm","factor":"0.001","offset":"0"},{"id":"Centimetre","category":"Length","symbol":"cm","factor":"0.01","offset":"0"},{"id":"Metre","category":"Length","symbol":"m","factor":"1","offset":"0"},{"id":"Kilometre","category":"Length","symbol":"km","factor":"1000","offset":"0"},{"id":"Inch","category":"Length","symbol":"in","factor":"0.0254","offset":"0"},{"id":"Foot","category":"Length","symbol":"ft","factor":"0.3048","offset":"0"},{"id":"Yard","category":"Length","symbol":"yd","factor":"0.9144","offset":"0"},{"id":"Mile","category":"Length","symbol":"mi","factor":"1609.344","offset":"0"},{"id":"Gram","category":"Mass","symbol":"g","factor":"0.001","offset":"0"},{"id":"Kilogram","category":"Mass","symbol":"kg","factor":"1","offset":"0"},{"id":"Ounce","category":"Mass","symbol":"oz","factor":"0.028349523125","offset":"0"},{"id":"Pound","category":"Mass","symbol":"lb","factor":"0.45359237","offset":"0"},{"id":"Stone","category":"Mass","symbol":"st","factor":"6.35029318","offset":"0"},{"id":"Celsius","category":"Temperature","symbol":"°C","factor":"1","offset":"273.15"},{"id":"Fahrenheit","category":"Temperature","symbol":"°F","factor":"1","offset":"0"},{"id":"Kelvin","category":"Temperature","symbol":"K","factor":"1","offset":"0"},{"id":"Millilitre","category":"Volume","symbol":"mL","factor":"0.001","offset":"0"},{"id":"Litre","category":"Volume","symbol":"L","factor":"1","offset":"0"},{"id":"CubicMetre","category":"Volume","symbol":"m³","factor":"1000","offset":"0"},{"id":"UsGallon","category":"Volume","symbol":"US gal","factor":"3.785411784","offset":"0"},{"id":"Byte","category":"Data","symbol":"B","factor":"1","offset":"0"},{"id":"Kilobyte","category":"Data","symbol":"kB","factor":"1000","offset":"0"},{"id":"Megabyte","category":"Data","symbol":"MB","factor":"1000000","offset":"0"},{"id":"Gigabyte","category":"Data","symbol":"GB","factor":"1000000000","offset":"0"},{"id":"Terabyte","category":"Data","symbol":"TB","factor":"1000000000000","offset":"0"}];
// Decimal rational arithmetic avoids binary rounding in input and unit factors.
const rational=text=>{const t=decimal(text);if(t===null)return null;const [i,f='']=t.split('.');return [BigInt((i||'0')+f),10n**BigInt(f.length)];};
const add=(a,b)=>[a[0]*b[1]+b[0]*a[1],a[1]*b[1]];
const mul=(a,b)=>[a[0]*b[0],a[1]*b[1]];
const div=(a,b)=>[a[0]*b[1],a[1]*b[0]];
const neg=a=>[-a[0],a[1]];
function format(a) {
  let [n,d]=a;if(d<0n){n=-n;d=-d;}const sign=n<0n?'-':'';n=n<0n?-n:n;if(n===0n)return '0';
  let exponent=n.toString().length-d.toString().length;
  const above=e=>e>=0?n>=d*10n**BigInt(e):n*10n**BigInt(-e)>=d;
  if(!above(exponent))exponent--;
  const places=15-exponent,p=10n**BigInt(Math.abs(places));
  const numerator=places>=0?n*p:n,denominator=places>=0?d:d*p;
  let q=numerator/denominator;const rem=numerator%denominator;
  if(rem*2n>denominator||(rem*2n===denominator&&q%2n===1n))q++;
  let str=q.toString();if(places>0){str=str.padStart(places+1,'0');str=str.slice(0,-places)+'.'+str.slice(-places);str=str.replace(/0+$/,'').replace(/\.$/,'');}else str+='0'.repeat(-places);
  return sign+str;
}
function convert(input,from,to) {
  let value=rational(input);if(!value)return null;
  let base=from.id==='Fahrenheit'?add(div(mul(add(value,[-32n,1n]),[5n,1n]),[9n,1n]),rational('273.15')):add(mul(value,rational(from.factor)),rational(from.offset));
  if(from.category==='Temperature'&&base[0]<0n)return null;
  return format(to.id==='Fahrenheit'?add(div(mul(add(base,neg(rational('273.15'))),[9n,1n]),[5n,1n]),[32n,1n]):div(add(base,neg(rational(to.offset))),rational(to.factor)));
}
export function conversionSummary(s) {
  const category=s.converterCategory??'Length',units=UNITS.filter(u=>u.category===category);
  const from=units.find(u=>u.id===s.converterFrom)??units.find(u=>u.id===(category==='Length'?'Metre':''))??units[0];
  const to=units.find(u=>u.id===s.converterTo)??units.find(u=>u.id===(category==='Length'?'Centimetre':''))??units[1];
  const editingLeft=s.converterEditingLeft!==false,input=String(s.converterInput??1),output=convert(input,editingLeft?from:to,editingLeft?to:from),invalid=input.trim()!==''&&output===null;
  return { category, units, from, to, editingLeft, input, output, invalid, inputUnit: (editingLeft ? from : to).symbol, outputUnit: (editingLeft ? to : from).symbol };
}

function converter(s,set) {
  const { category, units, from, to, editingLeft, input, output, invalid } = conversionSummary(s);
  const icons={Length:'straighten',Mass:'weight',Temperature:'thermostat',Volume:'water_drop',Data:'storage'};
  const name=u=>u.id.replace(/([a-z])([A-Z])/g,'$1 $2').replace('Us Gallon','US gallon');
  return html`<div class="measure-body converter-body">
    <div class="measure-categories"><md-chip-set aria-label="Unit category">${Object.keys(icons).map(c=>html`<md-filter-chip label=${c} ?selected=${category===c} @click=${()=>{const us=UNITS.filter(u=>u.category===c);set?.({converterCategory:c,converterFrom:us[0].id,converterTo:us[1].id,converterInput:'1',converterEditingLeft:true});}}><md-icon slot="icon" class="sc-rounded">${icons[c]}</md-icon></md-filter-chip>`)}</md-chip-set></div>
    <div class="converter-pair">${[[true,'From',from],[false,'To',to]].map(([left,label,unit],index)=>html`
      ${index?html`<md-filled-tonal-icon-button class="converter-swap" aria-label="Swap units" @click=${()=>set?.({converterFrom:to.id,converterTo:from.id,converterEditingLeft:!editingLeft})}><md-icon>swap_vert</md-icon></md-filled-tonal-icon-button>`:nothing}
      <div class="measure-conversion-row converter-card ${left?'':'converter-card--result'}">
        <span class="converter-unit-name">${name(unit)}</span>
        <div class="measure-unit-picker"><md-filled-tonal-button aria-label="${label} unit" @click=${e=>{const menu=e.currentTarget.nextElementSibling;menu.anchorElement=e.currentTarget;menu.show();}}>${unit.symbol}<md-icon slot="icon">expand_more</md-icon></md-filled-tonal-button><md-menu positioning="absolute">${units.map(u=>html`<md-menu-item @click=${()=>set?.({[left?'converterFrom':'converterTo']:u.id})}><div slot="headline">${u.symbol}</div><div slot="supporting-text">${name(u)}</div></md-menu-item>`)}</md-menu></div>
        ${field(label,left===editingLeft?input:output??'',v=>set?.({converterInput:v,converterEditingLeft:left}),invalid&&left===editingLeft)}
      </div>`)}</div>
    ${invalid?html`<p class="chance-error" role="alert">Enter a valid value.</p>`:html`<p class="converter-hint"><md-icon aria-hidden="true">edit</md-icon>Edit either value to convert back.</p>`}
    ${category==='Data'?html`<p>Data units use decimal multiples (1 kB = 1000 B).</p>`:nothing}
  </div>`;
}
const rounds=new WeakMap();
function stopRound(root) {const run=rounds.get(root);if(run){clearTimeout(run.timer);run.observer.disconnect();rounds.delete(root);}}
function startReaction(root,set) {
  stopRound(root);
  set?.({reactionPhase:'waiting',reactionAutoStart:false});
  const run={signal:0};
  run.observer=new MutationObserver(()=>{if(!root.isConnected)stopRound(root);});
  run.observer.observe(root.ownerDocument,{subtree:true,childList:true});
  run.timer=setTimeout(()=>{if(root.isConnected){run.signal=performance.now();set?.({reactionPhase:'signal'});}else stopRound(root);},1500+Math.random()*3500);
  rounds.set(root,run);
}
function reaction(s,set) {
  const phase=s.reactionPhase??'idle',last=Number(s.reactionLast??0),history=s.reactionHistory??[],running=['waiting','signal'].includes(phase);
  const start=e=>startReaction(e.currentTarget.closest('.measure-reaction'),set);
  const tap=e=>{const root=e.currentTarget.closest('.measure-reaction');if(phase==='waiting'){stopRound(root);set?.({reactionPhase:'early'});}else if(phase==='signal'){const run=rounds.get(root);if(!run?.signal)return;const result=Math.max(0,Math.round(performance.now()-run.signal)),h=[...history,result].slice(-5);stopRound(root);set?.({reactionPhase:'result',reactionLast:result,reactionBest:s.reactionBest?Math.min(s.reactionBest,result):result,reactionAverage:Math.floor(h.reduce((a,b)=>a+b,0)/h.length),reactionHistory:h,reactionRound:Math.min(5,(s.reactionRound??0)+1)});}};
  const label={idle:'Tap Start when ready',waiting:'Wait for green.',signal:'TAP NOW!',early:'Too early!',result:last+' ms'}[phase];
  return html`<div class="measure-body measure-reaction" ${ref(el=>{if(el&&s.reactionAutoStart&&set)requestAnimationFrame(()=>{if(el.isConnected&&!rounds.has(el))startReaction(el,set);});})}><div class="measure-reaction__pad measure-reaction__pad--${phase}" role="button" aria-disabled=${!running} tabindex=${running?0:-1} aria-live="polite" @click=${tap} @keydown=${e=>{if(['Enter',' '].includes(e.key)){e.preventDefault();tap(e);}}}><strong>${label}</strong>${phase==='result'?html`<md-assist-chip label=${last<200?'Lightning ⚡':last<=250?'Fast 🚀':last<=350?'Average 👍':'Relaxed ☕'}></md-assist-chip>`:nothing}</div><div class="measure-actions"><md-filled-button ?disabled=${running} @click=${start}><md-icon slot="icon">play_arrow</md-icon>${phase==='idle'?'Start':running?'In progress…':'Try again'}</md-filled-button>${history.length?html`<md-outlined-button @click=${e=>{stopRound(e.currentTarget.closest('.measure-reaction'));set?.({reactionPhase:'idle',reactionHistory:[],reactionBest:0,reactionAverage:0,reactionRound:0});}}><md-icon slot="icon">refresh</md-icon>Reset</md-outlined-button>`:nothing}</div>${s.reactionBest||history.length?html`<div class="measure-reaction__history"><div class="measure-summary"><b>Best: ${s.reactionBest} ms</b><span>Average: ${s.reactionAverage} ms</span></div>${history.length?html`<p>Round ${s.reactionRound??history.length} of 5</p><div class="measure-history-chips">${history.map(time=>html`<md-assist-chip label="${time} ms"></md-assist-chip>`)}</div>`:nothing}</div>`:nothing}</div>`;
}

export function protractorDialog(s,set,close) {
  const win=windowOf(s.window),width=s.protractorWidth??win.width,height=s.protractorHeight??win.height;
  const angle=Math.max(0,Math.min(180,Number(s.protractorAngle??45))),cy=Math.max(140,(height-204)/2),r=Math.max(60,Math.min(width-24,cy-96));
  const point=(d,dist)=>[Math.sin(d*Math.PI/180)*dist,cy-Math.cos(d*Math.PI/180)*dist];
  const [x,y]=point(angle,r);
  const move=e=>{if(e.type==='pointermove'&&!e.buttons)return;const rect=e.currentTarget.getBoundingClientRect(),px=Math.max(0,(e.clientX-rect.left)*width/rect.width),py=(e.clientY-rect.top)*height/rect.height;set?.({protractorAngle:Math.round(Math.atan2(px,cy-py)*180/Math.PI)});};
  return html`<div class="measure-protractor-dialog">
    <svg class="measure-protractor" ${ref(el=>{if(el&&set)requestAnimationFrame(()=>{if(el.isConnected&&(Math.abs(el.clientWidth-width)>1||Math.abs(el.clientHeight-height)>1))set({protractorWidth:el.clientWidth,protractorHeight:el.clientHeight});});})} viewBox="0 0 ${width} ${height}" aria-label="Protractor, ${angle} degrees" @pointerdown=${e=>{e.currentTarget.setPointerCapture(e.pointerId);move(e);}} @pointermove=${move}>
      <path d="M0 ${cy-r} A${r} ${r} 0 0 1 0 ${cy+r} Z" fill="var(--md-sys-color-surface-container-low)" stroke="var(--md-sys-color-outline-variant)"/>
      <path d="M0 ${cy}V${cy-r} A${r} ${r} 0 0 1 ${x} ${y} Z" fill="var(--md-sys-color-primary-container)"/>
      ${Array.from({length:181},(_,d)=>{const [x1,y1]=point(d,r),[x2,y2]=point(d,r-(d%10===0?20:d%5===0?12:6)),[tx,ty]=point(d,r-38);return svg`<line x1=${x1} y1=${y1} x2=${x2} y2=${y2} stroke="currentColor" opacity=${d%5?'.4':'1'}/>${d%10?nothing:svg`<text x=${Math.max(12,tx)} y=${ty} dominant-baseline="middle" text-anchor="middle" fill="currentColor">${d}</text>`}`;})}
      <path d="M0 ${cy-r}V${cy}L${x} ${y}" fill="none" stroke="var(--md-sys-color-primary)" stroke-width="4"/><circle cx="0" cy=${cy} r="9" fill="var(--md-sys-color-primary)"/><circle cx=${x} cy=${y} r="8" fill="var(--md-sys-color-primary)" stroke="var(--md-sys-color-surface)" stroke-width="3"/>
    </svg>
    <header><md-icon class="sc-rounded" aria-hidden="true">architecture</md-icon><h3>Protractor</h3><md-icon-button aria-label="Close" @click=${close}><md-icon>close</md-icon></md-icon-button></header>
    <div class="measure-protractor-controls measure-body"><div class="protractor-readout"><div><span class="protractor-eyebrow">ANGLE</span><div class="measure-display" aria-live="polite">${angle}°</div></div><span class="protractor-kind">${angle===0?"Zero":angle<90?"Acute":angle===90?"Right angle":angle<180?"Obtuse":"Straight"}</span></div><p>Align the edge, then drag the arm.</p>${slider({value:angle,min:0,max:180,step:1,label:'Protractor angle',onInput:set&&(v=>set({protractorAngle:v}))})}<div class="protractor-presets">${[0,45,90,180].map(value=>html`<md-text-button aria-label="Set angle to ${value} degrees" @click=${()=>set?.({protractorAngle:value})}>${value}°</md-text-button>`)}</div></div>
  </div>`;
}
