export const UI_SELECTORS: Record<string, {critical: Record<string, string>; secondary?: Record<string, string>}> = {
  "wheel": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "input": ".wheel-body md-outlined-text-field",
      "wheel": ".wheel-stage",
      "result": ".wheel-outcome",
      "spin": ".wheel-body md-filled-button"
    }
  },
  "result": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "input": ".wheel-body md-outlined-text-field",
      "wheel": ".wheel-stage",
      "result": ".wheel-outcome",
      "spin": ".wheel-body md-filled-button"
    }
  },
  "choices": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "input": ".wheel-body md-outlined-text-field",
      "wheel": ".wheel-stage",
      "result": ".wheel-outcome",
      "spin": ".wheel-body md-filled-button"
    }
  },
  "number": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "result": ".random-picker__result",
      "controls": ".random-picker__settings",
      "button": ".random-picker__generate"
    }
  },
  "coin": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "coin": ".chance-body .chance-coin-stage",
      "result": ".chance-side",
      "button": ".chance-body md-filled-button"
    }
  },
  "dice": {
    "critical": {
      "title": ".tk-sheet-title-row",
      "dice": ".chance-body .chance-dice-stage",
      "button": ".chance-body md-filled-button"
    }
  }
};
