##############################################
# App - ProGuard / R8 rules
##############################################

# Crashlytics: keep line numbers for readable crashes
-keepattributes SourceFile,LineNumberTable

# Kotlinx Serialization
-keepattributes *Annotation*,InnerClasses,EnclosingMethod

-keepclassmembers @kotlinx.serialization.Serializable class com.d4rk.android.apps.** {
    public static ** Companion;
}

-keepclassmembers class com.d4rk.android.apps.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class **$$serializer { *; }

# Optional: reduce annoying warnings if they appear (not required)
-dontwarn kotlinx.coroutines.**
-dontwarn io.ktor.**
