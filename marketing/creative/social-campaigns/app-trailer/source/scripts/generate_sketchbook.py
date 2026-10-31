import os, sys, json
from PIL import Image, ImageDraw, ImageFont

# Path definitions
ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))
SOURCE = os.path.join(ROOT, 'source')
OUTPUT = os.path.join(ROOT, 'output', 'images')
RAW_FRAMES = os.path.join(OUTPUT, 'sketchbook-raw-frames')
FONT_PATH = os.path.join(SOURCE, 'public', 'fonts', 'GoogleSansFlex.ttf')

# Color palette
CREAM = (255, 245, 222)       # #FFF5DE
INK = (59, 45, 20)            # #3B2D14
INK_MUTED = (115, 95, 60)     # #735F3C
CARD_BG = (255, 255, 255)     # #FFFFFF
CARD_BORDER = (229, 213, 181) # #E5D5B5
CARD_SHADOW = (59, 45, 20, 25)

ACT_COLORS = [
    (230, 168, 35),   # Act 1: Gold #E6A823
    (42, 140, 130),   # Act 2: Teal #2A8C82
    (46, 125, 50),    # Act 3: Green #2E7D32
    (194, 130, 0),    # Act 4: Amber #C28200
    (176, 48, 96),    # Act 5: Rose #B03060
    (242, 201, 76),   # Act 6: Gold Boom #F2C94C
]

