from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, PngImagePlugin
import json

source=Path(__file__).resolve().parents[1]
output=source.parent/'output'
timeline=json.loads((source/'src/timeline.json').read_text())
sheet=Image.new('RGB',(1920,1160),'#FFF4D9')
draw=ImageDraw.Draw(sheet)
font=ImageFont.truetype(str(source/'public/fonts/GoogleSansFlex.ttf'),26)
for i,scene in enumerate(timeline['scenes']):
    frame=Image.open(output/f'images/scene-{i+1:02}.png').convert('RGB')
    frame.thumbnail((480,520),Image.Resampling.LANCZOS)
    x=(i%4)*480;y=(i//4)*580
    sheet.paste(frame,(x,y+35))
    draw.text((x+16,y+8),f"{i+1}. {scene['title']}",font=font,fill='#3B2D14')
meta=PngImagePlugin.PngInfo()
meta.add_itxt('Title','App Toolkit: toolbox trailer review')
meta.add_itxt('Author','Mihai-Cristian Condrea')
meta.add_itxt('Description','Eight scenes: the toolbox opens, live widgets form a bento screen, and Quick Tools, Measure and Play appear.')
sheet.save(output/'images/contact-sheet.png',pnginfo=meta)
print('Saved eight-scene contact sheet.')

# CI thumbnails allow visual review when the local render workspace is unavailable.
import os, base64, io
if os.environ.get('TRAILER_REVIEW_LOG') == '1':
    for i in range(1,9):
        thumb=Image.open(output/f'images/scene-{i:02}.png').convert('RGB')
        buffer=io.BytesIO();thumb.save(buffer,format='JPEG',quality=85)
        encoded=base64.b64encode(buffer.getvalue()).decode('ascii')
        for part,start in enumerate(range(0,len(encoded),12000)):
            print(f'TRAILER_REVIEW_FRAME:{i}:{part}:'+encoded[start:start+12000])
