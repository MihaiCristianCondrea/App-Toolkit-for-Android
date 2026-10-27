import { html } from "lit";
import { iconButton, navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";

/**
 * Developer apps catalog matching `AppsListScreen.kt`, `AppsList.kt`, and `AppCard.kt`.
 */
const DEVELOPER_APPS = [
  {
    id: "com.d4rk.cleaner",
    name: "Smart Cleaner for Android",
    icon: "cleaning_services",
    palette: "teal",
    installed: true,
    summary: "One-tap phone cleaner, storage analyzer, and duplicate remover.",
  },
  {
    id: "com.d4rk.qrcodescanner.plus",
    name: "QR & Bar Code Scanner",
    icon: "qr_code_scanner",
    palette: "gold",
    installed: true,
    summary: "Fast QR and barcode reader with custom code generator.",
  },
  {
    id: "com.d4rk.cartcalculator",
    name: "Shopping Cart Calculator",
    icon: "shopping_cart",
    palette: "orange",
    installed: false,
    summary: "Track live cart totals, discounts, and sales tax while shopping.",
  },
  {
    id: "com.d4rk.netprobe",
    name: "Net Probe - Networking Tools",
    icon: "network_check",
    palette: "teal",
    installed: false,
    summary: "Ping, traceroute, DNS lookup, and LAN port scanner.",
  },
  {
    id: "com.d4rk.androidtutorials",
    name: "Android Insider",
    icon: "android",
    palette: "gold",
    installed: true,
    summary: "Kotlin, Jetpack Compose, and Android architecture lessons.",
  },
  {
    id: "com.d4rk.lowbrightness",
    name: "Low Brightness for Android",
    icon: "brightness_low",
    palette: "amber",
    installed: false,
    summary: "Dim your screen below system minimum for comfortable night reading.",
  },
  {
    id: "com.d4rk.musicsleeptimer.plus",
    name: "Naptime: Sleep Sounds & Timer",
    icon: "bedtime",
    palette: "magenta",
    installed: false,
    summary: "Ambient sleep sounds and automatic media playback sleep timer.",
  },
  {
    id: "com.d4rk.englishwithlidia.plus",
    name: "English with Lidia Plus",
    icon: "school",
    palette: "orange",
    installed: false,
    summary: "Vocabulary, grammar lessons, and interactive English practice.",
  },
];

const APP_FILTERS = [
  { id: "All", label: "All", icon: "apps" },
  { id: "Installed", label: "Installed", icon: "check_circle" },
  { id: "NotInstalled", label: "Not installed", icon: "block" },
  { id: "Favorites", label: "Favorite Apps", icon: "star_outline" },
];

export default {
  id: "apps",
  title: "Apps",
  category: "catalog",
  categoryLabel: "Apps Catalog",
  tier: 1,
  palette: "google",
  source: "sample/feature/apps/src/main/kotlin/.../ui/AppsListScreen.kt",
  description:
    "1:1 translation of AppsListScreen.kt, AppsList.kt, and AppCard.kt: search top app bar, TopListFilters (All, Installed, Not installed, Favorite Apps), 2-column square aspect-ratio tonal cards (28dp radius, zero borders), and Open random app Extended FAB.",
  defaults: {
    filter: "All",
    searchQuery: "",
    drawerOpen: false,
    favorites: ["com.d4rk.cleaner", "com.d4rk.androidtutorials"],
  },
  controls: [
    {
      type: "presets",
      label: "Catalog states",
      options: [
        ["All Apps Catalog", { filter: "All", searchQuery: "" }],
        ["Installed Only", { filter: "Installed", searchQuery: "" }],
        ["Not Installed", { filter: "NotInstalled", searchQuery: "" }],
        ["Favorite Apps", { filter: "Favorites", searchQuery: "" }],
      ],
    },
    {
      key: "filter",
      label: "Filter Chip",
      type: "choice",
      options: ["All", "Installed", "NotInstalled", "Favorites"],
    },
  ],
  render(s) {
    const favSet = new Set(s.favorites || []);
    const selectedFilter = s.filter || "All";
    const query = (s.searchQuery || "").trim().toLowerCase();
    const cols =
      s.window === "desktop" ? 4 : s.window === "fold" ? 3 : 2;

    const filteredApps = DEVELOPER_APPS.filter((app) => {
      if (selectedFilter === "Installed" && !app.installed) return false;
      if (selectedFilter === "NotInstalled" && app.installed) return false;
      if (selectedFilter === "Favorites" && !favSet.has(app.id)) return false;
      if (
        query &&
        !app.name.toLowerCase().includes(query) &&
        !app.id.toLowerCase().includes(query) &&
        !app.summary.toLowerCase().includes(query)
      ) {
        return false;
      }
      return true;
    });

    const toggleFavorite = (id) => {
      const next = new Set(favSet);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      s.set({ favorites: [...next] });
    };

    const content = html`
      <!-- AppsListFilters (AppsList.kt lines 248-303) -->
      <div class="ai-filter-row" style="padding:16px 16px 4px">
        <span class="ai-filter-label">Sort by:</span>
        <md-chip-set aria-label="Filter apps">
          ${APP_FILTERS.map(
            (f) => html`
              <md-filter-chip
                .label=${f.label}
                ?selected=${selectedFilter === f.id}
                @click=${() => s.set({ filter: f.id })}
              >
                <md-icon slot="icon">${f.icon}</md-icon>
              </md-filter-chip>
            `,
          )}
        </md-chip-set>
      </div>

      <div class="tk-screen-pad" style="padding-top:12px;padding-bottom:96px">
        <div class="apps-grid" style="--apps-cols:${cols}">
          ${filteredApps.map((app) => {
            const isFav = favSet.has(app.id);
            return html`
              <!-- AppCard.kt: square aspect-ratio Card(shape = 28.dp), top-end Star + Share, centered 72dp icon + SemiBold name -->
              <article class="app-tile-card">
                <div class="app-tile-actions">
                  ${iconButton({
                    name: "star_outline",
                    selectedIcon: "star",
                    selected: isFav,
                    label: "Favorite",
                    onClick: () => toggleFavorite(app.id),
                  })}
                  ${iconButton({
                    name: "share",
                    label: "Share",
                  })}
                </div>

                <div
                  class="app-tile-icon"
                  style="
                    background:color-mix(in srgb, var(--tk-${app.palette}-container) 22%, var(--md-sys-color-surface-container-highest));
                    color:var(--tk-${app.palette}-content);
                  "
                >
                  <span class="material-symbols-outlined" style="font-size:36px">
                    ${app.icon}
                  </span>
                </div>

                <h3 class="app-tile-name">${app.name}</h3>
              </article>
            `;
          })}
        </div>
      </div>
    `;

    const fab = html`
      <md-fab
        class="ai-extended-fab"
        variant="primary"
        label="Open random app"
      >
        <md-icon slot="icon">casino</md-icon>
      </md-fab>
    `;

    return screen({
      top: topAppBar({
        search: "Search apps",
        searchValue: s.searchQuery || "",
        onSearch: (v) => s.set({ searchQuery: v }),
        onNav: () => s.set({ drawerOpen: true }),
      }),
      content,
      fab,
      bottom: navigationBar("apps", s.go, s.window),
      drawer: navigationDrawer({
        open: s.drawerOpen,
        onClose: () => s.set({ drawerOpen: false }),
        onNavigate: (target) => s.go(target, {}),
      }),
      navType: s.navType,
      window: s.window,
      tab: "apps",
      onSelect: s.go,
    });
  },
};