# 24 Keyframes metadata across 6 Acts
ACTS = [
    {
        "num": 1,
        "title": "ACT I: THE AWAKENING & CHEST OPENING",
        "time": "0.00s – 3.32s",
        "theme_color": ACT_COLORS[0],
        "summary": "Three geometric accents spring into the frame on opening motif notes. The central App Toolkit toolbox mark arrives, anticipates, and swings its lid 128° about the right hinge to become the launch base.",
        "frames": [
            {
                "time": 0.80,
                "file": "frame_01_0.80s.png",
                "label": "T + 0.80s · F72",
                "title": "Motif Accents",
                "desc": "Geometric shapes spring in on musical notes, framing an empty focal centre.",
                "tech": "pop(t, T.motif) · Corner transforms"
            },
            {
                "time": 1.85,
                "file": "frame_02_1.85s.png",
                "label": "T + 1.85s · F167",
                "title": "Mark Pop & Settle",
                "desc": "The central App Toolkit icon springs into view with drop shadow.",
                "tech": "pop(t, T.mark, 0.55) · Scale 0.2 -> 1.0"
            },
            {
                "time": 2.48,
                "file": "frame_03_2.48s.png",
                "label": "T + 2.48s · F223",
                "title": "Chest Lid Swing",
                "desc": "Intact lid rotates 128° around right lip like a toolbox chest, handle attached.",
                "tech": "rotate(open * 128deg) · Origin: 89% 44%"
            },
            {
                "time": 3.10,
                "file": "frame_04_3.10s.png",
                "label": "T + 3.10s · F279",
                "title": "Launcher Position",
                "desc": "Toolbox body settles into the lower quadrant as the launchpad for tools.",
                "tech": "lerp(540, 870, low) · Recoil ready"
            }
        ]
    },
    {
        "num": 2,
        "title": "ACT II: THE FLIGHT & BENTO ASSEMBLY",
        "time": "3.32s – 10.08s",
        "theme_color": ACT_COLORS[1],
        "summary": "Four live widgets erupt from the toolbox mouth into parabolic flight. As the music gathers, they assemble into a 2x2 Bento grid whose inner corners ease to 8px against a mint/teal ground wipe.",
        "frames": [
            {
                "time": 3.80,
                "file": "frame_05_3.80s.png",
                "label": "T + 3.80s · F342",
                "title": "Compass Ascent",
                "desc": "Compass launches from the toolbox mouth on high parabolic trajectory.",
                "tech": "Flight arc · Recoil kickback on mark"
            },
            {
                "time": 4.80,
                "file": "frame_06_4.80s.png",
                "label": "T + 4.80s · F432",
                "title": "Quad Flight Path",
                "desc": "Bubble Level and Flashlight join Compass in flight with dynamic camera tracking.",
                "tech": "Multi-lane flight · Elevation level 3"
            },
            {
                "time": 6.00,
                "file": "frame_07_6.00s.png",
                "label": "T + 6.00s · F540",
                "title": "2x2 Bento Lock",
                "desc": "Four cards lock into a unified bento grid; inner seam radii ease to 8px.",
                "tech": "BENTO_RADII easing · Gap shrinks to 4px"
            },
            {
                "time": 8.00,
                "file": "frame_08_8.00s.png",
                "label": "T + 8.00s · F720",
                "title": "'One place.' Arrives",
                "desc": "Teal ground wipe expands as bold kinetic typography introduces the product.",
                "tech": "Wipe(TEAL) · Words('One place.')"
            }
        ]
    },
    {
        "num": 3,
        "title": "ACT III: BENTO TO PHONE & QUICK TOOLS",
        "time": "10.08s – 17.14s",
        "theme_color": ACT_COLORS[2],
        "summary": "The Bento grid seamlessly transitions into the Pixel 9 Pro phone frame. Each live tool lifts out of the display on its musical note, oscillating and interacting while its feature title lands.",
        "frames": [
            {
                "time": 10.60,
                "file": "frame_09_10.60s.png",
                "label": "T + 10.60s · F954",
                "title": "Phone Shell Forms",
                "desc": "Pixel 9 Pro device frame and native screen assemble smoothly around the bento.",
                "tech": "Phone shell reveal · LiveScreen bridge"
            },
            {
                "time": 11.80,
                "file": "frame_10_11.80s.png",
                "label": "T + 11.80s · F1062",
                "title": "Compass Lift",
                "desc": "'Quick tools.' title appears; Compass lifts forward with live oscillating needle.",
                "tech": "Compass lift · Scale 1.7x · Angle -4°"
            },
            {
                "time": 13.60,
                "file": "frame_11_13.60s.png",
                "label": "T + 13.60s · F1224",
                "title": "Flash Dimmer Lift",
                "desc": "Flashlight widget lifts into hero framing with live brightness slider filling.",
                "tech": "flashLevel 1-5 live · Elevation shadow"
            },
            {
                "time": 14.80,
                "file": "frame_12_14.80s.png",
                "label": "T + 14.80s · F1332",
                "title": "Morse & SOS Beacon",
                "desc": "SOS emergency widget lifts with authentic high-contrast strobe pattern.",
                "tech": "sosActive pulse · Synchronized to beat"
            }
        ]
    },
    {
        "num": 4,
        "title": "ACT IV: MEASURE SUITE - PRECISION HEROES",
        "time": "17.14s – 21.98s",
        "theme_color": ACT_COLORS[3],
        "summary": "A gold floral ground wipe introduces the Measure tab. Four precision tools push across the left column—Protractor, Unit Converter, Bill Splitter, and Counter—updating live on each bass onset.",
        "frames": [
            {
                "time": 17.30,
                "file": "frame_13_17.30s.png",
                "label": "T + 17.30s · F1557",
                "title": "Measure Tab Push",
                "desc": "Camera dives into the Measure tab as gold floral ground wipe sweeps the canvas.",
                "tech": "Wipe(GOLD) · Words('Measure.')"
            },
            {
                "time": 18.20,
                "file": "frame_14_18.20s.png",
                "label": "T + 18.20s · F1638",
                "title": "'Angles.' Protractor",
                "desc": "Protractor hero showcases dynamic angle measurement from 20° to 135°.",
                "tech": "Live Protractor · protractorAngle lerp"
            },
            {
                "time": 19.80,
                "file": "frame_15_19.80s.png",
                "label": "T + 19.80s · F1782",
                "title": "'Split.' Bill Splitter",
                "desc": "Bill Splitter hero computes per-person totals with live tip selector controls.",
                "tech": "Live Bill Splitter · splitPeople toggle"
            },
            {
                "time": 21.00,
                "file": "frame_16_21.00s.png",
                "label": "T + 21.00s · F1890",
                "title": "'Counts.' Counter",
                "desc": "Landscape Counter hero increments upward on each rhythmic audio onset.",
                "tech": "Live Counter · counterValue increment"
            }
        ]
    },
    {
        "num": 5,
        "title": "ACT V: PLAY & DECIDE - THE FORTUNE WHEEL",
        "time": "21.98s – 26.26s",
        "theme_color": ACT_COLORS[4],
        "summary": "A soft-burst pink ground wipe signals Play & Decide. The Decision Wheel expands to full height, spinning 1,260° to select 'Sushi', followed by a cascade of Random Number, Dice, and Coin Flip.",
        "frames": [
            {
                "time": 22.40,
                "file": "frame_17_22.40s.png",
                "label": "T + 22.40s · F2016",
                "title": "'Let chance decide.'",
                "desc": "Playful chapter heading enters as soft pink burst wipes the background.",
                "tech": "Wipe(PINK) · Words('Let chance decide.')"
            },
            {
                "time": 23.60,
                "file": "frame_18_23.60s.png",
                "label": "T + 23.60s · F2124",
                "title": "Decision Wheel Spin",
                "desc": "Wheel scales up to 1.75x and spins rapidly across 1,260 degrees of rotation.",
                "tech": "wheelRotation 1260° · Scale 1.75x"
            },
            {
                "time": 24.60,
                "file": "frame_19_24.60s.png",
                "label": "T + 24.60s · F2214",
                "title": "Winner: 'Sushi'",
                "desc": "Wheel pointer clicks to rest on 'Sushi' with celebratory highlight.",
                "tech": "wheelWinner 'Sushi' · Audio accent"
            },
            {
                "time": 25.70,
                "file": "frame_20_25.70s.png",
                "label": "T + 25.70s · F2313",
                "title": "Chance Trio Lands",
                "desc": "Random Number (73), Dice Roll (5), and Coin Flip land in a balanced row.",
                "tech": "Triple card drop · Shared baseline y970"
            }
        ]
    },
    {
        "num": 6,
        "title": "ACT VI: GRAND FINALE - 3D FAN & GOLD BOOM",
        "time": "26.26s – 29.88s",
        "theme_color": ACT_COLORS[5],
        "summary": "Three Pixel 9 Pro devices fan out in dramatic 3D perspective beside the app identity and Google Play badge. A final crescendo anticipation triggers an explosive golden supernova.",
        "frames": [
            {
                "time": 26.90,
                "file": "frame_21_26.90s.png",
                "label": "T + 26.90s · F2421",
                "title": "3D Device Fan",
                "desc": "Three devices fan out with depth perspective (-7°, 0°, +7°) showing all tabs.",
                "tech": "Phone fan · pop(t, T.fan) · 3D depth"
            },
            {
                "time": 28.20,
                "file": "frame_22_28.20s.png",
                "label": "T + 28.20s · F2538",
                "title": "Identity & Play Store",
                "desc": "Toolbox mark, 'App Toolkit. Your toolkit.' and Google Play badge hold.",
                "tech": "Identity hold · 27.90s - 29.40s"
            },
            {
                "time": 29.45,
                "file": "frame_23_29.45s.png",
                "label": "T + 29.45s · F2650",
                "title": "Climax Anticipation",
                "desc": "Mark contracts slightly before the crescendo explosion.",
                "tech": "pop(t, T.boom) · Squash anticipation"
            },
            {
                "time": 29.80,
                "file": "frame_24_29.80s.png",
                "label": "T + 29.80s · F2682",
                "title": "Golden Supernova",
                "desc": "Full-frame kit-gold burst sweeps the canvas; final toolbox mark lands at center.",
                "tech": "Wipe(BOOM) · Soft burst 16x scale"
            }
        ]
    }
]

