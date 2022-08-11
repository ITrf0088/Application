package org.rasulov.application.screens.main.tabs.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentDashboardBinding
import org.rasulov.application.model.Repositories
import org.rasulov.application.model.boxes.entities.Box
import org.rasulov.application.utils.viewModelCreator
import org.rasulov.application.views.DashboardItemView

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel by viewModelCreator { DashboardViewModel(Repositories.boxesRepository) }

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("itlife0088", "onCreate: $this")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        clearBoxViews()

        Log.d("itr0088", "onCreateview: ${parentFragment?.parentFragmentManager} $this")
        Log.d("itr0088", "onCreateview: $childFragmentManager $this")
        Log.d("itlife0088", "onCreatedView: $this")
        viewModel.boxes.observe(viewLifecycleOwner) { renderBoxes(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("itlife0088", "onDestroyView: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("itlife0088", "onDestroy: $this")
    }

    private fun renderBoxes(boxes: List<Box>) {
        clearBoxViews()
        if (boxes.isEmpty()) {
            binding.noBoxesTextView.visibility = View.VISIBLE
            binding.boxesContainer.visibility = View.INVISIBLE
        } else {
            binding.noBoxesTextView.visibility = View.INVISIBLE
            binding.boxesContainer.visibility = View.VISIBLE
            createBoxes(boxes)
        }
    }

    private fun createBoxes(boxes: List<Box>) {

        // let's create boxes here by using dynamic view generation

        val width = resources.getDimensionPixelSize(R.dimen.dashboard_item_width)
        val height = resources.getDimensionPixelSize(R.dimen.dashboard_item_height)
        val generatedIdentifiers = boxes.map { box ->
            val id = View.generateViewId()
            val dashboardItemView = DashboardItemView(requireContext())
            dashboardItemView.setBox(box)
            dashboardItemView.id = id
            dashboardItemView.tag = box
            dashboardItemView.setOnClickListener(boxClickListener)
            val params = ConstraintLayout.LayoutParams(width, height)
            binding.boxesContainer.addView(dashboardItemView, params)
            return@map id
        }.toIntArray()
        binding.flowView.referencedIds = generatedIdentifiers
    }

    private fun clearBoxViews() {
        binding.boxesContainer.removeViews(1, binding.root.childCount - 1)

    }

    private val boxClickListener = View.OnClickListener {
        val box = it.tag as Box
        val direction = DashboardFragmentDirections.actionDashboardFragmentToBoxFragment(
            box.id,
            box.colorName,
            box.colorValue
        )
        findNavController().navigate(direction)
    }

}