const bill={critical:{title:".tk-sheet-title-row",amount:".bill-input-row",tip:".bill-tip-toggle + .bill-tip-expansion",people:".measure-people",result:".measure-bill",share:".measure-bill > strong",rounding:".measure-bill > p"}};
export const UI_SELECTORS:Record<string,{critical:Record<string,string>;secondary?:Record<string,string>}>={
 dinner:bill,tip:bill,friends:bill,currency:bill,
 toolkit:{critical:{bill:".numbers-card[data-tool=bill_splitter]",converter:".numbers-card[data-tool=unit_converter]",counter:".numbers-card[data-tool=counter]"}}
};
