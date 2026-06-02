# Landing screen

The Landing screen is the first bottom-bar destination in the host app. It gives users a compact
"what should I do next?" hub before they open the full Apps & Tools catalog or the Quick Tools list.

## Content model

The current implementation is intentionally lightweight and stateless:

- A time-aware greeting (`Good morning`, `Good afternoon`, `Good evening`, or `Good night`).
- **Recent Tool**: highlights Bubble Level and opens Quick Tools.
- **Favorite App**: highlights Cleaner and opens Apps & Tools.
- **Quick Actions**: highlights Coin Flip, Compass, and Flashlight and opens Quick Tools.
- **Latest Update**: highlights Toolkit 2.0 and opens Quick Tools.

The route delegates navigation through callbacks so the composable remains stateless and does not own
business logic.

## Navigation

`LandingRoute` is the default top-level route and the first bottom-bar item. Legacy startup values
that are no longer valid still fall back to Landing, while Apps & Tools and Quick Tools remain direct
bottom-bar destinations.