print("Loaded ACTS specification.")

def create_rounded_mask(size, radius):
    mask = Image.new('L', size, 0)
    draw = ImageDraw.Draw(mask)
    draw.rounded_rectangle((0, 0, size[0], size[1]), radius=radius, fill=255)
    return mask

def wrap_text(text, font, max_width, draw):
    words = text.split()
    lines = []
    current_line = []
    for word in words:
        test_line = ' '.join(current_line + [word])
        bbox = draw.textbbox((0, 0), test_line, font=font)
        if bbox[2] - bbox[0] <= max_width:
            current_line.append(word)
        else:
            if current_line:
                lines.append(' '.join(current_line))
                current_line = [word]
            else:
                lines.append(word)
    if current_line:
        lines.append(' '.join(current_line))
    return lines

def render_act_sheet(act):
    # Sheet dimensions: 1920 x 780
    W, H = 1920, 780
    sheet = Image.new('RGB', (W, H), CREAM)
    draw = ImageDraw.Draw(sheet)
    
    font_act_badge = ImageFont.truetype(FONT_PATH, 16)
    font_act_title = ImageFont.truetype(FONT_PATH, 26)
    font_act_time = ImageFont.truetype(FONT_PATH, 18)
    font_act_desc = ImageFont.truetype(FONT_PATH, 15)
    
    font_card_badge = ImageFont.truetype(FONT_PATH, 13)
    font_card_title = ImageFont.truetype(FONT_PATH, 20)
    font_card_desc = ImageFont.truetype(FONT_PATH, 14)
    font_card_tech = ImageFont.truetype(FONT_PATH, 12)
    
    # Header
    header_y = 36
    # Act Pill
    act_badge_text = f"ACT {act['num']}"
    badge_bbox = draw.textbbox((0, 0), act_badge_text, font=font_act_badge)
    badge_w = badge_bbox[2] - badge_bbox[0] + 24
    badge_h = 30
    draw.rounded_rectangle((48, header_y, 48 + badge_w, header_y + badge_h), radius=15, fill=act['theme_color'])
    draw.text((48 + 12, header_y + 6), act_badge_text, font=font_act_badge, fill=(255, 255, 255))
    
    # Act Title
    title_x = 48 + badge_w + 16
    draw.text((title_x, header_y + 1), act['title'].replace(f"ACT {act['num']}: ", ""), font=font_act_title, fill=INK)
    
    # Time Badge on the right
    time_text = f"⏱ {act['time']}"
    time_bbox = draw.textbbox((0, 0), time_text, font=font_act_time)
    time_w = time_bbox[2] - time_bbox[0] + 24
    time_x = W - 48 - time_w
    draw.rounded_rectangle((time_x, header_y, time_x + time_w, header_y + 30), radius=15, fill=(245, 235, 210), outline=CARD_BORDER, width=1)
    draw.text((time_x + 12, header_y + 5), time_text, font=font_act_time, fill=INK)
    
    # Summary
    summary_lines = wrap_text(act['summary'], font_act_desc, W - 96, draw)
    for idx, line in enumerate(summary_lines[:2]):
        draw.text((48, header_y + 44 + idx * 20), line, font=font_act_desc, fill=INK_MUTED)
        
    # Cards layout
    card_w = 432
    card_h = 580
    card_gap = 32
    start_x = 48
    start_y = 145
    
    thumb_w = 404
    thumb_h = 227
    thumb_mask = create_rounded_mask((thumb_w, thumb_h), 10)
    
    for i, frame in enumerate(act['frames']):
        cx = start_x + i * (card_w + card_gap)
        cy = start_y
        
        # Draw Card Background & Border
        draw.rounded_rectangle((cx, cy, cx + card_w, cy + card_h), radius=18, fill=CARD_BG, outline=CARD_BORDER, width=1)
        
        # Load & Paste Thumbnail
        raw_img_path = os.path.join(RAW_FRAMES, frame['file'])
        if os.path.exists(raw_img_path):
            img = Image.open(raw_img_path).convert('RGB')
            img_thumb = img.resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
            sheet.paste(img_thumb, (cx + 14, cy + 14), thumb_mask)
            
            # Subtle thumbnail border
            draw.rounded_rectangle((cx + 14, cy + 14, cx + 14 + thumb_w, cy + 14 + thumb_h), radius=10, outline=(220, 210, 190), width=1)
        
        # Frame Time Pill
        time_pill = frame['label']
        pill_bbox = draw.textbbox((0, 0), time_pill, font=font_card_badge)
        pw = pill_bbox[2] - pill_bbox[0] + 18
        ph = 24
        py = cy + 14 + thumb_h + 16
        
        # Soft theme-tinted background for time pill
        tint_bg = tuple(int(c * 0.15 + 255 * 0.85) for c in act['theme_color'])
        draw.rounded_rectangle((cx + 14, py, cx + 14 + pw, py + ph), radius=12, fill=tint_bg)
        draw.text((cx + 14 + 9, py + 4), time_pill, font=font_card_badge, fill=act['theme_color'])
        
        # Frame Title
        draw.text((cx + 14, py + 34), frame['title'], font=font_card_title, fill=INK)
        
        # Frame Action Description (wrapped)
        desc_lines = wrap_text(frame['desc'], font_card_desc, card_w - 28, draw)
        for d_idx, d_line in enumerate(desc_lines[:3]):
            draw.text((cx + 14, py + 68 + d_idx * 20), d_line, font=font_card_desc, fill=INK_MUTED)
            
        # Divider line
        draw.line((cx + 14, cy + card_h - 60, cx + card_w - 14, cy + card_h - 60), fill=(240, 230, 210), width=1)
        
        # Technical detail tag
        draw.text((cx + 14, cy + card_h - 44), f"⚙ {frame['tech']}", font=font_card_tech, fill=(145, 125, 90))
        
    out_name = f"sketchbook-act{act['num']}-{act['title'].split(':')[1].strip().lower().replace(' ', '-').replace('&', 'and')}.png"
    out_path = os.path.join(OUTPUT, out_name)
    sheet.save(out_path)
    print(f"Saved: {out_name}")
    return out_name, sheet

