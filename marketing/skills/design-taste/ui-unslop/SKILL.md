---
name: ui-unslop
description: Remove formulaic AI-generated styling from existing web UI. Use when a page, component, or layout looks generic, templated, over-decorated, vibe-coded, or needs a final design cleanup while preserving behavior, brand, and accessibility.
---

# UI Unslop

Finish the cleanup in one invocation. Work across the whole requested surface, render the result, and close every fixable gap before returning. Preserve product meaning and behavior. Do not preserve a generic visual treatment merely because the text inside it is useful.

## 1. Read the surface

Inspect the rendered UI, the implementation, and any brand or design-system evidence. If the project can run, capture the current surface before editing. Read enough of the surrounding code to avoid local fixes that fight the rest of the product.

Write down, for your own working state:

- the surface type and its one job
- the primary, secondary, and tertiary information or actions
- the content model: sequence, comparison, collection, explanation, evidence, or transaction
- the visual rules already owned by the product
- the repeated visual rules that make unrelated content look the same

If the surface has no supported visual direction, derive one from its subject, audience, and content. State it as one sentence about what the interface must communicate, not as a style label. Precision is a valid direction; do not add a signature effect when none is earned.

Completion criterion: the job, hierarchy, content model, product-owned rules, and generic repeated rules are explicit.

## 2. Audit before editing

Read [the audit catalog](references/pattern-catalog.md). Inspect both pixels and source across the entire requested surface. Check three levels:

1. **Structure:** page template, section order, repeated openings, content representation, and density.
2. **System:** typography, color, spacing, radius, borders, shadows, imagery, copy, and motion.
3. **Instances:** every catalog pattern and every occurrence of each repeated treatment.

Apply the earning test to each visible choice:

1. What information, interaction, hierarchy, or brand rule does this treatment encode?
2. Would removing the treatment make the interface harder to understand or use?
3. Is the treatment repeated only where that same meaning repeats?
4. For a stylistic choice rather than a standard control or signifier, would the same rationale fit an unrelated product? If so, it is generic rather than product-specific.

Separate content from treatment. A useful label may stay while its pill, uppercase tracking, tint, icon box, or repeated placement goes. A truthful statistic may stay while its generic four-card strip goes.

Do not edit until every recurring treatment and every applicable catalog row has a keep, replace, or remove decision. Keep requires concrete product, content, interaction, or brand evidence. Familiarity and polish are not evidence.

Completion criterion: the audit covers the full surface and identifies causes, not only conspicuous components.

## 3. Rebuild the hierarchy

Fix high-level causes before local symptoms:

1. Match the structure to the content model. Use aligned rows or tables for comparison, ordered steps for a real sequence, prose for explanation, charts and direct labels for evidence, and cards only for self-contained units that benefit from enclosure.
2. Make primary, secondary, and tertiary roles perceptibly different through placement, scale, contrast, and grouping.
3. Reduce the visual system to the rules the product needs. Reuse valid tokens; consolidate accidental variants.
4. Remove decorative framing before restyling it. Replace it only when the content needs a different structure or signifier.
5. Keep one coherent product-owned idea if the evidence supports it. Let the content and data provide variety; do not manufacture variety with alternating templates.

Do not swap into another stock look. Warm cream with a display serif, dark canvas with an acid accent, and broadsheet rules are as generic as purple gradients when they do not follow from the product. A restrained interface can still be specific through its content structure, typography, proportions, and exact details.

Make all related changes in one coherent implementation pass. Preserve routes, data, state, semantics, keyboard behavior, and truthful content. Do not add dependencies or assets unless the result requires them and the user authorized the scope.

Completion criterion: every changed visual rule follows from the surface's job, hierarchy, content, interaction, or established brand.

## 4. Prove the result

Render the changed surface at its relevant desktop width and at 320 CSS pixels. Include other documented breakpoints and important interaction states. Compare the before and after at the same sizes.

Run these gates:

- **First glance:** blur or squint at the screenshot. The intended primary element wins, supporting levels remain distinct, and repeated containers do not flatten the page.
- **Representation:** each section uses the structure that best supports its content. Enclosure, numbering, icons, statistics, and controls carry real meaning.
- **Residual:** scan the full rendered surface from top to bottom and re-scan source. No unjustified catalog occurrence or repeated generic treatment remains.
- **Second-order default:** the result has not traded the original slop for a current design trend. Its main stylistic decisions cannot be pasted unchanged onto an unrelated product.
- **Restraint:** removing any remaining decorative element would now lose useful hierarchy, meaning, interaction feedback, or brand identity.
- **Quality floor:** core actions work; content reflows without loss or horizontal page scrolling; focus is visible; text and controls have sufficient contrast; and reduced-motion behavior works.

Any failed gate reopens the work. Fix all failures, render again, and rerun the gates within this invocation. Do not return a first-pass result with fixable visual findings. If rendering is impossible, complete the source audit and state exactly which visual gates remain unverified. If a blocker prevents a fix, name the blocker and the remaining occurrence.

Completion criterion: every gate passes, or the only open items are blocked and reported with evidence.

## Output

Report only:

- the structural and system causes removed
- the product or content logic that replaced them
- the valid treatments intentionally kept and their evidence
- the viewports and states verified

Do not dump the working audit unless the user asks for it.
