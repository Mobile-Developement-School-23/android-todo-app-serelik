package com.serelik.todoapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.data.local.ThemeStorage
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.databinding.FragmentSettingsBinding
import com.serelik.todoapp.extensions.getStringRes
import com.serelik.todoapp.model.ThemeType
import com.serelik.todoapp.ui.MainActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val tokenStorage by lazy { TokenStorage(requireContext()) }
    private val themeStorage by lazy { ThemeStorage(requireContext()) }

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ThemeBottomSheetFragment.setFragmentResult(this, ::setNewTheme)

        binding.toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.textViewCurrentTheme.text = getString(themeStorage.getTheme().getStringRes())

        binding.textViewLogout.setOnClickListener {
            logout()
        }

        binding.textViewCurrentTheme.setOnClickListener { showBottomSheet() }
    }

    private fun setNewTheme(themeType: ThemeType) {
        themeStorage.saveTheme(themeType)

        val activity = activity as? MainActivity
        activity?.changeTheme(themeType)
        binding.textViewCurrentTheme.text = getString(themeStorage.getTheme().getStringRes())
    }

    private fun showBottomSheet() {
        ThemeBottomSheetFragment().show(supportFragmentManager, "bottom sheet")
    }

    private fun logout() {
        tokenStorage.removeToken()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
