############################################################
# App release ProGuard/R8 rules
############################################################

# 1) Crashlytics: keep file/line numbers for readable crash reports
-keepattributes SourceFile,LineNumberTable

# 2) Kotlinx Serialization (your app also uses kotlinx.serialization via AppToolkit)
-keepclassmembers @kotlinx.serialization.Serializable class com.d4rk.android.apps.** {
    public static ** Companion;
}
-keepclassmembers class com.d4rk.android.apps.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class **$$serializer { *; }
