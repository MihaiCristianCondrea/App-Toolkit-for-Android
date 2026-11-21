# Consumer integration

The sample demonstrates how a host assembles the Toolkit and implements its extension points.
A consumer's resolved version is the compatibility target. Inspect the version catalog or
dependency declaration, then the resolved dependency graph, including transitive modules or source
substitution when relevant. The sample and source checkout can be ahead of that version.

Look for a public component, provider, resource override, callback, theme, route or configuration
hook before adding a local implementation. Put host providers in the module that owns their
product behavior and register them in the app's composition root. Follow the target's existing
graph and DI setup. Check that destinations are registered and required providers are bound.

When Toolkit source is absent but the dependency is installed, use the architecture and screen references to design the feature. IDE
declarations, resolved artifacts and versioned API documentation establish which signatures can be
called. If the required API is missing, explain the limitation and use a supported extension point
where one fits. Consider a dependency upgrade only within task scope.

## Example: a host asks for a FAQ

Search for FAQ and Help in the host and its resolved Toolkit API. In the Toolkit checkout,
`library/feature/faq/README.md` documents `FaqScreen`, `HelpRoute`, `helpPage()` and
`faqModule`. The assembled graph and sample show how the route and bindings fit together.
The existing feature already owns catalog retrieval, fallback, UI state, reporting and retry.

If the resolved version exposes that integration, reuse the registered Help destination or wire
it through the supported graph entry point. Follow that version's host requirements for the
catalog, localized fallback strings and ad configuration. Use `android-localization` if changing
those resources and `navigation-3` if changing the graph. Verify that Help opens with the correct
content and that the fallback works. A request for FAQ content alone needs no new ViewModel,
repository, use case or DataStore.
