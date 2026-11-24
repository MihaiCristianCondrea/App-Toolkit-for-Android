---
title: "Badges"
source_url: "https://m3.material.io/components/badges/guidelines"
lastmod: "2026-02-05"
snapshot_at: "2026-05-21T03:29:14.331Z"
section: "components"
assets:
  - "assets/images/53dc135c6979-mg0wmkr4-01.png"
  - "assets/images/d9e0a3017612-mg0wmphl-02.png"
  - "assets/images/a78ee2e31daa-mi46ui92-03.png"
  - "assets/images/81e54bc113f1-mi46uzu8-04.png"
  - "assets/images/f108f72d8c26-lvm8dp3k-5.png"
  - "assets/images/7682dc81a1e8-lvm8fkil-7.png"
  - "assets/images/0a525ba9b3dd-mg0womos-07.png"
  - "assets/images/d1a9277f07f9-me8jybps-08.png"
  - "assets/images/7560f8e1c00d-me8k2962-9-do.png"
  - "assets/images/7dffa3adcb6c-me8k2vet-10-dont.png"
  - "assets/images/9fa2d1ebc20a-mg0wr0gz-11_do.png"
  - "assets/images/9ca9a02c1543-mg0wr4mw-12_dont.png"
  - "assets/images/644f6f2c53f0-me8ks60f-13.png"
  - "assets/images/5e5ae536d3bf-mg0wrqi2-14_do.png"
  - "assets/images/e5eab57c98ff-mg0wru4w-15_dont.png"
  - "assets/images/8c4abddcb288-mg0wsl1o-16_do.png"
  - "assets/images/ae5a1b931b65-me8kxkle-17_caution.png"
  - "assets/images/b9d30c453989-me8kyp7i-18_do.png"
  - "assets/images/38faee1b3cdf-me8kzbcm-19_dont.png"
---

# Badges

Badges show notifications, counts, or status information on navigation items and icons

## Guidelines

![Diagram of 4 badges in different configurations on a navigation bar's destination icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wmkr4-01.png?alt=media&token=2a5ee969-d4fd-499e-8716-f5c028e7dc71>)

_Large badges and a small badge in a navigation bar_

## Usage

Badges are used to indicate a notification, item count, or other information relating to a navigation destination. They are placed on the ending edge of icons, typically within other components.

There are two variants:

1.  Small badge

2.  Large badge

![Diagram of 4 badges in different configurations on a navigation bar's destination icons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wmphl-02.png?alt=media&token=1f32b375-fb78-43fe-a17e-8b92081ed44b>)

_Navigation bar with four badges_

A **small badge** is a simple circle, used to indicate an unread notification.  

A **large badge** contains label text communicating item count information.

![A small badge is a circle with no characters.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi46ui92-03.png?alt=media&token=2036ae76-31a4-44d1-b535-3dda6b642893>)

_Small badge_

![A large badge holds 4 characters and expands its container's width but not height.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fm3%2Fimages%2Fmi46uzu8-04.png?alt=media&token=2c742611-3a29-4104-80e3-fd3c425d0a76>)

_Large badge_

### With other components

Badges are most commonly used within other components, such as navigation bar, navigation rail, app bars, and tabs.

![Navigation bar with 3 icon buttons. 2 icons buttons have badges and 1 doesn't.
](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flvm8dp3k-5.png?alt=media&token=9e000d82-46f1-40c5-a9a7-e6b6046e04ae>)

_In navigation bars, hide the badge once the destination has been selected_

## Anatomy

![Small and large badges on 2 icon buttons.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flvm8fkil-7.png?alt=media&token=3f431e5e-5e88-41bd-bbcd-052f2e6202fd>)

_Small badge; Large badge container; Large badge label_

## Container

There are two container options for the badge: 

-   Small badge with no text

-   Large badge with text

![A small badge on a navigation item.
](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0womos-07.png?alt=media&token=50e7d36f-b594-4edc-84bd-980b95aa1331>)

_A small badge uses only shape to indicate a status change or new notification_

![Number 10 displayed within large badge on a navigation item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8jybps-08.png?alt=media&token=9ceea2e2-7e57-4f49-add4-668242cc5b09>)

_A large badge displays a number within a container to indicate a quantifiable status change related to a destination_

Badge containers are anchored inside the icon bounding box. As the number count increases for large badges, their width expands, but keeps the same placement.

Badges use a color intended to stand out against labels, icons, and navigation elements. Use the default color mapping to avoid color conflict issues.

![Small and large badges on the left side of 2 navigation items in a right-to-left language.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8k2962-9-do.png?alt=media&token=532cb06e-da53-45d2-ba95-83ffc62ea499>)

_Change the position of the badge for right-to-left languages_

![Small and large badges at random positions on 3 icon buttons on a navigation rail.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8k2vet-10-dont.png?alt=media&token=6390c1de-9d89-42b8-b383-793abc96f691>)

_Badges have fixed positions. Don’t change the position of the badge arbitrarily or place the badge over the icon._

![Small and large badges in default red color on 3 navigation items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wr0gz-11_do.png?alt=media&token=e91ea890-94ed-4e0b-a484-868b5f16308a>)

_Use the default badge color_

![Small and large badges in custom colors on 3 navigation items.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wr4mw-12_dont.png?alt=media&token=c58658ac-aea9-4126-a88c-9e7ee6b76930>)

_Avoid using custom color roles for the badge container and label text. If custom roles are necessary, make sure they have contrast of at least 3:1._

### Label text

Label large badges with counts or a status. The maximum number of characters within large badge label text is four, including a + to indicate more.

![4 icons with increasing number badges. The badges represent quantities, using a "+" symbol for quantities over 999.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8ks60f-13.png?alt=media&token=55a2ca2d-c2a3-4849-ab90-d860253ad5c2>)

_Large badges with one to four characters_

Use the recommended maximum character count to ensure labels don’t extend beyond the badge container.

![4-digit numbers condensed to a 3-digit badge with "+" to fit the badge container's width.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wrqi2-14_do.png?alt=media&token=37353d5f-7e7f-4ef8-b6c4-866d5015b818>)

_Truncate badge labels as needed_

![4-digit and 5-digit number badges on navigation items exceed the badge container's width and get cut off at the edge.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wru4w-15_dont.png?alt=media&token=79eceaeb-887c-4b7a-b950-b2ba0a84a24e>)

_Don’t let the badge get cut off or collide with another element_

## Placement

![Large badge to the right of a navigation rail item.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fmg0wsl1o-16_do.png?alt=media&token=85852fb2-22ac-43ad-90db-e27b958c792e>)

_Use a large badge to show count information when visual collisions aren’t an issue, such as in a navigation rail_

![Small badge on an icon button in an app bar.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8kxkle-17_caution.png?alt=media&token=276fc105-e418-4f3f-887f-3a449899e5ee>)

_Use a small badge when spaces are tightly constrained, such as app bars. Small badges won’t run into the edge of the screen._

![Large badge placed at the end of a tab.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8kyp7i-18_do.png?alt=media&token=83a75fa1-4971-49ed-8297-51b97ce1ca52>)

_When an icon with a badge is followed by text or another element, place a large badge at the trailing edge_

![Large badge overlapping the icon and text in a tab.](<https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Fme8kzbcm-19_dont.png?alt=media&token=b7e028e1-cddb-46f2-9efd-7c671e6dae6e>)

_Avoid using a large badge when it might overlap with a trailing element. Either place it at the trailing edge or use a small badge instead._
