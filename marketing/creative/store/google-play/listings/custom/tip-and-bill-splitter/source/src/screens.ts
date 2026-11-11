export const SCREENS:Record<string,[string,Record<string,unknown>]> = {
 dinner:["numbers",{activeSheet:"bill_splitter",billAmount:"84.50",billCurrency:"USD",billTipEnabled:true,tipPercent:15,splitPeople:3}],
 tip:["numbers",{activeSheet:"bill_splitter",billAmount:"52.00",billCurrency:"EUR",billTipEnabled:true,tipPercent:10,splitPeople:2}],
 friends:["numbers",{activeSheet:"bill_splitter",billAmount:"126.00",billCurrency:"GBP",billTipEnabled:true,tipPercent:10,splitPeople:4}],
 currency:["numbers",{activeSheet:"bill_splitter",billAmount:"4800",billCurrency:"JPY",billTipEnabled:true,tipPercent:10,splitPeople:2}],
 toolkit:["numbers",{activeSheet:"none",billAmount:"84.50",billCurrency:"USD",tipPercent:15,splitPeople:3}],
};
export const EXPECTED_BILLS:Record<string,{tip:string;total:string;share:string;formatted:string}>={
 "84.50:15:3:USD":{tip:"1268",total:"9718",share:"3240",formatted:"$32.40"},
 "52.00:10:2:EUR":{tip:"520",total:"5720",share:"2860",formatted:"€28.60"},
 "126.00:10:4:GBP":{tip:"1260",total:"13860",share:"3465",formatted:"£34.65"},
 "4800:10:2:JPY":{tip:"480",total:"5280",share:"2640",formatted:"¥2,640"}
};
export const SCROLL:Record<string,number>={};
export const TABLET_SCREENS=["dinner","tip","friends","currency"];
export const DESKTOP_SCREENS=TABLET_SCREENS;
