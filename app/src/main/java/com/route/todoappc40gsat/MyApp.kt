package com.route.todoappc40gsat

import android.app.Application
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import com.route.todoappc40gsat.utilts.Constants
import com.route.todoappc40gsat.utilts.applyModeChange

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        setNightMode()
    }

    private fun setNightMode() {
        val isDark = getSharedPreferences(Constants.SETTING_SHARED_PREFERENCES_NAME, MODE_PRIVATE).getBoolean(Constants.DARK_MODE_KEY,getDeviceModeState())
        applyModeChange(isDark)
    }

    private fun getDeviceModeState():Boolean{
        val currentNightMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK
        return currentNightMode == UI_MODE_NIGHT_YES
    }
}