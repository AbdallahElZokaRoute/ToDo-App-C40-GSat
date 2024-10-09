package com.route.todoappc40gsat.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.route.todoappc40gsat.R
import com.route.todoappc40gsat.databinding.FragmentSettingsBinding
import com.route.todoappc40gsat.utilts.Constants
import com.route.todoappc40gsat.utilts.applyModeChange
import com.route.todoappc40gsat.utilts.getCurrentDeviceLanguage

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var SettingShared:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(getString(R.string.settings))

        SettingShared = requireContext().getSharedPreferences(Constants.SETTING_SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)

        setInitialLanguageState()
        setInitialModeState()

        initLanguageViewAdapter()
        initModeViewAdapter()

        onClickToChangeLanguage()
        onClickToChangeMode()
    }

    override fun onStart() {
        super.onStart()
        initLanguageViewAdapter()
        initModeViewAdapter()
    }

    private fun onClickToChangeMode() {
        binding.autoCompleteMode.setOnItemClickListener { adapterView, view, index, id ->
            val selectedMode = adapterView.getItemAtPosition(index).toString()
            binding.autoCompleteMode.setText(selectedMode)
            val isDark = (selectedMode == getString(R.string.dark))
            applyModeChange(isDark)
            val editMode = SettingShared.edit()
            editMode.apply{
                putBoolean(Constants.SETTING_SHARED_PREFERENCES_NAME,isDark)
                apply()
            }
        }
    }



    private fun onClickToChangeLanguage() {
        binding.autoCompleteLanguage.setOnItemClickListener { adapterView, view, index, id ->
            val selectedLanguage = adapterView.getItemAtPosition(index).toString()
            binding.autoCompleteLanguage.setText(selectedLanguage)
            val languageCode = if(selectedLanguage == getString(R.string.english))
                Constants.ENGLISH_CODE
            else
                Constants.ARABIC_CODE

            applyLanguageChange(languageCode)

        }
    }

    private fun applyLanguageChange(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        activity?.recreate()
//        activity?.let {
//            it.finish()
//            it.startActivity(it.intent)
//        }
    }

    private fun initModeViewAdapter() {
        val mode = resources.getStringArray(R.array.mode).toList()
        val modeAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,mode)
        binding.autoCompleteMode.setAdapter(modeAdapter)
    }

    private fun initLanguageViewAdapter() {
        val language = resources.getStringArray(R.array.language).toList()
        val languagesAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,language)
        binding.autoCompleteLanguage.setAdapter(languagesAdapter)
    }

    private fun setInitialModeState() {
        val currentMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK
        if (currentMode == UI_MODE_NIGHT_NO){
            binding.autoCompleteMode.setText(getString(R.string.light))
            changeModeIcon(Constants.LIGHT)
        }else{
            binding.autoCompleteMode.setText(getString(R.string.dark))
            changeModeIcon(Constants.DARK)
        }
    }

    private fun changeModeIcon(Mode: String) {
        if (Mode == Constants.LIGHT){
            binding.modeDropdown.setStartIconDrawable(R.drawable.sunny)
        }else{
            binding.modeDropdown.setStartIconDrawable(R.drawable.night)
        }
    }

    private fun setInitialLanguageState() {
       val currentLocal = AppCompatDelegate.getApplicationLocales()[0]?.language ?: getCurrentDeviceLanguage(requireContext())
        var language = if(currentLocal == Constants.ENGLISH_CODE) getString(R.string.english) else getString(R.string.arabic)
        binding.autoCompleteLanguage.setText(language)
    }


}
