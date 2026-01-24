############################################################
# AppToolkit consumer rules
############################################################

# 1) Kotlinx Serialization
-keepclassmembers @kotlinx.serialization.Serializable class com.d4rk.android.libs.apptoolkit.** {
    public static ** Companion;
}
-keepclassmembers class com.d4rk.android.libs.apptoolkit.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class **$$serializer { *; }
