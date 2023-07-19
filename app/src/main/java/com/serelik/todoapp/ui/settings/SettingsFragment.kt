package com.serelik.todoapp.ui.settings

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.R
import com.serelik.todoapp.data.local.ThemeStorage
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.databinding.FragmentSettingsBinding
import com.serelik.todoapp.di.SettingsFragmentComponent
import com.serelik.todoapp.extensions.getStringRes
import com.serelik.todoapp.model.ThemeType
import com.serelik.todoapp.notification.ReminderManager
import com.serelik.todoapp.ui.MainActivity
import javax.inject.Inject

class SettingsFragment() : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val tokenStorage by lazy { TokenStorage(requireContext()) }
    private val themeStorage by lazy { ThemeStorage(requireContext()) }

    private lateinit var component: SettingsFragmentComponent

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    private var timeSetListener = OnTimeSetListener { view, hours, minutes ->

        val deadline = hours * 60 + minutes
        viewModel.save(deadline)
        formatTime(hours, minutes)
    }

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .settingsFragmentComponent().create()

        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTimeNotification()

        ThemeBottomSheetFragment.setFragmentResult(this, ::setNewTheme)

        binding.toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        binding.textViewCurrentTheme.text = getString(themeStorage.getTheme().getStringRes())

        binding.textViewLogout.setOnClickListener {
            logout()
        }

        binding.textViewCurrentTheme.setOnClickListener { showBottomSheet() }

        setupSwitch()

    }

    private fun setupSwitch() {
        binding.switchIsEnabledDeadline.setOnCheckedChangeListener { switchView, isChecked ->
            if (!switchView.isPressed) {
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                showTimePicker()
            } else {
                binding.textViewDeadlineNotification.text = null
                viewModel.save(ReminderManager.DO_NOT_NOTIFY)
            }
        }
    }

    private fun setNewTheme(themeType: ThemeType) {
        themeStorage.saveTheme(themeType)

        val activity = activity as? MainActivity
        activity?.changeTheme(themeType)
        binding.textViewCurrentTheme.text = getString(themeStorage.getTheme().getStringRes())
    }

    private fun showBottomSheet() {
        ThemeBottomSheetFragment().show(supportFragmentManager, BOTTOM_SHEET)
    }

    private fun logout() {
        tokenStorage.removeToken()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    private fun showTimePicker() {
        val notificationTime = if (viewModel.getNewDeadlineTimeNotification() >= 0)
            viewModel.getNewDeadlineTimeNotification()
        else
            0

        val hours = notificationTime / 60
        val minutes = notificationTime % 60

        val dialog = TimePickerDialog(
            requireContext(),
            timeSetListener,
            hours,
            minutes,
            true
        )
        dialog.setOnCancelListener { viewModel.save(ReminderManager.DO_NOT_NOTIFY) }
        dialog.show()
    }

    private fun setupTimeNotification() {
        if (viewModel.getNewDeadlineTimeNotification() != -1) {
            binding.switchIsEnabledDeadline.isChecked = true
            val time = viewModel.getNewDeadlineTimeNotification()
            val hours = time / 60
            val minutes = time % 60
            formatTime(hours, minutes)
        } else {
            binding.switchIsEnabledDeadline.isChecked = false

        }
    }

    private fun formatTime(hours: Int, minutes: Int) {

        binding.textViewDeadlineNotification.text = String.format("%02d:%02d", hours, minutes)
    }

    companion object {
        const val BOTTOM_SHEET = "BOTTOM_SHEET"
    }
}
