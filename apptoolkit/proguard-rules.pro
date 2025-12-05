# Preserve the public API surface for consumers while allowing the library implementation
# to be optimized. Only manifest-registered Android components need to retain their
# names; other classes can be obfuscated safely during the release build.
-keep class com.d4rk.android.libs.apptoolkit.app.** extends android.app.Activity
-keep class com.d4rk.android.libs.apptoolkit.core.services.FirebaseNotificationsService

# Keep annotations and metadata that are required by Compose, serialization, and DI at runtime.
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeInvisibleAnnotations,RuntimeInvisibleParameterAnnotations,AnnotationDefault,Signature,InnerClasses,EnclosingMethod
