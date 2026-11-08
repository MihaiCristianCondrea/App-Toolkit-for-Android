export const SCREENS:Record<string,[string,Record<string,unknown>]> = {
  "length": [
    "numbers",
    {
      "converterCategory": "Length",
      "converterFrom": "Metre",
      "converterTo": "Centimetre",
      "converterInput": "1",
      "activeSheet": "unit_converter"
    }
  ],
  "temperature": [
    "numbers",
    {
      "converterCategory": "Temperature",
      "converterFrom": "Celsius",
      "converterTo": "Fahrenheit",
      "converterInput": "20",
      "activeSheet": "unit_converter"
    }
  ],
  "mass": [
    "numbers",
    {
      "converterCategory": "Mass",
      "converterFrom": "Kilogram",
      "converterTo": "Pound",
      "converterInput": "2.26796185",
      "activeSheet": "unit_converter"
    }
  ],
  "volume": [
    "numbers",
    {
      "converterCategory": "Volume",
      "converterFrom": "Litre",
      "converterTo": "Millilitre",
      "converterInput": "1",
      "activeSheet": "unit_converter"
    }
  ],
  "data": [
    "numbers",
    {
      "converterCategory": "Data",
      "converterFrom": "Gigabyte",
      "converterTo": "Megabyte",
      "converterInput": "1",
      "activeSheet": "unit_converter"
    }
  ],
  "reverse": [
    "numbers",
    {
      "converterCategory": "Length",
      "converterFrom": "Centimetre",
      "converterTo": "Metre",
      "converterInput": "2",
      "converterEditingLeft": false,
      "activeSheet": "unit_converter"
    }
  ]
};
export const SCROLL:Record<string,number>={};
/** Expected values for the captured examples, checked against UnitConversionRepository's decimal factors. */
export const EXPECTED_VALUES:Record<string,[string,string]>={
 "Metre:Centimetre:1:left":["1","100"],
 "Celsius:Fahrenheit:20:left":["20","68"],
 "Kilogram:Pound:2.26796185:left":["2.26796185","5"],
 "Litre:Millilitre:1:left":["1","1000"],
 "Gigabyte:Megabyte:1:left":["1","1000"],
 "Centimetre:Metre:2:right":["200","2"]
};
