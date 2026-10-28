import { html, nothing } from "lit";
import { classMap } from "lit/directives/class-map.js";
import { ref } from "lit/directives/ref.js";
import "material-next-web-components/marketing.js";
import {asset} from "./assets.js";
import { shellLayout, LIST_DETAIL } from "./layout.js";

/**
 * Material Symbol icon (https://fonts.google.com/icons). `fill` mirrors Compose's Filled
 * variants; `rounded` uses Material Symbols Rounded.
 */
export const icon = (name, { fill = false, size, color, cls = "", rounded = false } = {}) =>
  html`<md-icon
    class=${cls}
    style="${size ? `--md-icon-size:${size}px;` : ""}${color ? `color:${color};` : ""}${fill ? "font-variation-settings:'FILL' 1;" : ""}${rounded ? "--md-icon-font:'Material Symbols Rounded';" : ""}"
    >${name}</md-icon
  >`;

/**
 * Android status bar: clean 12:00 clock on the left, battery icon on the right.
 */
export const statusBar = () => html`
  <div class="sc-status">
    <div class="sc-status__left">
      <span class="sc-status__time">12:00</span>
    </div>
    <div class="sc-status__right">
      <span class="sc-status__icon">${icon("signal_wifi_4_bar", { size: 16 })}</span>
      <span class="sc-status__icon">${icon("signal_cellular_4_bar", { size: 16 })}</span>
      <span class="sc-status__icon">${icon("battery_full", { size: 16 })}</span>
    </div>
  </div>
`;

/**
 * Material Web icon button. `selectedIcon` makes it a toggle (`selected` drives its state);
 * the selected icon is drawn filled, as Compose's Filled icons are.
 */
export const iconButton = ({ name, label, onClick, selected, selectedIcon, disabled = false, cls = "" }) =>
  selectedIcon
    ? html`<md-icon-button class=${cls} toggle .selected=${Boolean(selected)} ?disabled=${disabled} aria-label=${label}
        @click=${(e) => { e.stopPropagation(); onClick?.(e); }}>
        <md-icon>${name}</md-icon><md-icon slot="selected" class="sc-filled">${selectedIcon}</md-icon>
      </md-icon-button>`
    : html`<md-icon-button class=${cls} ?disabled=${disabled} aria-label=${label}
        @click=${(e) => { e.stopPropagation(); onClick?.(e); }}><md-icon>${name}</md-icon></md-icon-button>`;

const NAV_BUTTONS = { menu: ["menu", "Open navigation drawer"], back: ["arrow_back", "Navigate up"] };

/**
 * Top app bars as the app draws them (App Toolkit shell, Material 3 TopAppBar).
 */
export const topAppBar = ({
  variant = "small",
  title = "",
  nav = "menu",
  actions = [],
  on = [],
  search,
  searchValue = "",
  onSearch,
  onNav,
  onAction,
  showMore = true,
  multiline = false,
  article = false,
} = {}) => {
  const navBtn = nav
    ? iconButton({ name: NAV_BUTTONS[nav]?.[0] ?? nav, label: NAV_BUTTONS[nav]?.[1] ?? nav, onClick: () => onNav?.() })
    : nothing;
  const acts = actions.map((a) => {
    const { name, icon: actIcon = name, label = name } = typeof a === "object" ? a : { name: a };
    return iconButton({ name: actIcon, selectedIcon: actIcon, selected: on.includes(name), label, onClick: () => onAction?.(name) });
  });
  const trailing = acts.length
    ? acts
    : showMore
      ? iconButton({ name: "more_vert", label: "More options", onClick: () => onAction?.("more") })
      : nothing;
  const bar = classMap({ "sc-topbar": true, "sc-topbar--article": article, "sc-topbar--no-nav": !nav });

  if (search) {
    const placeholder = typeof search === "string" ? search : "Search";
    return html`<div class="sc-topbar sc-topbar--search">
      ${navBtn}
      <md-filled-text-field class="sc-search" type="search" aria-label=${placeholder} placeholder=${placeholder}
        .value=${searchValue} @input=${(e) => onSearch?.(e.target.value)}>
        <md-icon slot="leading-icon">search</md-icon>
        ${searchValue ? html`<md-icon-button slot="trailing-icon" aria-label="Clear search" @click=${() => onSearch?.("")}><md-icon>close</md-icon></md-icon-button>` : nothing}
      </md-filled-text-field>
      ${trailing}
    </div>`;
  }

  if (variant === "large") {
    return html`<div class="sc-topbar sc-topbar--large">
      <div class=${bar}>${navBtn}<span class="sc-topbar__title ${multiline ? "sc-topbar__title--multiline" : ""}">${title}</span>${trailing}</div>
      <div class="sc-topbar__large-wrap"><div class="sc-topbar__large-title">${title}</div></div>
    </div>`;
  }

  return html`<div class=${bar}>
    ${navBtn}
    <span class="sc-topbar__title ${multiline ? "sc-topbar__title--multiline" : ""}">${title}</span>
    ${trailing}
  </div>`;
};

