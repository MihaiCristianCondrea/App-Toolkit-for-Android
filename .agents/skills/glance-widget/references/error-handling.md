API features for improving error handling on Glance are included beginning in Android 15. This page provides best practices regarding these APIs.

## Use a try-catch block around non-composable components

Compose doesn't allow try-catch blocks around composables, but lets you wrap your app's other logic in these blocks. This lets you use Compose for your error view, as shown in the following example:

    provideContent {
           var isError = false;
           var data = null
           try {
               val repository = (context.applicationContext as MyApplication).myRepository
               data = repository.loadData()
           } catch (e: Exception) {
               isError = true;
               //handleError
           }

           if (isError) {
               ErrorView()
           } else {
               Content(data)
           }
       }

## Default error layout

If there is an uncaught exception or a Compose error, Glance displays a default error layout:
![An error message showing the type of error and a suggestion for where
to look for it](https://developer.android.com/static/develop/ui/compose/images/default_error_layout.png) **Figure 1.**Glance 1.0 default error
layout ![A box with the text 'Can't show content'](https://developer.android.com/static/develop/ui/compose/images/new_error_layout.png) **Figure 2.**Glance 1.1.0 default error layout

Glance lets developers provide an XML layout as a fallback if composition fails. This means that there was an error in the Compose code. This error UI also appears if you have an uncaught error in
your app's code.

    class UpgradeWidget : GlanceAppWidget(errorUiLayout = R.layout.error_layout)

This layout is a static layout that your user can't interact with, but is good in emergency cases.
![Contains a heading and a text field to display an error message](https://developer.android.com/static/develop/ui/compose/images/custom_error_layout.png) **Figure 3.**Example custom error layout

## Add actions to the default error UI

As of Glance 1.1.0, Glance lets you override the default error handling code. This way, you can add action callbacks in the event of an uncaught exception or error in composition.

To use this feature, override the `onCompositionError()` function:

    GlanceAppWidget.onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    )

In this function, Glance falls back to the `RemoteViews` API for error handling. This lets you specify layouts and action handlers using XML.

The following examples show you, step-by-step, how to create an error UI that includes a button to send feedback:

1. Write the `error_layout.xml` file:

       <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
          style="@style/Widget.MyApplication.AppWidget.Error"
          android:id="@android:id/background"
          android:layout_width="match_parent"
          android:textSize="24sp"
          android:layout_height="match_parent"
          android:orientation="vertical">

          <TextView
              android:id="@+id/error_title_view"
              android:layout_width="match_parent"
              android:textColor="@color/white"
              android:textFontWeight="800"
              android:layout_height="wrap_content"
              android:text="Example Widget Error" />

          <LinearLayout
              android:layout_width="match_parent"
              android:orientation="horizontal"
              android:paddingTop="4dp"
              android:layout_height="match_parent">

              <ImageButton
               android:layout_width="64dp"
               android:layout_height="64dp"
               android:layout_gravity="center"
               android:tint="@color/white"
               android:id="@+id/error_icon"
               android:src="@drawable/heart_broken_fill0_wght400_grad0_opsz24"
              />
              <TextView
                  android:id="@+id/error_text_view"
                  android:layout_width="wrap_content"
                  android:textColor="@color/white"
                  android:layout_height="wrap_content"
                  android:layout_gravity="center"
                  android:padding="8dp"
                  android:textSize="16sp"
                  android:layout_weight="1"
                  android:text="Useful Error Message!" />
          </LinearLayout>

       </LinearLayout>

2. Override the `onCompositionError` function:

       override fun onCompositionError(
          context: Context,
          glanceId: GlanceId,
          appWidgetId: Int,
          throwable: Throwable
       ) {
          val rv = RemoteViews(context.packageName, R.layout.error_layout)
          rv.setTextViewText(
              R.id.error_text_view,
              "Error was thrown. \nThis is a custom view \nError Message: `${throwable.message}`"
          )
          rv.setOnClickPendingIntent(R.id.error_icon, getErrorIntent(context, throwable))
          AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rv)
       }

3. Create a pending intent that references your `GlanceAppWidgetReceiver`:

       private fun getErrorIntent(context: Context, throwable: Throwable): PendingIntent {
           val intent = Intent(context, UpgradeToHelloWorldPro::class.java)
           intent.setAction("widgetError")
           return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
       }

4. Handle the intent in your `GlanceAppWidgetReceiver`:

       override fun onReceive(context: Context, intent: Intent) {
          super.onReceive(context, intent)
          Log.e("ErrorOnClick", "Button was clicked.");
       }