# Component index

One row per Material component: where its curated reference, official pages, and images are, and
what it is called on each platform. Use it to load only the files a task needs.

Paths:

- Curated reference: `references/components/<file>.md` (selection rules, anatomy, sizes, states,
  color and type roles, do's and don'ts).
- Official pages: `documentation/components/<folder>/` with `overview.md`, `specs.md`,
  `guidelines.md`, and `accessibility.md` where published.
- Images: `grep -i "^components_<folder>/" visuals/INDEX.tsv`.
- Exact dp values: the matching section of `references/component-tokens.md`.

Platform names are orientation, recorded from Compose Material 3 1.5.0, MDC-Android 1.14, and
`@material/web` 2.5. Confirm the exact API in the project's installed version before writing code.
"None" means no supported component; build it from semantics and tokens.

## Actions

| Component | Curated | Folder | Compose | Views | Web |
|---|---|---|---|---|---|
| All buttons (choosing a type) | `buttons.md` | `all-buttons.md` (page) | | | |
| Buttons | `buttons.md` | `buttons` | `Button`, `FilledTonalButton`, `ElevatedButton`, `OutlinedButton`, `TextButton`; toggle variants | `MaterialButton` with styles | `md-filled-button`, `md-filled-tonal-button`, `md-elevated-button`, `md-outlined-button`, `md-text-button` |
| Icon buttons | `buttons.md` | `icon-buttons` | `IconButton`, `FilledIconButton`, `FilledTonalIconButton`, `OutlinedIconButton`, toggle variants | `MaterialButton` icon styles | `md-icon-button`, `md-filled-icon-button`, `md-filled-tonal-icon-button`, `md-outlined-icon-button` |
| Button groups | `buttons.md` | `button-groups` | `ButtonGroup` | `MaterialButtonGroup` | None |
| Split button | `buttons.md` | `split-button` | `SplitButtonLayout` | `MaterialSplitButton` | None |
| Segmented buttons (no longer recommended) | `buttons.md` | `segmented-buttons` | `SingleChoiceSegmentedButtonRow`, `MultiChoiceSegmentedButtonRow` | `MaterialButtonToggleGroup` | None |
| FAB | `fabs.md` | `floating-action-button` | `FloatingActionButton`, `SmallFloatingActionButton`, `MediumFloatingActionButton`, `LargeFloatingActionButton` | `FloatingActionButton` | `md-fab` |
| Extended FAB | `fabs.md` | `extended-fab` | `ExtendedFloatingActionButton` | `ExtendedFloatingActionButton` | `md-fab` with `label` |
| FAB menu | `fabs.md` | `fab-menu` | `FloatingActionButtonMenu`, `ToggleFloatingActionButton` | None | None |

## Communication and feedback

| Component | Curated | Folder | Compose | Views | Web |
|---|---|---|---|---|---|
| Badges | `feedback.md` | `badges` | `Badge`, `BadgedBox` | `BadgeDrawable` | None |
| Progress indicators | `feedback.md` | `progress-indicators` | `LinearProgressIndicator`, `CircularProgressIndicator`, wavy variants | `LinearProgressIndicator`, `CircularProgressIndicator` | `md-linear-progress`, `md-circular-progress` |
| Loading indicator | `feedback.md` | `loading-indicator` | `LoadingIndicator`, `ContainedLoadingIndicator` (experimental in 1.5.0) | `LoadingIndicator` | None |
| Snackbar | `feedback.md` | `snackbar` | `Snackbar`, `SnackbarHost` | `Snackbar` | None |
| Tooltips | `feedback.md` | `tooltips` | `TooltipBox`, `PlainTooltip`, `RichTooltip` | Platform `TooltipCompat` | None |
| Menus | `feedback.md` | `menus` | `DropdownMenu`, `DropdownMenuItem`, `ExposedDropdownMenuBox` | `PopupMenu`, `ListPopupWindow`, exposed dropdown via `TextInputLayout` | `md-menu`, `md-menu-item`, `md-sub-menu`; `md-filled-select`, `md-outlined-select` |
| Date pickers | `feedback.md` | `date-pickers` | `DatePicker`, `DatePickerDialog`, `DateRangePicker` | `MaterialDatePicker` | None |
| Time pickers | `feedback.md` | `time-pickers` | `TimePicker`, `TimeInput` | `MaterialTimePicker` | None |

## Containment