/**
 * App Toolkit's three primary bottom navigation tabs (`Quick Tools`, `Measure`, `Apps`).
 */
const NAV = [
  { id: "quick-tools", label: "Quick Tools", icon: "grid_view", rounded: true },
  { id: "numbers", label: "Measure", icon: "straighten" },
  { id: "apps", label: "Apps", icon: "apps" },
];

/** Screen (and state) each tab opens. */
export const NAV_TARGETS = {
  "quick-tools": ["quick-tools", {}],
  numbers: ["numbers", {}],
  apps: ["apps", {}],
};

/**
 * Bottom navigation bar mirroring App Toolkit's Navigation 3 host using official `<md-navigation-bar>` & `<md-navigation-tab>`.
 */
export const navigationBar = (selected = "numbers", onSelect, win) =>
  shellLayout(win).framed
    ? nothing
    : html`
        <md-navigation-bar class="sc-navbar" .activeIndex=${Math.max(0, NAV.findIndex((n) => n.id === selected))}>
          ${NAV.map(
            (n) => html`<md-navigation-tab label=${n.label} ?active=${n.id === selected} @click=${() => onSelect?.(...NAV_TARGETS[n.id])}>
              <md-icon slot="active-icon" class="sc-filled ${n.rounded ? "sc-rounded" : ""}">${n.icon}</md-icon>
              <md-icon slot="inactive-icon" class=${n.rounded ? "sc-rounded" : ""}>${n.inactiveIcon ?? n.icon}</md-icon>
            </md-navigation-tab>`,
          )}
        </md-navigation-bar>
      `;

/** The drawer's entries pinned to the bottom (App Toolkit toolkitFooter()). */
const DRAWER_ENTRIES = [
  { id: "settings", label: "Settings", icon: "settings" },
  { id: "help", label: "Help & feedback", icon: "help_outline" },
  { id: "share", label: "Share", icon: "share" },
];

export const navigationRail = ({ tab, onSelect, expanded = false } = {}) => html`
  <nav class="sc-rail ${expanded ? "sc-rail--expanded" : ""}" aria-label="Navigation">
    <div class="sc-rail__header">
      <md-icon-button aria-label=${expanded ? "Collapse navigation" : "Expand navigation"}><md-icon>${expanded ? "menu_open" : "menu"}</md-icon></md-icon-button>
      ${expanded ? html`<span class="sc-rail__title">App Toolkit</span>` : nothing}
    </div>
    <div class="sc-rail__items">
      ${NAV.map((n) => html`<button class="sc-rail__item ${n.id === tab ? "is-selected" : ""}" @click=${() => onSelect?.(...NAV_TARGETS[n.id])}>
        <span class="sc-rail__indicator">${icon(n.id === tab ? n.icon : n.inactiveIcon ?? n.icon, { fill: n.id === tab })}</span>
        <span class="sc-rail__label">${n.label}</span>
      </button>`)}
    </div>
    <div class="sc-rail__items sc-rail__footer">
      ${DRAWER_ENTRIES.map((e) => html`<button class="sc-rail__item">
        <span class="sc-rail__indicator">${icon(e.icon)}</span><span class="sc-rail__label">${e.label}</span>
      </button>`)}
    </div>
  </nav>`;

