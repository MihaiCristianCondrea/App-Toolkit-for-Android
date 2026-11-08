export const UI_SELECTORS: Record<string, {critical: Record<string,string>; secondary?:Record<string,string>}> = {
 "toolkit":{critical:{ruler:".numbers-card[data-tool=ruler]",protractor:".numbers-card[data-tool=protractor]",converter:".numbers-card[data-tool=unit_converter]",bill:".numbers-card[data-tool=bill_splitter]"}},
  "length": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint"
    }
  },
  "temperature": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint"
    }
  },
  "mass": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint"
    }
  },
  "volume": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint"
    }
  },
  "data": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint",
      "decimal": ".converter-body > p:not(.converter-hint)"
    }
  },
  "reverse": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "category": ".measure-categories md-filter-chip[selected]",
      "from": ".converter-card@0",
      "to": ".converter-card@1",
      "swap": ".converter-swap",
      "hint": ".converter-hint"
    }
  }
};
