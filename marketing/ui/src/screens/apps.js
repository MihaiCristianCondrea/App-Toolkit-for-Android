import { html, nothing } from "lit";
import { iconButton, navigationBar, navigationDrawer, screen, topAppBar } from "../kit/shell.js";

/** Public catalog the Android app reads (ApiHost.kt, `GET api/v1/apps`). */
const APPS_URL = "https://android-apps-metadata-backend.mihaicristiancondrea.workers.dev/api/v1/apps";
const PLAY_URL = "https://play.google.com/store/apps/details?id=";

/** One request per page load; every state change reuses it. */
let request = null;
const loadApps = () =>
  (request ??= fetch(APPS_URL)
    .then((r) => {
      if (!r.ok) throw new Error(`HTTP ${r.status}`);
      return r.json();
    })
    .then((json) => json.data.apps.map((a) => ({
      name: a.name,
      id: a.package_name,
      icon: a.icon_logo,
      summary: a.short_description ?? "",
      category: a.category?.label ?? "",
    })))
    .catch((error) => {
      request = null;
      throw error;
    }));

const FILTERS = [
  { id: "All", label: "All", icon: "apps" },
  { id: "Favorites", label: "Favorite Apps", icon: "star_outline" },
];

export default {
  id: "apps",
  title: "Apps",
  category: "catalog",
  categoryLabel: "Apps Catalog",
  tier: 1,
  palette: "google",
  description: "The developer's live app catalog from the metadata API, with search, favorites and Open random app.",
  defaults: {
    filter: "All",
    searchQuery: "",
    drawerOpen: false,
    favorites: [],
    apps: null,
    appsError: false,
  },
  controls: [
    {
      type: "presets",
      label: "Catalog states",
      options: [
        ["All apps", { filter: "All", searchQuery: "" }],
        ["Favorite apps", { filter: "Favorites", searchQuery: "" }],
      ],
    },
    { key: "filter", label: "Filter", type: "choice", options: ["All", "Favorites"] },
  ],
  render(s) {
    if (!s.apps && !s.appsError) {
      loadApps().then((apps) => s.set({ apps }), () => s.set({ appsError: true }));
    }

    const favorites = new Set(s.favorites || []);
    const query = (s.searchQuery || "").trim().toLowerCase();
    const cols = s.window === "desktop" ? 4 : s.window === "fold" ? 3 : 2;
    const apps = (s.apps || []).filter((app) =>
      (s.filter !== "Favorites" || favorites.has(app.id)) &&
      (!query || [app.name, app.id, app.summary].some((v) => v.toLowerCase().includes(query))));

    const toggleFavorite = (id) => {
      const next = new Set(favorites);
      next.has(id) ? next.delete(id) : next.add(id);
      s.set({ favorites: [...next] });
    };
    const openApp = (app) => window.open(PLAY_URL + encodeURIComponent(app.id), "_blank", "noopener");
    const openRandom = () => apps.length && openApp(apps[Math.floor(Math.random() * apps.length)]);

    const card = (app) => html`
      <article class="app-tile-card" role="link" tabindex="0" aria-label="${app.name} on Google Play"
        @click=${() => openApp(app)} @keydown=${(e) => e.key === "Enter" && openApp(app)}>
        <div class="app-tile-actions">
          ${iconButton({ name: "star_outline", selectedIcon: "star", selected: favorites.has(app.id), label: `Favorite ${app.name}`, onClick: () => toggleFavorite(app.id) })}
        </div>
        <img class="app-tile-icon" src=${app.icon} alt="" loading="lazy" referrerpolicy="no-referrer" />
        <h3 class="app-tile-name">${app.name}</h3>
        <p class="app-tile-tag">${app.category}</p>
      </article>`;

    const body = s.appsError
      ? html`<div class="app-state"><p>Couldn't load the catalog.</p>
          <md-filled-tonal-button @click=${() => s.set({ appsError: false })}>Retry</md-filled-tonal-button></div>`
      : !s.apps
        ? html`<div class="apps-grid" style="--apps-cols:${cols}" aria-busy="true">${Array.from({ length: cols * 3 }, () => html`<div class="app-tile-card app-tile-card--skeleton"></div>`)}</div>`
        : apps.length
          ? html`<div class="apps-grid" style="--apps-cols:${cols}">${apps.map(card)}</div>`
          : html`<div class="app-state"><p>${s.filter === "Favorites" ? "No favorites yet. Tap a star to add one." : "No apps match your search."}</p></div>`;

    const content = html`
      <div class="ai-filter-row" style="padding:8px 16px 4px">
        <md-chip-set aria-label="Filter apps">
          ${FILTERS.map((f) => html`<md-filter-chip .label=${f.label} ?selected=${s.filter === f.id} @click=${() => s.set({ filter: f.id })}><md-icon slot="icon">${f.icon}</md-icon></md-filter-chip>`)}
        </md-chip-set>
      </div>
      <div class="tk-screen-pad" style="padding-top:8px;padding-bottom:96px">${body}</div>`;

    const fab = s.apps?.length
      ? html`<md-fab class="ai-extended-fab" variant="primary" label="Open random app" @click=${openRandom}><md-icon slot="icon">casino</md-icon></md-fab>`
      : nothing;

    return screen({
      top: topAppBar({ search: "Search apps", searchValue: s.searchQuery || "", onSearch: (v) => s.set({ searchQuery: v }), onNav: () => s.set({ drawerOpen: true }) }),
      content,
      fab,
      bottom: navigationBar("apps", s.go, s.window),
      drawer: navigationDrawer({ open: s.drawerOpen, onClose: () => s.set({ drawerOpen: false }), onNavigate: (t) => s.go(t, {}) }),
      navType: s.navType,
      window: s.window,
      tab: "apps",
      onSelect: s.go,
    });
  },
};
