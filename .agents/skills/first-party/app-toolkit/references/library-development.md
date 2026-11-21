# Library development

Read the owning module's README and inspect its dependencies, public signatures, Koin bindings,
and route registration before editing. Place behavior in the module that owns it. The assembled
Toolkit graph wires feature modules and public host contracts. It does not own their implementations.

Prefer an internal ViewModel, event, state and implementation when the host only opens a public
screen or route. Preserve a documented public ViewModel and its exposed types unless the task
authorizes an API change. Kotlin visibility alone does not establish what a published version
supports. Check publishing configuration, the artifact, and compatibility checks when relevant.

Inspect host-facing defaults, persisted identifiers, and telemetry keys before changing them.
For a deliberate API or integration change, update the owning README and consumer-facing changelog,
and check dependent modules. Keep product policy and host-specific strings or integrations in the host.
