# Preserve the public API surface for consumers while allowing internal code to be optimized.
-keep class com.d4rk.android.libs.apptoolkit.** { public protected *; }

# Keep annotations and metadata that are required by Compose, serialization, and DI at runtime.
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeInvisibleAnnotations,RuntimeInvisibleParameterAnnotations,AnnotationDefault,Signature,InnerClasses,EnclosingMethod