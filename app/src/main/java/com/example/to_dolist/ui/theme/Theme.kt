package com.example.to_dolist.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark
)

val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun TODOListTheme(
    themeAccent: ThemeAccent = ThemeAccent.DEFAULT,
    themeMode: ThemeModeOption = ThemeModeOption.SYSTEM,
    wallpaperOption: WallpaperOption = WallpaperOption.PURPLE_BLUE,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeModeOption.LIGHT -> false
        ThemeModeOption.DARK -> true
        ThemeModeOption.TRANSLUCENT -> true
        ThemeModeOption.SYSTEM -> systemDark
    }

    val baseColorScheme = when {
        themeAccent == ThemeAccent.DEFAULT && isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        themeAccent == ThemeAccent.DEFAULT && !isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDark -> {
            when (themeAccent) {
                ThemeAccent.BLUE -> DarkColorScheme.copy(primary = BluePrimaryDark, primaryContainer = BluePrimaryContainerDark)
                ThemeAccent.GREEN -> DarkColorScheme.copy(primary = GreenPrimaryDark, primaryContainer = GreenPrimaryContainerDark)
                ThemeAccent.ORANGE -> DarkColorScheme.copy(primary = OrangePrimaryDark, primaryContainer = OrangePrimaryContainerDark)
                ThemeAccent.TEAL -> DarkColorScheme.copy(primary = TealPrimaryDark, primaryContainer = TealPrimaryContainerDark)
                ThemeAccent.PINK -> DarkColorScheme.copy(primary = PinkPrimaryDark, primaryContainer = PinkPrimaryContainerDark)
                else -> DarkColorScheme
            }
        }
        else -> {
            when (themeAccent) {
                ThemeAccent.BLUE -> LightColorScheme.copy(primary = BluePrimaryLight, primaryContainer = BluePrimaryContainerLight)
                ThemeAccent.GREEN -> LightColorScheme.copy(primary = GreenPrimaryLight, primaryContainer = GreenPrimaryContainerLight)
                ThemeAccent.ORANGE -> LightColorScheme.copy(primary = OrangePrimaryLight, primaryContainer = OrangePrimaryContainerLight)
                ThemeAccent.TEAL -> LightColorScheme.copy(primary = TealPrimaryLight, primaryContainer = TealPrimaryContainerLight)
                ThemeAccent.PINK -> LightColorScheme.copy(primary = PinkPrimaryLight, primaryContainer = PinkPrimaryContainerLight)
                else -> LightColorScheme
            }
        }
    }

    // If Translucent theme, make surfaces semi-transparent for glassmorphism effect
    val colorScheme = if (themeMode == ThemeModeOption.TRANSLUCENT) {
        baseColorScheme.copy(
            surface = Color.Transparent,
            surfaceVariant = baseColorScheme.surfaceVariant.copy(alpha = 0.25f),
            background = Color.Transparent
        )
    } else {
        baseColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            if (themeMode == ThemeModeOption.TRANSLUCENT) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(wallpaperOption.brush)
                ) {
                    content()
                }
            } else {
                content()
            }
        }
    )
}
