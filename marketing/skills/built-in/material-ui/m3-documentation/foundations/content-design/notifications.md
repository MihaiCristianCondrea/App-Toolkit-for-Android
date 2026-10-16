---
title: "Notifications"
source_url: "https://m3.material.io/foundations/content-design/notifications"
lastmod: "2025-09-26"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "foundations"
assets:
  - "assets/images/fc3c24363406-lw53k6lt-1.png"
  - "assets/images/f8defb8b824d-lw53kfhk-2.png"
  - "assets/images/b08ea6699147-lw53kk0k-3.png"
  - "assets/images/00e6771765bf-lw53kq5j-4.png"
  - "assets/images/11464b2bff49-lw53kvqx-5.png"
  - "assets/images/612db39d4adf-lw53l2ot-6.png"
  - "assets/images/4c35b04398ad-lw53l86v-7.png"
  - "assets/images/51575623c392-lw53lep9-8.png"
  - "assets/images/79d7486ea615-lw53lk9g-9.png"
  - "assets/images/1b81d8f15a2a-lw53ls3l-10.png"
  - "assets/images/60ea5a2792ac-lw53lzfr-11.png"
  - "assets/images/a51b9e71c186-lw53m528-12.png"
  - "assets/images/d68b4b65c191-lw53mcby-13.png"
  - "assets/images/c18301710107-lw53mhmr-14.png"
  - "assets/images/90f345221681-lw53mqhm-15.png"
  - "assets/images/56cc6c5c2310-lw53mx58-16.png"
  - "assets/images/139bc196aa19-lw53n553-17.png"
  - "assets/images/333e7a793394-lw53nb80-18.png"
  - "assets/images/0cf58d7c294a-lw53nkf2-19.png"
  - "assets/images/1d9da28afd68-lw53ntem-20.png"
  - "assets/images/d18f55a72f5c-lw53nz6w-21.png"
  - "assets/images/0d8d84dd5cd3-lw53o6jr-22.png"
---

# Notifications

Notifications help people see information they need or want at the right time

Notifications should:

-   Be about the user, not the product
-   Be precise, timely, actionable, contextual, and relevant
-   Give users easy controls to opt out
-   Not be used to send unsolicited ads

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53k6lt-1.png?alt=media&token=f0fb34d7-3f8a-4111-8b7f-89033fb27a28>)

### Put important information at the top

Put the most important info at the beginning and make it easy to understand. People skim rather than read, often in an F shape. When it makes sense, try to move the most critical information to the front of sentences, rather than the end, where it may get overlooked or truncated.

![Three notifications from Google apps that include the point of the notification in the header.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53kfhk-2.png?alt=media&token=9c5b4033-2c38-4e18-85aa-5390cb29b785>)

_Prioritize the most important information_

![Three notifications from Google apps that include app names repeated in the header, instead of important information.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53kk0k-3.png?alt=media&token=90f1d8b9-c6cc-49ed-997b-6453a0644af4>)

_Don't waste characters on app names, niceties, or unimportant information_

### Tell users what they can do

If you’re prompting someone to take action, make that clear. CTAs should be concise, specific, and actionable. If you know what motivates people to take action, add it.

![Two notifications from Google maps that include actionable information in the header.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53kq5j-4.png?alt=media&token=6137b006-1c0b-4cf2-94fc-128d12daa42f>)

_Clearly guide the user to their next action /_

![Two notifications from Google apps with non-actionable headers.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53kvqx-5.png?alt=media&token=9b0ad64e-6f95-44de-8f5e-f12cb2ba2f58>)

_Don't bury next steps_

### Make it relevant and personal

A notification shouldn’t be sent to everyone. The more you broaden the audience and the message for a notification, the more you risk being irrelevant. Consider user segments, in-app behavior, and personalized information to make sure notifications go to people who will benefit most from them.

![A Google maps notification in Memphis, TN that’s personalized to the user’s location downtown.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53l2ot-6.png?alt=media&token=4f5c0fd2-4f29-4a75-a5c6-0dafff49b473>)

_Be specific to capture user attention_

![A Google maps notification in Memphis, TN that’s generic to the city, rather than the area.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53l86v-7.png?alt=media&token=1da7ac0b-893b-4cbf-a2b7-0dac9c458820>)

_Don’t be so generic as to lose value_

### Avoid dynamic text

Dynamic text are words that are implemented by engineering to change based on the user or context, like adding a user’s campaign name to headline or “good afternoon” when users log in at certain times.

Try not to use dynamic text in notifications. It often breaks character limits, especially in headlines when translated. Text that gets truncated in the headline will not expand, even in expandable notifications. If you must use dynamic text in the headline, try to pair it with no more than one additional word. Create a backup notification that fits the character count when your primary notification won’t.

![Two notifications, one with dynamic text in the body, and the other with dynamic text in the header that’s formatted to always be short.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53lep9-8.png?alt=media&token=74837af0-9c5a-4440-ae6a-1b31f806484a>)

_Place dynamic text in the notification body, where there is more space. If you place dynamic text in the title, keep it very short._