# Render all 6 act sheets
rendered_sheets = []
for act in ACTS:
    name, sheet_img = render_act_sheet(act)
    rendered_sheets.append((name, sheet_img))


def render_master_storyboard(acts_subset, filename, title_suffix=""):
    # Header: 220px
    # Each act: 90px header + 540px cards + 50px gap = 680px
    header_h = 220
    act_h = 670
    W = 1920
    H = header_h + len(acts_subset) * act_h + 80
    
    sheet = Image.new('RGB', (W, H), CREAM)
    draw = ImageDraw.Draw(sheet)
    
    font_main_title = ImageFont.truetype(FONT_PATH, 38)
    font_sub_title = ImageFont.truetype(FONT_PATH, 18)
    font_meta_badge = ImageFont.truetype(FONT_PATH, 13)
    
    font_act_badge = ImageFont.truetype(FONT_PATH, 16)
    font_act_title = ImageFont.truetype(FONT_PATH, 24)
    font_act_time = ImageFont.truetype(FONT_PATH, 17)
    font_act_desc = ImageFont.truetype(FONT_PATH, 14)
    
    font_card_badge = ImageFont.truetype(FONT_PATH, 12)
    font_card_title = ImageFont.truetype(FONT_PATH, 19)
    font_card_desc = ImageFont.truetype(FONT_PATH, 13)
    font_card_tech = ImageFont.truetype(FONT_PATH, 11)
    
    # 1. Main Header
    draw.rounded_rectangle((48, 36, W - 48, 184), radius=20, fill=CARD_BG, outline=CARD_BORDER, width=1)
    
    # Header Title
    full_title = f"APP TOOLKIT  ·  TRAILER STORYBOARD {title_suffix}".strip()
    draw.text((80, 56), full_title, font=font_main_title, fill=INK)
    draw.text((80, 108), "Complete Keyframe Narrative & Gesture Choreography · Composition: AppTrailerV2 · 16:9 90fps · 29.88s", font=font_sub_title, fill=INK_MUTED)
    
    # Meta Pills in Header
    pills = [
        ("⏱ Total Duration: 29.88s", (245, 235, 210)),
        ("⚡ 94 Beat Onsets Tracked", (230, 245, 230)),
        ("📱 Pixel 9 Pro 3D Fan", (230, 240, 250)),
        ("🎨 Kit Palette System", (255, 235, 240))
    ]
    px = 80
    for ptext, pcol in pills:
        pbbox = draw.textbbox((0, 0), ptext, font=font_meta_badge)
        pw = pbbox[2] - pbbox[0] + 20
        draw.rounded_rectangle((px, 140, px + pw, 166), radius=13, fill=pcol, outline=CARD_BORDER, width=1)
        draw.text((px + 10, 145), ptext, font=font_meta_badge, fill=INK)
        px += pw + 14
        
    # 2. Render Each Act
    thumb_w = 404
    thumb_h = 227
    thumb_mask = create_rounded_mask((thumb_w, thumb_h), 10)
    card_w = 432
    card_h = 530
    card_gap = 32
    start_x = 48
    
    current_y = header_h + 30
    
    for act in acts_subset:
        # Act Header Banner
        act_y = current_y
        
        # Act Pill
        act_badge_text = f"ACT {act['num']}"
        badge_bbox = draw.textbbox((0, 0), act_badge_text, font=font_act_badge)
        badge_w = badge_bbox[2] - badge_bbox[0] + 24
        draw.rounded_rectangle((48, act_y, 48 + badge_w, act_y + 30), radius=15, fill=act['theme_color'])
        draw.text((48 + 12, act_y + 6), act_badge_text, font=font_act_badge, fill=(255, 255, 255))
        
        # Act Title
        clean_title = act['title'].replace(f"ACT {act['num']}: ", "")
        draw.text((48 + badge_w + 16, act_y + 2), clean_title, font=font_act_title, fill=INK)
        
        # Time Badge
        time_text = f"⏱ {act['time']}"
        time_bbox = draw.textbbox((0, 0), time_text, font=font_act_time)
        time_w = time_bbox[2] - time_bbox[0] + 24
        time_x = W - 48 - time_w
        draw.rounded_rectangle((time_x, act_y, time_x + time_w, act_y + 28), radius=14, fill=(245, 235, 210), outline=CARD_BORDER, width=1)
        draw.text((time_x + 12, act_y + 5), time_text, font=font_act_time, fill=INK)
        
        # Summary text
        summary_lines = wrap_text(act['summary'], font_act_desc, W - 96, draw)
        for idx, line in enumerate(summary_lines[:2]):
            draw.text((48, act_y + 42 + idx * 19), line, font=font_act_desc, fill=INK_MUTED)
            
        # Cards
        cards_y = act_y + 90
        for i, frame in enumerate(act['frames']):
            cx = start_x + i * (card_w + card_gap)
            cy = cards_y
            
            # Card background
            draw.rounded_rectangle((cx, cy, cx + card_w, cy + card_h), radius=18, fill=CARD_BG, outline=CARD_BORDER, width=1)
            
            # Thumbnail
            raw_img_path = os.path.join(RAW_FRAMES, frame['file'])
            if os.path.exists(raw_img_path):
                img = Image.open(raw_img_path).convert('RGB')
                img_thumb = img.resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
                sheet.paste(img_thumb, (cx + 14, cy + 14), thumb_mask)
                draw.rounded_rectangle((cx + 14, cy + 14, cx + 14 + thumb_w, cy + 14 + thumb_h), radius=10, outline=(220, 210, 190), width=1)
            
            # Time badge
            py = cy + 14 + thumb_h + 14
            time_pill = frame['label']
            pill_bbox = draw.textbbox((0, 0), time_pill, font=font_card_badge)
            pw = pill_bbox[2] - pill_bbox[0] + 16
            ph = 22
            tint_bg = tuple(int(c * 0.15 + 255 * 0.85) for c in act['theme_color'])
            draw.rounded_rectangle((cx + 14, py, cx + 14 + pw, py + ph), radius=11, fill=tint_bg)
            draw.text((cx + 14 + 8, py + 3), time_pill, font=font_card_badge, fill=act['theme_color'])
            
            # Title
            draw.text((cx + 14, py + 30), frame['title'], font=font_card_title, fill=INK)
            
            # Desc
            desc_lines = wrap_text(frame['desc'], font_card_desc, card_w - 28, draw)
            for d_idx, d_line in enumerate(desc_lines[:3]):
                draw.text((cx + 14, py + 60 + d_idx * 18), d_line, font=font_card_desc, fill=INK_MUTED)
                
            # Divider
            draw.line((cx + 14, cy + card_h - 48, cx + card_w - 14, cy + card_h - 48), fill=(240, 230, 210), width=1)
            
            # Tech
            draw.text((cx + 14, cy + card_h - 34), f"⚙ {frame['tech']}", font=font_card_tech, fill=(145, 125, 90))
            
        current_y += act_h
        
    out_path = os.path.join(OUTPUT, filename)
    sheet.save(out_path)
    print(f"Saved Master Storyboard: {filename} ({W}x{H})")
    return out_path

# Render Complete Master Storyboard (All 6 Acts)
render_master_storyboard(ACTS, "sketchbook-trailer-v2-complete.png")

# Render Volume 1 (Acts 1-3: Launch, Bento & Quick Tools)
render_master_storyboard(ACTS[:3], "sketchbook-vol1-launch-and-tools.png", "· PART 1 (ACTS I–III)")

# Render Volume 2 (Acts 4-6: Measure, Play & Finale)
render_master_storyboard(ACTS[3:], "sketchbook-vol2-measure-and-finale.png", "· PART 2 (ACTS IV–VI)")
