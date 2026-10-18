package com.example.to_dolist.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val PrimaryLight = Color(0xFF6750A4)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFEADDFF)
val OnPrimaryContainerLight = Color(0xFF21005D)

val SecondaryLight = Color(0xFF625B71)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE8DEF8)
val OnSecondaryContainerLight = Color(0xFF1D192B)

val TertiaryLight = Color(0xFF7D5260)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFD8E4)
val OnTertiaryContainerLight = Color(0xFF31111D)

val BackgroundLight = Color(0xFFFEF7FF)
val OnBackgroundLight = Color(0xFF1D1B20)
val SurfaceLight = Color(0xFFFEF7FF)
val OnSurfaceLight = Color(0xFF1D1B20)
val SurfaceVariantLight = Color(0xFFE7E0EC)
val OnSurfaceVariantLight = Color(0xFF49454F)

val PrimaryDark = Color(0xFFD0BCFF)
val OnPrimaryDark = Color(0xFF381E72)
val PrimaryContainerDark = Color(0xFF4F378B)
val OnPrimaryContainerDark = Color(0xFFEADDFF)

val SecondaryDark = Color(0xFFCCC2DC)
val OnSecondaryDark = Color(0xFF332D41)
val SecondaryContainerDark = Color(0xFF4A4458)
val OnSecondaryContainerDark = Color(0xFFE8DEF8)

val TertiaryDark = Color(0xFFEFB8C8)
val OnTertiaryDark = Color(0xFF492532)
val TertiaryContainerDark = Color(0xFF633B48)
val OnTertiaryContainerDark = Color(0xFFFFD8E4)

val BackgroundDark = Color(0xFF141218)
val OnBackgroundDark = Color(0xFFE6E1E5)
val SurfaceDark = Color(0xFF141218)
val OnSurfaceDark = Color(0xFFE6E1E5)
val SurfaceVariantDark = Color(0xFF49454F)
val OnSurfaceVariantDark = Color(0xFFCAC4D0)

// Accent Colors for Customization
// Blue
val BluePrimaryLight = Color(0xFF0061A4)
val BluePrimaryContainerLight = Color(0xFFD1E4FF)
val BluePrimaryDark = Color(0xFF9ECAFF)
val BluePrimaryContainerDark = Color(0xFF00497D)

// Green
val GreenPrimaryLight = Color(0xFF006D3B)
val GreenPrimaryContainerLight = Color(0xFF8AF8BE)
val GreenPrimaryDark = Color(0xFF68D89C)
val GreenPrimaryContainerDark = Color(0xFF00522B)

// Orange
val OrangePrimaryLight = Color(0xFF8F4C36)
val OrangePrimaryContainerLight = Color(0xFFFFDBD0)
val OrangePrimaryDark = Color(0xFFFFB59D)
val OrangePrimaryContainerDark = Color(0xFF713521)

// Teal
val TealPrimaryLight = Color(0xFF006874)
val TealPrimaryContainerLight = Color(0xFF97F0FF)
val TealPrimaryDark = Color(0xFF4FD8EB)
val TealPrimaryContainerDark = Color(0xFF004F58)

// Pink
val PinkPrimaryLight = Color(0xFF984061)
val PinkPrimaryContainerLight = Color(0xFFFFD9E2)
val PinkPrimaryDark = Color(0xFFFFB1C8)
val PinkPrimaryContainerDark = Color(0xFF7C2949)

enum class ThemeAccent {
    DEFAULT,
    BLUE,
    PURPLE,
    GREEN,
    ORANGE,
    TEAL,
    PINK
}

enum class ThemeModeOption {
    LIGHT,
    DARK,
    SYSTEM,
    TRANSLUCENT
}

enum class WallpaperOption(val title: String, val brush: Brush) {
    PURPLE_BLUE(
        "Purple & Blue",
        Brush.linearGradient(listOf(Color(0xFF2E0854), Color(0xFF1E3C72), Color(0xFF2A5298)))
    ),
    SUNSET_ORANGE(
        "Sunset Glow",
        Brush.linearGradient(listOf(Color(0xFF4A154B), Color(0xFFFF5E62), Color(0xFFFF9966)))
    ),
    EMERALD_TEAL(
        "Emerald Teal",
        Brush.linearGradient(listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)))
    ),
    MIDNIGHT_SPACE(
        "Midnight Space",
        Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF0F2027), Color(0xFF1A1A2E)))
    ),
    ABSTRACT_GLOW(
        "Abstract Glow",
        Brush.linearGradient(listOf(Color(0xFF3A1C71), Color(0xFFD76D77), Color(0xFFFFAF7B)))
    )
}
