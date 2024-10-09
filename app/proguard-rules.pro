# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep the AppModule class for Hilt dependency injection
-keep class re.recodestudios.clock_app.di.AppModule{ *; }

# Keep data layer classes
-keep class re.recodestudios.clock_app.feature_clock.data.data_source.** { *; } # Data sources
-keep class re.recodestudios.clock_app.feature_clock.data.repository.** { *; } # Repositories
-keep class re.recodestudios.clock_app.feature_clock.data.scheduler.** { *; } # Schedulers

# Keep domain layer classes
-keep class re.recodestudios.clock_app.feature_clock.domain.model.** { *; } # Domain models
-keep class re.recodestudios.clock_app.feature_clock.domain.repository.** { *; } # Repository interfaces
-keep class re.recodestudios.clock_app.feature_clock.domain.use_case.** { *; } # Use cases

# Keep presentation layer ViewModels
-keep class re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components.AddAlarmViewModel { *; }
-keep class re.recodestudios.clock_app.feature_clock.presentation.alarm.components.AlarmsViewModel { *; }

# Keep MainActivity and AlarmApp
-keep class re.recodestudios.clock_app.feature_clock.presentation.MainActivity { *; }
-keep class re.recodestudios.clock_app.AlarmApp { *; }

 # Keep file R and Manifest
    -keep class **.R
    -keep class **.R$* {*;}
    -keep class **.BuildConfig {*;}
    -keep class **.Manifest {*;}

# Keep Room components
-keep class androidx.room.** { *; } # Room database classes
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public ** getOpenHelper();
    public ** getDatabase();
}
-keep @androidx.room.Entity class * { *; } # Room entities
-keep @androidx.room.Dao class * { *; } # Room DAOs
-keep @androidx.room.DatabaseView class * { *; } # Room database views
-keep @androidx.room.TypeConverters class * { *; } # Room type converters

# Keep external libraries (Bouncy Castle, Conscrypt, OpenJSSE)
-keep class org.bouncycastle.jsse.** { *; }
-dontwarn org.bouncycastle.jsse.**

-keep class org.conscrypt.** { *; }
-dontwarn org.conscrypt.**

-keep class org.openjsse.** { *; }
-dontwarn org.openjsse.**

# Keep Hilt components and annotations
-keepattributes *Annotation* # Preserve annotations
-keep class dagger.hilt.** { *; } # Hilt classes
-keep class javax.inject.** { *; } # javax.inject classes
-keep class javax.annotation.** { *; } # javax.annotation classes

-keepclassmembers class * {
    @dagger.hilt.InstallIn <methods>; # Keep methods annotated with @InstallIn
}

-keepclassmembers class * {
    @dagger.hilt.android.HiltAndroidApp <methods>; # Keep methods annotated with @HiltAndroidApp
}

-keep class dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; } # Keep HiltViewModelFactory
-keep class dagger.hilt.android.components.** { *; } # Keep Hilt components

# Keep Hilt generated classes
-keep class re.recodestudios.clock_app.*Hilt* { *; }
-keep,allowobfuscation,allowshrinking @dagger.hilt.EntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.EarlyEntryPoint class *