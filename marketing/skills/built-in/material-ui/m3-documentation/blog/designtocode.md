---
title: "Design to Code: Turning Handoffs Into High-fives"
source_url: "https://m3.material.io/blog/designtocode"
lastmod: "2024-11-11"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "blog"
assets:
  - "assets/images/f4c9b7f34f0e-qpnv-ci_KsPIo_qayXp3E8Tv4xKUIYA-JAgJdiOdmjLD_UuGP7875Lg65YqmBysrvbMBmTP0rM7jknOXrWd2ZZ1BVN-sY-B2.png"
  - "assets/images/ddc743002cb2-i6AGtN52tZIPNjmWc8k-VU_EVfS7-0Nc7jZS4qnKT12SRpn1CprTmu7Ml3LX8xqpUt8z4Ehw_AwHa3e39YWubsIHJqj7U_M5.png"
  - "assets/images/9d9409837804-Gli8S2kChMQ1mGgVDqs7fTlT6j9Rm1vZY5C7se6_ZjaKjIEldtnMdhNlZa_Rmrz0Sv2OOQv0hcvN1L5VTlqCGeSyPo0L84j7.png"
---

# Design to Code: Turning Handoffs Into High-fives

Teaming up with Figma to bring great UI from design to code

![Image](<https://lh3.googleusercontent.com/qpnv-ci_KsPIo_qayXp3E8Tv4xKUIYA-JAgJdiOdmjLD_UuGP7875Lg65YqmBysrvbMBmTP0rM7jknOXrWd2ZZ1BVN-sY-B2avEOdsV4xeqcPSewItI>)

Published: 2021-10-27T09:05:00

Design systems like Material have profoundly changed the way teams build digital products. What was once a wild landscape of team-specific design practices is now a well understood set of guidelines and shared components. Material creates both a baseline for product excellence and an expressive vocabulary to realize exciting new experiences, while design tools like Figma help translate that vocabulary into compelling UI.

## The handoff problem

![UI handoff remains one of the biggest pain points for teams building digital products](<https://lh3.googleusercontent.com/i6AGtN52tZIPNjmWc8k-VU_EVfS7-0Nc7jZS4qnKT12SRpn1CprTmu7Ml3LX8xqpUt8z4Ehw_AwHa3e39YWubsIHJqj7U_M5pnc3rjgC3V801Qcg5NoE>)

UI handoff remains one of the biggest pain points for teams building digital products

Despite those improvements, design systems have yet to address one major friction point in product design: The messy handoff of UI between designers and developers. Even with great design systems our teams are still subject to lossy, ad-hoc practices for translating our ideas into working code. What typeface is this? Which component should I use? How does this thing stretch? We’re still copying values from emails, chat and bug tickets, and it doesn’t feel productive. Most of our time is spent improving process artifacts rather than the product itself.

## Design to code with Figma and Android Studio

![Our design to code workflow preview allows teams to package UI components in Figma for Jetpack Compose Android projects](<https://lh3.googleusercontent.com/Gli8S2kChMQ1mGgVDqs7fTlT6j9Rm1vZY5C7se6_ZjaKjIEldtnMdhNlZa_Rmrz0Sv2OOQv0hcvN1L5VTlqCGeSyPo0L84j7MxtNzo8fVpR-iDlaoIUN>)

Our design to code workflow preview allows teams to package UI components in Figma for Jetpack Compose Android projects

This year at Android Developer Summit [our team shared a glimpse of how we’re addressing this problem](https://www.youtube.com/watch?v=CMC8X1qVPEw) with a new design to code workflow. The workflow is built with both designers and developers in mind, and introduces some new ideas for how to describe, exchange and iterate UI.

To achieve this, Material Design is teaming up with Figma, the leading UI design tool. Figma is all in on design to code workflows, and we’re working together to ensure the right mental models and connections exist across the tools we use.

Our design to code workflow allows teams to create UI components in Figma and export them in a portable container we call a UI Package. These Packages can be directly used in Jetpack Compose projects for Android applications, can be edited in Figma, and can be directly updated in code with good developer ergonomics for component reuse and change management. Our current work is focused on high-fidelity translation of layout, appearance, dynamic data bindings and interaction design intent, with plans to extend the UI Package model in the future.

## Get a glimpse of the future

We hope you’ll check out our lightning talk from Android Developer Summit to learn more. Over the next few months, we’d like to work with a small group of teams to help shape this new workflow, so if you’re interested in joining our early access program [please sign up using this form](https://services.google.com/fb/forms/designtocode/) and we’ll be in touch!
