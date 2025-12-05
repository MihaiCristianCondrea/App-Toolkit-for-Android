# Preserve runtime annotations relied on by downstream apps for reflection or serialization.
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations

# Keep Android entry points defined by the library so they remain discoverable by the framework.
-keep class com.d4rk.android.libs.apptoolkit.** extends android.app.Activity
-keep class com.d4rk.android.libs.apptoolkit.** extends android.app.Service
-keep class com.d4rk.android.libs.apptoolkit.** extends android.content.BroadcastReceiver
-keep class com.d4rk.android.libs.apptoolkit.** extends android.content.ContentProvider
-keep class com.d4rk.android.libs.apptoolkit.** extends android.app.Application