| Component | Curated | Folder | Compose | Views | Web |
|---|---|---|---|---|---|
| Cards | `containment.md` | `cards` | `Card`, `ElevatedCard`, `OutlinedCard` | `MaterialCardView` | None |
| Carousel | `containment.md` | `carousel` | `HorizontalMultiBrowseCarousel`, `HorizontalUncontainedCarousel`, `HorizontalCenteredHeroCarousel` | `CarouselLayoutManager` | None |
| Lists | `containment.md` | `lists` | `ListItem` | `RecyclerView` with Material list styles (Expressive list in 1.14) | `md-list`, `md-list-item` |
| Divider | `containment.md` | `divider` | `HorizontalDivider`, `VerticalDivider` | `MaterialDivider` | `md-divider` |
| Bottom sheets | `containment.md` | `bottom-sheets` | `ModalBottomSheet`, `BottomSheetScaffold` | `BottomSheetDialogFragment`, `BottomSheetBehavior` | None |
| Side sheets | `containment.md` | `side-sheets` | None | `SideSheetDialog`, `SideSheetBehavior` | None |
| Dialogs | `containment.md` | `dialogs` | `AlertDialog`, `BasicAlertDialog` | `MaterialAlertDialogBuilder` | `md-dialog` |

## Selection and input

| Component | Curated | Folder | Compose | Views | Web |
|---|---|---|---|---|---|
| Checkbox | `input.md` | `checkbox` | `Checkbox`, `TriStateCheckbox` | `MaterialCheckBox` | `md-checkbox` |
| Radio button | `input.md` | `radio-button` | `RadioButton` | `MaterialRadioButton` | `md-radio` |
| Switch | `input.md` | `switch` | `Switch` | `MaterialSwitch` | `md-switch` |
| Sliders | `input.md` | `sliders` | `Slider`, `RangeSlider`, `VerticalSlider` | `Slider`, `RangeSlider` | `md-slider` |
| Chips | `input.md` | `chips` | `AssistChip`, `FilterChip`, `InputChip`, `SuggestionChip`, elevated variants | `Chip`, `ChipGroup` | `md-chip-set`, `md-assist-chip`, `md-filter-chip`, `md-input-chip`, `md-suggestion-chip` |
| Text fields | `input.md` | `text-fields` | `TextField`, `OutlinedTextField` | `TextInputLayout`, `TextInputEditText` | `md-filled-text-field`, `md-outlined-text-field` |
| Search | `input.md` | `search` | `SearchBar`, `DockedSearchBar`, and the Expressive search app bar APIs | `SearchBar`, `SearchView` | None |

## Navigation and bars

| Component | Curated | Folder | Compose | Views | Web |
|---|---|---|---|---|---|
| Navigation bar | `navigation.md` | `navigation-bar` | `ShortNavigationBar` (flexible), `NavigationBar` (baseline, no longer recommended) | `BottomNavigationView` | None |
| Navigation rail | `navigation.md` | `navigation-rail` | `WideNavigationRail`, `ModalWideNavigationRail`, `NavigationRail` (baseline) | `NavigationRailView` | None |
| Navigation drawer (no longer recommended) | `navigation.md` | `navigation-drawer` | `ModalNavigationDrawer`, `DismissibleNavigationDrawer`, `PermanentNavigationDrawer` | `NavigationView` in `DrawerLayout` | None |
| Tabs | `navigation.md` | `tabs` | `PrimaryTabRow`, `SecondaryTabRow`, scrollable variants, `Tab` | `TabLayout` | `md-tabs`, `md-primary-tab`, `md-secondary-tab` |
| App bars | `bars.md` | `app-bars` | `TopAppBar`, `CenterAlignedTopAppBar`, `MediumFlexibleTopAppBar`, `LargeFlexibleTopAppBar`, `BottomAppBar` | `MaterialToolbar` in `AppBarLayout`, `BottomAppBar` | None |
| Toolbars | `bars.md` | `toolbars` | `HorizontalFloatingToolbar`, `VerticalFloatingToolbar`, `FlexibleBottomAppBar` | `FloatingToolbarLayout`, `DockedToolbarLayout` | None |

Adaptive navigation across window sizes: `adaptive-layouts.md`.

## Not components, but often needed

| Need | Where |
|---|---|
| Interaction states and state layers | `interaction.md`, `accessibility.md`, `documentation/foundations/interaction/states/` |
| Elevation per component | `foundations.md`, `documentation/styles/elevation/tokens.md` |
| Icons (Material Symbols) | `foundations.md`, `documentation/styles/icons/` |
| XR variants of app bars, dialogs, navigation, toolbars | `documentation/foundations/xr/components/` |
| Watch components and layout | `documentation/foundations/watches/` and the `wear-compose-m3` skill |
