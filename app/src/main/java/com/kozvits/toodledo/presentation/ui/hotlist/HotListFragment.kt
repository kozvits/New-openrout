package com.kozvits.toodledo.presentation.ui.hotlist

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kozvits.toodledo.R
import com.kozvits.toodledo.databinding.FragmentHotListBinding
import com.kozvits.toodledo.domain.model.*
import com.kozvits.toodledo.presentation.adapter.TaskAdapter
import com.kozvits.toodledo.presentation.viewmodel.HotListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotListFragment : Fragment() {

    private var _binding: FragmentHotListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HotListViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHotListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSortControls()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                val action = HotListFragmentDirections.actionHotListToEditTask(task.id)
                findNavController().navigate(action)
            },
            onCheckClick = { task -> viewModel.completeTask(task.id) }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun setupSortControls() {
        val fields = SortField.entries
        val fieldNames = fields.map { it.label }.toTypedArray()

        binding.btnSort1.setOnClickListener {
            showSortDialog(getString(R.string.sort_primary), fieldNames) { idx, order ->
                viewModel.setSort1(fields[idx], order)
                binding.btnSort1.text = "${fields[idx].label} ${if (order == SortOrder.DESC) "↓" else "↑"}"
            }
        }
        binding.btnSort2.setOnClickListener {
            showSortDialog(getString(R.string.sort_secondary), fieldNames) { idx, order ->
                viewModel.setSort2(fields[idx], order)
                binding.btnSort2.text = "${fields[idx].label} ${if (order == SortOrder.DESC) "↓" else "↑"}"
            }
        }
        binding.btnSort3.setOnClickListener {
            showSortDialog(getString(R.string.sort_tertiary), fieldNames) { idx, order ->
                viewModel.setSort3(fields[idx], order)
                binding.btnSort3.text = "${fields[idx].label} ${if (order == SortOrder.DESC) "↓" else "↑"}"
            }
        }

        // Defaults
        binding.btnSort1.text = "${SortField.PRIORITY.label} ↓"
        binding.btnSort2.text = "${SortField.DUE_DATE.label} ↑"
        binding.btnSort3.text = getString(R.string.sort_none)
    }

    private fun showSortDialog(title: String, fieldNames: Array<String>, onSelected: (Int, SortOrder) -> Unit) {
        var selectedIdx = 0
        var selectedOrder = SortOrder.ASC
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setSingleChoiceItems(fieldNames, 0) { _, idx -> selectedIdx = idx }
            .setNeutralButton("↓ DESC") { _, _ -> onSelected(selectedIdx, SortOrder.DESC) }
            .setPositiveButton("↑ ASC") { _, _ -> onSelected(selectedIdx, SortOrder.ASC) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotList.collect { tasks ->
                    taskAdapter.submitList(tasks)
                    binding.emptyState.visibility =
                        if (tasks.isEmpty()) View.VISIBLE else View.GONE
                    binding.tvTaskCount.text = "${tasks.size} ${getString(R.string.tasks)}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
