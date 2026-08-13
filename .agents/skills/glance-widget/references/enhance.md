This guide includes details for optional widget enhancements that are straightforward to implement and improve your users' widget experience.

## Add a name to your widget

Widgets need to have a unique name when they are displayed in the widget picker.

Widgets' names are loaded from the `label` attribute of the widget's `receiver` element in the AndroidManifest.xml file.

    <receiver
        ....
       android:label="Memories">
         ....
    </receiver>

## Add a description for your widget

Starting in Android 12, provide a description for the widget picker to display for your widget.
![A widget picker showing a widget and its description](https://developer.android.com/static/images/appwidgets/description.png) **Figure 1.** Sample widget picker showing a widget and its description.

Provide a description for your widget using the `description` attribute of the `&lt;appwidget-provider&gt;` element:

    <appwidget-provider
        android:description="@string/my_widget_description">
    </appwidget-provider>

> [!NOTE]
> **Note:** Be concise. There is no character limit, but the representation and available space for the description might differ depending on the device.

You can use the [`descriptionRes`](https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo#descriptionRes) attribute on previous versions of Android, but it is ignored by the
widget picker.