![Two notifications with dynamic text in the header that’s too long and truncates.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53lk9g-9.png?alt=media&token=86ba3704-96ca-4aeb-9394-20ef722483e1>)

_Don’t place dynamic text in a long notification title_

### Mind your characters

Stay within these suggested character counts so text doesn’t get cut off:

-   Title: <29 chars
-   Collapsed body: <40 chars
-   Expanded  body: <80 chars; start with collapsed body and add to it
-   Button: 1-2 buttons (1-2 words each)


There’s more room for text on later versions of Android, but these limits are still recommended to prevent truncation on smaller devices.

![A short, relevant, and informative notification.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53ls3l-10.png?alt=media&token=0975f21e-7522-40bb-acfb-28d8405bac9e>)

_Keep notifications short and information-dense_

### SMS messages

SMS helps users get messages when they might not have access to their Google Account. It’s used for important or urgent communication only. 

SMS breaks into multiple messages after a certain number of characters, resulting in increased costs. To avoid this, stick to the following character limits:

-   Latin languages:  <160 characters
-   Non-latin languages: <134 characters

If translation is needed, let translators know about the character limits in the message description.

![An SMS notification that’s brief and truncates sensitive information.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53lzfr-11.png?alt=media&token=ea00548e-29b5-471d-8ac2-e47206c29e54>)

_SMS messages should be brief and important_

![An SMS notification that’s too long with too many links.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53m528-12.png?alt=media&token=2be76632-4674-4858-b601-29bd9d4820df>)

_Don’t use full emails or website links for text length_

### Emoji with caution

Use emoji sparingly. Many don’t translate and aren’t universally understood across cultures. Experiments show that face and hand gesture emoji perform better than generic emoji because they tell a better story. Emoji should enhance content, not replace it.

Since it’s not clear to users whether emoji mean we’re sympathizing with them or attempting to project our feelings onto them, don’t use emoji to accentuate bad news. In experiments, there was a strong negative reaction to negative emoji, such as frowning face, anguished face, and weary face.

Gen Z adds another cultural nuance to emoji by [inventing new meanings](https://www.textnow.com/blog/the-next-generation-of-emojis-gen-z-explained/) that go beyond the official or literal [definitions](https://emojipedia.org/).

![A notification about coffee with a coffee emoji at the end of the header.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53mcby-13.png?alt=media&token=22d82f3d-5533-472a-96cc-6407c99ca119>)

_Use emoji only to enhance a message_

![A notification about high traffic volume on a route to work that uses upset emoji. A notification about coffee that substitutes a coffee emoji for the word “coffee.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53mhmr-14.png?alt=media&token=faa05f8f-9a9c-4b5f-bb79-eed427812ccb>)

_Don’t add negative emotions to a message. Don’t replace words with emoji._

### Don’t overdo delight

What seems funny or cute may not come across that way. When vying for limited user attention, be useful. Don’t focus on delight: delicately added and tested polish can better support Google’s brand.

![A notification that’s short and effective](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53mqhm-15.png?alt=media&token=3e236ec8-a5a2-4629-8241-1a3afd37c40d>)

_Prioritize straightforward and useful messages /_

![A notification that’s meant to be funny, but comes across as creepy where the application calls the user “human.”](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53mx58-16.png?alt=media&token=0b860837-dfa8-44ef-9ee6-bf98657a1617>)

_Jokes aren’t appropriate for notifications_

### Be day-specific

Use the days of the week. Don’t use “today,” “tomorrow,” “tonight,” or similar words. About 20% of users don’t see notifications on the day they’re sent, so “tomorrow” might be read when it’s “today” for the user.

An exception is when a notification auto-dismisses at a specific timestamp.

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53n553-17.png?alt=media&token=58155094-2c32-4ec2-bddd-120f3f6697ac>)

_Specify the day of the week so that it makes sense if someone reads it the following day_

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53nb80-18.png?alt=media&token=67b73253-b600-4cf6-9fe6-65422e5f4582>)

_Don’t use relative terms like “today” or “tomorrow”_

### Don’t interrupt the flow

If you have notifications or emails related to the onboarding process, make sure they don’t trigger during onboarding.

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53nkf2-19.png?alt=media&token=8026372e-8d39-4ce0-a778-81e633c335f8>)

_Don't prompt the user with a notification or email that might interrupt an important moment_

### Don’t name the product (again)

An app’s name or logo is already included in a notification’s design. Use the limited space for other information.

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53ntem-20.png?alt=media&token=bb68096c-e27f-46f3-9de3-375ab98ceba3>)

_Use the available space for useful information_

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53nz6w-21.png?alt=media&token=9ccad6e0-2b10-4d67-85b3-91391d6d56f8>)

_Don’t waste space repeating information that is already included by the OS_

### Opting in and out

Give users a way to opt-out of notifications in context. If you don’t, they’ll have to dig into settings and may get frustrated. If you do offer opt-outs or opt-ins, make it clear what benefit the user is getting or losing.

![Image](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flw53o6jr-22.png?alt=media&token=4a6e7dfa-4d4d-445d-b0bf-46fe7d9275b8>)

_Make it easy for users to start or stop receiving notifications_
