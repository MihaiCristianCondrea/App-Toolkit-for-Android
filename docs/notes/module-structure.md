# Module structure

Source packages follow the responsibility owned by the module: `data`, `domain`, `ui`, and `di`.
Only layers with code that belongs to them are required.

- `data` owns repositories, data sources, persistence, and SDK mappings. A feature that consumes a
  core repository can omit its own data layer; display and theme do this.
- `domain` owns domain models and useful business operations. It is optional when the UI can use
  a repository directly; permissions, sample components, and sample settings do this.
- `ui` owns presentation and navigation. An integration that only exposes a repository need not
  provide a UI layer.
- `di` owns the module's injectable implementation bindings. Composition roots assemble these
  modules and supply host configuration and extension points.

Feature-specific resources live in the owning feature's `src/main/res`. Shared resources belong
to a core module only when their responsibility is shared. Resource-only Android modules such as
`sample/core/ui` expose a generated `R` class and need no placeholder Kotlin source tree.

General Settings is a content route inside the settings composition feature; the root settings
screen and `ui/general` have distinct navigation responsibilities.
