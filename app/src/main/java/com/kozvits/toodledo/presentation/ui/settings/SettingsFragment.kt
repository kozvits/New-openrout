package com.kozvits.toodledo.presentation.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kozvits.toodledo.R
import com.kozvits.toodledo.databinding.FragmentSettingsBinding
import com.kozvits.toodledo.domain.model.SyncSettings
import com.kozvits.toodledo.domain.model.SyncState
import com.kozvits.toodledo.presentation.viewmodel.SyncViewModel
import com.kozvits.toodledo.util.formatDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SyncViewModel by viewModels()

    private val intervalOptions = listOf(15, 30, 60, 180, 360, 720)
    private val intervalLabels = listOf("15 min", "30 min", "1 hour", "3 hours", "6 hours", "12 hours")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerInterval.adapter = ArrayAdapter(
            requireContext(), R.layout.item_spinner_dark, intervalLabels)

        binding.btnSyncNow.setOnClickListener { viewModel.syncNow() }

        binding.switchAutoSync.setOnCheckedChangeListener { _, checked ->
            binding.layoutInterval.visibility = if (checked) View.VISIBLE else View.GONE
        }

        binding.btnSave.setOnClickListener { saveSettings() }

        observeViewModel()
    }

    private fun saveSettings() {
        val current = viewModel.settings.value ?: SyncSettings()
        val selectedInterval = intervalOptions[binding.spinnerInterval.selectedItemPosition]
        val newSettings = current.copy(
            autoSyncEnabled = binding.switchAutoSync.isChecked,
            syncIntervalMinutes = selectedInterval,
            apiLogin = binding.editApiLogin.text.toString().trim(),
            apiPassword = binding.editApiPassword.text.toString().trim()
        )
        viewModel.saveSettings(newSettings)

        // Обновить WorkManager
        val app = requireActivity().application as com.kozvits.toodledo.ToodledoApp
        if (newSettings.autoSyncEnabled) {
            app.scheduleSyncWorker(selectedInterval.toLong())
        } else {
            app.cancelSyncWorker()
        }
        Snackbar.make(binding.root, R.string.settings_saved, Snackbar.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.settings.observe(viewLifecycleOwner) { settings ->
            binding.switchAutoSync.isChecked = settings.autoSyncEnabled
            binding.layoutInterval.visibility = if (settings.autoSyncEnabled) View.VISIBLE else View.GONE
            val idx = intervalOptions.indexOf(settings.syncIntervalMinutes).coerceAtLeast(0)
            binding.spinnerInterval.setSelection(idx)
            binding.editApiLogin.setText(settings.apiLogin)
            binding.editApiPassword.setText(settings.apiPassword)
            binding.tvLastSync.text = if (settings.lastSyncTime > 0)
                getString(R.string.last_sync, formatDateTime(settings.lastSyncTime))
            else getString(R.string.never_synced)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.syncState.collect { state ->
                when (state) {
                    is SyncState.Running -> {
                        binding.btnSyncNow.isEnabled = false
                        binding.progressSync.visibility = View.VISIBLE
                        binding.tvSyncStatus.text = getString(R.string.syncing)
                    }
                    is SyncState.Success -> {
                        binding.btnSyncNow.isEnabled = true
                        binding.progressSync.visibility = View.GONE
                        binding.tvSyncStatus.text = getString(R.string.sync_success)
                        binding.tvLastSync.text = getString(R.string.last_sync, formatDateTime(state.timestamp))
                    }
                    is SyncState.Error -> {
                        binding.btnSyncNow.isEnabled = true
                        binding.progressSync.visibility = View.GONE
                        binding.tvSyncStatus.text = getString(R.string.sync_error, state.message)
                    }
                    else -> {
                        binding.btnSyncNow.isEnabled = true
                        binding.progressSync.visibility = View.GONE
                        binding.tvSyncStatus.text = ""
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
