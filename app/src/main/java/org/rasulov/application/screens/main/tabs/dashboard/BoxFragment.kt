package org.rasulov.application.screens.main.tabs.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentBoxBinding
import org.rasulov.application.model.Repositories
import org.rasulov.application.utils.viewModelCreator
import org.rasulov.application.views.DashboardItemView
import org.rasulov.application.utils.observeEvent

class BoxFragment : Fragment(R.layout.fragment_box) {

    private lateinit var binding: FragmentBoxBinding
    private val args by navArgs<BoxFragmentArgs>()
    private val viewModel by viewModelCreator {
        BoxViewModel(
            getBoxId(),
            Repositories.boxesRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("itlife0088", "onCreate: $this")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(view)

        binding.root.setBackgroundColor(DashboardItemView.getBackgroundColor(getColorValue()))
        binding.boxTextView.text = getString(R.string.this_is_box, getColorName())

        binding.goBackButton.setOnClickListener { onGoBackButtonPressed() }
        Log.d("itlife0088", "onCreatedView: $this")

        listenShouldExitEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("itlife0088", "onDestroyView: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("itlife0088", "onDestroy: $this")
    }

    override fun onResume() {
        super.onResume()
        Log.d(
            "itrr0088",
            "backstack ${requireActivity().supportFragmentManager.backStackEntryCount}"
        )
    }

    private fun onGoBackButtonPressed() {
        findNavController().popBackStack()
    }

    private fun listenShouldExitEvent() =
        viewModel.shouldExitEvent.observeEvent(viewLifecycleOwner) { shouldExit ->
            if (shouldExit) {
                // close the screen if the box has been deactivated
                findNavController().popBackStack()
            }
        }

    private fun getBoxId() = args.boxId

    private fun getColorValue() = args.colorValue

    private fun getColorName() = args.colorName

}