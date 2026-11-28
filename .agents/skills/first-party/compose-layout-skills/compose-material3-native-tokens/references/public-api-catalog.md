# Public AndroidX API discovery catalog

Snapshot of official reference pages consulted on 2026-11-27. This is not an exhaustive symbol list or compatibility matrix. Newer APIs may require a pre-release dependency or opt-in. Verify against resolved artifacts, including platform-specific availability. The links below are Android Compose Material 3, not Material 2, Wear, or TV.

| API owner (official reference) | Relevant public members to inspect |
|---|---|
| [MaterialTheme](https://developer.android.com/reference/kotlin/androidx/compose/material3/MaterialTheme) | `colorScheme`, `shapes`, `typography`, `motionScheme` |
| [ButtonDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/ButtonDefaults) | `ContentPadding`, `ButtonWithIconContentPadding`, `IconSpacing`, `IconSize`, `MinHeight`, `MinWidth`, `ExtraSmallContentPadding`, `ExtraLargeContentPadding`, `contentPaddingFor`, `iconSpacingFor`, `iconSizeFor`, `shapesFor`, `textStyleFor`, variant colors/elevation factories |
| [ButtonGroupDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/ButtonGroupDefaults) | `HorizontalArrangement`, `ConnectedSpaceBetween`, `connectedLeadingButtonShapes`, `connectedTrailingButtonShapes` |
| [SplitButtonDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/SplitButtonDefaults) | `Spacing`, `leadingButtonContentPaddingFor`, `trailingButtonContentPaddingFor`, `leadingButtonShapesFor`, `SmallLeadingButtonContentPadding`, `SmallTrailingButtonContentPadding` |
| [ListItemDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/ListItemDefaults) | `ContentPadding`, `SegmentedGap`, `segmentedShapes`, `shapes`, `elevation`, state-aware colors |
| [MenuDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/MenuDefaults) | `DropdownMenuItemContentPadding`, `DropdownMenuItemHorizontalArrangement`, `DropdownMenuGroupContentPadding`, `DropdownMenuGroupLabelHorizontalPadding`, `DropdownMenuItemTrailingLabelHorizontalPadding`, `DropdownMenuSelectableItemContentPadding`, `GroupSpacing`, `HorizontalDividerPadding`, `LeadingIconSize`, `TrailingIconSize`, `TonalElevation`, `ShadowElevation` |
| [AssistChipDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/AssistChipDefaults) | `ContentPadding`, `HorizontalSpacing`, `IconSize`, `Height` |
| [SuggestionChipDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/SuggestionChipDefaults) | `ContentPadding`, `HorizontalSpacing`, `IconSize`, `Height` |
| [FilterChipDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/FilterChipDefaults) | `horizontalArrangement`, `shapes`, state-aware border/colors/elevation |
| [InputChipDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/InputChipDefaults) | `contentPadding`, `horizontalArrangement`, `HorizontalSpacing`, `CompactHorizontalSpacing`, `AvatarSize`, `IconSize`, `Height` |
| [TopAppBarDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/TopAppBarDefaults) | `ContentPadding`, window insets, scroll behaviors and colors |
| [BottomAppBarDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/BottomAppBarDefaults) | `ContentPadding`, `ContainerElevation`, window insets |
| [FloatingToolbarDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/FloatingToolbarDefaults) | `ContentPadding`, toolbar shapes, colors and scroll behavior |
| [NavigationDrawerItemDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/NavigationDrawerItemDefaults) | `ItemPadding`, colors |
| [TextFieldDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/TextFieldDefaults) | `contentPaddingWithLabel`, `contentPaddingWithoutLabel`, colors, shape and minimum dimensions |
| [OutlinedTextFieldDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/OutlinedTextFieldDefaults) | `contentPaddingWithLabel`, `contentPaddingWithoutLabel`, colors, shape, border thickness and minimum dimensions |
| [CardDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/CardDefaults) | `shape`, `outlinedShape`, `cardColors`, `cardElevation`, `outlinedCardBorder`, matching variant factories |
| [IconButtonDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/IconButtonDefaults) | size-specific icon/container sizes, round/square/pressed/selected shapes, `toggleableShapes`, variant colors |
| [SegmentedButtonDefaults](https://developer.android.com/reference/kotlin/androidx/compose/material3/SegmentedButtonDefaults) | `itemShape`, `borderStroke`, colors |
| [Scaffold](https://developer.android.com/reference/kotlin/androidx/compose/material3/Scaffold.composable) | `ScaffoldDefaults.contentWindowInsets`, content `PaddingValues` and inset consumption |
| [MotionScheme](https://developer.android.com/reference/kotlin/androidx/compose/material3/MotionScheme) | spatial and effects animation specs |
| [Material 3 package](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) | `contentColorFor`, `LocalContentColor`, `LocalTextStyle`, `minimumInteractiveComponentSize`, `LocalMinimumInteractiveComponentSize`, `ColorScheme.surfaceColorAtElevation`, `MaterialShapes` and additional component defaults |

Additional owners to inspect as needed: `ToggleButtonDefaults`, `FloatingActionButtonDefaults`, `NavigationBarDefaults`, `NavigationRailDefaults`, `SearchBarDefaults`, `ModalBottomSheetDefaults`, and defaults for progress indicators, switches, checkboxes, sliders, tabs, badges and dividers. Read the actual component's public signature; do not assume every owner exposes all token categories.

## Version and maintenance references

- [Material 3 release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3)
- [Material 3 theming in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [AndroidX Material 3 package API](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)