export const permanentDrawer = ({ tab, onSelect } = {}) => html`
  <nav class="sc-pdrawer" aria-label="Navigation">
    <div class="sc-pdrawer__header">
      <img src=${asset("/logos/app_logo.svg")} alt="" /><span>App Toolkit</span>
    </div>
    <div class="sc-pdrawer__items">
      ${NAV.map((n) => html`<button class="sc-pdrawer__item ${n.id === tab ? "is-selected" : ""}" @click=${() => onSelect?.(...NAV_TARGETS[n.id])}>
        ${icon(n.id === tab ? n.icon : n.inactiveIcon ?? n.icon, { fill: n.id === tab })}<span>${n.label}</span>
      </button>`)}
    </div>
    <div class="sc-pdrawer__items sc-pdrawer__footer">
      ${DRAWER_ENTRIES.map((e) => html`<button class="sc-pdrawer__item">${icon(e.icon)}<span>${e.label}</span></button>`)}
    </div>
  </nav>`;

export const listDetail = ({ list, detail, detailCls = "", detailStyle = "" }) => html`
  <div class="sc-panes">
    <div class="sc-pane sc-pane--list">${list}</div>
    <div class="sc-pane-separator" aria-hidden="true"><span></span></div>
    <div class="sc-pane sc-pane--detail ${detailCls}" style=${detailStyle}>${detail}</div>
  </div>`;

export const listPaneWidth = (win) => {
  const l = shellLayout(win);
  return (l.width - l.navWidth - LIST_DETAIL.separator) * LIST_DETAIL.listFraction;
};

export const gestureNavigationPill = () => html`
  <div class="sc-sysnav sc-sysnav--gesture" aria-hidden="true"><span class="sc-sysnav__handle"></span></div>
`;

export const threeButtonNavigation = ({ onBack, onHome, onRecents } = {}) => html`
  <div class="sc-sysnav sc-sysnav--buttons">
    <button class="sc-sysnav__btn" @click=${() => onBack?.()} aria-label="Back">
      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M17 4.5v15a1 1 0 0 1-1.5.86l-11.25-7.5a1 1 0 0 1 0-1.72L15.5 3.64A1 1 0 0 1 17 4.5z"/></svg>
    </button>
    <button class="sc-sysnav__btn" @click=${() => onHome?.()} aria-label="Home">
      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><circle cx="12" cy="12" r="8"/></svg>
    </button>
    <button class="sc-sysnav__btn" @click=${() => onRecents?.()} aria-label="Recents">
      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><rect x="5" y="5" width="14" height="14" rx="2.5"/></svg>
    </button>
  </div>
`;

export const systemNavigationBar = ({ type = "gesture", onBack, onHome, onRecents } = {}) =>
  type === "buttons"
    ? threeButtonNavigation({ onBack, onHome, onRecents })
    : gestureNavigationPill();

export const navigationDrawer = ({ open = false, onClose, onNavigate } = {}) => {
  if (!open) return nothing;
  return html`
    <div class="ai-drawer-overlay">
      <div class="ai-drawer-scrim" @click=${() => onClose?.()}></div>
      <aside class="ai-drawer-sheet">
        <div class="ai-drawer-header">
          <img src=${asset("/logos/app_logo.svg")} alt="App Toolkit Logo" class="ai-drawer-logo" />
          <h2 class="ai-drawer-title">App Toolkit</h2>
        </div>
        <div class="ai-drawer-body"></div>
        <div class="ai-drawer-footer">
          <div class="ai-drawer-item" @click=${() => { onClose?.(); }}>
            ${icon("help_outline", { size: 24 })}
            <span>Help &amp; feedback</span>
          </div>
          <div class="ai-drawer-item" @click=${() => { onClose?.(); }}>
            ${icon("share", { size: 24 })}
            <span>Share</span>
          </div>
        </div>
      </aside>
    </div>
  `;
};

