package com.route.todoappc40gsat.utilts

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

fun getCurrentDeviceLanguage(context: Context):String {
    return context.resources.configuration.locales[0].language
}

fun applyModeChange(isDark: Boolean) {
    if (isDark)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}