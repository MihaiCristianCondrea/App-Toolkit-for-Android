export const SCREENS:Record<string,[string,Record<string,unknown>]>={
  "wheel": [
    "play",
    {
      "activeToolId": "decision_wheel",
      "wheelText": "Pizza\nPasta\nSushi\nSalad",
      "wheelRotation": 0,
      "wheelWinner": ""
    }
  ],
  "result": [
    "play",
    {
      "activeToolId": "decision_wheel",
      "wheelText": "Pizza\nPasta\nSushi\nSalad",
      "wheelRotation": 1575,
      "wheelWinner": "Sushi"
    }
  ],
  "choices": [
    "play",
    {
      "activeToolId": "decision_wheel",
      "wheelText": "Walk\nRead\nCook\nDraw",
      "wheelRotation": 0,
      "wheelWinner": ""
    }
  ],
  "number": [
    "play",
    {
      "activeToolId": "random_number",
      "randomMin": "1",
      "randomMax": "100",
      "randomResult": "37"
    }
  ],
  "coin": [
    "play",
    {
      "activeToolId": "coin_flip",
      "coinSide": "Tails"
    }
  ],
  "dice": [
    "play",
    {
      "activeToolId": "dice_roll",
      "diceResult": 5
    }
  ]
};
export const SCROLL:Record<string,number>={};
