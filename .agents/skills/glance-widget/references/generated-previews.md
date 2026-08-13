Generated widget previews allow you to create dynamic, personalized previews for your widgets that accurately reflect how they will appear on a user's home screen. They are provided through a push
API, meaning your app provides the preview at any point during its lifecycle without receiving an explicit request from the widget host.

This guide covers how to provide previews for Glance-based widgets. If your widget is implemented with `RemoteViews`,
see [Add previews to your widget picker](https://developer.android.com/develop/ui/views/appwidgets/previews).

To improve your app's widget picker experience for Glance widgets, provide a generated widget preview using `GlanceAppWidget.providePreview` on Android 15 and later devices, and specify a [
`previewImage`](https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo#previewImage) for earlier versions, and as a fallback on Android 15+ if a generated preview isn't
available.

For more information, see [Enrich your app with live updates and widgets](https://www.youtube.com/watch?v=_Akf_u08p7U) on YouTube.

## Set up your app for generated widget previews

To show Generated Widget Previews on Android 15 or later device, first set the `compileSdk` value to 35 or later in the module `build.gradle` file to have the ability to provide [
`RemoteViews`](https://developer.android.com/reference/android/widget/RemoteViews) to the widget picker

Apps can then use `setWidgetPreview` in `GlanceAppWidgetManager`. To prevent abuse and mitigate system health concerns, `setWidgetPreview` is a rate-limited API. The default limit is approximately two
calls per hour.

## Generate updated preview with Jetpack Glance

For widgets built with Jetpack Glance, do the following:

1. Override the `GlanceAppWidget.providePreview` function to provide the composable content for the preview. As you would in `provideGlance`, load your app's data and pass it to the widget's content
   composable, to make the preview show accurate data. Unlike `provideGlance`, this is a single composition with no recomposition or effects.

2. Call `GlanceAppWidgetManager.setWidgetPreviews` to generate and publish the preview.

There isn't a callback from the system to provide previews, so your app must decide when to call `setWidgetPreviews`. The update strategy depends on your widget's use case:

- If the widget has static information or is a quick action, set the preview when the app is first launched.
- You can set the preview once your app has data; for example, after a user sign-in or initial setup.
- You can set up a periodic task to update the previews at a chosen cadence.

### Troubleshoot Generated Previews

A common issue is that after you generate a preview, images, icons, or other composables might be missing from the preview image, relative to the widget's drop size. This drop size is defined by the
`targetCellWidth` and `targetCellHeight` if specified, or by the `minWidth` and `minHeight` in
the [app widget provider info file](https://developer.android.com/develop/ui/views/appwidgets#widget-sizing-attributes).

This occurs because Android, by default, renders only composables visible at the widget's minimum size. In other words, Android sets the `previewSizeMode` to `SizeMode.Single` by default. It uses
`android:minHeight` and `android:minWidth` in the [app widget provider info XML](https://developer.android.com/develop/ui/views/appwidgets#widget-sizing-attributes) to determine which composables to
draw.

To fix this, override `previewSizeMode` in your `GlanceAppWidget` and set it to `SizeMode.Responsive`, providing a set of `DpSize` values. This tells Android all the layout sizes it needs to render
for the preview, which ensures all elements display correctly.

Optimize for specific form factors. Supply one or two sizes starting from the minimum and following your widget's breakpoints. Specify at least one
`previewImage` [for backward compatibility](https://developer.android.com/develop/ui/compose/glance/generated-previews#bc-previews). You can find the appropriate minimum DP values for different grid
sizes in the [widget design guidance](https://developer.android.com/design/ui/mobile/guides/widgets/sizing).

> [!NOTE]
> **Note:** Prefer `SizeMode.Exact` over `SizeMode.Responsive` when sizing your widget (not the preview).
> See [the documentation](https://developer.android.com/develop/ui/compose/glance/build-ui#define-sizemode) for an overview of `SizeMode`.

## Backward compatibility with widget previews

To let widget pickers on devices running versions lower than Android 15 show previews of your widget, or as a fallback for Generated previews on Android 15+, specify the [
`previewImage`](https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo#previewImage) attribute.

If you change the widget's appearance, update the preview image.