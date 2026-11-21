# Consumer integration

The sample demonstrates how a host assembles the Toolkit and implements its extension points.
A consumer's resolved version is the compatibility target. Inspect the version catalog or
dependency declaration, then the resolved dependency graph, including transitive modules or source
substitution when relevant. The sample and source checkout can be ahead of that version.

Look for a public component, provider, resource override, callback, theme, route or configuration
hook before adding a local implementation. Put host providers in the module that owns their
product behavior and register them in the app's composition root. Follow the target's existing
graph and DI setup. Check that destinations are registered and required providers are bound.

When source is absent, use the architecture and screen references to design the feature. IDE
declarations, resolved artifacts and versioned API documentation establish which signatures can be
called. If the required API is missing, explain the limitation and use a supported extension point
where one fits. Consider a dependency upgrade only within task scope.
