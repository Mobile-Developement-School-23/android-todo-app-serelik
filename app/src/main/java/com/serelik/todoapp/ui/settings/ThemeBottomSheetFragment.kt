package com.serelik.todoapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.BottomSheetThemeBinding
import com.serelik.todoapp.model.ThemeType

class ThemeBottomSheetFragment : BottomSheetDialogFragment(R.layout.bottom_sheet_theme) {
    private val binding by viewBinding(BottomSheetThemeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.TextViewThemeTypeSystem.setOnClickListener {
            setFragmentResult(ThemeType.SYSTEM)
        }

        binding.TextViewThemeTypeDark.setOnClickListener {
            setFragmentResult(ThemeType.DARK)
        }

        binding.TextViewThemeTypeLight.setOnClickListener {
            setFragmentResult(ThemeType.LIGHT)
        }
    }

    private fun setFragmentResult(result: ThemeType) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(EXTRA_KEY to result.name)
        )
        dismiss()
    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY_Settings"
        const val EXTRA_KEY = "EXTRA_KEY_Settings"

        fun setFragmentResult(
            fragment: Fragment,
            action: (themeType: ThemeType) -> Unit
        ) {
            fragment.setFragmentResultListener(REQUEST_KEY) { key, bundle ->
                val themeTypeName =
                    bundle.getString(EXTRA_KEY) ?: return@setFragmentResultListener
                val themeType = ThemeType.valueOf(themeTypeName)
                action(themeType)
            }
        }
    }
}
