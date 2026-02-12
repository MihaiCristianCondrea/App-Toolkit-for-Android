##############################################
# AppToolkit - consumer rules
##############################################

# Kotlinx Serialization: keep generated serializers + companions
-keepattributes InnerClasses,EnclosingMethod

-keep @kotlinx.serialization.Serializable class com.d4rk.android.libs.apptoolkit.** { *; }

-keepclassmembers class com.d4rk.android.libs.apptoolkit.** {
    public static ** Companion;
}

-keepclassmembers class **$Companion {
    public kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class **$$serializer { *; }
