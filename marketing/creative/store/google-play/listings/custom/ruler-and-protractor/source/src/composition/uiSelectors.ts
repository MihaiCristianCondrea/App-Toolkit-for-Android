export type UiSpec = { critical: Record<string, string>; secondary?: Record<string, string> };
export const UI_SELECTORS: Record<string, UiSpec> = {
 ruler: {critical: {title: ".measure-ruler__bar", reading: ".measure-ruler__card", scale: ".measure-ruler__scale"}},
 protractor: {critical: {title: ".measure-protractor-dialog header", dial: ".measure-protractor", controls: ".measure-protractor-controls"}},
 advanced: {critical: {title: ".measure-ruler__bar", reading: ".measure-ruler__card", scale: ".measure-ruler__scale"}},
 converter: {critical: {title: ".tk-sheet-title-row", from: "text:span|Metre + md-outlined-text-field@0", to: "text:span|Centimetre + md-outlined-text-field@1"}},
 measure: {critical: {}},
};