export const screen = ({
  top,
  content,
  bottom,
  overlay,
  fab,
  drawer,
  raised = false,
  scrolled = false,
  scrollKey,
  scrollPosition = 0,
  cls = "",
  contentCls = "",
  onWheel,
  onScroll,
  navType = "gesture",
  onBack,
  onHome,
  onRecents,
  window: win,
  tab,
  onSelect,
  carded = true,
}) => {
  const layout = shellLayout(win);
  const systemInset = layout.systemNav ? (navType === "buttons" ? 48 : 24) : 0;
  const bottomInset = bottom && bottom !== nothing && !layout.framed ? 80 : 0;
  const size = `${win ? `width:${layout.width}px;height:${layout.height}px;` : ""}--sc-system-nav-height:${systemInset}px;--sc-bottom-bar-height:${bottomInset}px`;
  const handleScroll = (e) => {
    const scrollTop = e.target.scrollTop;
    const isScrolled = scrollTop > 4;
    const sc = e.target.closest(".sc-screen");
    if (sc) {
      sc.classList.toggle("sc-screen--scrolled", isScrolled);
      if (scrollTop <= 0) {
        sc.style.removeProperty("--sc-collapse");
        sc.style.removeProperty("--sc-title-opacity");
      } else {
        const expandedHeight = parseFloat(getComputedStyle(sc).getPropertyValue("--sc-expanded-height")) || 88;
        const collapseK = Math.min(1, Math.max(0, scrollTop / expandedHeight));
        sc.style.setProperty("--sc-collapse", collapseK.toFixed(3));
        sc.style.setProperty("--sc-title-opacity", collapseK > 0.75 ? ((collapseK - 0.75) / 0.25).toFixed(3) : "0");
      }
    }
    onScroll?.(e);
  };

  const contentRef = ref((el) => {
    if (!el) return;
    if (scrollKey !== undefined && el.__scrollKey !== scrollKey) {
      el.__scrollKey = scrollKey;
      el.__lastScrolled = scrolled;
      requestAnimationFrame(() => {
        if (!el.isConnected || el.__scrollKey !== scrollKey) return;
        el.scrollTop = scrollPosition || (scrolled ? 110 : 0);
        handleScroll({ target: el });
      });
    } else if (el.__lastScrolled !== scrolled) {
      el.__lastScrolled = scrolled;
      if (scrolled && el.scrollTop === 0) el.scrollTop = 110;
      else if (!scrolled && el.scrollTop > 0) el.scrollTop = 0;
    }
  });
  const contentEl = html`<div
    class="sc-content ${contentCls}"
    ${contentRef}
    @scroll=${handleScroll}
    @wheel=${(e) => onWheel && (e.preventDefault(), onWheel(e.deltaY))}
  >${content}</div>`;
  const systemNav = layout.systemNav ? systemNavigationBar({ type: navType, onBack, onHome, onRecents }) : nothing;

  if (layout.framed) {
    const nav = layout.mode === "permanentDrawer" ? permanentDrawer({ tab, onSelect }) : navigationRail({ tab, onSelect });
    return html`
      <div class="sc-screen sc-screen--framed sc-screen--${layout.mode} ${carded ? "sc-screen--carded" : ""} ${scrolled ? "sc-screen--scrolled" : ""} ${cls}" style=${size}>
        ${layout.statusBar ? statusBar() : nothing}
        <div class="sc-frame">
          ${nav}
          <div class="sc-frame__body">
            ${top ?? nothing}
            <div class="sc-frame__card">
              ${contentEl}
              ${fab ? html`<div class="sc-fab">${fab}</div>` : nothing} ${bottom ?? nothing}
            </div>
          </div>
        </div>
        ${systemNav}
        ${overlay ?? nothing}
      </div>
    `;
  }

  return html`
    <div class="sc-screen ${raised ? "sc-screen--raised" : ""} ${scrolled ? "sc-screen--scrolled" : ""} ${layout.system === "desktop" ? "sc-screen--desktop" : ""} ${cls}" style=${size}>
      ${layout.statusBar ? statusBar() : nothing} ${top ?? nothing}
      ${contentEl}
      ${fab ? html`<div class="sc-fab">${fab}</div>` : nothing} ${bottom ?? nothing}
      ${systemNav}
      ${drawer ?? nothing}
      ${overlay ?? nothing}
    </div>
  `;
};

// Device artwork and display geometry are owned by Material Next.
export const device = (inner, {shadow = true, device: name = "pixel_9_pro_xl"} = {}) =>
  html`<mnw-device-frame device=${name} ?shadow=${shadow}>${inner}</mnw-device-frame>`;
